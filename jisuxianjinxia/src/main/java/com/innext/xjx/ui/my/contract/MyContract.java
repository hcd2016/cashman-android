package com.innext.xjx.ui.my.contract;

import com.innext.xjx.base.BaseView;
import com.innext.xjx.ui.my.bean.MoreContentBean;

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
