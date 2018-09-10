package com.innext.xjx.ui.login.presenter;

import com.innext.xjx.base.BasePresenter;
import com.innext.xjx.ui.login.bean.LoginBean;
import com.innext.xjx.http.HttpManager;
import com.innext.xjx.http.HttpSubscriber;
import com.innext.xjx.ui.login.contract.LoginContract;

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
