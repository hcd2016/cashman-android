package com.innext.xjx.ui.my.contract;

import com.innext.xjx.base.BaseView;

/**
 * Created by Administrator on 2017/2/13 0013.
 */

public interface UploadPOIContract {
    interface View extends BaseView{
        void uploadPOISuccess();
    }
    interface Presenter {
        void uploadPOI(String longitude,
                       String latitude,
                       String address,
                       String time);
    }
}
