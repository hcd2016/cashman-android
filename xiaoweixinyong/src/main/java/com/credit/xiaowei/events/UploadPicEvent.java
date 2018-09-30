package com.credit.xiaowei.events;

public class UploadPicEvent {

	public final static int UPLOAD_SUCCESS = 0;
	public final static int UPLOAD_FAILED = 1;
	public String message;
	private int type;
	private String picType;
	private int pos;
	public UploadPicEvent()
	{
		
	}
	public UploadPicEvent(int type,String message)
	{
		this.type=type;
		this.message = message;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getPos() {
		return pos;
	}
	public void setPos(int pos) {
		this.pos = pos;
	}
	public String getPicType() {
		return picType;
	}
	public void setPicType(String picType) {
		this.picType = picType;
	}
	
	
}
