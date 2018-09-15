package com.innext.pretend.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.innext.pretend.adapter.PtdInfoAdapter;
import com.innext.pretend.bean.HotListBean;
import com.innext.pretend.ptd_util.RetrofitUtil;
import com.innext.xjx.R;
import com.innext.xjx.base.BaseFragment;
import com.innext.xjx.widget.DrawableCenterTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PtdInfoFragment extends BaseFragment {
    public static PtdInfoFragment ptdInfoFragment;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;
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
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    Unbinder unbinder1;
    @BindView(R.id.fl_no_record)
    FrameLayout flNoRecord;
    Unbinder unbinder2;
    private PtdInfoAdapter ptdInfoAdapter;

    public static PtdInfoFragment getInstance() {
        if (ptdInfoFragment == null) {
            ptdInfoFragment = new PtdInfoFragment();
        }
        return ptdInfoFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_info;
    }

    @Override
    public void initPresenter() {
        requestData();
    }

    public void requestData() {
        Call<List<HotListBean>> call = RetrofitUtil.create().getHotList();
        call.enqueue(new Callback<List<HotListBean>>() {
            @Override
            public void onResponse(Call<List<HotListBean>> call, Response<List<HotListBean>> response) {
                List<HotListBean> list = response.body();
                if (null != list && list.size() != 0) {
                    ptdInfoAdapter.clearData();
                    ptdInfoAdapter.addData(list);
                    flNoRecord.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                } else {
                    flNoRecord.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                ptdInfoAdapter.notifyDataSetChanged();
                refresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<HotListBean>> call, Throwable t) {
                refresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void loadData() {
        mTitle.setTitle(false, "热点资讯");
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ptdInfoAdapter = new PtdInfoAdapter();
        recyclerView.setAdapter(ptdInfoAdapter);
        refresh.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.theme_color));
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestData();
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder1.unbind();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder2 = ButterKnife.bind(this, rootView);
        return rootView;
    }
}
