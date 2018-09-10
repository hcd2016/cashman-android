package com.innext.xjx.events;

import android.content.Context;

public class SchemEvent extends BaseEvent {

	private Context context;
	private String url;
	public SchemEvent(Context context, String url)
	{
		this.context = context;
		this.url = url;
	}
	public Context getContext() {
		return context;
	}
	public void setContext(Context context) {
		this.context = context;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
