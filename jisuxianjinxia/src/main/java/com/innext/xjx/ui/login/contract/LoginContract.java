package com.innext.xjx.ui.login.contract;

import com.innext.xjx.base.BaseView;
import com.innext.xjx.ui.my.bean.UserInfoBean;

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
