package com.credit.xiaowei.ui.my.contract;

import com.credit.xiaowei.base.BaseView;
import com.credit.xiaowei.ui.my.bean.MoreContentBean;

/**
 * Created by Administrator on 2017/2/13 0013.
 */

public interface MyContract {
    interface View extends BaseView{
        void userInfoSuccess(MoreContentBean result);
    }
    interface Presenter {
        void getInfo();
    }
}
