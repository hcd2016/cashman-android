package com.credit.xiaowei.ui.authentication.contract;

import android.widget.ImageView;

import com.credit.xiaowei.base.BaseView;
import com.credit.xiaowei.ui.authentication.bean.ImageDataBean;
import com.credit.xiaowei.ui.authentication.bean.PersonalInformationRequestBean;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 作者：${黑哥} on 2017/2/15 0015 15:08
 * <p>  个人信息
 * 邮箱：3244345578@qq.com
 */
public interface PersonalInformationContract {
    interface View extends BaseView {
        //个人信息
        void PersonalInformationSccess(PersonalInformationRequestBean bean);

        //上传图片
        void UpLoadImageSccess(ImageDataBean imageDataBean, int type, File file, ImageView mImageView);

        //保存个人信息
        void SaveInformationSuccess();
    }
    //获取个人信息
    interface presenter{
        void  getPersonalInformation();

        void  getUpLoadImage( File file,Map<String,Integer> map,ImageView mImageView);
        //保存个人信息
        void  getSaveInformation(int tag,HashMap<String,String> map);

    }





}
