package com.innext.xjx.ui.lend.contract;

import com.innext.xjx.base.BaseView;
import com.innext.xjx.ui.lend.bean.ExpenseDetailBean;

import java.util.List;

/**
 * 首页费用详情
 * Created by xiejingwen on 2017/3/7 0007.
 */

public interface ExpenseDetailContract {
    interface View extends BaseView{
        void loadExpenseDetailSuccess(List<ExpenseDetailBean> result);
    }
    interface Presenter {
        void loadExpenseDetail(String money,String day);
    }
}
