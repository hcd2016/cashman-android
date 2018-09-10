package com.innext.xjx.ui.login.presenter;/**
 * Created by Administrator on 2017/2/15 0015.
 */

import com.innext.xjx.base.BasePresenter;
import com.innext.xjx.http.HttpManager;
import com.innext.xjx.http.HttpSubscriber;
import com.innext.xjx.ui.login.contract.ResetPwdContract;

/**
 * Created by xiejingwen at 2017/2/15 0015
 */
public class ResetPwdPresenter extends BasePresenter<ResetPwdContract.View> implements ResetPwdContract.Presenter {
    @Override
    public void resetPwd(String phone, String code, String password) {
        toSubscribe(HttpManager.getApi().resetPwd(phone, code, password), new HttpSubscriber() {
            @Override
            protected void _onStart() {
                mView.showLoading("");
            }

            @Override
            protected void _onNext(Object o) {
                mView.resetPwdSuccess();
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
