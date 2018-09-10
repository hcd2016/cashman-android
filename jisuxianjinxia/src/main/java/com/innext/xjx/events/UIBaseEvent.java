package com.innext.xjx.events;

public class UIBaseEvent{
	public final static int EVENT_DEFAULT = -1;//默认
	public final static int EVENT_LOGIN = 0;//登陆
	public final static int EVENT_LOGOUT = 1;//退出
	public final static int EVENT_LOAN_SUCCESS = 2;//申请成功(申请成功可能会被拒)
	public final static int EVENT_LOAN_FAILED = 3;//申请失败
	public final static int EVENT_SET_PAYPWD = 4;//设置交易密码
	public final static int EVENT_SET_PWD = 5;//设置登录密码
	public final static int EVENT_REPAY_SUCCESS = 6;//还款或续期成功
	public final static int EVENT_BANK_CARD_SUCCESS = 7;//银行卡绑定或修改成功
	public final static int EVENT_BORROW_MONEY_SUCCESS = 8;//借款成功(已打款,进入还款阶段)
	public final static int EVENT_REALNAME_AUTHENTICATION_SUCCESS = 8;//实名认证成功

	private String code;
	private String message;
	public UIBaseEvent(){};
	public UIBaseEvent(String code, String message)
	{
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
