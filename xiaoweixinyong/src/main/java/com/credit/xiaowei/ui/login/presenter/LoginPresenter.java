package com.credit.xiaowei.ui.login.presenter;

import com.credit.xiaowei.base.BasePresenter;
import com.credit.xiaowei.ui.login.bean.LoginBean;
import com.credit.xiaowei.http.HttpManager;
import com.credit.xiaowei.http.HttpSubscriber;
import com.credit.xiaowei.ui.login.contract.LoginContract;

/**
 * Created by xiejingwen at 2017/2/15 0015
 */

public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.presenter{
    @Override
    public void login(String username, String password) {
        toSubscribe(HttpManager.getApi().login(username, password), new HttpSubscriber<LoginBean>() {
            @Override
            public void _onStart() {
                mView.showLoading("登录中...");
            }

            @Override
            protected void _onNext(LoginBean login) {
                mView.loginSuccess(login.getItem());
            }
            @Override
            protected void _onError(String message) {
                mView.showErrorMsg(message,null);
            }
            @Override
            protected void _onCompleted() {
                mView.stopLoading();
            }
        });
    }
}
