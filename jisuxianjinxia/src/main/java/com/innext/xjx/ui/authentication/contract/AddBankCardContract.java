package com.innext.xjx.ui.authentication.contract;

import com.innext.xjx.base.BaseView;
import com.innext.xjx.bean.BankItem;

import java.util.List;

/**
 * Created by xiejingwen on 2017/2/18 0018.
 */

public interface AddBankCardContract {
    interface View extends BaseView {
        void getCardCodeSuccess();

        void getBankCardListSuccess(List<BankItem> list);

        void addBankCardSuccess(String signpath);
    }

    interface Presenter {
        void getCardCode(String phone);

        void getBankCardList();

        void addBankCard(String phone,
                         String code,
                         String card_no,
                         String bank_id);
    }
}
