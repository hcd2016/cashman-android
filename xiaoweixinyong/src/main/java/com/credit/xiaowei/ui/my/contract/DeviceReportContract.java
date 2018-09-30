package com.credit.xiaowei.ui.my.contract;/**
 * Created by Administrator on 2017/2/15 0015.
 */

import com.credit.xiaowei.base.BaseView;

/**
 * Created by xiejingwen at 2017/2/15 0015
 */
public interface DeviceReportContract  {
    interface View extends BaseView{
        void deviceReportSuccess();
    }
    interface Presenter{
        void deviceReport(String device_id,
                          String installed_time,
                          String uid,
                          String username,
                          String net_type,
                          String identifyID,
                          String appMarket);
    }
}
