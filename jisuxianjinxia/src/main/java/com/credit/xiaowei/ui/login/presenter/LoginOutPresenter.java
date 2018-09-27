package com.credit.xiaowei.ui.login.presenter;

import com.credit.xiaowei.base.BasePresenter;
import com.credit.xiaowei.http.HttpManager;
import com.credit.xiaowei.http.HttpSubscriber;
import com.credit.xiaowei.ui.login.contract.LoginOutContract;

/**
 * Created by xiejingwen at 2017/2/15 0015
 */

public class LoginOutPresenter extends BasePresenter<LoginOutContract.View> implements LoginOutContract.Presenter {
    public final String TYPE_LOGIN_OUT = "loginOut";
    @Override
    public void loginOut() {
        toSubscribe(HttpManager.getApi().loginOut(), new HttpSubscriber() {
            @Override
            protected void _onStart() {
                mView.showLoading("退出中...");
            }

            @Override
            protected void _onNext(Object o) {
                mView.loginOutSuccess();
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorMsg(message,TYPE_LOGIN_OUT);
            }

            @Override
            protected void _onCompleted() {
                mView.stopLoading();
            }
        });
    }
}
