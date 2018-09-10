package com.innext.xjx.ui.authentication.contract;

import com.innext.xjx.base.BaseView;
import com.innext.xjx.ui.authentication.bean.GetWorkInfoBean;

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
