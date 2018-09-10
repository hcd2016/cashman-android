package com.innext.xjx.ui.repayment.contract;

import com.innext.xjx.base.BaseView;
import com.innext.xjx.ui.repayment.bean.RepaymentItemBean;

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
