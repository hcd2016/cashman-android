package com.innext.xjx.events;


import com.innext.xjx.ui.main.FragmentFactory;

public class ChangeTabMainEvent extends UIBaseEvent {
	private FragmentFactory.FragmentStatus tab;
	public ChangeTabMainEvent(FragmentFactory.FragmentStatus tab)
	{
		this.tab = tab;
	}

	public ChangeTabMainEvent(FragmentFactory.FragmentStatus tab, String code, String message)
	{
		super(code,message);
		this.tab = tab;
	}
	
	public void setTab(FragmentFactory.FragmentStatus tab) {
		this.tab = tab;
	}

	public FragmentFactory.FragmentStatus getTab()
	{
		return tab;
	}
}

