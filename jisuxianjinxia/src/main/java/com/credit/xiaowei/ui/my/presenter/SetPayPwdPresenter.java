package com.credit.xiaowei.ui.my.presenter;

import com.credit.xiaowei.base.BasePresenter;
import com.credit.xiaowei.http.HttpManager;
import com.credit.xiaowei.http.HttpSubscriber;
import com.credit.xiaowei.ui.my.contract.SetPayPwdContract;

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
