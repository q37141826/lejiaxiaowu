package com.qixiu.lejia.core.service.complaint;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.qixiu.lejia.R;
import com.qixiu.lejia.api.AppApi;
import com.qixiu.lejia.api.RequestCallback;
import com.qixiu.lejia.app.LoginStatus;
import com.qixiu.lejia.base.BaseMultiStateAct;
import com.qixiu.lejia.beans.ComplaintTag;
import com.qixiu.lejia.beans.Shop;
import com.qixiu.lejia.beans.resp.ComplaintResp;
import com.qixiu.lejia.utils.ToastUtils;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Long on 2018/5/17
 */
public class ComplaintCreateAct extends BaseMultiStateAct {

    private static final String TAG = "ComplaintCreateAct";

    private TextView      mTargetSelector;
    private TagFlowLayout mFlowLayout;

    private List<Shop> mComplaintTargets;       //投诉对象
    private String     mSelectedTargetId;       //已选择的投诉对象ID(默认列表第一个)

    private Disposable mDisposable;

    public static void start(Activity context, int requestCode) {
        Intent starter = new Intent(context, ComplaintCreateAct.class);
        context.startActivityForResult(starter, requestCode);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadComplaintTargetsAndTags();
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    protected View onCreateContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.act_complaint_create, null, false);
    }

    @Override
    protected void onContentViewCreated(View view) {
        mTargetSelector = view.findViewById(R.id.shop_selector);
        mFlowLayout = view.findViewById(R.id.flow);

        mTargetSelector.setOnClickListener(this::showComplaintTargetSelectPopup);

        view.findViewById(R.id.submit).setOnClickListener(v -> {
            EditText edit = view.findViewById(R.id.edit);
            doSubmit(edit.getText().toString());
        });
    }

    //显示选择投诉对象Popup
    private void showComplaintTargetSelectPopup(View v) {
        PopupWindow window = new PopupWindow();
        window.setWidth(v.getWidth());
        window.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.bg_complaint_popup));
        window.setOutsideTouchable(true);
        ListView listView = new ListView(this);
        listView.setAdapter(new ArrayAdapter<>(this, R.layout.complaint_target, mComplaintTargets));
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Shop shop = mComplaintTargets.get(position);
            mTargetSelector.setText(shop.getTitle());
            mSelectedTargetId = shop.getId();
            window.dismiss();
        });
        window.setContentView(listView);
        window.showAsDropDown(v, 0, 0, Gravity.BOTTOM);
    }

    @Override
    protected void onReload() {
        loadComplaintTargetsAndTags();
    }

    @Override
    protected void onDestroy() {
        if (mDisposable != null) {
            mDisposable.dispose();
        }
        mDisposable = null;
        super.onDestroy();
    }

    @SuppressWarnings("unchecked")
    private void doSubmit(String content) {
        if (TextUtils.isEmpty(content)) {
            ToastUtils.showShort(this, "请填写描述内容");
            return;
        }

        Set<Integer> set = mFlowLayout.getSelectedList();
        if (set.isEmpty()) {
            ToastUtils.showShort(this, "请选择投诉类型");
            return;
        }
        ComplaintTag tag = (ComplaintTag) mFlowLayout.getAdapter().getItem(set.iterator().next());
        call = AppApi.get().createComplaint(LoginStatus.getToken(), tag.getId(), mSelectedTargetId, content);
        call.enqueue(new RequestCallback(this) {
            @Override
            protected void onSuccess(Object o) {
                setResult(RESULT_OK);
                finish();
            }
        });

    }


    //加载投诉对象和标签数据
    @SuppressWarnings("unchecked")
    private void loadComplaintTargetsAndTags() {
        mDisposable = Observable.zip(AppApi.get().complaintShops(), AppApi.get().complaintTags(),
                (shopsResp, tagsResp) -> new ComplaintResp(tagsResp.getBody(), shopsResp.getBody()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResp, e -> switchToErrorState());
    }

    private void handleResp(ComplaintResp resp) {
        switchToContentState();

        //设置投诉类型标签
        handleTags(resp.getTags());

        //投诉对象
        List<Shop> shops = resp.getShops();
        Shop newShop = new Shop();
        newShop.setId("0");
        newShop.setTitle("乐家品牌");
        shops.add(newShop);
        if (shops != null && !shops.isEmpty()) {
            mComplaintTargets = shops;
            Shop shop = shops.get(0);
            mSelectedTargetId = shop.getId();
            mTargetSelector.setText(shop.getTitle());
        }

    }

    private void handleTags(List<ComplaintTag> tags) {
        mFlowLayout.setAdapter(new TagAdapter<ComplaintTag>(tags) {
            @Override
            public View getView(FlowLayout parent, int position, ComplaintTag tag) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                View view = inflater.inflate(R.layout.item_complaint_tag, parent, false);
                TextView tv = (TextView) view;
                tv.setText(tag.getText());
                return view;
            }
        });
    }

}
