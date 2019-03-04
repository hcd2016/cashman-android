package com.credit.xiaowei.http;


import com.credit.pretend.bean.HotListBean;
import com.credit.pretend.bean.InfoDetailBean;
import com.credit.xiaowei.bean.BaseResponse;
import com.credit.xiaowei.ui.authentication.bean.BankInfoBean;
import com.credit.xiaowei.ui.authentication.bean.GetBankListBean;
import com.credit.xiaowei.ui.authentication.bean.GetPicListBean;
import com.credit.xiaowei.ui.authentication.bean.GetRelationBean;
import com.credit.xiaowei.ui.authentication.bean.GetWorkInfoBean;
import com.credit.xiaowei.ui.authentication.bean.ImageDataBean;
import com.credit.xiaowei.ui.authentication.bean.OtherProductBean;
import com.credit.xiaowei.ui.authentication.bean.PersonalInformationRequestBean;
import com.credit.xiaowei.ui.authentication.bean.PertfecInformationRequestBean;
import com.credit.xiaowei.ui.authentication.bean.SaveInfoBean;
import com.credit.xiaowei.ui.lend.bean.ConfirmLoanResponseBean;
import com.credit.xiaowei.ui.lend.bean.ExpenseDetailBean;
import com.credit.xiaowei.ui.lend.bean.HomeIndexResponseBean;
import com.credit.xiaowei.ui.login.bean.LoginBean;
import com.credit.xiaowei.ui.login.bean.RegisterBean;
import com.credit.xiaowei.ui.my.bean.Lottery;
import com.credit.xiaowei.ui.my.bean.MoreBean;
import com.credit.xiaowei.ui.my.bean.TransactionBean;
import com.credit.xiaowei.ui.repayment.bean.RepaymentBean;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import rx.Observable;

/**
 *
 */
public interface HttpApi {

    //所有需要的泛型 添加：BaseResponse<UserInfo>

    //上传用户信息
    @FormUrlEncoded
    @POST("credit-app/device-report")
    Observable<BaseResponse> deviceReport(@Field("device_id") String device_id,
                                          @Field("installed_time") String installed_time,
                                          @Field("uid") String uid,
                                          @Field("username") String username,
                                          @Field("net_type") String net_type,
                                          @Field("identifyID") String identifyID,
                                          @Field("appMarket") String appMarket);

    /*******************************  借款模块 start ***********************/
    //首页
    @GET("credit-app/index")
    Observable<BaseResponse<HomeIndexResponseBean>> index();

    //首页借款费用详情
    @FormUrlEncoded
    @POST("credit-user/service-charge")
    Observable<BaseResponse<List<ExpenseDetailBean>>> loadExpenseDetail(@Field("moneyAmount") String money,
                                                                        @Field("loanTerm") String day);

    //验证借款
    @FormUrlEncoded
    @POST("credit-loan/get-confirm-loan")
    Observable<BaseResponse<ConfirmLoanResponseBean>> toLoan(@Field("money") String money,
                                                             @Field("period") String period);

    //首页借款被拒绝后调用的接口
    @FormUrlEncoded
    @POST("credit-loan/confirm-failed-loan")
    Observable<BaseResponse> confirmFailed(@Field("id") String id);

    //申请借款
    @FormUrlEncoded
    @POST("credit-loan/apply-loan")
    Observable<BaseResponse> applyLoan(@Field("money") String money,
                                       @Field("period") String period,
                                       @Field("pay_password") String pay_password);

    //上传短信 app列表 联系人  type:1短信，2app，3通讯录
    @FormUrlEncoded
    @POST("credit-info/up-load-contents")
    Observable<BaseResponse> upLoadContents(@Field("type") String type,
                                            @Field("data") String data);
    /*******************************  借款模块 end ***********************/

    /*******************************  还款模块 start ***********************/
    //还款页面接口
    @GET("credit-loan/get-my-loan")
    Observable<BaseResponse<RepaymentBean>> getMyLoan();

    /*******************************  还款模块 end ***********************/

    /*******************************  登录模块 start ***********************/
    //登录
    @Headers("showDialog:true")
    @FormUrlEncoded
    @POST("credit-user/login")
    Observable<BaseResponse<LoginBean>> login(@Field("username") String username,
                                              @Field("password") String password,
                                              @Field("tdFmdevice") String blackbox);

    //退出登录
    @GET("credit-user/logout")
    Observable<BaseResponse> loginOut();

    //注册
    @Headers("showDialog:true")
    @FormUrlEncoded
    @POST("credit-user/register")
    Observable<BaseResponse<RegisterBean>> register(@Field("phone") String phone,
                                                    @Field("code") String code,
                                                    @Field("password") String password,
                                                    @Field("source") String source,
                                                    @Field("invite_code") String invite_code,
                                                    @Field("user_from") String user_from);

    //获取注册验证码，验证到已注册则跳转登录
    @Headers("showDialog:true")
    @FormUrlEncoded
    @POST("credit-user/reg-get-code")
    Observable<BaseResponse> getRegisterCode(@Field("phone") String phone);

    //忘记密码
    @Headers("showDialog:true")
    @FormUrlEncoded
    @POST("credit-user/reset-pwd-code")
    Observable<BaseResponse> forgetPwd(@Field("phone") String phone,
                                       @Field("type") String type);

    //找回登录密码/交易密码验证用户和手机验证码
    @Headers("showDialog:true")
    @FormUrlEncoded
    @POST("credit-user/verify-reset-password")
    Observable<BaseResponse> verifyResetPwd(@Field("phone") String phone,
                                            @Field("code") String code,
                                            @Field("realname") String realname,
                                            @Field("id_card") String id_card,
                                            @Field("type") String type);

    //找回密码设置新密码
    @FormUrlEncoded
    @POST("credit-user/reset-password")
    Observable<BaseResponse> resetPwd(@Field("phone") String phone,
                                      @Field("code") String code,
                                      @Field("password") String password);

    //修改登录密码
    @Headers("showDialog:true")
    @FormUrlEncoded
    @POST("credit-user/change-pwd")
    Observable<BaseResponse> changePwd(@Field("old_pwd") String old_pwd,
                                       @Field("new_pwd") String new_pwd);

    /*******************************  登陆模块 end ***********************/

    /**********************************   我的模块       *************************/
    //我的页面 数据
    @GET("credit-user/get-info")
    Observable<BaseResponse<MoreBean>> getInfo();

    //抽奖码
    @FormUrlEncoded
    @POST("jsaward/awardCenterWeb/userDrawAwardList")
    Observable<BaseResponse<Lottery>> lotteryRequest(@Field("phone") String phone,
                                                     @Field("page") String page,
                                                     @Field("pageSize") String pageSize);

    //借款记录
    @FormUrlEncoded
    @POST("credit-loan/get-my-orders")
    Observable<BaseResponse<TransactionBean>> recordRequest(@Field("page") String page,
                                                            @Field("pagsize") String pageSize);

    //上传定位信息
    @FormUrlEncoded
    @POST("credit-info/upload-location")
    Observable<BaseResponse> uploadLocation(@Field("longitude") String longitude,
                                            @Field("latitude") String latitude,
                                            @Field("address") String address,
                                            @Field("time") String time);

    //意见反馈
    @Headers("showDialog:true")
    @FormUrlEncoded
    @POST("credit-info/feedback")
    Observable<BaseResponse> feedBack(@Field("content") String content);

    //修改交易密码
    @Headers("showDialog:true")
    @FormUrlEncoded
    @POST("credit-user/change-paypassword")
    Observable<BaseResponse> changePayPassWord(@Field("old_pwd") String old_pwd,
                                               @Field("new_pwd") String new_pwd);

    //设置交易密码
    @FormUrlEncoded
    @POST("credit-user/set-paypassword")
    Observable<BaseResponse> setPayPwd(@Field("password") String password);

    /**********************************    个人认证       *******************************/

    //获取认证信息
    @GET("credit-card/get-verification-info")
    Observable<BaseResponse<PertfecInformationRequestBean>> getPertfecInfo();

    //获取个人认证信息
    @GET("credit-card/get-person-info")
    Observable<BaseResponse<PersonalInformationRequestBean>> getPersonalInformation();

    //保存个人信息已实名认证
    @Headers("showDialog:true")
    @FormUrlEncoded
    @POST("credit-card/get-person-infos")
    Observable<BaseResponse> getRersonInformation(@FieldMap Map<String, String> map);

    //保存个人信息未实名认证
    @Headers("showDialog:true")
    @FormUrlEncoded
    @POST("credit-info/save-person-info")
    Observable<BaseResponse> getSaveRersonInformation(@FieldMap Map<String, String> map);

    /*******************************    上传图片   *****************************************/
    @Multipart
    @POST("picture/upload-image")
    Observable<BaseResponse<ImageDataBean>> uploadImageFile(
            @Part MultipartBody.Part File, @PartMap Map<String, Integer> map);

    //获取图片集合
    //1:身份证,2:学历证明,3:工作证明,4:薪资证明,5:财产证明,6、工牌照片、7、个人名片，8、银行卡 100:其它证明
    @FormUrlEncoded
    @POST("picture/get-pic-list")
    Observable<BaseResponse<GetPicListBean>> getPicList(@Field("type") String type);

    //获取认证列表-紧急联系人
    @GET("credit-card/get-contacts")
    Observable<BaseResponse<GetRelationBean>> getContacts();

    //获取认证列表-保存紧急联系人
    @FormUrlEncoded
    @POST("credit-card/get-contactss")
    Observable<BaseResponse> saveContacts(@Field("type") String type,
                                          @Field("mobile") String mobile,
                                          @Field("name") String name,
                                          @Field("relation_spare") String relation_spare,
                                          @Field("mobile_spare") String mobile_spare,
                                          @Field("name_spare") String name_spare);

    //获取工作信息
    @GET("credit-card/get-work-info")
    Observable<BaseResponse<GetWorkInfoBean>> getWorkInfo();

    //保存工作信息
    @Headers("showDialog:true")
    @FormUrlEncoded
    @POST("credit-card/save-work-info")
    Observable<BaseResponse> saveWorkInfo(@FieldMap Map<String, String> params);

    //获取银行卡验证码
    @FormUrlEncoded
    @POST("credit-card/get-code")
    Observable<BaseResponse> getCardCode(@Field("phone") String phone);

    //获取银行卡列表
    @GET("credit-card/bank-card-info")
    Observable<BaseResponse<GetBankListBean>> getBankCardList();

    //添加银行卡
    @Headers("showDialog:true")
    @FormUrlEncoded
    @POST("credit-card/add-bank-card")
    Observable<BaseResponse<BankInfoBean>> addBankCard(@Field("phone") String phone,
                                                       @Field("code") String code,
                                                       @Field("card_no") String card_no,
                                                       @Field("bank_id") String bank_id);

    //上传支付宝数据
    @FormUrlEncoded
    @POST("credit-alipay/get-user-info")
    Observable<BaseResponse<SaveInfoBean>> saveAlipayInfo(@Field("data") String data);


    /**
     * 伪装相关接口
     * ---------------------------------------------------------------------------------------------------
     */
    //是否开启伪装
    @FormUrlEncoded
    @POST("funs/switch")
    Call<JsonObject> getIsOpenPretend(@Field("appversion") String appversion,
                                      @Field("channelname") String channel,
                                      @Field("platform") String platform);

    //伪热点列表
    @GET("funs/gethotpoint")
    Call<List<HotListBean>> getHotList();

    //伪热点详情
    @GET("funs/gethotpointdetail")
    Call<InfoDetailBean> getInfoDetail(@Query("id") String id);

    //伪热获取图形验证码
    @GET("captcha.svl")
    Observable<ResponseBody> getImgVerification();

    //查询提交
    @FormUrlEncoded
    @POST("funs/getcreditreport")
    Call<JsonObject> commitQuery(@Field("userName") String userName,
                                 @Field("idno") String idno,
                                 @Field("phone") String phone,
                                 @Field("captcha") String captcha,
                                 @Field("RCaptchaKey") String RCaptchaKey,
                                 @Field("platform") String platform);

    /**
     * ---------------------------------------------------------------------------------------------------
     */

    //版本更新检查
    @GET("version/update")
    Call<JsonObject> checkUpdateVersion(@Query("appversion") String appVersion, @Query("platform") String platform);

    //检测借款金额是否超出限制
    @GET("credit-loan/check-limit")
    Call<JsonObject> checkLimit();

    //被拒绝时展示的其他产品列表
    @GET("loanmarketinfoforapp")
    Call<OtherProductBean> getOtherProductList();

    //其他产品跳转提交
    @GET("countPvUv")
    Observable<BaseResponse<JsonObject>> commitMoreClick(@Query("id") String id);
}
