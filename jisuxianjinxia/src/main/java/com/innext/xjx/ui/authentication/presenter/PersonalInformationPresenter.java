package com.innext.xjx.ui.authentication.presenter;

import android.widget.ImageView;

import com.innext.xjx.base.BasePresenter;
import com.innext.xjx.http.HttpManager;
import com.innext.xjx.http.HttpSubscriber;
import com.innext.xjx.ui.authentication.bean.ImageDataBean;
import com.innext.xjx.ui.authentication.bean.PersonalInformationRequestBean;
import com.innext.xjx.ui.authentication.contract.PersonalInformationContract;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;

/**
 * 作者：${黑哥} on 2017/2/15 0015 18:08
 * <p>
 * 邮箱：3244345578@qq.com
 */
public class PersonalInformationPresenter extends BasePresenter<PersonalInformationContract.View> implements PersonalInformationContract.presenter{
    public final String TYPE_GET_INFO = "getInfo";
    @Override
    public void getPersonalInformation() {
        //请求用户信息接口
        toSubscribe(HttpManager.getApi().getPersonalInformation(), new HttpSubscriber<PersonalInformationRequestBean>() {
            @Override
            protected void _onStart() {
                mView.showLoading("正在加载...");
            }
            @Override
            protected void _onNext(PersonalInformationRequestBean personalInformationRequestBean) {
                mView.PersonalInformationSccess(personalInformationRequestBean);
            }
            @Override
            protected void _onError(String message) {
                mView.showErrorMsg(message,TYPE_GET_INFO);
            }

            @Override
            protected void _onCompleted() {
                mView.stopLoading();
            }
        });


    }


    // 上传文件
    @Override
    public void getUpLoadImage(final File file, final Map<String,Integer> map,final ImageView mImageView) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("attach", file.getName(), requestBody);
        toSubscribe(HttpManager.getApi().uploadImageFile(part, map), new HttpSubscriber<ImageDataBean>() {
            @Override
            protected void _onStart() {
                mView.showLoading("正在验证...");
            }

            @Override
            protected void _onNext(ImageDataBean imageDataBean) {
                mView.UpLoadImageSccess(imageDataBean,Integer.valueOf(map.get("type")),file,mImageView);
            }
            @Override
            protected void _onError(String message) {
                mView.showErrorMsg(message,null);
            }

            @Override
            protected void _onCompleted() {
                mView.stopLoading();
            }
        });

    }
    //请求保存个人信息接口
    @Override
    public void getSaveInformation(int tag, HashMap<String,String> map) {
        Observable ob = null;
        if(tag==1){
            ob=HttpManager.getApi().getSaveRersonInformation(map);
        }else{
            ob=HttpManager.getApi().getRersonInformation(map);
        }
        toSubscribe(ob, new HttpSubscriber() {
            @Override
            protected void _onStart() {
                mView.showLoading("保存中...");
            }
            @Override
            protected void _onNext(Object o) {
                mView.SaveInformationSuccess();
            }
            @Override
            protected void _onError(String message) {
                mView.showErrorMsg(message,null);
            }

            @Override
            protected void _onCompleted() {
                mView.stopLoading();
            }
        });
    }


}
