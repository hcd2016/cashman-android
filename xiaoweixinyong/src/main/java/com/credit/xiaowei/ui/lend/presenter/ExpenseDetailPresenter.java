package com.credit.xiaowei.ui.lend.presenter;

import com.credit.xiaowei.base.BasePresenter;
import com.credit.xiaowei.http.HttpManager;
import com.credit.xiaowei.http.HttpSubscriber;
import com.credit.xiaowei.ui.lend.bean.ExpenseDetailBean;
import com.credit.xiaowei.ui.lend.contract.ExpenseDetailContract;

import java.util.List;

/**
 * Created by xiejingwen on 2017/3/7 0007.
 */

public class ExpenseDetailPresenter extends BasePresenter<ExpenseDetailContract.View> implements ExpenseDetailContract.Presenter{

    @Override
    public void loadExpenseDetail(String money, String day) {
        toSubscribe(HttpManager.getApi().loadExpenseDetail(money, day), new HttpSubscriber<List<ExpenseDetailBean>>() {
            @Override
            protected void _onStart() {
                mView.showLoading("正在获取费用...");
            }

            @Override
            protected void _onNext(List<ExpenseDetailBean> expenseDetailBeen) {
                if (expenseDetailBeen==null){
                    mView.showErrorMsg("费用获取失败,请重试",null);
                    mView.loadExpenseDetailSuccess(expenseDetailBeen);
                }else if (expenseDetailBeen.size()==0){
                    mView.showErrorMsg("无费用详情信息",null);
                }else{
                    mView.loadExpenseDetailSuccess(expenseDetailBeen);
                }
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
