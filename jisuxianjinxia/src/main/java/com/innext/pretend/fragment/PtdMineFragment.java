package com.innext.pretend.fragment;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innext.pretend.activity.HistoryQueryActivity;
import com.innext.pretend.activity.PtdSettingActivity;
import com.innext.xjx.R;
import com.innext.xjx.base.BaseFragment;
import com.innext.xjx.util.ViewUtil;
import com.innext.xjx.widget.DrawableCenterTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class PtdMineFragment extends BaseFragment {
    public static PtdMineFragment ptdMineFragment;
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
    @BindView(R.id.ll_item_history_query)
    LinearLayout llItemHistoryQuery;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.ll_item_setting)
    LinearLayout llItemSetting;
    Unbinder unbinder;

    public static PtdMineFragment getInstance() {
        if (ptdMineFragment == null) {
            ptdMineFragment = new PtdMineFragment();
        }
        return ptdMineFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.ptd_fragment_mine;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void loadData() {
        mTitle.setTitle(false,"我的");
        tvVersion.setText("V"+ViewUtil.getAppVersion(getActivity()));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.ll_item_history_query, R.id.ll_item_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_item_history_query://历史查询
                startActivity(HistoryQueryActivity.class);
                break;
            case R.id.ll_item_setting://设置
                startActivity(PtdSettingActivity.class);
                break;
        }
    }
}
