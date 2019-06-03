package com.qixiu.lejia.core.sign;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qixiu.adapter.BindableRecyclerAdapter;
import com.qixiu.adapter.BindableItem;
import com.qixiu.lejia.R;
import com.qixiu.lejia.api.BaseResponse;
import com.qixiu.lejia.api.RequestCallback;
import com.qixiu.lejia.api.ResponseError;
import com.qixiu.lejia.api.AppApi;
import com.qixiu.lejia.app.LoginStatus;
import com.qixiu.lejia.beans.PayDetails;
import com.qixiu.lejia.items.PayDetailsCaptionItem;
import com.qixiu.lejia.items.PayDetailsCostItem;
import com.qixiu.lejia.mvp.CallUtil;
import com.qixiu.lejia.utils.Lists;
import com.qixiu.widget.MultiStateLayout;

import java.util.List;

import retrofit2.Call;

/**
 * Created by Long on 2018/4/26
 * <pre>
 *     缴费明细
 * </pre>
 */
public class PayDetailsDialog extends BottomSheetDialogFragment {

    private static final String TAG = "PayDetailsDialog";

    private static final String KEY_TYPE = "TYPE";
    private static final String KEY_ROOM_ID = "ROOM_ID";
    private static final String KEY_LEASE = "LEASE";
    private static final String KEY_PERIODS = "PERIODS";
    private static final String SIGN_TYPE = "SIGN_TYPE";

    private MultiStateLayout multiStateLayout;
    private RecyclerView recyclerView;

    private Call<BaseResponse<List<PayDetails>>> mCall;

    /**
     * @param type 1-企业  0-普通员工
     */
    public static PayDetailsDialog newInstance(int type, String roomId, int lease, int periods,String sign_type) {
        Bundle args = new Bundle();
        args.putInt(KEY_TYPE, type);
        args.putString(KEY_ROOM_ID, roomId);
        args.putInt(KEY_LEASE, lease);
        args.putInt(KEY_PERIODS, periods);
        args.putString(SIGN_TYPE, sign_type);
        PayDetailsDialog fragment = new PayDetailsDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_pay_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        multiStateLayout = view.findViewById(R.id.multi_state_layout);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        load();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        CallUtil.cancel(mCall);
    }

    public void show(FragmentManager fm) {
        super.show(fm, TAG);
    }

    private void load() {
        Bundle args = getArguments();
        int type = args.getInt(KEY_TYPE);
        if (type == 0) {
            String roomId = args.getString(KEY_ROOM_ID);
            int lease = args.getInt(KEY_LEASE);
            int periods = args.getInt(KEY_PERIODS);
            String signType=args.getString(SIGN_TYPE);
            mCall = AppApi.get().personalPayDetails(LoginStatus.getToken(), roomId, lease, periods,signType);
        }else {
            mCall = AppApi.get().corporatePayDetails(LoginStatus.getToken());
        }

        mCall.enqueue(new RequestCallback<List<PayDetails>>() {
            @Override
            protected void onSuccess(List<PayDetails> payDetails) {
                multiStateLayout.setViewState(MultiStateLayout.VIEW_STATE_CONTENT);
                handleResp(payDetails);
            }

            @Override
            protected void onFailure(ResponseError error) {
                multiStateLayout.setViewState(MultiStateLayout.VIEW_STATE_ERROR);
            }
        });
    }

    private void handleResp(List<PayDetails> resp) {
        List<BindableItem> items = Lists.newArrayList();
        for (PayDetails details : resp) {
            items.add(PayDetailsCaptionItem.of(details));
            items.addAll(PayDetailsCostItem.of(details.getCosts()));
        }
        BindableRecyclerAdapter adapter = new BindableRecyclerAdapter(items);
        recyclerView.setAdapter(adapter);
    }


}
