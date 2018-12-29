package com.qixiu.lejia.core.me.message;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.qixiu.lejia.BuildConfig;
import com.qixiu.lejia.R;
import com.qixiu.lejia.api.request.C_CodeBean;
import com.qixiu.lejia.api.request.OKHttpRequestModel;
import com.qixiu.lejia.api.request.OKHttpUIUpdataListener;
import com.qixiu.lejia.app.LoginStatus;
import com.qixiu.lejia.base.BaseWhiteStateBarActivity;
import com.qixiu.lejia.beans.mine.MessageDetailsBean;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class MessageDetailsActivity extends BaseWhiteStateBarActivity implements OKHttpUIUpdataListener {
    private String detailsUrl = BuildConfig.BASE_URL + "/Home/UserCenter/noticedetail";
    private OKHttpRequestModel okHttpRequestModel;
    private String ne_id;
    private  TextView textViedw_title_messageDetails,
            textview_time_messageDetails,
            textView_content_message_details,
            textView_phone_messageDetails;

    public static void start(Context context, Intent intent) {
        if (intent == null) {
            intent = new Intent(context, MessageListActivity.class);
        }
        context.startActivity(intent);
    }

    @NonNull
    @Override
    protected View onCreateContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.activity_message_details, null, false);
    }

    @Override
    protected void onContentViewCreated(View view) {
        textViedw_title_messageDetails=findViewById(R.id.textViedw_title_messageDetails);
        textview_time_messageDetails=findViewById(R.id.textview_time_messageDetails);
        textView_content_message_details=findViewById(R.id.textView_content_message_details);
        textView_phone_messageDetails=findViewById(R.id.textView_phone_messageDetails);


        setTitle("乐家助手");
        okHttpRequestModel = new OKHttpRequestModel(this);
        ne_id = getIntent().getStringExtra("id");
        load();
    }

    @SuppressWarnings("unchecked")
    private void load() {
        Map<String, String> map = new HashMap<>();
        map.put("uid", LoginStatus.getToken());
        map.put("ne_id", ne_id);
        okHttpRequestModel.okhHttpPost(detailsUrl, map, new MessageDetailsBean());
    }

    @Override
    protected void onReload() {

    }

    @Override
    public void onSuccess(Object data, int i) {
        if (data instanceof MessageDetailsBean) {
            MessageDetailsBean bean = (MessageDetailsBean) data;
            textViedw_title_messageDetails.setText(bean.getO().getNe_title());
            textview_time_messageDetails.setText(bean.getO().getNe_createtime());
            textView_content_message_details.setText(bean.getO().getNe_content());
            textView_phone_messageDetails.setText(bean.getO().getSt_house_tel());

        }
        switchToContentState();
    }

    @Override
    public void onError(Call call, Exception e, int i) {
        switchToErrorState();
    }

    @Override
    public void onFailure(C_CodeBean c_codeBean) {
        switchToContentState();
    }
}
