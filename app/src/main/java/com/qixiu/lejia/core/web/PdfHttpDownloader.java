package com.qixiu.lejia.core.web;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

public class PdfHttpDownloader {
    private Context context;

    public PdfHttpDownloader(Context context) {
        this.context = context;
    }

    /**
     * 打开pdf
     * pdf:PDF url
     * name:pdf文件的名字
     */
    public void startPdfActivity(String pdf, String name) {
        String terPath = getSDPath() + "/trader/" + name + ".PDF";
        File file = new File(terPath);
        if (file.exists()) {
            Intent intent = getPdfFileIntent(terPath);
            context.startActivity(intent);
        }
    }

    public static Intent getPdfFileIntent(String path) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(path));
        i.setDataAndType(uri, "application/pdf");
        return i;
    }


    private String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);   //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();
    }
}
