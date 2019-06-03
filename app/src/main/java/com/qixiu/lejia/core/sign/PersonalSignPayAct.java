package com.qixiu.lejia.core.sign;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Checkable;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.qixiu.lejia.BuildConfig;
import com.qixiu.lejia.R;
import com.qixiu.lejia.api.AppApi;
import com.qixiu.lejia.api.RequestCallback;
import com.qixiu.lejia.api.request.BaseBean;
import com.qixiu.lejia.api.request.C_CodeBean;
import com.qixiu.lejia.api.request.OKHttpRequestModel;
import com.qixiu.lejia.api.request.OKHttpUIUpdataListener;
import com.qixiu.lejia.app.LoginStatus;
import com.qixiu.lejia.beans.Rent;
import com.qixiu.lejia.beans.Room;
import com.qixiu.lejia.common.PayUtils;
import com.qixiu.lejia.databinding.ActPersonSignPayBinding;
import com.qixiu.lejia.utils.DatetimeConstants;
import com.qixiu.lejia.utils.NumUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.Call;

import static android.provider.CallLog.Calls.TYPE;

/**
 * Created by Long on 2018/4/28
 * <pre>
 *     个人签约支付页
 * </pre>
 */
public class PersonalSignPayAct extends BaseSignPayActivity {


    private ActPersonSignPayBinding mBinding;
    TextView price;
    //所有的租期
    private String[] allLeases;

    //租期(1--12)默认3
    private int mLease = 1;
    //分期期数(1是押一付一,2是押一付三,3是半年付,4是全年付)默认2
    private int mPeriods = 1;

    //房间ID
    private String mRoomId;
    //签约ID
    private String mSignedId;
    private String matainTime;
    private String pay_type;//4代表续租
    private PriceBean priceBean;
    private String monthPrice;


    public static void start(Context context) {
        Intent starter = new Intent(context, PersonalSignPayAct.class);
        context.startActivity(starter);
    }

    public static void start(Context context, String time) {
        Intent starter = new Intent(context, PersonalSignPayAct.class);
        starter.putExtra(DATA, time);
        context.startActivity(starter);
    }

    public static void start(Context context, String time, String id, String type) {
        Intent starter = new Intent(context, PersonalSignPayAct.class);
        starter.putExtra(DATA, time);
        starter.putExtra(TYPE, type);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActPersonSignPayBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        //如果是续租，那么标记一个type
        pay_type = getIntent().getStringExtra(TYPE);
        if (pay_type == null) {
            pay_type = "";
        }
        //租期开始时间
        mBinding.startDate.setText(DateFormat.format(DatetimeConstants.YTD_CN, new Date()));
        matainTime = getIntent().getStringExtra(DATA);

        //设置分期点击事件
        setPeriodsClickListener();

        //设置租期点击事件
        mBinding.lease.setOnClickListener(v -> showAllLeases());

        //缴费明细点击事件
        mBinding.payDetails.setOnClickListener(v -> {
            PayDetailsDialog.newInstance(0, mRoomId, mLease, mPeriods,pay_type)
                    .show(getSupportFragmentManager());
        });

        mBinding.pay.setOnClickListener(v -> {
            int checkedRadioButtonId = mBinding.payWays.getCheckedRadioButtonId();
            mBinding.pay.setEnabled(false);
            if (checkedRadioButtonId == R.id.wx_pay) {
                startPay(PayUtils.PayWay.WX);
            } else {
                startPay(PayUtils.PayWay.ALI);
            }
        });

        //加载数据
        loadRoomInfo();

    }

    @Override
    public void onPayCancel() {
        super.onPayCancel();
        mBinding.pay.setEnabled(true);
    }

    @Override
    public void onPaySuccess() {
        super.onPaySuccess();
        mBinding.pay.setEnabled(true);
    }

    @Override
    public void onPayFailure() {
        super.onPayFailure();
        mBinding.pay.setEnabled(true);
    }

    @Override
    protected String getSignedId() {
        return mSignedId;
    }

    @SuppressWarnings("unchecked")
    private void startPay(@PayUtils.PayWay int payWay) {
        Rent rent = mBinding.getRent();
        if (rent == null) {
            showErrorMsg("请选择租期");
            return;
        }
        if (!TextUtils.isEmpty(pay_type)) {
            int type = Integer.parseInt(pay_type);
            super.startPay(payWay, type, mRoomId, rent.getFirstPay(), mLease, mPeriods, rent.getFirstPay(),
                    rent.getMonthlyPay());
        } else {
            super.startPay(payWay, mRoomId, rent.getFirstPay(), mLease, mPeriods, rent.getFirstPay(),
                    rent.getMonthlyPay());
        }
    }

    /**
     * 显示所有租期
     */
    private void showAllLeases() {
        if (allLeases == null) {
            allLeases = new String[12];
            for (int i = 0; i < 12; i++) {
                allLeases[i] = String.format(Locale.getDefault(), "%d个月", i + 1);
            }
        }

        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_title_leases)
                .setItems(allLeases, (v, index) -> {
                    int lease = index + 1;
                    if (mLease != lease) {
                        mLease = lease;
                        //文字显示
                        mBinding.lease.setText(allLeases[index]);
                        //重新计算禁用
                        disablePeriods(mLease);
                        //重新选择分期
                        if (mLease >= 6) {
                            mPeriods = 2;
                        } else {
                            mPeriods = 1;
                        }
                        selectPeriods(mPeriods);
                        //计算租金
                        calculateRent(mRoomId, mLease, mPeriods, true);
                    }
                })
                .show();

    }

    //设置租期点击监听
    private void setPeriodsClickListener() {
        int childCount = mBinding.periods.getChildCount();
        for (int i = 0; i < childCount; i++) {
            mBinding.periods.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectPriod(v);
                }
            });
//
//
//                    .setOnClickListener(v -> {
//                        //重新计算租金
//
//                    });
        }
    }

    public void selectPriod(View v) {
        mPeriods = mBinding.periods.indexOfChild(v) + 1;
        //选中该期数
        selectPeriods(mPeriods);
        calculateRent(mRoomId, mLease, mPeriods, true);
    }

    /**
     * 加载房间信息
     */
    @SuppressWarnings("unchecked")
    private void loadRoomInfo() {
        call = AppApi.get().personalSignFifthStep(LoginStatus.getToken(), pay_type);
        call.enqueue(new RequestCallback<Room>() {
            @Override
            protected void onSuccess(Room room) {
                mRoomId = room.getId();
                mSignedId = room.getSignedId();
                mBinding.setRoom(room);
                monthPrice = room.getRo_long_money();
                //禁用不可选的期数
                disablePeriods(mLease);
                //默认选择期数(押一付三)
                selectPeriods(mPeriods);

                //加载房租
                calculateRent(room.getId(), mLease, mPeriods, false);
                mBinding.startDate.setText(room.getSd_starttime());
                if (!TextUtils.isEmpty(matainTime)) {
                    mBinding.startDate.setText(matainTime);
                }
            }
        });
    }

    /**
     * 计算房租费用
     *
     * @param roomId  房间ID
     * @param lease   租期
     * @param periods 期数(押一付三)
     */
    @SuppressWarnings("unchecked")
    private void calculateRent(String roomId, int lease, int periods, boolean showIndicator) {
        if (showIndicator) showLoadIndicator();
        call = AppApi.get().calculateRent(LoginStatus.getToken(), roomId, lease, pay_type, periods);
        call.enqueue(new RequestCallback<Rent>() {
            @Override
            protected void onSuccess(Rent rent) {
//                if(pay_type.equals("4")){
//                    rent.setFirstPay(lease*NumUtils.getDouble(monthPrice)+"");
//                }
                mBinding.setRent(rent);
                dismissLoadIndicator();
            }

            @Override
            protected void onComplete() {
                if (showIndicator) dismissLoadIndicator();
            }
        });

        String url = BuildConfig.BASE_URL + "/Home/Sign/userpay";
        OKHttpRequestModel okHttpRequestModel = new OKHttpRequestModel(new OKHttpUIUpdataListener() {
            @Override
            public void onSuccess(Object data, int i) {
                if (data instanceof PriceBean) {
                    priceBean = (PriceBean) data;
                    price = mBinding.getRoot().findViewById(R.id.price);
                    price.setText(priceBean.getO().getRo_long_money() + "元/月");
                }

            }

            @Override
            public void onError(Call call, Exception e, int i) {

            }

            @Override
            public void onFailure(C_CodeBean c_codeBean) {

            }
        });
        Map<String, String> map = new HashMap<>();
        map.put("uid", LoginStatus.getToken());
        okHttpRequestModel.okhHttpPost(url, map, new PriceBean());
    }

    /**
     * 选中期数
     *
     * @param periods 期数下标
     */
    private void selectPeriods(int periods) {
        //清除选中
        for (int i = 0; i < mBinding.periods.getChildCount(); i++) {
            View child = mBinding.periods.getChildAt(i);
            ((Checkable) child).setChecked(false);
        }
        //重新选中
        View child = mBinding.periods.getChildAt(periods - 1);
        ((Checkable) child).setChecked(true);
    }

    /**
     * 禁用不可选的期数
     * 例如：选择1个月 -->只能选择押一付一
     *
     * @param lease 租期
     */
    private void disablePeriods(int lease) {
        int startIndex = getDisableStartIndex(lease);
        for (int i = startIndex; i < mBinding.periods.getChildCount(); i++) {
            View child = mBinding.periods.getChildAt(i);
            child.setEnabled(false);
        }
        for (int i = 0; i < startIndex; i++) {
            View child = mBinding.periods.getChildAt(i);
            child.setEnabled(true);
        }
        if (lease >= 6) {
            CheckedTextView child = (CheckedTextView) mBinding.periods.getChildAt(0);
            child.setEnabled(false);
            child.setChecked(false);
        }
    }


    /**
     * 获取开始禁用的下标(总共只有4个选项)
     *
     * @param lease 租期
     * @return 开始下标
     */
    private int getDisableStartIndex(int lease) {
        if (0 < lease && lease < 3) {
            return 1;
        } else if (3 <= lease && lease < 6) {
            return 2;
        } else if (6 <= lease && lease < 12) {
            return 3;
        } else {
            return 4;
        }
    }


    public static class PriceBean extends BaseBean<PriceBean.OBean> {

        public static class OBean {
            private String ro_number;
            private String ro_name;
            private String ap_size;
            private String ap_room;
            private String ap_log;
            private String ro_short_money;
            private String ro_long_money;
            private String sd_ro_id;
            private String sd_id;

            public String getRo_number() {
                return ro_number;
            }

            public void setRo_number(String ro_number) {
                this.ro_number = ro_number;
            }

            public String getRo_name() {
                return ro_name;
            }

            public void setRo_name(String ro_name) {
                this.ro_name = ro_name;
            }

            public String getAp_size() {
                return ap_size;
            }

            public void setAp_size(String ap_size) {
                this.ap_size = ap_size;
            }

            public String getAp_room() {
                return ap_room;
            }

            public void setAp_room(String ap_room) {
                this.ap_room = ap_room;
            }

            public String getAp_log() {
                return ap_log;
            }

            public void setAp_log(String ap_log) {
                this.ap_log = ap_log;
            }

            public String getRo_short_money() {
                return ro_short_money;
            }

            public void setRo_short_money(String ro_short_money) {
                this.ro_short_money = ro_short_money;
            }

            public String getRo_long_money() {
                return ro_long_money;
            }

            public void setRo_long_money(String ro_long_money) {
                this.ro_long_money = ro_long_money;
            }

            public String getSd_ro_id() {
                return sd_ro_id;
            }

            public void setSd_ro_id(String sd_ro_id) {
                this.sd_ro_id = sd_ro_id;
            }

            public String getSd_id() {
                return sd_id;
            }

            public void setSd_id(String sd_id) {
                this.sd_id = sd_id;
            }
        }
    }
}
