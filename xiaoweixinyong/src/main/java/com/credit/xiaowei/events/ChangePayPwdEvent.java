package com.credit.xiaowei.events;


import com.credit.xiaowei.bean.LendBean;

public class ChangePayPwdEvent extends BaseEvent {

	private boolean status;
	private LendBean lendBean;
	public ChangePayPwdEvent(boolean status,LendBean lendBean)
	{
		this.status = status;
		this.lendBean = lendBean;
	}
	
	public boolean getStatus()
	{
		return status;
	}

	public LendBean getLendBean() {
		return lendBean;
	}

	public void setLendBean(LendBean lendBean) {
		this.lendBean = lendBean;
	}
}
