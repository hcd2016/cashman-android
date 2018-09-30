package com.credit.xiaowei.ui.my.adapter;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.credit.xiaowei.R;
import com.credit.xiaowei.ui.my.bean.Lottery;
import com.credit.xiaowei.widget.recycler.BaseRecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gym on 2017/2/15 0015.
 * 描述：抽奖码适配器
 */

public class MyLotteryAdapter extends BaseRecyclerAdapter<MyLotteryAdapter.ViewHolder, Lottery.ItemBean> {

    @Override
    public ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.layout_item_lottery, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void mOnBindViewHolder(ViewHolder holder, int position) {
        if (position % 2 == 0) {
            holder.mLlLayout.setBackgroundColor(Color.parseColor("#f6f6f6"));
        } else {
            holder.mLlLayout.setBackgroundColor(Color.parseColor("#ededed"));
        }
        holder.mTvPeriod.setText(data.get(position).getPeriods() + "");
        holder.mTvLotteryYards.setText(data.get(position).getLuckyDraw() + "");
        holder.mTvFrom.setText(data.get(position).getStepName());
        if (data.get(position).getStatus() == 0) {
            holder.mTvStatus.setText("未开奖");
            holder.mTvStatus.setTextColor(mContext.getResources().getColor(R.color.black_6));
            holder.mTvStatus.setTypeface(Typeface.DEFAULT);
        } else if (data.get(position).getStatus() == 1) {
            holder.mTvStatus.setText("未中奖");
            holder.mTvStatus.setTextColor(mContext.getResources().getColor(R.color.black_6));
            holder.mTvStatus.setTypeface(Typeface.DEFAULT);
        } else if (data.get(position).getStatus() == 2) {
            holder.mTvStatus.setText("抽中\n" + data.get(position).getAwardMoney() + "元");
            holder.mTvStatus.setTextColor(mContext.getResources().getColor(R.color.theme_color));
            holder.mTvStatus.setTypeface(Typeface.DEFAULT_BOLD);
            //mHolder.mTvStatus.setText("已发放");
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ll_layout)
        LinearLayout mLlLayout;
        @BindView(R.id.tv_period)
        TextView mTvPeriod;
        @BindView(R.id.tv_lottery_yards)
        TextView mTvLotteryYards;
        @BindView(R.id.tv_from)
        TextView mTvFrom;
        @BindView(R.id.tv_status)
        TextView mTvStatus;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
