package com.innext.xjx.ui.lend.contract;

import com.innext.xjx.base.BaseView;
import com.innext.xjx.ui.lend.bean.ConfirmLoanBean;
import com.innext.xjx.ui.lend.bean.HomeIndexResponseBean;

/**
 * Created by xiejingwen at 2017/2/15 0015
 */

public interface LendContract {
    interface View extends BaseView{
        void indexSuccess(HomeIndexResponseBean result);
        void toLoanSuccess(ConfirmLoanBean result);
        void confirmFailedSuccess();
    }
    interface Presenter{
        /**
         * 首页数据
         */
        void loadIndex();

        /**
         * 验证借款信息
         * @param money
         * @param period
         */
        void toLoan(String money,String period);
        /**
         * 借款被拒 点击按钮调用。
         *
         * @param id
         */
        void confirmFailed(String id);
    }
}
