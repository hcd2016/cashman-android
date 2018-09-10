package com.innext.xjx.ui.authentication.contract;

import com.innext.xjx.base.BaseView;
import com.innext.xjx.ui.authentication.bean.PertfecInformationRequestBean;

/**
 * 作者：${黑哥} on 2017/2/14 0014 15:14
 * <p>
 * 邮箱：3244345578@qq.com
 */
public interface PertfecInformationContract {
    interface View extends BaseView {
        void PertfecInformationSccess(PertfecInformationRequestBean bean);

    }
    interface presenter{
        void  getPertfecInformation();
    }






}
