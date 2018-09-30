package com.credit.xiaowei.ui.login.contract;

import com.credit.xiaowei.base.BaseView;

/**
 * Created by Administrator on 2017/2/13 0013.
 */

public interface LoginOutContract {
    interface View extends BaseView{
        void loginOutSuccess();
    }
    interface Presenter{
        void loginOut();
    }
}
