package com.innext.xjx.ui.lend.presenter;

import com.innext.xjx.base.BasePresenter;
import com.innext.xjx.ui.lend.bean.ConfirmLoanResponseBean;
import com.innext.xjx.ui.lend.bean.HomeIndexResponseBean;
import com.innext.xjx.http.HttpManager;
import com.innext.xjx.http.HttpSubscriber;
import com.innext.xjx.ui.lend.contract.LendContract;

/**
 * 首页presenter
 */

public class LendPresenter extends BasePresenter<LendContract.View> implements LendContract.Presenter{
    public final String TYPE_INDEX = "index";
    public final String  TYPE_LOAN = "toLoan";
    public final String TYPE_FAILED = "failed";
    @Override
    public void loadIndex() {
        toSubscribe(HttpManager.getApi().index(), new HttpSubscriber<HomeIndexResponseBean>() {
            @Override
            public void _onStart() {
                mView.showLoading("加载中...");
            }

            @Override
            protected void _onNext(HomeIndexResponseBean homeIndexResponseBean) {
                if (homeIndexResponseBean==null||homeIndexResponseBean.getItem()==null){
                    mView.showErrorMsg("获取信息失败,请稍后重新",TYPE_INDEX);
                }else{
                    mView.indexSuccess(homeIndexResponseBean);
                }
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorMsg(message,TYPE_INDEX);
            }

            @Override
            protected void _onCompleted() {
                mView.stopLoading();
            }
        });
    }

    @Override
    public void toLoan(String money, String period) {
        toSubscribe(HttpManager.getApi().toLoan(money, period), new HttpSubscriber<ConfirmLoanResponseBean>() {
            @Override
            public void _onStart() {
                mView.showLoading("验证中...");
            }

            @Override
            protected void _onNext(ConfirmLoanResponseBean confirmLoanResponseBean) {
                if (confirmLoanResponseBean!=null&&confirmLoanResponseBean.getItem()!=null){
                    mView.toLoanSuccess(confirmLoanResponseBean.getItem());
                }else{
                    mView.showErrorMsg("获取验证信息失败,请稍后重新",TYPE_LOAN);
                }
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorMsg(message,TYPE_LOAN);
            }

            @Override
            protected void _onCompleted() {
                mView.stopLoading();
            }
        });
    }

    @Override
    public void confirmFailed(String id) {
        toSubscribe(HttpManager.getApi().confirmFailed(id), new HttpSubscriber() {
            @Override
            public void _onStart() {
                mView.showLoading("");
            }

            @Override
            protected void _onNext(Object o) {
                mView.confirmFailedSuccess();
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorMsg(message,TYPE_FAILED);
            }

            @Override
            protected void _onCompleted() {
                mView.stopLoading();
            }
        });
    }
}
