package com.credit.xiaowei.ui.login.presenter;

import com.credit.xiaowei.base.BasePresenter;
import com.credit.xiaowei.http.HttpManager;
import com.credit.xiaowei.http.HttpSubscriber;
import com.credit.xiaowei.ui.login.contract.UpdataPwdContract;

/**
 * Created by gyl on 2017/2/16 0016.
 */

public class UpdataPwdPresenter extends BasePresenter<UpdataPwdContract.View> implements UpdataPwdContract.Presenter {
    public final String UPDATA_PWD = "UpdataPwd";

    @Override
    public void UpdataPwd(String old_pwd, String new_pwd) {
        toSubscribe(HttpManager.getApi().changePwd(old_pwd, new_pwd), new HttpSubscriber() {
            @Override
            protected void _onStart() {
                mView.showLoading("");
            }

            @Override
            protected void _onNext(Object o) {
                mView.UpdataPwdSuccess();
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorMsg(message, null);
            }

            @Override
            protected void _onCompleted() {
                mView.stopLoading();
            }
        });
    }
}
