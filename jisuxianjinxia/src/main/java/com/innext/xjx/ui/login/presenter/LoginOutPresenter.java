package com.innext.xjx.ui.login.presenter;

import com.innext.xjx.base.BasePresenter;
import com.innext.xjx.http.HttpManager;
import com.innext.xjx.http.HttpSubscriber;
import com.innext.xjx.ui.login.contract.LoginOutContract;

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
