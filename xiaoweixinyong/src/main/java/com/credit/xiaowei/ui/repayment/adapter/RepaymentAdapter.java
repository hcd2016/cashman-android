package com.credit.xiaowei.ui.repayment.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.credit.xiaowei.R;
import com.credit.xiaowei.ui.repayment.bean.RepaymentListBean;
import com.credit.xiaowei.util.Tool;
import com.credit.xiaowei.widget.recycler.BaseRecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 还款列表适配器
 */

public class RepaymentAdapter extends BaseRecyclerAdapter<RepaymentAdapter.ViewHolder, RepaymentListBean> {
    private final int TYPE_1 = 100;
    private final int TYPE_2 = 101;
    private int viewType;
    @Override
    public ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.repayment_status_item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void mOnBindViewHolder(ViewHolder holder, int position) {
        holder.mTvRepaymentTime.setText(item.getPlan_fee_time());
        holder.mTvRepaymentMoney.setText("￥" + item.getDebt());
        holder.mTvLoanMoney.setText("到账金额：" + item.getReceipts() + "元");
        holder.mTvLoanInterest.setText("服务费用：" + item.getCounter_fee() + "元");
        holder.mTvStatusRepayment.setText(Html.fromHtml(item.getText_tip()));
        if (viewType == TYPE_2) {
            holder.mRlRepayment.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                    , Tool.dip2px(mContext, 125)));
            holder.mTvLate.setVisibility(View.VISIBLE);
            holder.mTvLateFee.setVisibility(View.VISIBLE);
            if (holder.mTvStatusRepayment.getText().toString().indexOf("已逾期") != -1) {
                holder.mTvLate.setText("逾期费用：");
                holder.mTvLateFee.setText(item.getLate_fee() + "元");
            }
        } else if (viewType == TYPE_1){
            holder.mRlRepayment.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                    , Tool.dip2px(mContext, 105)));
            holder.mTvLate.setVisibility(View.GONE);
            holder.mTvLateFee.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (super.getItemViewType(position) == TYPE_CONTENT) {
            if (data.get(position-getHeadersCount()).getText_tip() != null && data.get(position-getHeadersCount()).getText_tip().indexOf("已逾期") != -1) {
                viewType = TYPE_2;
            } else {
                viewType = TYPE_1;
            }
        }
        return super.getItemViewType(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_repayment_time)
        TextView mTvRepaymentTime;
        @BindView(R.id.tv_repayment_money)
        TextView mTvRepaymentMoney;
        @BindView(R.id.tv_loan_money)
        TextView mTvLoanMoney;
        @BindView(R.id.tv_loan_interest)
        TextView mTvLoanInterest;
        @BindView(R.id.tv_late)
        TextView mTvLate;
        @BindView(R.id.tv_late_fee)
        TextView mTvLateFee;
        @BindView(R.id.tv_status_repayment)
        TextView mTvStatusRepayment;
        @BindView(R.id.rl_repayment)
        RelativeLayout mRlRepayment;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
