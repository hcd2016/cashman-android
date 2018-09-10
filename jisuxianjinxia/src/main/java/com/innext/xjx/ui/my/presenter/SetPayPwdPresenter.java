package com.innext.xjx.ui.my.presenter;

import com.innext.xjx.base.BasePresenter;
import com.innext.xjx.http.HttpManager;
import com.innext.xjx.http.HttpSubscriber;
import com.innext.xjx.ui.my.contract.SetPayPwdContract;

/**
 * Created by gyl on 2017/2/18 0018.
 */

public class SetPayPwdPresenter extends BasePresenter<SetPayPwdContract.View> implements SetPayPwdContract.Presenter {
    @Override
    public void setPayPwd(String password) {
        toSubscribe(HttpManager.getApi().setPayPwd(password), new HttpSubscriber() {
            @Override
            protected void _onStart() {
                mView.showLoading("");
            }

            @Override
            protected void _onNext(Object o) {
                mView.setPayPwdSuccess();
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorMsg(message, null);
            }

            @Override
            protected void _onCompleted() {
                mView.stopLoading();
            }
        });
    }
}
