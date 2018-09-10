package com.innext.xjx.ui.login.contract;/**
 * Created by Administrator on 2017/2/16 0016.
 */

import com.innext.xjx.base.BaseView;
import com.innext.xjx.ui.my.bean.UserInfoBean;

/**
 * Created by xiejingwen at 2017/2/16 0016
 */
public interface RegisterContract {
    interface View extends BaseView {
        void registerSuccess(UserInfoBean info);
    }

    interface Presenter {
        void toRegister(String phone,
                        String code,
                        String password,
                        String source,
                        String invite_code,
                        String user_from);
    }
}
