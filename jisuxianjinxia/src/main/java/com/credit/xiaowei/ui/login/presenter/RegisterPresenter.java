package com.credit.xiaowei.ui.login.presenter;/**
 * Created by Administrator on 2017/2/16 0016.
 */

import com.credit.xiaowei.base.BasePresenter;
import com.credit.xiaowei.http.HttpManager;
import com.credit.xiaowei.http.HttpSubscriber;
import com.credit.xiaowei.ui.login.bean.RegisterBean;
import com.credit.xiaowei.ui.login.contract.RegisterContract;

/**
 * Created by xiejingwen at 2017/2/16 0016
 */
public class RegisterPresenter extends BasePresenter<RegisterContract.View> implements RegisterContract.Presenter {
    private final String TYPE_REGISTER = "register";
    @Override
    public void toRegister(String phone, String code, String password, String source, String invite_code,String user_from) {
        toSubscribe(HttpManager.getApi().register(phone,code,password,source,invite_code,user_from)
        ,new HttpSubscriber<RegisterBean>(){

                    @Override
                    protected void _onStart() {
                        mView.showLoading("注册中...");
                    }

                    @Override
                    protected void _onNext(RegisterBean register) {
                        if (register!=null&&register.getItem()!=null){
                            mView.registerSuccess(register.getItem());
                        }else{
                            mView.showErrorMsg("注册信息获取失败，请重试",TYPE_REGISTER);
                        }
                    }

                    @Override
                    protected void _onError(String message) {
                        mView.showErrorMsg(message,TYPE_REGISTER);
                    }

                    @Override
                    protected void _onCompleted() {
                        mView.stopLoading();
                    }
                });
    }
}
