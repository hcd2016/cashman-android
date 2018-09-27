package com.credit.xiaowei.ui.login.contract;/**
 * Created by Administrator on 2017/2/16 0016.
 */

import com.credit.xiaowei.base.BaseView;
import com.credit.xiaowei.ui.my.bean.UserInfoBean;

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
