package com.innext.xjx.ui.authentication.contract;

import com.innext.xjx.base.BaseView;
import com.innext.xjx.ui.authentication.bean.PicListBean;
import com.innext.xjx.ui.authentication.bean.SelectPicBean;

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
