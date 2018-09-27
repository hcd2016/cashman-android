package com.credit.xiaowei.ui.login.contract;

import com.credit.xiaowei.base.BaseView;

/**
 * Created by gyl on 2017/2/16 0016.
 */

public interface UpdataPwdContract {
    interface View extends BaseView {
        void UpdataPwdSuccess();
    }

    interface Presenter {
        void UpdataPwd(String old_pwd,
                      String new_pwd);
    }
}
