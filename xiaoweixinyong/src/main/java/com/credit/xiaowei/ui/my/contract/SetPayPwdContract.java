package com.credit.xiaowei.ui.my.contract;

import com.credit.xiaowei.base.BaseView;

/**
 * Created by gyl on 2017/2/18 0018.
 */

public interface SetPayPwdContract {
    interface View extends BaseView {
        void setPayPwdSuccess();
    }

    interface Presenter {
        void setPayPwd(String password);
    }
}
