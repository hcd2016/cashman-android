package com.innext.xjx.ui.login.contract;

import com.innext.xjx.base.BaseView;

/**
 * Created by Administrator on 2017/2/9 0009.
 */

public interface ForgetPwdContract {
    interface View extends BaseView {
        void forgetPwdSuccess();
    }

    interface presenter {
        void forgetPwd(String phone,
                       String type);
    }
}
