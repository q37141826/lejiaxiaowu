package com.qixiu.lejia.base;

import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.qixiu.lejia.mvp.CallUtil;
import com.qixiu.lejia.mvp.LoadIndicatorView;
import com.qixiu.lejia.widget.dialog.LoadIndicatorDialog;

import retrofit2.Call;

/**
 * Created by Long on 2016/12/23
 * 显示加载指示的Fragment
 */
public class BaseLoadIndicatorFrag extends Fragment
        implements LoadIndicatorView, LoadIndicatorDialog.OnCancelListener {

    protected Call call;

    @Override
    public void showLoadIndicator() {
        LoadIndicatorDialog dialog = LoadIndicatorDialog.newInstance();
        dialog.setCancelable(loadIndicatorCancelable());
        dialog.show(getChildFragmentManager());
    }

    /**
     * 是否可以取消加载指示器显示{@link android.app.Dialog#setCancelable(boolean)}
     *
     * @return true 是 false 否
     */
    protected boolean loadIndicatorCancelable() {
        return true;
    }

    @Override
    public void dismissLoadIndicator() {
        Fragment fragment = getChildFragmentManager()
                .findFragmentByTag(LoadIndicatorDialog.TAG_LOAD_INDICATE_DIALOG);
        if (fragment != null && fragment instanceof LoadIndicatorDialog) {
            LoadIndicatorDialog loadIndicatorDialog = (LoadIndicatorDialog) fragment;
            loadIndicatorDialog.dismiss();
        }
    }

    @Override
    public void showErrorMsg(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        CallUtil.cancel(call);
    }

    @Override
    public void onDestroy() {
        CallUtil.cancel(call);
        super.onDestroy();
    }
}
