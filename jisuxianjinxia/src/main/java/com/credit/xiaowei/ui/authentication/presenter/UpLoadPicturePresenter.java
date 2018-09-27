package com.credit.xiaowei.ui.authentication.presenter;

import com.credit.xiaowei.base.BasePresenter;
import com.credit.xiaowei.http.HttpManager;
import com.credit.xiaowei.http.HttpSubscriber;
import com.credit.xiaowei.ui.authentication.bean.GetPicListBean;
import com.credit.xiaowei.ui.authentication.bean.PicListBean;
import com.credit.xiaowei.ui.authentication.bean.SelectPicBean;
import com.credit.xiaowei.ui.authentication.contract.UpLoadPictureContract;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiejingwen on 2017/2/21 0021.
 */

public class UpLoadPicturePresenter extends BasePresenter<UpLoadPictureContract.View> implements UpLoadPictureContract.Presenter {
    public final String TYPE_GET_PIC = "getPicList";
    public final String TYPE_UPLOAD_PIC = "uploadPic";

    @Override
    public void getPicList(String type) {
        toSubscribe(HttpManager.getApi().getPicList(type), new HttpSubscriber<GetPicListBean>() {
            @Override
            protected void _onStart() {
                mView.showLoading("加载中...");
            }

            @Override
            protected void _onNext(GetPicListBean getPicListBean) {
                if (getPicListBean!=null&&getPicListBean.getItem()!=null){
                    PicListBean picListBean=getPicListBean.getItem();
                    for (int i = 0; i < picListBean.getData().size(); i++) {
                        SelectPicBean picInfo = picListBean.getData().get(i);
                        picInfo.setType(SelectPicBean.Type_Uploaded);
                    }
                    mView.getPicListSuccess(picListBean);
                }else{
                    mView.showErrorMsg("获取数据失败，请重试",TYPE_GET_PIC);
                }
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorMsg(message,TYPE_GET_PIC);
            }

            @Override
            protected void _onCompleted() {
                mView.stopLoading();
            }
        });
    }

    @Override
    public void uploadPic(final SelectPicBean info, Integer type) {
        Map<String,Integer> mParams=new HashMap<>();
        mParams.put("type",type);
        toSubscribe(HttpManager.getApi().uploadImageFile(putFile("attach",info.getUrl()), mParams), new HttpSubscriber() {
            @Override
            protected void _onStart() {
                mView.showLoading("上传中...");
            }

            @Override
            protected void _onNext(Object o) {
                mView.uploadPicSuccess(info);
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorMsg(message,TYPE_UPLOAD_PIC);
            }

            @Override
            protected void _onCompleted() {
                mView.stopLoading();
            }
        });
    }
}
