package com.innext.xjx.config;

public class Constant {

	//提额H5
	public static String LIMIT_URL = "http://app.souyijie.com/web/web/gotoRegisterNew/555";

	public static final String DEBUG_TAG = "logger";// LogCat的标记

	//sharedpren里面的常量
	public static final String SHARE_TAG_USERNAME = "username"; //用户名
	public static final String SHARE_TAG_SESSIONID = "sessionid";//sessionId
	public static final String SHARE_TAG_UID = "uid";//uid
	public static final String SHARE_TAG_HASSETPAYPWD = "hasSetPayPwd";//交易密码状态

	public static final String SHARE_USER_LOGIN_STATUS = "user_login";//用户登录状态
	public static final String SHARE_USER_INFO = "userinfo";//用户信息

	public static final String SHARE_TAG_COUNT_DOWN = "showCountDown";	//显示倒计时页面
	public static final String SHARE_TAG_REPAY_COUNT_DOWN = "showRepaymentCountDown";	//显示还款倒计时页面
	public static final String SHARE_TAG_NEXT_LOAN = "showNextLoanCountDown";	//显示下次申请借款倒计时页面

	public static final String SHARE_CALENDAR_PERMISSIONS = "calendarPermissions";//是否允许往日历中插入数据

	public static final String SHARE_CALENDAR_LOAN_DATE = "loanStartTime";	//日历中插入的下次可借款时间
	public static final String SHARE_CALENDAR_REPAY_DATE = "loanEndTime";	//日历中插入的还款时间
	public static final String SHARE_CALENDAR_REPAY_MONEY = "loanEndMoney";	//日历中插入的还款金额

	public static final String SHARE_TAG_REAL_NAME = "realName"; //实名姓名

	//判断是不是第一次进app 是的话暂时引导页
	public static final String IS_FIRST_LOGIN = "FirstLogin";//key
	public static final int HAS_ALREADY_LOGIN = 1;//首次
	public static final int NOT_FIRST_LOGIN = -1;

	//分享相关的key
	public static String WX_APP_KEY = "wx0fe8b6b8cdb10084";//微信KEY
	public static String WX_APP_SECRET = "bb315d93a9ca6b62fc5ae4371fe122c1";//微信SECRET
	public static String QQ_APP_ID = "1105823848";//QQ appid
	public static String QQ_APP_KEY = "KEYIMprMOv1WGgd9Lkh";//QQ appkey

	//支付结果
	public static final String PAY_RESULT_LEND_FAILED = "PAY_RESULT_LEND_FAILED";

	//PTD查询结果key
	public static final String QUERY_DATE_MAP = "query_date_map";//查询日期map
	//PTD验证码响应头KEY
	public static final String CAPTCHA_KEY = "captcha_key";//PTD验证码响应头KEY

	/************
	 * 提升额度配置
	 */
	public static final int TAG_QUOTA_PERSONAL = 1;//个人
	public static final int TAG_QUOTA_WORK = 2;//工作
	public static final int TAG_QUOTA_CONTACT = 3;//联系人
	public static final int TAG_QUOTA_BANK = 4;//银行卡信息
	public static final int TAG_QUOTA_PHONE = 5;//手机运营商
	public static final int TAG_QUOTA_MORE = 7;//更多
	public static final int TAG_QUOTA_ZMXY = 8;//芝麻信用
	public static final int TAG_QUOTA_ALIPAY = 9;//芝麻信用

	//首页弹窗
	public static final String MAIN_DIALOG_KEY = "MAIN_DIALOG_KEY";

	//位置信息上传间隔时间
	public static final int INTERVAL_TIME =30 * 60 * 1000;
	public static final int ELAPSED_TIME =30 * 1000;
	public static final int RETRIVE_SERVICE_COUNT = 50;
	public static final int ELAPSED_TIME_DELAY = 2*60*1000;//get GPS delayed
	public static final int BROADCAST_ELAPSED_TIME_DELAY = 2*60*1000;

	public static final String POI_SERVICE = "com.coder80.timer.service.UploadPOIService";
	public static final String POI_SERVICE_ACTION = "com.coder80.timer.service.UploadPOIService.action";
	public static final String TRANSITION_ANIMATION_SHOW_PIC = "showView";

}
