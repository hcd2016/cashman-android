package com.credit.xiaowei.ui.lend.contract;/**
 * Created by Administrator on 2017/2/15 0015.
 */

import com.credit.xiaowei.base.BaseView;

/**
 * Created by xiejingwen at 2017/2/15 0015
 */
public interface UploadContentsContract  {
    interface View extends BaseView {
        //type:1短信，2app，3通讯录
        void uploadSuccess(String type);
    }
    interface Presenter {
        void toUploadContents(String type,String data);
    }
}
