package com.innext.xjx.ui.authentication.presenter;

import com.innext.xjx.base.BasePresenter;
import com.innext.xjx.http.HttpManager;
import com.innext.xjx.http.HttpSubscriber;
import com.innext.xjx.ui.authentication.bean.PertfecInformationRequestBean;
import com.innext.xjx.ui.authentication.contract.PertfecInformationContract;

/**
 * 作者：${黑哥} on 2017/2/14 0014 14:49
 * <p>
 * 邮箱：3244345578@qq.com
 */
public class PerfectInformationPresenter extends BasePresenter<PertfecInformationContract.View> implements PertfecInformationContract.presenter{
    @Override
    public void getPertfecInformation() {
        //请求接口
        toSubscribe(HttpManager.getApi().getPertfecInfo(), new HttpSubscriber<PertfecInformationRequestBean>() {
            @Override
            protected void _onStart() {
                mView.showLoading("正在加载...");
            }
            @Override
            protected void _onNext(PertfecInformationRequestBean pertfecInformationRequestBean) {
                mView.PertfecInformationSccess(pertfecInformationRequestBean);
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

//
}
