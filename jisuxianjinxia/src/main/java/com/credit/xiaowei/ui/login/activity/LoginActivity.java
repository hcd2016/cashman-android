package com.credit.xiaowei.ui.login.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.credit.xiaowei.R;
import com.credit.xiaowei.app.App;
import com.credit.xiaowei.base.BaseActivity;
import com.credit.xiaowei.config.Constant;
import com.credit.xiaowei.dialog.ActionSheetDialog;
import com.credit.xiaowei.events.LoginEvent;
import com.credit.xiaowei.ui.login.contract.ForgetPwdContract;
import com.credit.xiaowei.ui.login.contract.LoginContract;
import com.credit.xiaowei.ui.login.presenter.ForgetPwdPresenter;
import com.credit.xiaowei.ui.login.presenter.LoginPresenter;
import com.credit.xiaowei.ui.my.bean.UserInfoBean;
import com.credit.xiaowei.util.Tool;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

import static com.credit.xiaowei.util.ToastUtil.showToast;

public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View ,ForgetPwdContract.View {
    @BindView(R.id.tv_user_name)
    TextView mTvUserName;
    @BindView(R.id.et_password)
    EditText mEtPassword;
    @BindView(R.id.tv_login)
    TextView mTvLogin;
    @BindView(R.id.tv_forget_pwd)
    TextView mTvForgetPwd;
    @BindView(R.id.tv_login_more)
    TextView mTvLoginMore;
    @BindView(R.id.scrollview)
    ScrollView mScrollview;

    private String resultPhone;
    private String tipMsg = "";
    private ForgetPwdPresenter forgetPwdPresenter;
    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
        forgetPwdPresenter = new ForgetPwdPresenter();
        forgetPwdPresenter.init(this);
    }

    @Override
    public void loadData() {
        resultPhone = getIntent().getStringExtra("phone");
        tipMsg = getIntent().getStringExtra("message");
        initView();
    }

    private void initView() {
        mTitle.setTitle(App.getAPPName());
        if (!TextUtils.isEmpty(tipMsg)) {
            mEtPassword.setHint(tipMsg);
        }
        mTvUserName.setText(getIntent().getStringExtra("tag"));
        mScrollview.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                mScrollview.smoothScrollTo(0, 200);
            }
        });
    }

    @OnClick({R.id.tv_login, R.id.tv_forget_pwd, R.id.tv_login_more})
    public void onClick(View view) {
        if (Tool.isFastDoubleClick()) return;
        switch (view.getId()) {
            case R.id.tv_login:
                if (mEtPassword.length() < 6) {
                    showToast("密码必须为6~16字符");
                } else {
                    mPresenter.login(resultPhone, mEtPassword.getText().toString());
                }
                break;
            case R.id.tv_forget_pwd:
                forgetPwdPresenter.forgetPwd(resultPhone,"find_pwd");
                break;
            case R.id.tv_login_more:
                new ActionSheetDialog(LoginActivity.this)
                        .builder()
                        .setCancelable(true)
                        .setCanceledOnTouchOutside(false)
                        .addSheetItem("注册", ActionSheetDialog.SheetItemColor.Red,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        Intent intent = new Intent(LoginActivity.this, RegisterPhoneActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(intent);
                                        finish();
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                        //得到InputMethodManager的实例
                                        imm.hideSoftInputFromWindow(mEtPassword.getWindowToken(), 0);
                                    }
                                })
                        .addSheetItem("切换账户", ActionSheetDialog.SheetItemColor.Red,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        Intent intent = new Intent(LoginActivity.this, RegisterPhoneActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(intent);
                                        finish();
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                        //得到InputMethodManager的实例
                                        imm.hideSoftInputFromWindow(mEtPassword.getWindowToken(), 0);
                                    }
                                })
                        .show();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //得到InputMethodManager的实例
        imm.hideSoftInputFromWindow(mEtPassword.getWindowToken(), 0);
    }


    @Override
    public void loginSuccess(UserInfoBean bean) {
        EventBus.getDefault().post(new LoginEvent(getApplicationContext(),bean));
        /*Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);*/
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //得到InputMethodManager的实例
        imm.hideSoftInputFromWindow(mEtPassword.getWindowToken(), 0);
        finish();
    }

    @Override
    public void showLoading(String title) {
        App.loadingContent(this, title);
    }

    @Override
    public void stopLoading() {
        App.hideLoading();
    }

    @Override
    public void showErrorMsg(String msg,String type) {
        showToast(msg);
    }

    @Override
    public void forgetPwdSuccess() {
        Intent intent = new Intent(LoginActivity.this, ForgetPwdActivity.class);
        intent.putExtra(Constant.SHARE_TAG_USERNAME, resultPhone);
        LoginActivity.this.startActivity(intent);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //得到InputMethodManager的实例
        imm.hideSoftInputFromWindow(mEtPassword.getWindowToken(), 0);
    }

}
