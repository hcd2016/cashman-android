package com.credit.xiaowei.ui.authentication.contract;

import com.credit.xiaowei.base.BaseView;
import com.credit.xiaowei.ui.authentication.bean.PicListBean;
import com.credit.xiaowei.ui.authentication.bean.SelectPicBean;

/**
 * Created by xiejingwen on 2017/2/21 0021.
 */

public interface UpLoadPictureContract {
    interface View extends BaseView{
        void getPicListSuccess(PicListBean data);
        void uploadPicSuccess(SelectPicBean info);
    }
    interface Presenter {
        void getPicList(String type);
        void uploadPic(SelectPicBean info,Integer type);
    }
}
