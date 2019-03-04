package com.credit.xiaowei.ui.lend.presenter;

import com.credit.xiaowei.base.BasePresenter;
import com.credit.xiaowei.http.HttpManager;
import com.credit.xiaowei.http.HttpSubscriber;
import com.credit.xiaowei.ui.lend.bean.ConfirmLoanResponseBean;
import com.credit.xiaowei.ui.lend.bean.HomeIndexResponseBean;
import com.credit.xiaowei.ui.lend.contract.LendContract;
import com.google.gson.JsonObject;

/**
 * 首页presenter
 */

public class LendPresenter extends BasePresenter<LendContract.View> implements LendContract.Presenter{
    public final String TYPE_INDEX = "index";
    public final String  TYPE_LOAN = "toLoan";
    public final String TYPE_FAILED = "failed";
    public final String TYPE_OTHER_PRODUCT = "oher_product";
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

//    @Override
//    public void showOtherProduct() {
//        toSubscribe(HttpManager.getApi().getOtherProductList(), new HttpSubscriber<OtherProductBean>() {
//            @Override
//            protected void _onStart() {
//                mView.showLoading("");
//            }
//
//            @Override
//            protected void _onNext(OtherProductBean otherProductBean) {
//                mView.showOtherProductSuccess(otherProductBean);
//            }
//
//            @Override
//            protected void _onError(String message) {
//                mView.showErrorMsg(message,TYPE_OTHER_PRODUCT);
//            }
//
//            @Override
//            protected void _onCompleted() {
//                mView.stopLoading();
//            }
//        });
//    }

    @Override
    public void moreCommit(int id, final String product_url) {
        toSubscribe(HttpManager.getApi().commitMoreClick(id+""), new HttpSubscriber<JsonObject>() {
            @Override
            protected void _onStart() {
                mView.showLoading("加载中");
            }

            @Override
            protected void _onNext(JsonObject JsonObject) {
                mView.moreCommitSucess(JsonObject,product_url);
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorMsg(message,"");
            }

            @Override
            protected void _onCompleted() {
                mView.stopLoading();
            }
        });
    }


}
