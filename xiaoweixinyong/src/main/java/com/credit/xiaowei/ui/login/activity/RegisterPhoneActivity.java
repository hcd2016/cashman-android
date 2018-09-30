package com.credit.xiaowei.ui.login.activity;


import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.credit.xiaowei.R;
import com.credit.xiaowei.app.App;
import com.credit.xiaowei.base.BaseActivity;
import com.credit.xiaowei.events.BaseEvent;
import com.credit.xiaowei.events.LoginEvent;
import com.credit.xiaowei.ui.login.contract.GetRegisterCodeContract;
import com.credit.xiaowei.ui.login.presenter.GetRegisterCodePresenter;
import com.credit.xiaowei.util.StringUtil;
import com.credit.xiaowei.widget.ClearEditText;
import com.credit.xiaowei.widget.keyboard.KeyboardNumberUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

import static com.credit.xiaowei.util.ToastUtil.showToast;

public class RegisterPhoneActivity extends BaseActivity<GetRegisterCodePresenter> implements GetRegisterCodeContract.View{

    @BindView(R.id.et_phone_number)
    ClearEditText mEtPhoneNumber;
    @BindView(R.id.scrollview)
    ScrollView mScrollview;
    @BindView(R.id.llCustomerKb)
    LinearLayout mLlCustomerKb;
    @BindView(R.id.tv_next)
    TextView mTvNext;
    private String userName;

    @Override
    public int getLayoutId() {
        return R.layout.activity_register_phone;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        EventBus.getDefault().register(this);
        mTitle.setTitle(App.getAPPName());
        mEtPhoneNumber.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean touchable = event.getX() > (mEtPhoneNumber.getWidth() - mEtPhoneNumber.getTotalPaddingRight())
                        && (event.getX() < ((mEtPhoneNumber.getWidth() - mEtPhoneNumber.getPaddingRight())));
                if (touchable) {
                    mEtPhoneNumber.setText("");
                } else if (!isKeyboardShow()) {
                    showKeyboard(mLlCustomerKb, KeyboardNumberUtil.CUSTOMER_KEYBOARD_TYPE.NUMBER, mEtPhoneNumber);
                }
                return true;
            }
        });
        mScrollview.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                mScrollview.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTvNext.requestFocus();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void LoginSuccess(BaseEvent event){
        if (event instanceof LoginEvent){
            finish();
        }
    }

    @OnClick(R.id.tv_next)
    public void onClick() {
        hideKeyboard();
        userName = mEtPhoneNumber.getText().toString().trim();
        if (TextUtils.isEmpty(userName)) {
            showToast("手机号码不能为空");
        } else if (StringUtil.isMobileNO(userName)) {
            mPresenter.getRegisterCode(userName);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            //得到InputMethodManager的实例
            imm.hideSoftInputFromWindow(mEtPhoneNumber.getWindowToken(), 0);
        } else {
            showToast("手机号码输入不正确");
        }
    }

    @Override
    public void getCodeSuccess() {
        Intent intent = new Intent(RegisterPhoneActivity.this, RegisterPasswordActivity.class);
        intent.putExtra("phone", userName);
        startActivity(intent);
    }

    @Override
    public void showErrorMsg(String msg, int code) {
        if (code == 1000) {
            // 这个手机号已经注册，直接登录
            Intent intent = new Intent(RegisterPhoneActivity.this, LoginActivity.class);
            intent.putExtra("tag", StringUtil.changeMobile(userName));
            intent.putExtra("phone", userName);
            intent.putExtra("message",msg);
            startActivity(intent);
        } else {
            showToast(msg);
        }
    }

    @Override
    public void showLoading(String content) {
        App.loadingContent(mActivity,content);
    }

    @Override
    public void stopLoading() {
        App.hideLoading();
    }

    @Override
    public void showErrorMsg(String msg, String type) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
