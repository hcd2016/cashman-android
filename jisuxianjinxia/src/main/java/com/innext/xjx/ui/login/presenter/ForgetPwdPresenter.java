package com.innext.xjx.ui.login.presenter;

import com.innext.xjx.base.BasePresenter;
import com.innext.xjx.http.HttpManager;
import com.innext.xjx.http.HttpSubscriber;
import com.innext.xjx.ui.login.contract.ForgetPwdContract;

/**
 * Created by xiejingwen on 2017/2/9 0009.
 */

public class ForgetPwdPresenter extends BasePresenter<ForgetPwdContract.View> implements ForgetPwdContract.presenter {
    public final String TYPE_FORGET_PWD = "ForgetPwd";
    @Override
    public void forgetPwd(String phone,String type) {
        toSubscribe(HttpManager.getApi().forgetPwd(phone,type), new HttpSubscriber() {
            @Override
            public void _onStart() {
                mView.showLoading("正在发送...");
            }

            @Override
            protected void _onNext(Object o) {
                mView.forgetPwdSuccess();
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorMsg(message,TYPE_FORGET_PWD);
            }

            @Override
            protected void _onCompleted() {
                mView.stopLoading();
            }
        });
    }
}
