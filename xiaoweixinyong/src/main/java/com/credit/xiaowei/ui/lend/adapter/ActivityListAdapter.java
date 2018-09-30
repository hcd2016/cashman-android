package com.credit.xiaowei.ui.lend.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.credit.xiaowei.R;
import com.credit.xiaowei.ui.lend.bean.HomeIndexResponseBean;
import com.credit.xiaowei.widget.recycler.BaseRecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ActivityListAdapter extends BaseRecyclerAdapter<ActivityListAdapter.ViewHolder,HomeIndexResponseBean.IndexImagesBean> {

    @Override
    public ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.layout_exercise_item,parent,false));
    }

    @Override
    public void mOnBindViewHolder(ViewHolder holder, int position) {
        Glide.with(mContext).load(item.getUrl())
                .placeholder(R.mipmap.banner)
                .error(R.mipmap.banner)
                .centerCrop()
                .into(holder.mActivityImg); //设置图片
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.activity_img)
        ImageView mActivityImg;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}


