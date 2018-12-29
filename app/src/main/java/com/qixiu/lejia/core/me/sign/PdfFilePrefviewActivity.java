package com.qixiu.lejia.core.me.sign;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.qixiu.lejia.R;
import com.qixiu.lejia.base.BaseWhiteStateBarActivity;
import com.qixiu.lejia.utils.PictureCut;
import com.tencent.smtt.sdk.TbsReaderView;
import com.tencent.smtt.sdk.WebView;

import java.io.File;

import static com.qixiu.lejia.core.sign.BaseSignPayActivity.DATA;

public class PdfFilePrefviewActivity extends BaseWhiteStateBarActivity implements TbsReaderView.ReaderCallback {
    TbsReaderView tbsView;
    RelativeLayout rl_tbsView;
    private WebView webview;

    public static void start(Context context, String path) {
        Intent intent = new Intent(context, PdfFilePrefviewActivity.class);
        intent.putExtra(DATA, path);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_file_prefview);
    }

    @NonNull
    @Override
    protected View onCreateContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_pdf_file_prefview, null, false);
    }

    @Override
    protected void onContentViewCreated(View view) {
        rl_tbsView = view.findViewById(R.id.rl_tbsView);
        webview = view.findViewById(R.id.webview);
        tbsView = new TbsReaderView(getContext(), this);
        rl_tbsView.addView(tbsView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        setTitle("合同预览");
    }

    @Override
    protected void onReload() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        switchToContentState();
        String path = getIntent().getStringExtra(DATA);
        File file = new File(path + ".pdf");
        PictureCut.cutFile(new File(path), file);
        webview.loadData(file.getPath(), "text/html", "UTF-8");

//        displayFile(file.getPath());
    }

    private void displayFile(String path) {
        Bundle bundle = new Bundle();
        bundle.putString("filePath", path);
        bundle.putString("tempPath", Environment.getExternalStorageDirectory().getPath());
        boolean result = tbsView.preOpen(getFileType(new File(path).getName()), false);
        if (result) {
            tbsView.openFile(bundle);
        }
    }


    @Override
    public void onCallBackAction(Integer integer, Object o, Object o1) {

    }

    /**
     * 后缀名的判断
     *
     * @param paramString
     * @return
     */
    private String getFileType(String paramString) {
        String str = "";
        if (TextUtils.isEmpty(paramString)) {
            Log.d("print", "paramString---->null");
            return str;
        }
        Log.d("print", "paramString:" + paramString);
        int i = paramString.lastIndexOf('.');
        if (i <= -1) {
            Log.d("print", "i <= -1");
            return str;
        }

        str = paramString.substring(i + 1);
        Log.d("print", "paramString.substring(i + 1)------>" + str);
        return str;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tbsView.onStop();
    }
}
