package com.credit.xiaowei.ui.my.contract;

import com.credit.xiaowei.base.BaseView;
import com.credit.xiaowei.ui.my.bean.TransactionRecordListBean;

import java.util.List;

/**
 * Created by gym on 2017/2/16 0016.
 * 描述：
 */

public interface TransactionRecordContract {
    interface View extends BaseView {
        void recordSuccess(List<TransactionRecordListBean> itemBean,String transactionBeanLinkUrl);
    }

    interface presenter {
        void recordRequest(String page, String pagesize);
    }
}
