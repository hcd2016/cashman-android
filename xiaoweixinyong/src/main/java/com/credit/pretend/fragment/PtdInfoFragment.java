package com.credit.pretend.fragment;

import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.credit.pretend.adapter.PtdInfoAdapter;
import com.credit.pretend.bean.HotListBean;
import com.credit.pretend.ptd_util.RetrofitUtil;
import com.credit.xiaowei.R;
import com.credit.xiaowei.base.BaseFragment;
import com.credit.xiaowei.widget.DrawableCenterTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PtdInfoFragment extends BaseFragment {
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
    private PtdInfoAdapter ptdInfoAdapter;

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
    }
}
