package com.qixiu.lejia.common;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.qixiu.lejia.R;
import com.qixiu.lejia.app.AppContext;
import com.qixiu.lejia.beans.ShareInfo;
import com.qixiu.lejia.utils.Logger;
import com.qixiu.lejia.utils.ToastUtils;
import com.qixiu.thirdparty.SdkConstants;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by Long on 2018/5/7
 * <pre>
 *     分享面板
 * </pre>
 */
public class SharePanel extends BottomSheetDialogFragment implements View.OnClickListener {

    private static final String TAG = "SharePanel";

    private static final String KEY_SHARE_INFO = "SHARE_INFO";

    private WeakReference<Activity> mHostAct;
    private IUiListener             iUiListener;

    private ShareInfo mShareInfo;

    private Bitmap mShareBitmap;

    public static SharePanel newInstance(ShareInfo shareInfo) {
        Bundle args = new Bundle();
        args.putParcelable(KEY_SHARE_INFO, shareInfo);
        SharePanel panel = new SharePanel();
        panel.setArguments(args);
        return panel;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IUiListener) {
            iUiListener = (IUiListener) context;
        }
        mHostAct = new WeakReference<>((Activity) context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mShareInfo = args.getParcelable(KEY_SHARE_INFO);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_share_panel, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.share_qq).setOnClickListener(this);
        view.findViewById(R.id.share_qzone).setOnClickListener(this);
        view.findViewById(R.id.share_wx).setOnClickListener(this);
        view.findViewById(R.id.share_wx_circle).setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //预加载要分享的图片
        loadShareImage();
    }

    private void loadShareImage() {
        //微信分享需要下载图片
        if (mShareInfo.getShareImageUrl() == null) {
            return;
        }
        Glide.with(this)
                .load(mShareInfo.getShareImageUrl())
                .asBitmap()
                .transform(new WXBitmapTransformation(getContext()))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        Logger.d(TAG, "onResourceReady: 分享图片下载成功！---- width = " + resource.getWidth() +
                                "---- height = " + resource.getHeight());
                        String size = Formatter.formatFileSize(getContext(), resource.getByteCount());
                        Logger.d(TAG, "onResourceReady: bitmap size = " + size);
                        mShareBitmap = resource;
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        Logger.d(TAG, "onLoadFailed: 分享图片下载失败！");
                        if (e != null) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    public void show(FragmentManager fm) {
        super.show(fm, TAG);
    }


    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (mHostAct == null || mHostAct.get() == null) {
            dismiss();
            return;
        }

        if (mShareInfo == null) {
            ToastUtils.showShort(getContext(), "分享失败");
            dismiss();
            return;
        }

        doShare(id);
    }

    private void doShare(int id) {
        switch (id) {
            case R.id.share_qq:
                shareToQQ();
                break;
            case R.id.share_qzone:
                shareToQzone();
                break;
            case R.id.share_wx:
                shareToWX(1);
                break;
            case R.id.share_wx_circle:
                shareToWX(2);
                break;
        }

        dismiss();
    }


    private void shareToQQ() {
        Tencent tencent = Tencent.createInstance(SdkConstants.QQ_APP_ID, AppContext.getContext());
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);

        params.putString(QQShare.SHARE_TO_QQ_TITLE, mShareInfo.getTitle());
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, mShareInfo.getContent());

        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, mShareInfo.getShareUrl());
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, mShareInfo.getShareImageUrl());
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "乐家小屋");
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
        tencent.shareToQQ(mHostAct.get(), params, iUiListener);
    }

    private void shareToQzone() {
        Tencent tencent = Tencent.createInstance(SdkConstants.QQ_APP_ID, AppContext.getContext());
        ArrayList<String> list = new ArrayList<>();
        list.add(mShareInfo.getShareImageUrl());

        final Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, mShareInfo.getTitle());//必填
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, mShareInfo.getContent());//选填
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, mShareInfo.getShareUrl());//必填
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, list);
        tencent.shareToQzone(mHostAct.get(), params, iUiListener);
    }


    private void shareToWX(int scene) {
        WXWebpageObject obj = new WXWebpageObject();
        obj.webpageUrl = mShareInfo.getShareUrl();

        WXMediaMessage msg = new WXMediaMessage(obj);
        msg.title = mShareInfo.getTitle();
        msg.description = mShareInfo.getContent();

        if (mShareBitmap == null) {
            //分享图片下载失败，使用默认APP图标当做分享图片
            msg.setThumbImage(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        } else {
            msg.setThumbImage(mShareBitmap);
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "webpage";
        req.message = msg;
        req.scene = scene == 1 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;

        IWXAPI wxapi = WXAPIFactory.createWXAPI(AppContext.getContext(), SdkConstants.WX_APP_ID, false);
        wxapi.sendReq(req);

    }

}
