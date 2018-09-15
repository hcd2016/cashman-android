package com.innext.pretend.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.innext.xjx.R;
import com.innext.xjx.base.BaseActivity;
import com.innext.xjx.widget.DrawableCenterTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 查询结果页
 */
public class QueryResultActivity extends BaseActivity {
    @BindView(R.id.tv_left)
    DrawableCenterTextView tvLeft;
    @BindView(R.id.tv_close)
    DrawableCenterTextView tvClose;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    DrawableCenterTextView tvRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_emotion)
    ImageView ivEmotion;
    @BindView(R.id.tv_desc_1)
    TextView tvDesc1;
    @BindView(R.id.tv_desc_2)
    TextView tvDesc2;
    @BindView(R.id.tv_credit_points)
    TextView tvCreditPoints;
    @BindView(R.id.tv_id_info)
    TextView tvIdInfo;
    @BindView(R.id.tv_phone_num_desc)
    TextView tvPhoneNumDesc;
    @BindView(R.id.tv_natural_info)
    TextView tvNaturalInfo;
    @BindView(R.id.tv_court_info)
    TextView tvCourtInfo;
    @BindView(R.id.tv_borrowing_info)
    TextView tvBorrowingInfo;

    @Override
    public int getLayoutId() {
        return R.layout.ptd_activity_query_result;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void loadData() {
        mTitle.setTitle("报告详情");
    }
}
