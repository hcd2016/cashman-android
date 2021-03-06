package com.credit.xiaowei.ui.my.presenter;

import com.credit.xiaowei.base.BasePresenter;
import com.credit.xiaowei.http.HttpManager;
import com.credit.xiaowei.http.HttpSubscriber;
import com.credit.xiaowei.ui.my.bean.MoreBean;
import com.credit.xiaowei.ui.my.contract.MyContract;

/**
 * 我的
 */

public class MyPresenter extends BasePresenter<MyContract.View> implements MyContract.Presenter{

    @Override
    public void getInfo() {
        toSubscribe(HttpManager.getApi().getInfo(),new HttpSubscriber<MoreBean>(){

            @Override
            protected void _onStart() {
                mView.showLoading("加载中...");
            }

            @Override
            protected void _onNext(MoreBean moreBean) {
                if (moreBean!=null&&moreBean.getItem()!=null){
                    mView.userInfoSuccess(moreBean.getItem());
                }else{
                    mView.showErrorMsg("数据获取失败，请重新获取",null);
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
