package com.innext.xjx.ui.my.presenter;

import com.innext.xjx.base.BasePresenter;
import com.innext.xjx.http.HttpManager;
import com.innext.xjx.http.HttpSubscriber;
import com.innext.xjx.ui.my.contract.ChangePayPwdContract;

/**
 * Created by gyl on 2017/2/18 0018.
 */

public class ChangPayPwdPresenter extends BasePresenter<ChangePayPwdContract.View> implements ChangePayPwdContract.Presenter{
    @Override
    public void changePayPwd(String old_pwd, String new_pwd) {
        toSubscribe(HttpManager.getApi().changePayPassWord(old_pwd, new_pwd), new HttpSubscriber() {
            @Override
            protected void _onStart() {
                mView.showLoading("");
            }

            @Override
            protected void _onNext(Object o) {
                mView.changePayPwdSuccess();
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
