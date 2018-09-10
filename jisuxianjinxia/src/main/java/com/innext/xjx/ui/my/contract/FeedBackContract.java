package com.innext.xjx.ui.my.contract;

import com.innext.xjx.base.BaseView;

/**
 * Created by Administrator on 2017/2/15 0015.
 */

public interface FeedBackContract {
    interface View extends BaseView {
        void feedBackSuccess();
    }
    interface Presenter {
        void feedBack(String content);
    }
}
