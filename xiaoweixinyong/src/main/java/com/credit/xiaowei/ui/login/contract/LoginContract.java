package com.credit.xiaowei.ui.login.contract;

import com.credit.xiaowei.base.BaseView;
import com.credit.xiaowei.ui.my.bean.UserInfoBean;

/**
 * Created by Administrator on 2017/2/9 0009.
 */

public interface LoginContract {
    interface View extends BaseView{
        void loginSuccess(UserInfoBean bean);
    }
    interface presenter{
        void login(String username,String password);
    }
}
