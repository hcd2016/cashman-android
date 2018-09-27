package com.credit.xiaowei.ui.login.contract;/**
 * Created by Administrator on 2017/2/15 0015.
 */

import com.credit.xiaowei.base.BaseView;

/**
 * Created by xiejingwen at 2017/2/15 0015
 */
public interface ResetPwdContract {
    interface View extends BaseView {
        void resetPwdSuccess();
    }

    interface Presenter {
        void resetPwd(String phone,
                      String code,
                      String password);
    }
}
