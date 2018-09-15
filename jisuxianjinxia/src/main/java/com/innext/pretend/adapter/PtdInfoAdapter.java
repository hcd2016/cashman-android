package com.innext.pretend.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.innext.pretend.bean.HotListBean;
import com.innext.xjx.R;
import com.innext.xjx.app.App;
import com.innext.xjx.widget.recycler.BaseRecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PtdInfoAdapter extends BaseRecyclerAdapter<PtdInfoAdapter.ViewHolder,HotListBean> {


    @Override
    public ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.ptd_item_info, null);
        return new ViewHolder(view);
    }

    @Override
    public void mOnBindViewHolder(ViewHolder holder, int position) {
        HotListBean item = data.get(position);
        Glide.with(mContext).load(App.getConfig().getBaseUrl()+item.getImg_url())
                .placeholder(R.mipmap.banner)
                .error(R.mipmap.banner)
                .centerCrop()
                .into(holder.ivImg); //设置图片
        holder.tvInfo.setText(item.getTitle());
        holder.tvDate.setText(item.getCreated_date());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_img)
        ImageView ivImg;
        @BindView(R.id.tv_info)
        TextView tvInfo;
        @BindView(R.id.tv_date)
        TextView tvDate;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

