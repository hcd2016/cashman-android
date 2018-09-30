package com.credit.xiaowei.ui.authentication.contract;

import com.credit.xiaowei.base.BaseView;
import com.credit.xiaowei.ui.authentication.bean.MyRelationBean;

/**
 * Created by xiejingwen at 2017/2/16 0016
 */
public interface EmergencyContactContract {
    interface View extends BaseView {
        void getContactsSuccess(MyRelationBean result);

        void saveContactsSuccess();
    }

    interface Presenter {
        void getContacts();

        void saveContacts(String type,
                          String mobile,
                          String name,
                          String relation_spare,
                          String mobile_spare,
                          String name_spare);
    }
}
