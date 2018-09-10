package com.innext.xjx.ui.login.contract;

import com.innext.xjx.base.BaseView;

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
