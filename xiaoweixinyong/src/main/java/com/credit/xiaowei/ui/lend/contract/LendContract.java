package com.credit.xiaowei.ui.lend.contract;

import com.credit.xiaowei.base.BaseView;
import com.credit.xiaowei.ui.lend.bean.ConfirmLoanBean;
import com.credit.xiaowei.ui.lend.bean.HomeIndexResponseBean;
import com.google.gson.JsonObject;

/**
 * Created by xiejingwen at 2017/2/15 0015
 */

public interface LendContract {
    interface View extends BaseView{
        void indexSuccess(HomeIndexResponseBean result);
        void toLoanSuccess(ConfirmLoanBean result);
        void confirmFailedSuccess();
//        void showOtherProductSuccess(OtherProductBean otherProductBean);
        void moreCommitSucess(JsonObject jsonObject, String product_url);
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

//        /**
//         * 借款被拒 展示其他产品(贷超)
//         */
//        void showOtherProduct();

        void moreCommit(int id, String product_url);
    }
}
