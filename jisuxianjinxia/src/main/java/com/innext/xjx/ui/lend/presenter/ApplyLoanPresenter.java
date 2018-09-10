package com.innext.xjx.ui.lend.presenter;

import com.innext.xjx.base.BasePresenter;
import com.innext.xjx.http.HttpManager;
import com.innext.xjx.http.HttpSubscriber;
import com.innext.xjx.ui.lend.contract.ApplyLoanContract;


public class ApplyLoanPresenter extends BasePresenter<ApplyLoanContract.View> implements ApplyLoanContract.Presenter {
    @Override
    public void applyLoan(String money, String period, String pay_password) {
        toSubscribe(HttpManager.getApi().applyLoan(money, period, pay_password), new HttpSubscriber() {
            @Override
            protected void _onStart() {
                mView.showLoading("申请中...");
            }

            @Override
            protected void _onNext(Object o) {
                mView.applyLoanSuccess();
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorMsg(message,null);
            }

            @Override
            protected void _onError(String message, int code) {
                super._onError(message, code);
                mView.applyLoanFaild(code,message);
            }

            @Override
            protected void _onCompleted() {
                mView.stopLoading();
            }
        });
    }
}
