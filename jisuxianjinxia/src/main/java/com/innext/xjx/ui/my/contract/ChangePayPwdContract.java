package com.innext.xjx.ui.my.contract;

import com.innext.xjx.base.BaseView;

/**
 * Created by gyl on 2017/2/18 0018.
 */

public interface ChangePayPwdContract {
    interface View extends BaseView {
        void changePayPwdSuccess();
    }

    interface Presenter {
        void changePayPwd(String old_pwd, String new_pwd);
    }
}
