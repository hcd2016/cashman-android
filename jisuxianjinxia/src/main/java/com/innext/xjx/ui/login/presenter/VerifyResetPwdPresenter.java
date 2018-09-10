package com.innext.xjx.ui.login.presenter;/**
 * Created by Administrator on 2017/2/15 0015.
 */

import com.innext.xjx.base.BasePresenter;
import com.innext.xjx.http.HttpManager;
import com.innext.xjx.http.HttpSubscriber;
import com.innext.xjx.ui.login.contract.VerifyResetPwdContract;

/**
 * Created by xiejingwen at 2017/2/15 0015
 */
public class VerifyResetPwdPresenter extends BasePresenter<VerifyResetPwdContract.View> implements VerifyResetPwdContract.presenter {
    @Override
    public void verifyResetPwd(String phone, String code, String realname, String id_card, String type) {
        toSubscribe(HttpManager.getApi().verifyResetPwd(phone, code, realname, id_card, type), new HttpSubscriber() {
            @Override
            protected void _onStart() {
                mView.showLoading("验证中...");
            }

            @Override
            protected void _onNext(Object o) {
                mView.verifySuccess();
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
