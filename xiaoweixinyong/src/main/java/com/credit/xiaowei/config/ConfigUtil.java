package com.credit.xiaowei.config;

import com.credit.xiaowei.app.App;
import com.credit.xiaowei.ui.my.bean.UserInfoBean;
import com.credit.xiaowei.util.ConvertUtil;
import com.credit.xiaowei.util.SpUtil;
import com.credit.xiaowei.util.ViewUtil;

public class ConfigUtil {

    //服务器地址
//    public String baseUrl = "http://super.xianjinxia.com/";
//    public static String baseUrl ="http://192.168.1.176:8080/xjx/";//张明服务器
//    public static String baseUrl ="http://192.168.1.161:8080/";//张书豪测试服务器
//      public static String baseUrl =" http://118.242.26.62:8083/";//张苏豪测试服务器
//    public  static String baseUrl ="http://118.242.26.62:8083/";//管家美
//    public String baseUrl = "http://super.xianjinxia.com/";
    //public static String baseUrl ="http://192.168.1.176:8080/xjx/";//张明服务器
    //public  String baseUrl ="http://192.168.1.161:8080/";//张苏豪测试服务器
    //public  String baseUrl =" http://118.242.26.62:8083/";//测试服务器
//    public  String baseUrl ="http://192.168.1.179:8080/";//测试服务器
//    public  String baseUrl ="http://192.168.1.145:8080/";//测试服务器
//    public static String baseUrl = "http://120.79.170.34:8080/";//正式服务器
//    public  static String baseUrl ="http://120.79.170.179:8080/";//测试服务器
//    public  static String baseUrl ="http://192.168.1.179:8081/";//测试服务器
//    public  static String baseUrl ="http://new.vpfinance.cn/appserver/";//测试服务器
    public  static String baseUrl ="http://39.108.146.92:8080/appserver/";//测试服务器


    public static boolean isOpenPretend = false;//是否开启伪页面
    private boolean isDebug = true;//是否调试模式
    //爬取支付宝数据js
    public String GET_ALIPAY_JS = baseUrl + "resources/js/alipay.js";

    //我的邀请码H5
    public String INVITATION_CODE = baseUrl + "page/detail";

    //活动中心H5
    public String ACTIVITY_CENTER = baseUrl + "content/activity";

    //帮助中心H5
    public String HELP = baseUrl + "help";

    //注册协议
    public String REGISTER_AGREEMENT = baseUrl + "act/light-loan-xjx/agreement";

    //信用授权协议
    public String CREDIT_AUTHORIZATION_AGREEMENT = baseUrl + "agreement/creditExtension";

    //关于我们
    public String ABOUT_US = baseUrl + "page/detailAbout";

    public static final String SHARED_USERLIST_KEY = "userList";

    private boolean isLogin = false;//用户的登陆态
    //private String channelName = "xjx-yingyongbao_a";//渠道号
//    private String channelName = "xjx-MySelf";//渠道号
    private String channelName = ViewUtil.getAppMetaData(App.getContext());//渠道号

    public ConfigUtil() {
        setUserInfo(getUserInfo());
    }


    public boolean isDebug() {
        return isDebug;
    }

    public void setDebug(boolean isDebug) {
        this.isDebug = isDebug;
    }


    public String getChannelName() {
        return channelName;
    }


    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public UserInfoBean getUserInfo() {
        return ConvertUtil.toObject(SpUtil.getString(Constant.SHARE_USER_INFO), UserInfoBean.class);
    }


    public void setUserInfo(UserInfoBean userInfo) {
        if (userInfo != null)
            isLogin = true;
        else
            isLogin = false;
        SpUtil.putString(Constant.SHARE_USER_INFO, ConvertUtil.toJsonString(userInfo));
    }

    //获取用户当前登录状态
    public boolean getLoginStatus() {
        return isLogin;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        GET_ALIPAY_JS = baseUrl + "resources/js/alipay.js";

        //我的邀请码H5
        INVITATION_CODE = baseUrl + "page/detail";

        //活动中心H5
        ACTIVITY_CENTER = baseUrl + "content/activity";

        //帮助中心H5
        HELP = baseUrl + "help";

        //注册协议
        REGISTER_AGREEMENT = baseUrl + "act/light-loan-xjx/agreement";

        //关于我们
        ABOUT_US = baseUrl + "page/detailAbout";

        CREDIT_AUTHORIZATION_AGREEMENT = baseUrl + "agreement/creditExtension";
    }
}
