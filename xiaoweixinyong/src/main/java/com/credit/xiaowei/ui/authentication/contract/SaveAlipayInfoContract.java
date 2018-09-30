package com.credit.xiaowei.ui.authentication.contract;

import com.credit.xiaowei.base.BaseView;

/**
 * Created by xiejingwen on 2017/2/20 0020.
 */

public interface SaveAlipayInfoContract {
    interface View extends BaseView{
        void saveInfoSuccess(String message);
    }
    interface Presenter{
        void toSaveInfo(String data);
    }
}
