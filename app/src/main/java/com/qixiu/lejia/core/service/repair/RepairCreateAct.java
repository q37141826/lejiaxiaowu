package com.qixiu.lejia.core.service.repair;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.qixiu.lejia.R;
import com.qixiu.lejia.api.AppApi;
import com.qixiu.lejia.api.RequestCallback;
import com.qixiu.lejia.api.ResponseStatus;
import com.qixiu.lejia.app.AppContext;
import com.qixiu.lejia.app.LoginStatus;
import com.qixiu.lejia.base.BaseToolbarAct;
import com.qixiu.lejia.beans.ServiceProject;
import com.qixiu.lejia.beans.SignedRoom;
import com.qixiu.lejia.utils.FileProviderUtil;
import com.qixiu.lejia.utils.Lists;
import com.qixiu.lejia.utils.Logger;
import com.qixiu.lejia.utils.RegexUtils;
import com.qixiu.lejia.utils.ToastUtils;
import com.qixiu.widget.SpaceItemDecoration;
import com.qixiu.widget.listener.TextChangeListener;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import top.zibin.luban.Luban;

/**
 * Created by Long on 2018/5/16
 * <pre>
 *      新增维修
 * </pre>
 */
public class RepairCreateAct extends BaseToolbarAct {

    private static final String TAG = "RepairCreateAct";

    private static final String PERM_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;

    private static final int REQUEST_CODE_CHOOSE = 0xff;
    private static final int REQ_PERM            = 233;
    private static final int MAX_IMAGE_SIZE      = 9;

    private ImagesAdapter mImagesAdapter;

    private TextView mServiceProject;
    private EditText mNameEdit;
    private EditText mPhoneEdit;
    private TextView mRoomInfo;
    private int      roomId;

    private EditText mDescEdit;
    private TextView mWords;
    private TextView mImageSize;

    private List<ServiceProject> mServiceProjects;
    private String               mSelectedServiceProjectId;

    private Disposable mDisposable;

    public static void start(Activity context, int requestCode) {
        if (LoginStatus.verifiedLogin(context)) {
            Intent starter = new Intent(context, RepairCreateAct.class);
            context.startActivityForResult(starter, requestCode);
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_create_repair);

        mServiceProject = findViewById(R.id.service_project);
        mNameEdit = findViewById(R.id.edit_name);
        mPhoneEdit = findViewById(R.id.edit_phone);
        mDescEdit = findViewById(R.id.edit_desc);
        mWords = findViewById(R.id.words);
        mImageSize = findViewById(R.id.image_size);
        mRoomInfo = findViewById(R.id.room_info);

        //描述输入监控
        watchDescEdit();

        RecyclerView imagesGrid = findViewById(R.id.recycler_view);
        imagesGrid.setLayoutManager(new GridLayoutManager(this, 3));
        imagesGrid.addItemDecoration(new SpaceItemDecoration(this, 8));

        mImagesAdapter = new ImagesAdapter(null, true);
        mImagesAdapter.setOnItemClickListener(new ImagesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int viewType, int position) {
                if (viewType == ImagesAdapter.ADD) {
                    checkPerm();
                }
            }

            @Override
            public void onItemDel(int position) {
                updateImageSizeView();
            }
        });
        imagesGrid.setAdapter(mImagesAdapter);


        //获取已签约房间信息
        loadSignedRoomInfo();
    }


    private void watchDescEdit() {
        mDescEdit.addTextChangedListener(new TextChangeListener() {

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                mWords.setText(MessageFormat.format("{0} / 300", length));
            }
        });
    }

    //检查存储权限
    private void checkPerm() {
        if (EasyPermissions.hasPermissions(this, PERM_STORAGE)) {
            startImagesPicker();
        } else {
            EasyPermissions.requestPermissions(this,
                    getString(R.string.rationale_image_pick_storage), REQ_PERM, PERM_STORAGE);
        }
    }

    @Override
    protected boolean loadIndicatorCancelable() {
        return false;
    }

    @AfterPermissionGranted(REQ_PERM)
    public void startImagesPicker() {
        //新增
        int size = MAX_IMAGE_SIZE - mImagesAdapter.getImageSize();
        Matisse.from(RepairCreateAct.this)
                .choose(MimeType.allOf())
                .theme(R.style.Matisse_Dracula)
                .countable(true)
                .maxSelectable(size)
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mImagesAdapter.onImageSelected(Matisse.obtainResult(data));
            updateImageSizeView();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onDestroy() {
        if (mDisposable != null) {
            mDisposable.dispose();
        }
        super.onDestroy();
    }

    @SuppressWarnings("ConstantConditions,unchecked")
    public void submit(View view) {
        String name = mNameEdit.getText().toString();
        String phone = mPhoneEdit.getText().toString();
        String desc = mDescEdit.getText().toString();

        if (TextUtils.isEmpty(mSelectedServiceProjectId)) {
            ToastUtils.showShort(this, "请选择服务项目");
            return;
        }
        if (TextUtils.isEmpty(name)) {
            ToastUtils.showShort(this, "请输入客户姓名");
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showShort(this, "请输入手机号");
            return;
        }
        if (!RegexUtils.isMobileExact(phone)) {
            ToastUtils.showShort(this, R.string.validate_phone_format_error);
            return;
        }
        if (TextUtils.isEmpty(desc)) {
            ToastUtils.showShort(this, "请输入问题描述");
            return;
        }

        //开始上传
        showLoadIndicator();
        mDisposable = Observable.just(mImagesAdapter.getUris())
                .map(uris -> {
                    //没有选择图片，直接返回
                    if (uris.isEmpty()) {
                        return Collections.<File>emptyList();
                    }
                    List<String> paths = Lists.newArrayList();
                    for (String uri : uris) {
                        String path = FileProviderUtil.getFilePath(AppContext.getContext(), Uri.parse(uri));
                        paths.add(path);
                    }
                    return Luban.with(AppContext.getContext())
                            .setTargetDir(FileProviderUtil.getCompressTargetDir(AppContext.getContext()))
                            .ignoreBy(100)
                            .load(paths).get();
                })
                .flatMap((Function<List<File>, ObservableSource<ResponseStatus>>) files -> {
                    MultipartBody.Builder builder = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("uid", LoginStatus.getToken())
                            .addFormDataPart("rep_id", mSelectedServiceProjectId)
                            .addFormDataPart("user_name", name)
                            .addFormDataPart("tel", phone)
                            .addFormDataPart("addres", mRoomInfo.getText().toString())
                            .addFormDataPart("ro_id", roomId + "")
                            .addFormDataPart("content", desc);

                    //添加上传图片
                    if (files != null && !files.isEmpty()) {
                        for (File file : files) {
                            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                            builder.addFormDataPart("image[]", file.getName(), requestBody);
                        }
                    }
                    return AppApi.get().createRepair(builder.build());
                })
                .doFinally(() -> {
                    Logger.d(TAG, "submit: 删除图片。。。");
//                    if (files != null && !files.isEmpty())
                    FileProviderUtil.clearCompressDir(AppContext.getContext());
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resp -> {
                    dismissLoadIndicator();
                    if (resp.isSuccess()) {
                        //成功
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        ToastUtils.showShort(this, resp.getMessage());
                    }
                }, t -> {
                    //失败
                    t.printStackTrace();
                    dismissLoadIndicator();
                });
    }


    @SuppressWarnings("unchecked")
    public void onSelectServiceProject(View view) {
        if (mServiceProjects != null) {
            showSelectServiceProjectsDialog(mServiceProjects);
            return;
        }
        call = AppApi.get().serviceProjects();
        call.enqueue(new RequestCallback<List<ServiceProject>>(this) {
            @Override
            protected void onSuccess(List<ServiceProject> list) {
                mServiceProjects = list;
                showSelectServiceProjectsDialog(list);
            }
        });
    }

    //获取已签约房间信息
    @SuppressWarnings("unchecked")
    private void loadSignedRoomInfo() {
        call = AppApi.get().signedRoomInfo(LoginStatus.getToken());
        call.enqueue(new RequestCallback<SignedRoom>() {
            @Override
            protected void onSuccess(SignedRoom o) {
                mRoomInfo.setText(o.getRoomNum());
                roomId = o.getRoomId();
            }
        });
    }

    private void updateImageSizeView() {
        mImageSize.setText(String.format(Locale.getDefault(), "%d/9", mImagesAdapter.getImageSize()));
    }

    private void showSelectServiceProjectsDialog(List<ServiceProject> list) {
        String[] items = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            items[i] = list.get(i).getName();
        }
        new AlertDialog.Builder(this)
                .setTitle("选择服务项目")
                .setItems(items, (dialog, which) -> {
                    mServiceProject.setText(items[which]);
                    mSelectedServiceProjectId = mServiceProjects.get(which).getId();
                })
                .show();
    }


}
