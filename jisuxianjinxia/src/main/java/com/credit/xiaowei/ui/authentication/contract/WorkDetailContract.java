package com.credit.xiaowei.ui.authentication.contract;

import com.credit.xiaowei.base.BaseView;
import com.credit.xiaowei.ui.authentication.bean.GetWorkInfoBean;

import java.util.Map;

/**
 * Created by xiejingwen at 2017/2/17 0017
 */
public interface WorkDetailContract {
    interface View extends BaseView{
        void getWorkInfoSuccess(GetWorkInfoBean.ItemBean result);
        void saveWorkInfoSuccess();
    }
    interface Presenter {
        void getWorkInfo();
        void saveWorkInfo(Map<String,String> params);
    }
}
