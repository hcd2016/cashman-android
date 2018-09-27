package com.credit.xiaowei.ui.my.presenter;

import com.credit.xiaowei.base.BasePresenter;
import com.credit.xiaowei.http.HttpManager;
import com.credit.xiaowei.http.HttpSubscriber;
import com.credit.xiaowei.ui.my.contract.UploadPOIContract;

/**
 * Created by Administrator on 2017/2/13 0013.
 */

public class UploadPOIPresenter extends BasePresenter<UploadPOIContract.View> implements UploadPOIContract.Presenter {
    public final String TYPE_UPLOAD_POI = "uploadPOI";
    @Override
    public void uploadPOI(String longitude, String latitude, String address, String time) {
        toSubscribe(HttpManager.getApi().uploadLocation(longitude, latitude, address, time)
                , new HttpSubscriber() {
                    @Override
                    protected void _onStart() {
                        mView.showLoading("");
                    }

                    @Override
                    protected void _onNext(Object o) {
                        mView.uploadPOISuccess();
                    }

                    @Override
                    protected void _onError(String message) {
                        mView.showErrorMsg(message,TYPE_UPLOAD_POI);
                    }

                    @Override
                    protected void _onCompleted() {
                        mView.stopLoading();
                    }
                });
    }
}
