package com.qixiu.lejia.widget.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qixiu.lejia.R;


/**
 * Created by Long on 2016/12/22
 * Loading Dialog
 */
public class LoadIndicatorDialog extends DialogFragment {

    public static final String TAG_LOAD_INDICATE_DIALOG = "LOAD_INDICATE_DIALOG";

    private OnCancelListener onCancelListener;

    public interface OnCancelListener {
        void onCancel(DialogInterface dialog);
    }


    public static LoadIndicatorDialog newInstance() {
        return new LoadIndicatorDialog();
    }

    public void show(FragmentManager fragmentManager) {
        super.show(fragmentManager, TAG_LOAD_INDICATE_DIALOG);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getParentFragment() != null && getParentFragment() instanceof OnCancelListener) {
            onCancelListener = (OnCancelListener) getParentFragment();
        } else if (context instanceof OnCancelListener) {
            onCancelListener = (OnCancelListener) context;
        }else {
            throw new RuntimeException("Host Activity or parentFragment must impl the OnCancelListener");
        }
    }

    @Override
    public void onDestroy() {
        onCancelListener = null;
        super.onDestroy();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.LoadIndicatorDialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.abc_dialog_load_indicator, container, false);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (onCancelListener != null) {
            onCancelListener.onCancel(dialog);
        }
    }


}
