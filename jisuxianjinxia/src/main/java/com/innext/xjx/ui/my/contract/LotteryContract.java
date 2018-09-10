package com.innext.xjx.ui.my.contract;

import com.innext.xjx.base.BaseView;
import com.innext.xjx.ui.my.bean.Lottery;

import java.util.List;

/**
 * Created by gym on 2017/2/15 0015.
 * 描述：
 */

public interface LotteryContract {
    interface View extends BaseView {
        void lotterySuccess(List<Lottery.ItemBean> itemBean);
    }

    interface presenter {
        void lotteryRequest(String phone, String page, String pageSize);
    }
}
