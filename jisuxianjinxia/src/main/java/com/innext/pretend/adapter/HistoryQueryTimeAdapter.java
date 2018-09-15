package com.innext.pretend.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.innext.pretend.bean.QueryResultBean;
import com.innext.xjx.R;
import com.innext.xjx.widget.recycler.BaseRecyclerAdapter;

import java.util.List;

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
    public void mOnBindViewHolder(ViewHolder holder, int position) {
        holder.tvTime.setText(data.get(position).getTime());
        holder.tvScore.setText(data.get(position).getScore());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_score)
        TextView tvScore;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
