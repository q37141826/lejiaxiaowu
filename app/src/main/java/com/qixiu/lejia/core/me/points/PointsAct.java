package com.qixiu.lejia.core.me.points;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qixiu.adapter.myrecycler.RecyclerBaseAdapter;
import com.qixiu.adapter.myrecycler.RecyclerBaseHolder;
import com.qixiu.lejia.BuildConfig;
import com.qixiu.lejia.R;
import com.qixiu.lejia.api.AppApi;
import com.qixiu.lejia.api.RequestCallback;
import com.qixiu.lejia.api.ResponseError;
import com.qixiu.lejia.api.request.BaseBean;
import com.qixiu.lejia.api.request.C_CodeBean;
import com.qixiu.lejia.api.request.OKHttpRequestModel;
import com.qixiu.lejia.api.request.OKHttpUIUpdataListener;
import com.qixiu.lejia.app.LoginStatus;
import com.qixiu.lejia.base.BaseWhiteStateBarActivity;
import com.qixiu.lejia.beans.PointsResp;
import com.qixiu.lejia.core.web.WebActivity;

import java.util.List;

import okhttp3.Call;

/**
 * Created by Long on 2018/5/16
 * <pre>
 *     信用积分
 * </pre>
 */
public class PointsAct extends BaseWhiteStateBarActivity implements View.OnClickListener, OKHttpUIUpdataListener {
    String listurl = BuildConfig.BASE_URL + "/Home/Index/integralrule";
    OKHttpRequestModel okHttpRequestModel = new OKHttpRequestModel(this);
    private TextView nickname;
    private TextView points;
    private LinearLayout lotto;
    private PointsAdapter adapterAdd;
    private PointsAdapter adapterMinues;


    public static void start(Context context) {
        Intent starter = new Intent(context, PointsAct.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadPoints();
        okHttpRequestModel.okHttpGet(listurl, null, new RulesBean());

    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    protected View onCreateContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.act_points, null, false);
    }

    @Override
    protected void onContentViewCreated(View view) {
        RecyclerView recyclerViewAdd=view.findViewById(R.id.recyclerViewAdd);
        RecyclerView recyclerViewMinues=view.findViewById(R.id.recyclerViewMinues);
        recyclerViewAdd.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMinues.setLayoutManager(new LinearLayoutManager(this));
        adapterAdd = new PointsAdapter();
        adapterMinues = new PointsAdapter();
        recyclerViewAdd.setAdapter(adapterAdd);
        recyclerViewMinues.setAdapter(adapterMinues);

        nickname = view.findViewById(R.id.nickname);
        points = view.findViewById(R.id.points);
        lotto = view.findViewById(R.id.lotto_layout); //抽奖
        lotto.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_points, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.detail) {
            // PointsDetailAct.start(this);
            PointsDetailNewAct.start(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onReload() {
        loadPoints();
    }

    @SuppressWarnings("unchecked")
    private void loadPoints() {
        call = AppApi.get().getPoints(LoginStatus.getToken());
        call.enqueue(new RequestCallback<PointsResp>() {
            @Override
            protected void onSuccess(PointsResp resp) {
                switchToContentState();
                nickname.setText(resp.getName());
                points.setText(resp.getPoints());
            }

            @Override
            protected void onFailure(ResponseError error) {
                switchToErrorState();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lotto_layout:
                //TODO 跳转到抽奖界面
//                MyPrizeAct.start(this);
                WebActivity.start(this, "积分大抽奖", (BuildConfig.BASE_H5_URL2 + "/goodaward.html?uid=" + LoginStatus.getToken()).replace("/tale.html", ""));
        }
    }

    @Override
    public void onSuccess(Object data, int i) {
        if (data instanceof RulesBean) {
            RulesBean rulesBean = (RulesBean) data;
            adapterAdd.refreshData(rulesBean.getO().getAdd());
            adapterMinues.refreshData(rulesBean.getO().getDeduct());
        }
    }

    public class PointsAdapter extends RecyclerBaseAdapter {



        @Override
        public int getLayoutId() {
            return R.layout.item_point_rules;
        }

        @Override
        public Object createViewHolder(View itemView, Context context, int viewType) {
            return new PointsHolder(itemView, context, this);
        }

        public class PointsHolder extends RecyclerBaseHolder {
            TextView textViewTitle, textViewIntroduce, textViewPoints;

            public PointsHolder(View itemView, Context context, RecyclerView.Adapter adapter) {
                super(itemView, context, adapter);
                textViewTitle=itemView.findViewById(R.id.textViewTitle);
                textViewIntroduce=itemView.findViewById(R.id.textViewIntroduce);
                textViewPoints=itemView.findViewById(R.id.textViewPoints);
            }

            @Override
            public void bindHolder(int position) {
                if(mData instanceof RulesBean.OBean.AddBean){
                    RulesBean.OBean.AddBean addBean= ( RulesBean.OBean.AddBean) mData;
                    textViewTitle.setText(addBean.getTitle());
                    textViewIntroduce.setText(addBean.getIntroduction());
                    textViewPoints.setText("+  "+addBean.getNum());
                    textViewPoints.setTextColor(mContext.getResources().getColor(R.color.color_points));
                }
                if(mData instanceof RulesBean.OBean.DeductBean){
                    RulesBean.OBean.DeductBean reduceBean= (RulesBean.OBean.DeductBean) mData;
                    textViewTitle.setText(reduceBean.getTitle());
                    textViewIntroduce.setText(reduceBean.getIntroduction());
                    textViewPoints.setText("-  "+reduceBean.getNum());
                    textViewPoints.setTextColor(mContext.getResources().getColor(R.color.red));
                }
            }
        }
    }


    @Override
    public void onError(Call call, Exception e, int i) {

    }

    @Override
    public void onFailure(C_CodeBean c_codeBean) {

    }


    public class RulesBean extends BaseBean<RulesBean.OBean> {
        public class OBean {
            /**
             * id : 1
             * title : 按时预约看房
             * introduction : 需门店人员确认
             * num : 10
             * type : 1
             * createtime : 2018-09-20 10:41:54
             * status : 1
             */

            private List<AddBean> add;
            /**
             * id : 7
             * title : 积分抽奖
             * introduction : 每次
             * num : 5
             * type : 2
             * createtime : 2018-09-20 10:47:48
             * status : 1
             */
            private List<DeductBean> deduct;

            public List<AddBean> getAdd() {
                return add;
            }

            public void setAdd(List<AddBean> add) {
                this.add = add;
            }

            public List<DeductBean> getDeduct() {
                return deduct;
            }

            public void setDeduct(List<DeductBean> deduct) {
                this.deduct = deduct;
            }

            public class AddBean {
                private String id;
                private String title;
                private String introduction;
                private String num;
                private String type;
                private String createtime;
                private String status;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getIntroduction() {
                    return introduction;
                }

                public void setIntroduction(String introduction) {
                    this.introduction = introduction;
                }

                public String getNum() {
                    return num;
                }

                public void setNum(String num) {
                    this.num = num;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public String getCreatetime() {
                    return createtime;
                }

                public void setCreatetime(String createtime) {
                    this.createtime = createtime;
                }

                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }
            }

            public class DeductBean {
                private String id;
                private String title;
                private String introduction;
                private String num;
                private String type;
                private String createtime;
                private String status;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getIntroduction() {
                    return introduction;
                }

                public void setIntroduction(String introduction) {
                    this.introduction = introduction;
                }

                public String getNum() {
                    return num;
                }

                public void setNum(String num) {
                    this.num = num;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public String getCreatetime() {
                    return createtime;
                }

                public void setCreatetime(String createtime) {
                    this.createtime = createtime;
                }

                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }
            }
        }
    }


}
