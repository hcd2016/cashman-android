package com.innext.xjx.ui.authentication.contract;

import com.innext.xjx.base.BaseView;

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
