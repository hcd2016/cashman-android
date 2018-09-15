package com.innext.pretend.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.innext.pretend.adapter.HistoryQueryDateAdapter;
import com.innext.pretend.bean.QueryResultBean;
import com.innext.xjx.R;
import com.innext.xjx.base.BaseActivity;
import com.innext.xjx.config.Constant;
import com.innext.xjx.util.SpUtil;
import com.innext.xjx.widget.DrawableCenterTextView;

import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 历史查询
 */
public class HistoryQueryActivity extends BaseActivity {
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
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.fl_no_record)
    FrameLayout flNoRecord;

    @Override
    public int getLayoutId() {
        return R.layout.ptd_activity_history_query;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void loadData() {
        mTitle.setTitle("历史查询");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        String json = SpUtil.getString(Constant.QUERY_DATE_MAP);
        Map<String, List<QueryResultBean>> map;
        if (!TextUtils.isEmpty(json)) {
            flNoRecord.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            map = new Gson().fromJson(json, new TypeToken<Map<String, List<QueryResultBean>>>() {
            }.getType());
            if (map != null) {
                HistoryQueryDateAdapter historyQueryDateAdapter = new HistoryQueryDateAdapter(this, map);
                recyclerView.setAdapter(historyQueryDateAdapter);
            }
        } else {//暂无记录
            flNoRecord.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }
}
