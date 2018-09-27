package com.credit.xiaowei.ui.my.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.credit.xiaowei.R;
import com.credit.xiaowei.ui.main.WebViewActivity;
import com.credit.xiaowei.ui.my.bean.TransactionRecordListBean;
import com.credit.xiaowei.widget.recycler.BaseRecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gym on 2017/2/15 0015.
 * 描述：抽奖码适配器
 */

public class TransactionRecordAdapter extends BaseRecyclerAdapter<TransactionRecordAdapter.ViewHolder, TransactionRecordListBean> {

    @Override
    public ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.transaction_record_item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void mOnBindViewHolder(ViewHolder holder, final int position) {
//        final TransactionRecordListBean bean = getItem(position);
        holder.tv_transaction_title.setText(data.get(position).getTitle());
        holder.tv_transaction_time.setText(data.get(position).getTime());
        holder.tv_transaction_status.setText(Html.fromHtml(data.get(position).getText()));

        holder.rl_transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("title", "");
                intent.putExtra("url", data.get(position).getUrl());
                mContext.startActivity(intent);
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
//        transactionHolder.rl_transaction = (RelativeLayout) convertView.findViewById(R.id.rl_transaction);
//        transactionHolder.tv_transaction_title = (TextView) convertView.findViewById(R.id.tv_transaction_title);
//        transactionHolder.tv_transaction_time = (TextView) convertView.findViewById(R.id.tv_transaction_time);
//        transactionHolder.tv_transaction_status = (TextView) convertView.findViewById(R.id.tv_transaction_status);
        @BindView(R.id.rl_transaction)
        RelativeLayout rl_transaction;
        @BindView(R.id.tv_transaction_title)
        TextView tv_transaction_title;
        @BindView(R.id.tv_transaction_time)
        TextView tv_transaction_time;
        @BindView(R.id.tv_transaction_status)
        TextView tv_transaction_status;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
