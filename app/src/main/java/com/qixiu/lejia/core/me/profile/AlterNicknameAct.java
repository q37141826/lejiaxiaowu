package com.qixiu.lejia.core.me.profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.qixiu.lejia.R;
import com.qixiu.lejia.api.AppApi;
import com.qixiu.lejia.api.RequestCallback;
import com.qixiu.lejia.app.LoginStatus;
import com.qixiu.lejia.base.BaseToolbarAct;

/**
 * Created by Long on 2018/5/4
 * <pre>
 *     修改昵称
 * </pre>
 */
public class AlterNicknameAct extends BaseToolbarAct {

    private static final String KEY_NICKNAME = "NICKNAME";

    private EditText mEdit;

    private String mOldNickname;

    public static void start(Context context, String oldNickname) {
        Intent starter = new Intent(context, AlterNicknameAct.class);
        starter.putExtra(KEY_NICKNAME, oldNickname);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(Color.WHITE);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//设置状态栏黑色字体
            //            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
//                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        mOldNickname = getIntent().getStringExtra(KEY_NICKNAME);

        setContentView(R.layout.act_alter_nickname);

        mEdit = findViewById(R.id.edit);
        mEdit.setText(mOldNickname);

        mEdit.setSelection(mOldNickname.length());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_affirm, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.affirm) {
            String newNickname = mEdit.getText().toString();
            if (!newNickname.equals(mOldNickname)) {
                alterNickname(newNickname);
            } else {
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("unchecked")
    private void alterNickname(String newNickname) {
        call = AppApi.get().alterNickname(LoginStatus.getToken(), newNickname);
        call.enqueue(new RequestCallback(this) {
            @Override
            protected void onSuccess(Object o) {
                //修改昵称成功
                ProfileEvent.getInstance().profileUpdate();
                finish();
            }
        });

    }
}
