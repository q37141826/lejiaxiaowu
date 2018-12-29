package com.qixiu.lejia.core.version;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qixiu.lejia.R;
import com.qixiu.lejia.service.DownloadService;

import java.io.File;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;


/**
 * Created by Long on 2017/3/25
 * 新版本信息展示对话框
 */
public class NewVersionDialog extends AppCompatDialogFragment
        implements View.OnClickListener, EasyPermissions.PermissionCallbacks {

    private static final String KEY_VERSION = "VERSION";

    private static final String PERM     = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final int    PERM_REC = 0xfa;

    private Version mExtra;

    public static NewVersionDialog newInstance(@NonNull Version version) {
        Bundle args = new Bundle();
        args.putParcelable(KEY_VERSION, version);
        NewVersionDialog fragment = new NewVersionDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Dialog);

        mExtra = getArguments().getParcelable(KEY_VERSION);
        assert mExtra != null;
        setCancelable(mExtra.getUpdateType() == 2);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_new_version, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View positive = view.findViewById(R.id.positive);
        View negative = view.findViewById(R.id.negative);

        positive.setOnClickListener(this);
        negative.setOnClickListener(this);

        //判断是否是强制
        if (mExtra.getUpdateType() == 1) {
            view.findViewById(R.id.negative).setVisibility(View.GONE);
        }

        /*//版本号
        ((TextView) view.findViewById(R.id.version_name))
                .setText(getString(R.string.version_name, mExtra.getVersionName()));
        //文件大小
        String fileSize = Formatter.formatFileSize(getContext(), mExtra.getApkSize());
        ((TextView) view.findViewById(R.id.version_size))
                .setText(getString(R.string.version_apk_size, fileSize));
        //更新时间
        ((TextView) view.findViewById(R.id.version_date))
                .setText(getString(R.string.version_update_date, mExtra.getUpdateDate()));
        //更新说明
        ((TextView) view.findViewById(R.id.version_explain))
                .setText(getString(R.string.version_update_explain, mExtra.getUpdateExplain()));*/

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.positive) {
            //启动下载服务
            //判断存储权限
            if (EasyPermissions.hasPermissions(getContext(), PERM)) {
                //已授权
                startDownloadService();
            } else {
                EasyPermissions.requestPermissions(this, getString(R.string.rationale_image_pick_storage),
                        PERM_REC, PERM);
            }
        } else if (v.getId() == R.id.negative) {
            dismiss();
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        startDownloadService();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            //永久拒绝
            new AlertDialog.Builder(getActivity())
                    .setMessage("权限被拒绝，无法下载！")
                    .show();
        }
    }

    private void startDownloadService() {
        File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String fileName = "乐家.apk";
        File file = new File(root, fileName);
        //如果存在旧文件，删除掉
        if (file.exists()) file.delete();
        DownloadService.start(getActivity().getApplicationContext(),
                file.getAbsolutePath(), mExtra.getApkLink());

        dismiss();

    }

}
