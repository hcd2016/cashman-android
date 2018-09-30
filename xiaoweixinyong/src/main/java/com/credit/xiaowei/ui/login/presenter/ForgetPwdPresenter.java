package com.credit.xiaowei.ui.login.presenter;

import com.credit.xiaowei.base.BasePresenter;
import com.credit.xiaowei.http.HttpManager;
import com.credit.xiaowei.http.HttpSubscriber;
import com.credit.xiaowei.ui.login.contract.ForgetPwdContract;

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
