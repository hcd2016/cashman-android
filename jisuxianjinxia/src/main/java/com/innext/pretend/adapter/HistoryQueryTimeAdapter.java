package com.innext.pretend.adapter;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innext.pretend.activity.HistoryQueryActivity;
import com.innext.pretend.activity.QueryResultActivity;
import com.innext.pretend.bean.QueryResultBean;
import com.innext.xjx.R;
import com.innext.xjx.widget.recycler.BaseRecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

//历史查询时间adapter
public class HistoryQueryTimeAdapter extends BaseRecyclerAdapter<HistoryQueryTimeAdapter.ViewHolder, QueryResultBean> {

    @Override
    public ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.ptd_item_query_time, null);
        return new ViewHolder(view);
    }

    @Override
    public void mOnBindViewHolder(ViewHolder holder, final int position) {
        holder.tvTime.setText(data.get(position).getTime());
        holder.tvScore.setText(data.get(position).getScore());
        holder.llItemContianer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof HistoryQueryActivity) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("queryDetailBean", data.get(position).getQueryDetailBean());
                    ((HistoryQueryActivity) mContext).startActivity(QueryResultActivity.class, bundle);
                }
            }
        });

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_score)
        TextView tvScore;
        @BindView(R.id.ll_item_contianer)
        LinearLayout llItemContianer;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
