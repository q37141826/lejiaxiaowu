package com.qixiu.lejia.core.me.roommate;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.qixiu.lejia.R;
import com.qixiu.lejia.api.RequestCallback;
import com.qixiu.lejia.api.AppApi;
import com.qixiu.lejia.app.LoginStatus;
import com.qixiu.lejia.mvp.CallUtil;
import com.qixiu.lejia.mvp.LoadIndicatorView;
import com.qixiu.lejia.utils.RegexUtils;
import com.qixiu.lejia.utils.ToastUtils;

/**
 * Created by Long on 2018/5/16
 * <pre>
 *     新增室友
 * </pre>
 */
public class RoommateCreateDialog extends BottomSheetDialogFragment {

    private static final String TAG = "RoommateCreateDialog";

    private EditText mNameEdit;
    private EditText mPhoneEdit;
    private EditText mIdEdit;
    private TextView mSexChooser;

    private OnAddRoommateCallback mCallback;
    private retrofit2.Call        mCall;

    interface OnAddRoommateCallback {
        void onAddRoommateSuccess();
    }

    public static RoommateCreateDialog newInstance() {
        return new RoommateCreateDialog();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAddRoommateCallback) {
            mCallback = (OnAddRoommateCallback) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.RoommateCreateDialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_create_roommate, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNameEdit = view.findViewById(R.id.edit_name);
        mPhoneEdit = view.findViewById(R.id.edit_phone);
        mIdEdit = view.findViewById(R.id.edit_id);
        mSexChooser = view.findViewById(R.id.sex);
        mSexChooser.setOnClickListener(v -> showChooseSexDialog());

        view.findViewById(R.id.cancel).setOnClickListener(v -> dismiss());
        view.findViewById(R.id.submit).setOnClickListener(v -> submit());
    }

    public void show(FragmentManager fm) {
        super.show(fm, TAG);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        CallUtil.cancel(mCall);
        super.onDismiss(dialog);
    }

    @SuppressWarnings("unchecked")
    private void submit() {
        String name = mNameEdit.getText().toString();
        String phone = mPhoneEdit.getText().toString();
        String id = mIdEdit.getText().toString();

        if (TextUtils.isEmpty(name)) {
            ToastUtils.showShort(getContext(), "请输入室友姓名");
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showShort(getContext(), "请输入室友手机号码");
            return;
        }
        if (!RegexUtils.isMobileExact(phone)) {
            ToastUtils.showShort(getContext(),R.string.validate_phone_format_error);
            return;
        }
        if (TextUtils.isEmpty(id)) {
            ToastUtils.showShort(getContext(), "请输入室友身份证号");
            return;
        }
        if (!RegexUtils.isIDCard18(id)) {
            ToastUtils.showShort(getContext(), "请输入正确的身份证号");
            return;
        }

        mCall = AppApi.get().addRoommate(LoginStatus.getToken(), name, phone,
                mSexChooser.getText().toString(), id);
        mCall.enqueue(new RequestCallback<Object>((LoadIndicatorView) getActivity()) {

            @Override
            protected void onSuccess(Object o) {
                dismiss();
                mCallback.onAddRoommateSuccess();
            }
        });
    }


    private void showChooseSexDialog() {
        final String[] sexList = getResources().getStringArray(R.array.sex_list);
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.sign_choose_sex)
                .setItems(sexList, (v, index) -> {
                    mSexChooser.setText(sexList[index]);
                })
                .show();
    }

}
