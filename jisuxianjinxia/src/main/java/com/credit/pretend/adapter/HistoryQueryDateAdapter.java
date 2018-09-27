package com.credit.pretend.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.credit.pretend.bean.QueryResultBean;
import com.credit.xiaowei.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

//历史查询日期adapter
public class HistoryQueryDateAdapter extends RecyclerView.Adapter<HistoryQueryDateAdapter.ViewHolder> {
    Map<String, List<QueryResultBean>> map = null;
    List<String> keyList = new ArrayList<>();
    Context context;

    public HistoryQueryDateAdapter(Context context, Map<String, List<QueryResultBean>> map) {
        this.map = map;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.ptd_item_query_date, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (map != null) {
            for (Map.Entry<String, List<QueryResultBean>> entry : map.entrySet()) {//为了获取当前item对应的数据
                keyList.add(entry.getKey());
            }
            for(Map.Entry<String, List<QueryResultBean>> entry : map.entrySet()) {
                if(keyList.get(position).equals(entry.getKey())) {
                    String item = entry.getKey();
                    final List<QueryResultBean> timeList = entry.getValue();
                    holder.tvDate.setText(item);

                    //时间列表初始化
                    HistoryQueryTimeAdapter historyQueryTimeAdapter = new HistoryQueryTimeAdapter();
                    holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    holder.recyclerView.setAdapter(historyQueryTimeAdapter);
                    historyQueryTimeAdapter.addData(timeList);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return map.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.recyclerView)
        RecyclerView recyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}


