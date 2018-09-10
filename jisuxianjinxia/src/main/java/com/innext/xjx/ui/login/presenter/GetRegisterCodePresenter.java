package com.innext.xjx.ui.login.presenter;

import com.innext.xjx.base.BasePresenter;
import com.innext.xjx.http.HttpManager;
import com.innext.xjx.http.HttpSubscriber;
import com.innext.xjx.ui.login.contract.GetRegisterCodeContract;

/**
 * Created by xiejingwen at 2017/2/15 0015
 */

public class GetRegisterCodePresenter extends BasePresenter<GetRegisterCodeContract.View> implements GetRegisterCodeContract.Presenter {
    public final String TYPE_REGISTER_CODE = "registerCode";
    @Override
    public void getRegisterCode(String phone) {
        toSubscribe(HttpManager.getApi().getRegisterCode(phone), new HttpSubscriber() {
            @Override
            protected void _onStart() {
                mView.showLoading("正在验证...");
            }

            @Override
            protected void _onNext(Object o) {
                mView.getCodeSuccess();
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorMsg(message,TYPE_REGISTER_CODE);
            }

            @Override
            protected void _onError(String message, int code) {
                super._onError(message, code);
                mView.showErrorMsg(message,code);
            }

            @Override
            protected void _onCompleted() {
                mView.stopLoading();
            }
        });
    }
}
