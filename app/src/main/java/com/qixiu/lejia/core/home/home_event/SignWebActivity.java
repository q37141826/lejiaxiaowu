package com.qixiu.lejia.core.home.home_event;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qixiu.lejia.BuildConfig;
import com.qixiu.lejia.R;
import com.qixiu.lejia.api.request.BaseBean;
import com.qixiu.lejia.api.request.C_CodeBean;
import com.qixiu.lejia.api.request.OKHttpRequestModel;
import com.qixiu.lejia.api.request.OKHttpUIUpdataListener;
import com.qixiu.lejia.app.LoginStatus;
import com.qixiu.lejia.base.BaseWhiteStateBarActivity;
import com.qixiu.lejia.beans.Event;
import com.qixiu.lejia.beans.ShareInfo;
import com.qixiu.lejia.beans.home.event.EventDetailsBean;
import com.qixiu.lejia.common.SharePanel;
import com.qixiu.lejia.utils.HtmlFormat;
import com.qixiu.lejia.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class SignWebActivity extends BaseWhiteStateBarActivity implements OKHttpUIUpdataListener<BaseBean>, View.OnClickListener {
    public static final String URL = "URL";
    public static final String ID = "ID";
    WebView web_view_sign;
    private String url;
    private String checkStateUrl = BuildConfig.BASE_URL + "/Home/Life/activityinfo";
    private OKHttpRequestModel okHttpRequestModel;
    private Button btn_sign_event;
    private View view;
    private TextView textView_title_event;
    private TextView textView_time_event;
    private TextView textView_address_event;
    private EditText edttext_name_event;
    private EditText edttext_phone_event;
    private Button btn_event_sign_pop;
    private EventDetailsBean bean;

    //活动报名接口
    String sighUrl = BuildConfig.BASE_URL + "/Home/Life/activityjoin";
    private String id;
    private RelativeLayout relativeLayout_bottom;

    Event event;


    public static void start(Context context, Intent intent) {
        if (intent == null) {
            intent = new Intent(context, SignWebActivity.class);
        }
        context.startActivity(intent);
    }


    @NonNull
    @Override
    protected View onCreateContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_sign_web, null, false);
    }

    @Override
    protected void onContentViewCreated(View view) {
        okHttpRequestModel = new OKHttpRequestModel(this);
        web_view_sign = view.findViewById(R.id.web_view_sign);
        btn_sign_event = view.findViewById(R.id.btn_sign_event);
        event=getIntent().getParcelableExtra("data");
        url = event.getAy_post_content();
        if (url.startsWith("http")) {
            web_view_sign.loadUrl(url);
        } else {
            String filePath = HtmlFormat.getNewContent(url);
            String baseUrl = "file://" + filePath;
            web_view_sign.loadDataWithBaseURL(baseUrl, filePath, "text/html", "utf-8", null);
        }
        initView(view);
        switchToContentState();
        load();
        btn_sign_event.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.refresh) {
            //刷新页面
            web_view_sign.reload();
        } else {
            if (bean != null) {
                ShareInfo shareInfo = new ShareInfo();
                String shareUrl=BuildConfig.BASE_H5_URL2+"?hid="+event.getId()+"&ay_id="+event.getId()+"&uid="+LoginStatus.getToken();
                shareInfo.setShareUrl(shareUrl);
                shareInfo.setTitle(event.getTitle());
                shareInfo.setContent(event.getIntro());
                shareInfo.setShareImageUrl(event.getImage());
                //显示分享面板
                SharePanel.newInstance(shareInfo).show(getSupportFragmentManager());
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView(View view) {
//        RelativeLayout relative_dismiss_pop = view.findViewById(R.id.relative_dismiss_pop);
        textView_title_event = view.findViewById(R.id.textView_title_event);
        textView_time_event = view.findViewById(R.id.textView_time_event);
        textView_address_event = view.findViewById(R.id.textView_address_event);
        edttext_name_event = view.findViewById(R.id.edttext_name_event);
        edttext_phone_event = view.findViewById(R.id.edttext_phone_event);
        btn_event_sign_pop = view.findViewById(R.id.btn_event_sign_pop);
        btn_event_sign_pop.setOnClickListener(this);
        relativeLayout_bottom = view.findViewById(R.id.relativeLayout_bottom);
        relativeLayout_bottom.setOnClickListener(this);
//        relative_dismiss_pop.setOnClickListener(this);

    }

    private void load() {
        Map<String, String> map = new HashMap<>();
        id =event.getId();
        map.put("ay_id", id);
        okHttpRequestModel.okhHttpPost(checkStateUrl, map, new EventDetailsBean());
    }

    @Override
    protected void onReload() {

    }


    @Override
    public void onSuccess(BaseBean data, int i) {
        if (data instanceof EventDetailsBean) {
            bean = (EventDetailsBean) data;
            if (bean.getO().getAy_status() == 3) {
                btn_sign_event.setText("活动已结束");
                btn_sign_event.setEnabled(false);
                btn_sign_event.setBackgroundColor(getResources().getColor(R.color.grey_200));
            } else if (bean.getO().getAy_status() == 2) {
                btn_sign_event.setText("活动进行中");
                btn_sign_event.setEnabled(false);
                btn_sign_event.setBackgroundColor(getResources().getColor(R.color.grey_200));
            } else {
                btn_sign_event.setEnabled(true);
                btn_sign_event.setText("活动报名");
                btn_sign_event.setBackgroundColor(getResources().getColor(R.color.button_default));
            }
        }
        if (data.getUrl().equals(sighUrl)) {
            ToastUtil.toast(data.getM());
            finish();
        }
        switchToContentState();
    }

    @Override
    public void onError(Call call, Exception e, int i) {
        switchToContentState();
    }

    @Override
    public void onFailure(C_CodeBean c_codeBean) {
        switchToContentState();
        ToastUtil.toast(c_codeBean.getM());
    }


    PopupWindow popupWindow;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_sign_event:
//                if (popupWindow != null) {
////                    showPop();
//                } else {
////                    creatPop();
//                }
                relativeLayout_bottom.setVisibility(View.VISIBLE);
                if (bean != null) {
                    textView_title_event.setText(bean.getO().getAy_title());
                    textView_time_event.setText("活动时间    " + bean.getO().getAy_starttime() + "--" + bean.getO().getAy_endtime());
                    textView_address_event.setText("活动地点    " + bean.getO().getAy_address());
                }
                break;
            case R.id.relative_dismiss_pop:
                popupWindow.dismiss();
                break;
            case R.id.relativeLayout_bottom:
                relativeLayout_bottom.setVisibility(View.GONE);
                break;

            case R.id.btn_event_sign_pop:
                Map<String, String> map = new HashMap();
                if (TextUtils.isEmpty(edttext_name_event.getText().toString().trim())) {
                    ToastUtil.toast("请输入您的姓名");
                    return;
                }
                if (TextUtils.isEmpty(edttext_phone_event.getText().toString().trim())) {
                    ToastUtil.toast("请输入您的手机号");
                    return;
                }
                map.put("ay_id", id);
                map.put("aj_name", edttext_name_event.getText().toString().trim());
                map.put("aj_tel", edttext_phone_event.getText().toString().trim());
                map.put("uid", LoginStatus.getToken());
                okHttpRequestModel.okhHttpPost(sighUrl, map, new BaseBean());
                break;


        }
    }

    private void creatPop() {
        view = View.inflate(this, R.layout.pop_event_sign, null);
        popupWindow = new PopupWindow(view);
        popupWindow.setClippingEnabled(false);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setFocusable(true);
//        popupWindow.setAnimationStyle(Animation.ZORDER_BOTTOM);
        RelativeLayout relative_dismiss_pop = view.findViewById(R.id.relative_dismiss_pop);
        textView_title_event = view.findViewById(R.id.textView_title_event);
        textView_time_event = view.findViewById(R.id.textView_time_event);
        textView_address_event = view.findViewById(R.id.textView_address_event);
        edttext_name_event = view.findViewById(R.id.edttext_name_event);
        edttext_phone_event = view.findViewById(R.id.edttext_phone_event);
        btn_event_sign_pop = view.findViewById(R.id.btn_event_sign_pop);
        btn_event_sign_pop.setOnClickListener(this);
        relative_dismiss_pop.setOnClickListener(this);
        if (bean != null) {
            textView_title_event.setText(bean.getO().getAy_title());
            textView_time_event.setText(bean.getO().getAy_starttime() + bean.getO().getAy_endtime());
            textView_address_event.setText(bean.getO().getAy_address());
        }

        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    private void showPop() {
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_refresh, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
