package com.innext.xjx.ui.my.contract;

import com.innext.xjx.base.BaseView;

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
