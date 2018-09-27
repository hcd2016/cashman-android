package com.credit.xiaowei.ui.login.contract;

import com.credit.xiaowei.base.BaseView;

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
