package com.innext.xjx.ui.my.contract;

import com.innext.xjx.base.BaseView;
import com.innext.xjx.ui.my.bean.TransactionRecordListBean;

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
