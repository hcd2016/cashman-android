package com.credit.xiaowei.ui.repayment.contract;

import com.credit.xiaowei.base.BaseView;
import com.credit.xiaowei.ui.repayment.bean.RepaymentItemBean;

/**
 * 还款
 */

public interface RepaymentContract {
    interface View extends BaseView{
        void repaymentSuccess(RepaymentItemBean result);
    }

    interface Presenter {
        void getMyLoan();
    }
}
