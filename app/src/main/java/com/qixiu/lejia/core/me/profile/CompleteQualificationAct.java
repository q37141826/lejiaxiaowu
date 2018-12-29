package com.qixiu.lejia.core.me.profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.qixiu.lejia.R;
import com.qixiu.lejia.api.AppApi;
import com.qixiu.lejia.api.RequestCallback;
import com.qixiu.lejia.app.LoginStatus;
import com.qixiu.lejia.base.BaseToolbarAct;
import com.qixiu.lejia.beans.QualificationInfo;
import com.qixiu.lejia.databinding.ActCompleteQualificationBinding;
import com.qixiu.lejia.utils.RegexUtils;

/**
 * Created by Long on 2018/5/8
 * <pre>
 *     资质信息
 * </pre>
 */
public class CompleteQualificationAct extends BaseToolbarAct {

    private ActCompleteQualificationBinding mBinding;

    public static void start(Context context) {
        Intent starter = new Intent(context, CompleteQualificationAct.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(Color.WHITE);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//设置状态栏黑色字体
            //            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
//                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        mBinding = ActCompleteQualificationBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mBinding.next.setOnClickListener(v -> verifyInput());

        loadQualificationInfo();

    }


    private void verifyInput() {
        //行业
        String industry = mBinding.editIndustry.getText().toString().trim();
        if (TextUtils.isEmpty(industry)) {
            showToast(R.string.sign_hint_industry);
            return;
        }

        //公司
        String company = mBinding.editCompany.getText().toString().trim();
        if (TextUtils.isEmpty(company)) {
            showToast(R.string.sign_hint_company);
            return;
        }

        //公司地址
        String companyAddress = mBinding.editCompanyAddress.getText().toString().trim();
        if (TextUtils.isEmpty(companyAddress)) {
            showToast(R.string.sign_hint_company_address);
            return;
        }

        //紧急联系人
        String contactName = mBinding.editContactName.getText().toString().trim();
        if (TextUtils.isEmpty(contactName)) {
            showToast(R.string.sign_hint_contact_name);
            return;
        }

        //紧急联系人手机
        String contactPhone = mBinding.editContactPhone.getText().toString().trim();
        if (TextUtils.isEmpty(contactPhone)) {
            showToast(R.string.sign_hint_contact_phone);
            return;
        }
        if (!RegexUtils.isMobileExact(contactPhone)) {
            showToast(R.string.sign_hint_invalid_phone);
            return;
        }

        //与紧急联系人关系
        String contactRelation = mBinding.editContactRelation.getText().toString().trim();
        if (TextUtils.isEmpty(contactRelation)) {
            showToast(R.string.sign_hint_contact_relation);
            return;
        }

        //post
        call = AppApi.get().signSecondStep(LoginStatus.getToken(), industry, company,
                companyAddress, contactName, contactPhone, contactRelation);
        //noinspection unchecked
        call.enqueue(new RequestCallback<String>(this) {
            @Override
            protected void onSuccess(String o) {
                finish();
            }
        });

    }


    @SuppressWarnings("unchecked")
    private void loadQualificationInfo() {
        call = AppApi.get().qualificationInfo(LoginStatus.getToken());
        call.enqueue(new RequestCallback<QualificationInfo>() {
            @Override
            protected void onSuccess(QualificationInfo info) {
                mBinding.editIndustry.setText(info.getProfession());
                mBinding.editCompany.setText(info.getCompany());
                mBinding.editCompanyAddress.setText(info.getCompanyAddress());
                mBinding.editContactName.setText(info.getEmergencyContactNmae());
                mBinding.editContactPhone.setText(info.getEmergencyContactPhone());
                mBinding.editContactRelation.setText(info.getRelationship());
            }
        });
    }

    private void showToast(int s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }


}
