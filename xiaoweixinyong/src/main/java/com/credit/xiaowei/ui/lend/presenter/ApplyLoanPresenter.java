package com.credit.xiaowei.ui.lend.presenter;

import com.credit.xiaowei.base.BasePresenter;
import com.credit.xiaowei.http.HttpManager;
import com.credit.xiaowei.http.HttpSubscriber;
import com.credit.xiaowei.ui.lend.contract.ApplyLoanContract;


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
