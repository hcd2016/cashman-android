package com.credit.xiaowei.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

public class KeyboardLinearLayout extends LinearLayout {
	private static final String TAG = KeyboardLinearLayout.class.getSimpleName();
	public static final byte KEYBOARD_STATE_SHOW = -3;
	public static final byte KEYBOARD_STATE_HIDE = -2;
	public static final byte KEYBOARD_STATE_INIT = -1;
	private boolean mHasInit;
	private boolean mHasKeybord;
	private int mHeight;
	private onKybdsChangeListener mListener;

	
	private Context _context;
	public KeyboardLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		_context = context;
	}

	public KeyboardLinearLayout(Context context) {
		super(context);
		_context = context;
	}
	/**
	 * set keyboard state listener
	 */
	public void setOnkbdStateListener(onKybdsChangeListener listener){
		mListener = listener;
	}
	
	private int spaceBuf=0;
	
	public int getSpace()
	{
		return spaceBuf;
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (!mHasInit) {
			mHasInit = true;
			mHeight = b;
			if (mListener != null) {
				mListener.onKeyBoardStateChange(KEYBOARD_STATE_INIT);
			}
		} else {
			mHeight = mHeight < b ? b : mHeight;// 取最大
		}
		if (mHasInit && mHeight-300 > b) {
			spaceBuf = mHeight-b;
			mHasKeybord = true;
			if (mListener != null) {
				mListener.onKeyBoardStateChange(KEYBOARD_STATE_SHOW);
			}
			Log.w(TAG, "show keyboard.......");
		}
		if (mHasInit && mHasKeybord && mHeight-300 < b) {
			mHasKeybord = false;
			if (mListener != null) {
				mListener.onKeyBoardStateChange(KEYBOARD_STATE_HIDE);
			}
			Log.w(TAG, "hide keyboard.......");
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	
	public interface onKybdsChangeListener{
		 void onKeyBoardStateChange(int state);
		 void onSoftKeyboardShown(boolean isShowing, int softKeyboardHeight);
	}
}
