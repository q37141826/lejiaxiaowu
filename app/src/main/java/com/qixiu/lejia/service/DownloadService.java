package com.qixiu.lejia.service;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;


import com.qixiu.lejia.utils.FileProviderUtil;
import com.qixiu.lejia.utils.Logger;

import java.io.File;

/**
 * Created by Long on 2016/10/9
 * APK 下载服务
 */
public class DownloadService extends Service {

    private static final String TAG = "DownloadService";

    public static boolean isRunning = false;

    private static final String KEY_DOWNLOAD_URL = "DOWNLOAD_URL";
    private static final String KEY_FILE_PATH = "FILE_PATH";

    private long mTaskId;
    private DownloadManager mDownloadManager;

    //文件下载路径
    private String mFilePath;


    /**
     * 启动下载文件服务
     *
     * @param context     context
     * @param filePath    文件绝对路径,必须是SD卡路径
     * @param downloadUrl 下载地址
     */
    public static void start(Context context, @NonNull String filePath, @NonNull String downloadUrl) {
        Intent starter = new Intent(context, DownloadService.class);
        starter.putExtra(KEY_DOWNLOAD_URL, downloadUrl);
        starter.putExtra(KEY_FILE_PATH, filePath);
        context.startService(starter);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isRunning = true;
        Logger.d(TAG, "onCreate: 启动下载服务");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mFilePath = intent.getStringExtra(KEY_FILE_PATH);
        String downloadUrl = intent.getStringExtra(KEY_DOWNLOAD_URL);

        if (TextUtils.isEmpty(downloadUrl)) {
            stopSelf();
            return START_NOT_STICKY;
        }

        Logger.d(TAG, "onStartCommand: 文件下载地址" + mFilePath);

        Logger.d(TAG, "onStartCommand:开始下载 " + downloadUrl);

        //创建下载任务,downloadUrl就是下载链接
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));

        //设置通知标题(文件名)
//        request.setTitle(mFilePath.substring(mFilePath.lastIndexOf("/") + 1));

        //漫游不下载
        request.setAllowedOverRoaming(false);

        //设置文件类型，可以在下载结束后自动打开该文件
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(downloadUrl));
        request.setMimeType(mimeString);

        //在通知栏中显示，默认就是显示的
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setVisibleInDownloadsUi(true);

        //允许被扫描
        request.allowScanningByMediaScanner();

        //指定下载路径和下载文件名
        request.setDestinationUri(Uri.parse("file://" + mFilePath));

        //获取下载管理器
        mDownloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

        //将下载任务加入下载队列，否则不会进行下载
        mTaskId = mDownloadManager.enqueue(request);

        //注册广播接收者，监听下载状态
        registerReceiver(receiver,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        return START_REDELIVER_INTENT;
    }

    //广播接受者，接收下载状态
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkDownloadStatus();//检查下载状态
        }
    };

    //检查下载状态
    private void checkDownloadStatus() {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(mTaskId);//筛选下载任务，传入任务ID，可变参数
        Cursor c = mDownloadManager.query(query);
        if (c.moveToFirst()) {
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                case DownloadManager.STATUS_PAUSED:
                    Logger.d(TAG, ">>>下载暂停");
                case DownloadManager.STATUS_PENDING:
                    Logger.d(TAG, ">>>下载延迟");
                case DownloadManager.STATUS_RUNNING:
                    Logger.d(TAG, ">>>正在下载");
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    Logger.d(TAG, ">>>下载完成");
                    //下载完成安装APK
                    installAPK(new File(mFilePath));
                    break;
                case DownloadManager.STATUS_FAILED:
                    Logger.d(TAG, ">>>下载失败");
                    break;
            }
        }
    }

    /**
     * 安装APK
     *
     * @param file APK
     */
    private void installAPK(File file) {
        if (!file.exists()) return;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProviderUtil.getUriForFile(getApplicationContext(), file);
        } else {
            uri = Uri.parse("file://" + file.toString());
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        //在服务中开启activity必须设置flag,后面解释
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
        //销毁服务
        stopSelf();
    }

    @Override
    public void onDestroy() {
        isRunning = false;
        unregisterReceiver(receiver);
        super.onDestroy();
        Logger.d(TAG, "onDestroy: ");
    }
}
