package com.innext.xjx.ui.login.contract;/**
 * Created by Administrator on 2017/2/15 0015.
 */

import com.innext.xjx.base.BaseView;

/**
 * Created by xiejingwen at 2017/2/15 0015
 */
public interface VerifyResetPwdContract {
    interface View extends BaseView {
        void verifySuccess();
    }
    interface presenter {
        void verifyResetPwd(String phone,
                       String code,
                       String realname,
                       String id_card,
                       String type);
    }
}
