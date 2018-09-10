package com.innext.xjx.ui.repayment.presenter;

import com.innext.xjx.base.BasePresenter;
import com.innext.xjx.ui.repayment.bean.RepaymentBean;
import com.innext.xjx.http.HttpManager;
import com.innext.xjx.http.HttpSubscriber;
import com.innext.xjx.ui.repayment.contract.RepaymentContract;

/**
 * 还款
 */

public class RePaymentPresenter extends BasePresenter<RepaymentContract.View> implements RepaymentContract.Presenter {
    @Override
    public void getMyLoan() {
        toSubscribe(HttpManager.getApi().getMyLoan(), new HttpSubscriber<RepaymentBean>() {

            @Override
            protected void _onStart() {
                mView.showLoading("加载中...");
            }

            @Override
            protected void _onNext(RepaymentBean repaymentBean) {
                if (repaymentBean != null && repaymentBean.getItem() != null) {
                    mView.repaymentSuccess(repaymentBean.getItem());
                } else {
                    mView.showErrorMsg("获取还款信息失败，请稍后重新",null);
                }
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
