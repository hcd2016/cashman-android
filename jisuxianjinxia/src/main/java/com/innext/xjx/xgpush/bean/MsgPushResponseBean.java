package com.innext.xjx.xgpush.bean;

public class MsgPushResponseBean{
	private int code;
	private String msg;
	private int action_type;
	private String url;
	private String url_title;
	private int is_share;
	private String comment;
	private int project_type;
	private int project_id;
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getAction_type() {
		return action_type;
	}
	public void setAction_type(int action_type) {
		this.action_type = action_type;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUrl_title() {
		return url_title;
	}
	public void setUrl_title(String url_title) {
		this.url_title = url_title;
	}
	public int getIs_share() {
		return is_share;
	}
	public void setIs_share(int is_share) {
		this.is_share = is_share;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public int getProject_type() {
		return project_type;
	}
	public void setProject_type(int project_type) {
		this.project_type = project_type;
	}
	public int getProject_id() {
		return project_id;
	}
	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}
	
}
