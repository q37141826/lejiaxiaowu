package com.qixiu.lejia.core.sign;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.qixiu.lejia.R;
import com.qixiu.lejia.api.RequestCallback;
import com.qixiu.lejia.api.AppApi;
import com.qixiu.lejia.app.LoginStatus;
import com.qixiu.lejia.base.BaseToolbarAct;
import com.qixiu.lejia.beans.QualificationInfo;
import com.qixiu.lejia.databinding.ActQualificationsBinding;
import com.qixiu.lejia.utils.RegexUtils;

/**
 * Created by Long on 2018/4/26
 * <pre>
 *     第二部 资质信息
 * </pre>
 */
public class QualificationsAct extends BaseToolbarAct {

    private static final String KEY_USER_TYPE = "USER_TYPE";

    private ActQualificationsBinding mBinding;

    @UserType
    private int mUserType;

    public static void start(Context context,@UserType int userType,String shopId) {
        if (LoginStatus.verifiedLogin(context)) {
            Intent starter = new Intent(context, QualificationsAct.class);
            starter.putExtra(KEY_USER_TYPE, userType);
            starter.putExtra("shopId", shopId);
            context.startActivity(starter);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActQualificationsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        //检查用户类型
        checkUserType();

        mBinding.next.setOnClickListener(v -> verifyInput());

        //load
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
                RentContractAct.start(QualificationsAct.this,mUserType,getIntent().getStringExtra("shopId"));
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

    private void checkUserType() {
        mUserType = getIntent().getIntExtra(KEY_USER_TYPE, UserType.PERSONAL);
        if (mUserType == UserType.CORPORATE) {
            mBinding.personalSteps.setVisibility(View.GONE);
            mBinding.corporateSteps.setVisibility(View.VISIBLE);
        }
    }

    private void showToast(int s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }



}
