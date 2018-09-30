package com.credit.xiaowei.ui.authentication.presenter;

import android.text.TextUtils;

import com.credit.xiaowei.base.BasePresenter;
import com.credit.xiaowei.http.HttpManager;
import com.credit.xiaowei.http.HttpSubscriber;
import com.credit.xiaowei.ui.authentication.bean.BankInfoBean;
import com.credit.xiaowei.ui.authentication.bean.GetBankListBean;
import com.credit.xiaowei.ui.authentication.contract.AddBankCardContract;

/**
 * Created by xiejingwen on 2017/2/18 0018.
 */

public class AddBankCardPresenter extends BasePresenter<AddBankCardContract.View> implements AddBankCardContract.Presenter {
    public final String TYPE_GET_CODE = "getCardCode";
    public final String TYPE_GET_CARD_LIST = "getCardList";
    public final String TYPE_ADD_BANK_CARD= "addBankCard";
    @Override
    public void getCardCode(String phone) {
        toSubscribe(HttpManager.getApi().getCardCode(phone),new HttpSubscriber(){

            @Override
            protected void _onStart() {
                mView.showLoading("发送中...");
            }

            @Override
            protected void _onNext(Object o) {
                mView.getCardCodeSuccess();
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorMsg(message,TYPE_GET_CODE);
            }

            @Override
            protected void _onCompleted() {
                mView.stopLoading();
            }
        });
    }

    @Override
    public void getBankCardList() {
        toSubscribe(HttpManager.getApi().getBankCardList(), new HttpSubscriber<GetBankListBean>() {
            @Override
            protected void _onStart() {
                mView.showLoading("加载中...");
            }

            @Override
            protected void _onNext(GetBankListBean getBankListBean) {
                if (getBankListBean!=null){
                    mView.getBankCardListSuccess(getBankListBean.getItem());
                }else{
                    mView.showErrorMsg("加载失败,请重试",TYPE_GET_CARD_LIST);
                }
            }

            @Override
            protected void _onError(String message) {
                mView.showErrorMsg(message,TYPE_GET_CARD_LIST);
            }

            @Override
            protected void _onCompleted() {
                mView.stopLoading();
            }
        });
    }

    @Override
    public void addBankCard(String phone, String code, String card_no, String bank_id) {
        toSubscribe(HttpManager.getApi().addBankCard(phone, code, card_no, bank_id)
                , new HttpSubscriber<BankInfoBean>() {
                    @Override
                    protected void _onStart() {
                        mView.showLoading("验证中..");
                    }

                    @Override
                    protected void _onNext(BankInfoBean bankInfoBean) {
                        if (bankInfoBean!=null&& !TextUtils.isEmpty(bankInfoBean.getSignpath())){
                            mView.addBankCardSuccess(bankInfoBean.getSignpath());
                        }else{
                            mView.showErrorMsg("保存失败,请重试",TYPE_ADD_BANK_CARD);
                        }
                    }

                    @Override
                    protected void _onError(String message) {
                        mView.showErrorMsg(message,TYPE_ADD_BANK_CARD);
                    }

                    @Override
                    protected void _onCompleted() {
                        mView.stopLoading();
                    }
                });
    }
}
