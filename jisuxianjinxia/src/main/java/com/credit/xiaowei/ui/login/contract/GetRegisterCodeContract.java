package com.credit.xiaowei.ui.login.contract;

import com.credit.xiaowei.base.BaseView;

/**
 * Created by Administrator on 2017/2/13 0013.
 */

public interface GetRegisterCodeContract {
    interface View extends BaseView{
        void getCodeSuccess();
        void showErrorMsg(String msg,int code);
    }
    interface Presenter {
        void getRegisterCode(String phone);
    }
}
