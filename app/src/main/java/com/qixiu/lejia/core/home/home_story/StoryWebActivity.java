package com.qixiu.lejia.core.home.home_story;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;

import com.qixiu.lejia.R;
import com.qixiu.lejia.base.BaseWhiteStateBarActivity;
import com.qixiu.lejia.beans.home.story.StoryBean;
import com.qixiu.lejia.utils.HtmlFormat;

public class StoryWebActivity extends BaseWhiteStateBarActivity {
        WebView webview;

    public static void start(Context context, Parcelable data){
        Intent intent=new Intent(context,StoryWebActivity.class);
        intent.putExtra("data",data);
        context.startActivity(intent);

    }
    @NonNull
    @Override
    protected View onCreateContentView(LayoutInflater inflater) {
        return  inflater.inflate(R.layout.activity_story_web, null, false);
    }

    @Override
    protected void onContentViewCreated(View view) {
        webview=view.findViewById(R.id.webview);
        StoryBean.ListBean bean=   getIntent().getParcelableExtra("data");
        setTitle(bean.getTitle());
        String filePath = HtmlFormat.getNewContent(bean.getContent());
        String baseUrl = "file://" + filePath;
        webview.loadDataWithBaseURL(baseUrl, filePath, "text/html", "utf-8", null);

    }

    @Override
    protected void onReload() {



    }
}
