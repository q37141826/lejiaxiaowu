package com.qixiu.lejia.api;

import com.qixiu.lejia.beans.BillHistory;
import com.qixiu.lejia.beans.Complaint;
import com.qixiu.lejia.beans.ComplaintTag;
import com.qixiu.lejia.beans.NoReadMessage;
import com.qixiu.lejia.beans.PayDetails;
import com.qixiu.lejia.beans.PointsHistory;
import com.qixiu.lejia.beans.PointsResp;
import com.qixiu.lejia.beans.QualificationInfo;
import com.qixiu.lejia.beans.RealProfile;
import com.qixiu.lejia.beans.Rent;
import com.qixiu.lejia.beans.RentDetail;
import com.qixiu.lejia.beans.RentPayStatus;
import com.qixiu.lejia.beans.Repair;
import com.qixiu.lejia.beans.RepairDetail;
import com.qixiu.lejia.beans.Room;
import com.qixiu.lejia.beans.RoomDetail;
import com.qixiu.lejia.beans.Roommate;
import com.qixiu.lejia.beans.ServiceProject;
import com.qixiu.lejia.beans.ShareInfo;
import com.qixiu.lejia.beans.Shop;
import com.qixiu.lejia.beans.ShopDetail;
import com.qixiu.lejia.beans.ShopLocation;
import com.qixiu.lejia.beans.SignedRoom;
import com.qixiu.lejia.beans.home.story.StoryBean;
import com.qixiu.lejia.beans.Token;
import com.qixiu.lejia.beans.UserLevel;
import com.qixiu.lejia.beans.UserProfile;
import com.qixiu.lejia.beans.WECostDetail;
import com.qixiu.lejia.beans.WXPayInfo;
import com.qixiu.lejia.beans.WePayStatus;
import com.qixiu.lejia.beans.mine.MessageListBean;
import com.qixiu.lejia.beans.resp.CorporateSignResp;
import com.qixiu.lejia.beans.resp.HomeResp;
import com.qixiu.lejia.beans.resp.LifeServiceResp;
import com.qixiu.lejia.beans.resp.MeResp;
import com.qixiu.lejia.beans.resp.QuestionResp;
import com.qixiu.lejia.core.version.Version;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Long on 2018/4/20
 * <pre>
 *     API接口  （测试 uid  2210）
 * </pre>
 */
public interface ApiService {

    /**
     * 获取微信access_token
     */
    @GET("https://api.weixin.qq.com/sns/oauth2/access_token?grant_type=authorization_code")
    Observable<String> getWXAccessToken(@Query("appid") String appId,
                                        @Query("secret") String secret,
                                        @Query("code") String code);

    /**
     * 获取微信用户资料
     */
    @GET("https://api.weixin.qq.com/sns/userinfo")
    Observable<String> getWXUserInfo(@Query("access_token") String accessToken,
                                     @Query("openid") String openId);

    /**
     * 获取短信验证码
     *
     * @param phone 手机号
     * @param scene 场景 1是登录,2是绑定手机,3是预约看房,4是立即签约第一步,5是企业员工认证,
     *              6是企业员工完善信息
     * @return call
     */
    @POST("Home/Base/send")
    @FormUrlEncoded
    Call<BaseResponse<Object>> getCode(@Field("tel") String phone, @Field("type") int scene);

    /**
     * 登录
     *
     * @param phone 手机号
     * @param code  验证码
     * @return call
     */
    @POST("Home/Login/login")
    @FormUrlEncoded
    Call<BaseResponse<Token>> login(@Field("tel") String phone,
                                    @Field("code") String code,
                                    @Field("type") String type,
                                    @Field("device_id") String device_id);


    /**
     * 第三方登录
     *
     * @param deviceId 设备唯一标识
     * @param nickname 昵称
     * @param avatar   头像
     */
    @POST("Home/Login/loginouth")
    @FormUrlEncoded
    Call<BaseResponse<Token>> thirdLogin(@Field("nickname") String nickname,
                                         @Field("user_log") String avatar,
                                         @Field("partyKey") String openid,
                                         @Field("type") int type,
                                         @Field("device_id") String deviceId);

    @POST("Home/Login/binding")
    @FormUrlEncoded
    Call<BaseResponse<Token>> loginBind(@Field("tel") String phone,
                                        @Field("code") String code,
                                        @Field("nickname") String nickname,
                                        @Field("user_log") String avatar,
                                        @Field("type") int type,
                                        @Field("partyKey") String openid,
                                        @Field("device_id") String deviceId);

    /**
     * 首页
     *
     * @return call
     */
    @GET("Home/Index/getindex")
    Call<BaseResponse<HomeResp>> index();

    @GET("Home/Life/storylist")
    Call<BaseResponse<List<StoryBean>>> storylist();


    /**
     * 我的
     *
     * @param token 用户登录凭证
     * @return call
     */
    @POST("Home/UserCenter/userlist")
    @FormUrlEncoded
    Call<BaseResponse<MeResp>> meInfo(@Field("uid") String token);


    /**
     * 生活服务
     *
     * @return call
     */
    @GET("Home/Life/banner")
    Call<BaseResponse<LifeServiceResp>> lifeService();


    /**
     * 用户签约第一步
     *
     * @param uid   uid
     * @param name  用户姓名
     * @param phone 手机号码
     * @param sex   性别
     * @param id    身份证号码
     * @param code  验证码
     * @return call
     */
    @POST("Home/Sign/usersignone")
    @FormUrlEncoded
    Call<BaseResponse<String>> signFirstStep(@Field("uid") String uid,
                                             @Field("name") String name,
                                             @Field("tel") String phone,
                                             @Field("sex") String sex,
                                             @Field("identity") String id,
                                             @Field("code") String code);
//                                             @Field("type") String type);

    /**
     * 用户签约第二步
     *
     * @param uid             uid
     * @param industry        行业
     * @param company         公司
     * @param companyAddress  公司地址
     * @param contactName     紧急联系人
     * @param contactPhone    紧急联系人手机
     * @param contactRelation 与紧急联系人关系
     * @return call
     */
    @POST("Home/Sign/usersigntwo")
    @FormUrlEncoded
    Call<BaseResponse<String>> signSecondStep(@Field("uid") String uid,
                                              @Field("industry") String industry,
                                              @Field("company_name") String company,
                                              @Field("address") String companyAddress,
                                              @Field("urgent_name") String contactName,
                                              @Field("urgent_tel") String contactPhone,
                                              @Field("relation") String contactRelation);

    /**
     * 获取用户级别
     *
     * @param uid uid
     * @return UserLevel
     */
    @POST("Home/Sign/usersignthree")
    @FormUrlEncoded
    Call<BaseResponse<UserLevel>> userLevel(@Field("uid") String uid, @Field("st_id") String stId);

    /**
     * 用户签约第四步
     *
     * @param uid uid
     * @return room
     */
    @POST("Home/sign/usersignfour")
    @FormUrlEncoded
    Call<BaseResponse<Room>> signFourStep(@Field("uid") String uid);


    /**
     * 个人用户签约第五步
     */
    @POST("Home/Sign/userpay")
    @FormUrlEncoded
    Call<BaseResponse<Room>> personalSignFifthStep(@Field("uid") String uid);

    /**
     * 企业用户第五步
     */
    @POST("Home/sign/userpayfive")
    @FormUrlEncoded
    Call<BaseResponse<CorporateSignResp>> corporateSignFifthStep(@Field("uid") String uid);

    /**
     * 计算房租
     *
     * @param uid     uid
     * @param roomId  房间id
     * @param lease   租期
     * @param periods 期数
     */
    @POST("Home/Sign/getmoney")
    @FormUrlEncoded
    Call<BaseResponse<Rent>> calculateRent(@Field("uid") String uid,
                                           @Field("ro_id") String roomId,
                                           @Field("time") int lease,
                                           @Field("sign_type") String sign_type,
                                           @Field("paytype") int periods);

    /**
     * 个人缴费明细
     */
    @POST("Home/Sign/getmoneydetail")
    @FormUrlEncoded
    Call<BaseResponse<List<PayDetails>>> personalPayDetails(@Field("uid") String uid,
                                                            @Field("ro_id") String roomId,
                                                            @Field("time") int lease,
                                                            @Field("paytype") int periods);

    /**
     * 企业用户缴费明细
     */
    @POST("Home/Sign/userpayfivedetail")
    @FormUrlEncoded
    Call<BaseResponse<List<PayDetails>>> corporatePayDetails(@Field("uid") String uid);


    /**
     * 个人资料
     */
    @POST("Home/UserCenter/personalinfo")
    @FormUrlEncoded
    Call<BaseResponse<UserProfile>> userProfile(@Field("uid") String uid);

    /**
     * 其他资料
     */
    @POST("Home/UserCenter/otherinfo")
    @FormUrlEncoded
    Call<BaseResponse<UserProfile>> userOtherProfile(@Field("uid") String uid);

    /**
     * 修改昵称
     */
    @POST("Home/UserCenter/updnickname")
    @FormUrlEncoded
    Call<BaseResponse> alterNickname(@Field("uid") String uid,
                                     @Field("ud_nickname") String nickname);

    /**
     * 修改头像
     */
    @POST("Home/UserCenter/updinfo")
    @FormUrlEncoded
    Call<BaseResponse> alterAvatar(@Field("uid") String uid,
                                   @Field("img") String base64Image);


    /**
     * 微信支付
     *
     * @param uid        uid
     * @param payWay     支付方式1是微信,2是支付宝
     * @param money      支付金额
     * @param type       缴费类型1是房租,2是水费,3是电费4是续租
     * @param roomId     用户缴费的房间id
     * @param lease      租期,签约时传入
     * @param periods    付款方式,1是押一付一,2是押一付三,3是半年付,4是全年付
     * @param firstPay   首次支付金额
     * @param monthlyPay 其余每次支付金额
     * @param rentBillId 房租账单id,如果用户是在房租账单页面支付,传入该id
     * @param weBillId   水电费账单id,如果用户是在水电费页面支付,传入该id
     */
    @POST("Home/Life/gopay")
    @FormUrlEncoded
    Call<BaseResponse<WXPayInfo>> wxPay(@Field("uid") String uid,
                                        @Field("paytype") int payWay,
                                        @Field("money") String money,
                                        @Field("type") int type,
                                        @Field("ro_id") String roomId,
                                        @Field("time") int lease,
                                        @Field("sd_money_type") int periods,
                                        @Field("sd_first_pay") String firstPay,
                                        @Field("sd_surplus_pay") String monthlyPay,
                                        @Field("pa_id") String rentBillId,
                                        @Field("hy_id") String weBillId);

    /**
     * 支付宝支付(参数同微信支付，因为返回类型不一样，才声明2个方法)
     */
    @POST("Home/Life/gopay")
    @FormUrlEncoded
    Call<BaseResponse<String>> aliPay(@Field("uid") String uid,
                                      @Field("paytype") int payWay,
                                      @Field("money") String money,
                                      @Field("type") int type,
                                      @Field("ro_id") String roomId,
                                      @Field("time") int lease,
                                      @Field("sd_money_type") int periods,
                                      @Field("sd_first_pay") String firstPay,
                                      @Field("sd_surplus_pay") String monthlyPay,
                                      @Field("pa_id") String rentBillId,
                                      @Field("hy_id") String weBillId);


    /*
     * 支付宝充值电费
     * */
    @POST("Home/Elemeter/elemeter_charge")
    @FormUrlEncoded
    Call<BaseResponse<String>> aliPayElectric(@Field("user_id") String user_id,
                                                @Field("pay_money") String pay_money,
                                                @Field("equipment_uuid") String equipment_uuid,
                                                @Field("store_id") String store_id,
                                                @Field("room_id") String room_id,
                                                @Field("pay_method") int pay_method);


    /*
    * 微信充值电费
    * */

    @POST("Home/Elemeter/elemeter_charge")
    @FormUrlEncoded
    Call<BaseResponse<WXPayInfo>> wxPayElectric(@Field("user_id") String user_id,
                                                @Field("pay_money") String pay_money,
                                                @Field("equipment_uuid") String equipment_uuid,
                                                @Field("store_id") String store_id,
                                                @Field("room_id") String room_id,
                                                @Field("pay_method") int pay_method);


    @POST("Home/UserCenter/perfectinfo")
    @FormUrlEncoded
    Call<BaseResponse<RealProfile>> realProfile(@Field("uid") String uid);

    @POST("Home/UserCenter/qualificationinfo")
    @FormUrlEncoded
    Call<BaseResponse<QualificationInfo>> qualificationInfo(@Field("uid") String uid);


    @POST("Home/Life/share")
    @FormUrlEncoded
    Call<BaseResponse<ShareInfo>> shareInfo(@FieldMap Map<String, String> map);

    @POST("Home/Index/get_user_room")
    @FormUrlEncoded
    Call<BaseResponse<SignedRoom>> signedRoom(@Field("uid") String uid);

    @POST("Home/Store/housetel")
    @FormUrlEncoded
    Call<BaseResponse<String>> contactHousekeeper(@Field("ro_id") String roomId);

    @POST("Home/Sign/score")
    @FormUrlEncoded
    Call<BaseResponse> payScore(@Field("uid") String uid,
                                @Field("sd_id") String roomId,
                                @Field("sd_score") int score);

    @POST("Home/Sign/getUser")
    @FormUrlEncoded
    Call<BaseResponse<UserLevel>> getUserType(@Field("uid") String uid);

    /**
     * 常见问题
     */
    @GET("Home/Life/question")
    Call<BaseResponse<QuestionResp>> questions();

    /**
     * 投诉类型标签
     */
    @GET("Home/Life/advisetype")
    Observable<BaseResponse<List<ComplaintTag>>> complaintTags();

    /**
     * 投诉类型门店
     */
    @GET("Home/Life/storename")
    Observable<BaseResponse<List<Shop>>> complaintShops();

    /**
     * 投诉列表
     *
     * @param uid uid
     */
    @POST("Home/Life/adviselist")
    @FormUrlEncoded
    Call<BaseResponse<List<Complaint>>> complaintList(@Field("uid") String uid);

    /**
     * 新建投诉
     *
     * @param uid      uid
     * @param tagId    标签id
     * @param targetId 投诉对象id
     * @param content  投诉内容
     */
    @POST("Home/Life/addadvise")
    @FormUrlEncoded
    Call<BaseResponse> createComplaint(@Field("uid") String uid,
                                       @Field("ad_ct_id") String tagId,
                                       @Field("ad_st_id") String targetId,
                                       @Field("ad_content") String content);

    /**
     * 投诉详情
     *
     * @param id 详情id
     */
    @POST("Home/Life/advisedetail")
    @FormUrlEncoded
    Call<BaseResponse<Complaint>> complaintDetail(@Field("ad_id") String id);

    /**
     * 报修列表
     */
    @POST("Home/Repair/get_repair_list")
    @FormUrlEncoded
    Call<BaseResponse<List<Repair>>> repairList(@Field("uid") String uid);


    /**
     * 新增报修
     */
    @POST("Home/Repair/set_repair")
    Observable<ResponseStatus> createRepair(@Body MultipartBody body);


    @POST("Home/Index/get_user_room")
    @FormUrlEncoded
    Call<BaseResponse<SignedRoom>> signedRoomInfo(@Field("uid") String uid);


    @GET("Home/Repair/get_repair_type")
    Call<BaseResponse<List<ServiceProject>>> serviceProjects();

    /**
     * 报修详情
     */
    @POST("Home/Repair/get_repair_detail")
    @FormUrlEncoded
    Call<BaseResponse<RepairDetail>> repairDetail(@Field("ri_id") String id);

    /**
     * 完成报修
     */
    @POST("Home/Repair/good_repair")
    @FormUrlEncoded
    Call<BaseResponse> doneRepair(@Field("ri_id") String id);

    /**
     * 删除报修
     */
    @POST("Home/Repair/del_repair")
    @FormUrlEncoded
    Call<BaseResponse> delRepair(@Field("ri_id") String id);

    /**
     * 室友列表
     */
    @POST("Home/Repair/get_mate")
    @FormUrlEncoded
    Call<BaseResponse<List<Roommate>>> roommateList(@Field("uid") String uid);

    /**
     * 添加室友
     */
    @POST("Home/Repair/add_mate")
    @FormUrlEncoded
    Call<BaseResponse> addRoommate(@Field("uid") String uid,
                                   @Field("name") String name,
                                   @Field("tel") String phone,
                                   @Field("sex") String sex,
                                   @Field("identity") String id);

    /**
     * 删除室友
     */
    @POST("Home/Repair/del_mate")
    @FormUrlEncoded
    Call<BaseResponse> delRoommate(@Field("rm_id") String roommateId);

    /**
     * 获取用户积分
     */
    @POST("Home/Repair/get_integral")
    @FormUrlEncoded
    Call<BaseResponse<PointsResp>> getPoints(@Field("uid") String uid);

    /**
     * 积分明细
     */
    @POST("Home/Repair/get_integral_detail")
    @FormUrlEncoded
    Call<BaseResponse<List<PointsHistory>>> pointsDetail(@Field("uid") String uid);

    /**
     * 检查版本
     */
    @GET("Home/UserCenter/version")
    Call<BaseResponse<Version>> checkVersion();


    /**
     * 门店详情
     *
     * @param shopId 门店ID
     */
    @POST("Home/Store/getstore")
    @FormUrlEncoded
    Call<BaseResponse<ShopDetail>> shopDetail(@Field("st_id") String shopId);

    /**
     * 房屋详情
     *
     * @param roomId 房屋ID
     */
    @POST("Home/Index/getapartment")
    @FormUrlEncoded
    Call<BaseResponse<RoomDetail>> roomDetail(@Field("ap_id") String roomId);

    /**
     * 所有店铺
     */
    @GET("Home/Index/getstoredetail")
    Call<BaseResponse<List<ShopLocation>>> allShops();


    /**
     * 房租缴纳状态
     */
    @POST("Home/Life/paytype")
    @FormUrlEncoded
    Call<BaseResponse<RentPayStatus>> rentPayStatus(@Field("uid") String uid);

    /**
     * 房租缴纳详情
     *
     * @param billId 账单ID
     */
    @POST("Home/Life/paydetail")
    @FormUrlEncoded
    Call<BaseResponse<RentDetail>> rentBillDetail(@Field("pa_id") String billId,
                                                  @Field("uid") String uid);


    /**
     * 预约看房
     *
     * @param uid     uid
     * @param shopId  预约的门店id
     * @param name    用户的姓名
     * @param phone   用户的电话
     * @param code    验证码
     * @param future  预约的时间
     * @param message 用户的留言(optional)
     * @return who care?
     */
    @POST("Home/Sign/setreserve")
    @FormUrlEncoded
    Call<BaseResponse> submitAppointment(@Field("uid") String uid,
                                         @Field("st_id") String shopId,
                                         @Field("name") String name,
                                         @Field("tel") String phone,
                                         @Field("code") String code,
                                         @Field("time") String future,
                                         @Field("message") String message);

    /**
     * 历史账单
     */
    @POST("Home/Life/bill")
    @FormUrlEncoded
    Call<BaseResponse<List<BillHistory>>> bill(@Field("uid") String uid);


    /**
     * 水电费缴纳状态
     */
    @POST("home/Life/getwater")
    @FormUrlEncoded
    Call<BaseResponse<WePayStatus>> wePayStatus(@Field("uid") String uid);

    /**
     * 水电费详情
     */
    @POST("Home/Life/waterdetail")
    @FormUrlEncoded
    Call<BaseResponse<WECostDetail>> wePayDetail(@Field("uid") String uid,
                                                 @Field("hy_id") String billId);

    /**
     * 获取未读消息
     */
    @POST("/Home/UserCenter/noticestatus")
    @FormUrlEncoded
    Call<BaseResponse<NoReadMessage>> getNoReadMessage(@Field("uid") String uid);

    /**
     * 获取消息列表
     */
    @POST("/Home/UserCenter/noticelist")
    @FormUrlEncoded
    Call<BaseResponse<MessageListBean>> getMessageList(@Field("uid") String uid);


    /**
     * 修改手机号提交这一步骤
     * user_tel	用户旧的手机号码//接收短信验证码的手机号码
     * repleace_tel用户更换之后的手机号
     * code	验证码
     * type  验证码类型
     */

    @POST("Home/Login/replacetel")
    @FormUrlEncoded
    Call<BaseResponse> changePhoneCommit(@Field("user_tel") String user_tel,
                                         @Field("repleace_tel") String repleace_tel,
                                         @Field("code") String code,
                                         @Field("type") String type);

}
