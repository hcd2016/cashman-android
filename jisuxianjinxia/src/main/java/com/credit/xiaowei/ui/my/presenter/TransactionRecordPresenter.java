package com.credit.xiaowei.ui.my.presenter;

import com.credit.xiaowei.base.BasePresenter;
import com.credit.xiaowei.http.HttpManager;
import com.credit.xiaowei.http.HttpSubscriber;
import com.credit.xiaowei.ui.my.bean.TransactionBean;
import com.credit.xiaowei.ui.my.contract.TransactionRecordContract;

/**
 * Created by gym on 2017/2/16 0016.
 * 描述：
 */

public class TransactionRecordPresenter extends BasePresenter<TransactionRecordContract.View> implements TransactionRecordContract.presenter {
    @Override
    public void recordRequest(String page, String pageSize) {
        toSubscribe(HttpManager.getApi().recordRequest(page, pageSize), new HttpSubscriber<TransactionBean>() {
            @Override
            protected void _onStart() {
                mView.showLoading("");
            }

            @Override
            protected void _onNext(TransactionBean transactionBean) {
                if (transactionBean.getItem() != null) {
                    mView.recordSuccess(transactionBean.getItem(),transactionBean.getLink_url());
                } else {
                    mView.showErrorMsg("数据获取失败，请重新获取", null);
                }
            }


            @Override
            protected void _onError(String message) {

            }

            @Override
            protected void _onCompleted() {

            }
        });
    }
}
