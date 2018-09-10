package com.innext.xjx.ui.lend.contract;

import com.innext.xjx.base.BaseView;

/**
 * 申请借款
 * Created by xiejingwen at 2017/2/15 0015
 */
public interface ApplyLoanContract {
    interface View extends BaseView{
        void applyLoanSuccess();
        void applyLoanFaild(int code,String message);
    }
    interface Presenter{
        void applyLoan(String money,String period,String pay_password);
    }
}
