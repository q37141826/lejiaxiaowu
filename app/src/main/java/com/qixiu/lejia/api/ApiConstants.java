package com.qixiu.lejia.api;

import android.text.TextUtils;

import com.qixiu.lejia.BuildConfig;
import com.qixiu.lejia.app.LoginStatus;

import java.util.Map;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

/**
 * Created by Long on 2018/4/28
 */
public final class ApiConstants {
    public static final String RMB="¥  ";
//    public  static String   hostWebUrl="http://lj.lejiaxiaowu.com/ljhouse.app0.001";
    public  static String   hostWebUrl="http://lejia.whtkl.cn/ljhouse.app0.001";

    private ApiConstants() {
    }

    //签约
    public static final String SIGNED      = "mysign";
    //签约详情
    public static final String SIGN_DETAIL = "signdetail";

    //预约
    public static final String APPOINTMENT = "myorder";

    //信用
    public static final String CREDIT = "myorder";

    //消息
    public static final String MESSAGE = "message";

    //个人资料
    public static final String PROFILE = "personal";

    //个人资料 其他资料
    public static final String OTHER_PROFILE = "otherdata";

    //室友
    public static final String ROOMIE = "message";

    //离我最近
    public static final String NEARBY = "housemap";

    //房间详情
    public static final String HOUSE_DETAIL = "housedetail";

    //门店详情
    public static final String HOUSE_INTRO = "houseintro";

    //房租
    public static final String RENT = "houserent";

    public static final String BILL = "bill";

    //水电
    public static final String HYDROELECTRIC = "waterpower";

    //维修
    public static final String REPAIR = "houserent";

    //提问
    public static final String QUESTIONS = "houserent";

    //投诉
    public static final String COMPLAINT = "houserent";

    //首页活动
    public static final String EVENT = "activity";

    //推荐
    public static final String RECOMMEND = "recommend";


    /**
     * 构造完整的H5访问地址
     *
     * @param path   相对路径
     * @param params 参数
     */
    public static String buildUrl(@NonNull String path, @Nullable Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        sb.append(BuildConfig.BASE_H5_URL)
                .append(path);

        if (!path.contains("?")) {
            sb.append("?");
        } else {
            sb.append("&");
        }

        //全局添加uid
        String token = LoginStatus.getToken();
        if (!TextUtils.isEmpty(token)) {
            sb.append("uid=")
                    .append(token)
                    .append("&");
        }

        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                sb.append(entry.getKey())
                        .append("=")
                        .append(entry.getValue())
                        .append("&");
            }
        }
        //删除最后一个&字符
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

}
