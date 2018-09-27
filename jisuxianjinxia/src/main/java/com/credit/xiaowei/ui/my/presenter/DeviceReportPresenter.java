package com.credit.xiaowei.ui.my.presenter;/**
 * Created by Administrator on 2017/2/15 0015.
 */

import com.credit.xiaowei.base.BasePresenter;
import com.credit.xiaowei.http.HttpManager;
import com.credit.xiaowei.http.HttpSubscriber;
import com.credit.xiaowei.ui.my.contract.DeviceReportContract;

/**
 * Created by xiejingwen at 2017/2/15 0015
 */
public class DeviceReportPresenter extends BasePresenter<DeviceReportContract.View> implements DeviceReportContract.Presenter {
    @Override
    public void deviceReport(String device_id, String installed_time, String uid, String username, String net_type, String identifyID, String appMarket) {
        toSubscribe(HttpManager.getApi().deviceReport(device_id, installed_time, uid, username, net_type, identifyID, appMarket)
                , new HttpSubscriber() {
                    @Override
                    protected void _onStart() {
                        mView.showLoading("");
                    }

                    @Override
                    protected void _onNext(Object o) {
                        mView.deviceReportSuccess();
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
