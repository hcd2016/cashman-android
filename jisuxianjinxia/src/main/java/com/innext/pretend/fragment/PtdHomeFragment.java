package com.innext.pretend.fragment;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.innext.pretend.activity.FillInInfoAvtivity;
import com.innext.xjx.R;
import com.innext.xjx.base.BaseFragment;
import com.innext.xjx.widget.DrawableCenterTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class PtdHomeFragment extends BaseFragment {
    public static PtdHomeFragment ptdHomeFragment;
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
    @BindView(R.id.btn_query)
    TextView btnQuery;
    Unbinder unbinder;

    public static PtdHomeFragment getInstance() {
        if (ptdHomeFragment == null) {
            ptdHomeFragment = new PtdHomeFragment();
        }
        return ptdHomeFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.ptd_fragment_home;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void loadData() {
        tvTitle.setText("查一查");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_query})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_query://查询
                startActivity(FillInInfoAvtivity.class);
                break;
        }
    }
}