package com.credit.xiaowei.ui.login.activity;

import android.content.Context;
import android.content.Intent;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.credit.xiaowei.R;
import com.credit.xiaowei.app.App;
import com.credit.xiaowei.base.BaseActivity;
import com.credit.xiaowei.config.Constant;
import com.credit.xiaowei.dialog.AlertFragmentDialog;
import com.credit.xiaowei.events.RefreshUIEvent;
import com.credit.xiaowei.events.UIBaseEvent;
import com.credit.xiaowei.ui.login.contract.ForgetPwdContract;
import com.credit.xiaowei.ui.login.contract.UpdataPwdContract;
import com.credit.xiaowei.ui.login.presenter.ForgetPwdPresenter;
import com.credit.xiaowei.ui.login.presenter.UpdataPwdPresenter;
import com.credit.xiaowei.util.SpUtil;
import com.credit.xiaowei.util.ToastUtil;
import com.credit.xiaowei.util.Tool;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by gyl on 2017/2/15 0015.
 */

public class UpdataPwdActivity extends BaseActivity<ForgetPwdPresenter> implements ForgetPwdContract.View, UpdataPwdContract.View {
    @BindView(R.id.tv_user_phone)
    TextView mTvUserPhone;
    @BindView(R.id.et_old_pwd)
    EditText mEtOldPwd;
    @BindView(R.id.et_new_pwd)
    EditText mEtNewPwd;
    @BindView(R.id.et_new_pwd_again)
    EditText mEtNewPwdAgain;
    private String mResultPhone;
    private String mPhone;
    private UpdataPwdPresenter mUpdataPwdPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_updata_resetpwd;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
        mUpdataPwdPresenter = new UpdataPwdPresenter();
        mUpdataPwdPresenter.init(this);
    }

    @Override
    public void loadData() {
        initTitle();
        initEtText();
        EventBus.getDefault().register(this);
        if (getIntent() != null) {
            mPhone = getIntent().getStringExtra("phone");
        }
        mTvUserPhone.setText(Tool.changeMobile(mPhone));
    }

    private void initEtText() {
        mEtOldPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mEtNewPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mEtNewPwdAgain.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mEtOldPwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        mEtNewPwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        mEtNewPwdAgain.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
    }

    private void initTitle() {
        mTitle.setTitle(true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, "修改登录密码");
        mTitle.setRightTitle("忘记", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mResultPhone = SpUtil.getString(Constant.SHARE_TAG_USERNAME);
                mPresenter.forgetPwd(mResultPhone, "find_pwd");
            }
        });
    }

    @OnClick({R.id.tv_complete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_complete:
                resetPwdRequest();
                break;
        }
    }

    private void resetPwdRequest() {
        String pwd = mEtNewPwd.getText().toString().trim();
        String confirmPwd = mEtNewPwdAgain.getText().toString().trim();
        String oldPwd = mEtOldPwd.getText().toString().trim();
        if (Tool.isBlank(oldPwd)) {
            ToastUtil.showToast("请输入旧密码");
            return;
        }
        if (Tool.isBlank(pwd) && Tool.isBlank(confirmPwd)) {
            ToastUtil.showToast("请输入新密码并确认");
            return;
        }
        if (oldPwd.length() < 6 || oldPwd.length() > 16 || pwd.length() < 6
                || pwd.length() > 16 || confirmPwd.length() < 6 || confirmPwd.length() > 16) {
            ToastUtil.showToast("密码长度要在6~16位哦");
            return;
        }
        if (!pwd.equals(confirmPwd)) {
            ToastUtil.showToast("两次密码不一致");
            return;
        }
        mUpdataPwdPresenter.UpdataPwd(oldPwd, pwd);
    }

    /***********
     * 登录密码
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(RefreshUIEvent event) {
        if (UIBaseEvent.EVENT_SET_PWD == event.getType()) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void forgetPwdSuccess() {
        Intent intent = new Intent(UpdataPwdActivity.this, ForgetPwdActivity.class);
        intent.putExtra(Constant.SHARE_TAG_USERNAME, mResultPhone);
        UpdataPwdActivity.this.startActivity(intent);
    }

    @Override
    public void showLoading(String content) {
        App.loadingContent(this, content);
    }

    @Override
    public void stopLoading() {
        App.hideLoading();
    }

    @Override
    public void showErrorMsg(String msg, String type) {
        ToastUtil.showToast(msg);
        if (!TextUtils.isEmpty(type) && type.equals(mUpdataPwdPresenter.UPDATA_PWD)) {
            new AlertFragmentDialog.Builder(this)
                    .setContent(msg).setRightBtnText("确定").setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
                @Override
                public void dialogRightBtnClick() {
                    finish();
                }
            }).build();
        }
    }

    @Override
    public void UpdataPwdSuccess() {
        new AlertFragmentDialog.Builder(this)
                .setContent("修改登录密码成功")
                .setRightBtnText("确定")
                .setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
                    @Override
                    public void dialogRightBtnClick() {
                        finish();
                    }
                }).build();
    }
}
