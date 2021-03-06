package com.credit.pretend.fragment;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.credit.pretend.activity.HistoryQueryActivity;
import com.credit.pretend.activity.PtdSettingActivity;
import com.credit.xiaowei.R;
import com.credit.xiaowei.base.BaseFragment;
import com.credit.xiaowei.util.ViewUtil;
import com.credit.xiaowei.widget.DrawableCenterTextView;

import butterknife.BindView;
import butterknife.OnClick;

public class PtdMineFragment extends BaseFragment {
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


    @Override
    public int getLayoutId() {
        return R.layout.ptd_fragment_mine;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void loadData() {
        mTitle.setTitle(false, "我的");
        tvVersion.setText("V" + ViewUtil.getAppVersion(getActivity()));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
