package com.credit.xiaowei.ui.authentication.adapter;

import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.credit.xiaowei.R;
import com.credit.xiaowei.config.Constant;
import com.credit.xiaowei.dialog.AlertFragmentDialog;
import com.credit.xiaowei.ui.authentication.bean.SelectPicBean;
import com.credit.xiaowei.util.ConvertUtil;
import com.credit.xiaowei.util.StringUtil;
import com.credit.xiaowei.util.Tool;
import com.credit.xiaowei.util.ViewUtil;
import com.credit.xiaowei.widget.recycler.BaseRecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SelectPicAdapter extends BaseRecyclerAdapter<SelectPicAdapter.ViewHolder,SelectPicBean> {
    FragmentActivity activity;
    public SelectPicAdapter(FragmentActivity activity){
        this.activity=activity;
    }
    @Override
    public SelectPicAdapter.ViewHolder mOnCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.layout_upload_pic_item,parent,false);
        //recycleView左右边距13dp,每行3个item,每个item左右边距2dp,所以要减去13*2+2*2*3
        int size = (ViewUtil.getScreenWidth(mContext)- Tool.dip2px(mContext,26+12))/3;
        view.setLayoutParams(new RecyclerView.LayoutParams(size,size));
        return new ViewHolder(view);
    }

    @Override
    public void mOnBindViewHolder(final ViewHolder holder, int position) {

        switch (item.getType()){
            case SelectPicBean.Type_None:
                holder.mTvStatus.setVisibility(View.GONE);
                holder.mIvDelete.setVisibility(View.VISIBLE);
                break;
            case SelectPicBean.Type_Add:
                holder.mTvStatus.setVisibility(View.GONE);
                holder.mIvDelete.setVisibility(View.GONE);
                holder.mIvPic.setImageResource(R.mipmap.icon_add_photo);
                int px = ConvertUtil.dip2px(mContext, 15);
                holder.mIvPic.setPadding(px, px, px, px);
                break;
            case SelectPicBean.Type_TakePhoto:
                holder.mTvStatus.setVisibility(View.GONE);
                holder.mIvDelete.setVisibility(View.GONE);
                holder.mIvPic.setImageResource(R.mipmap.icon_take_photo);
                int padding = ConvertUtil.dip2px(mContext, 15);
                holder.mIvPic.setPadding(padding, padding, padding, padding);
                break;
            case SelectPicBean.Type_Uploaded:
                holder.mIvDelete.setVisibility(View.GONE);
                holder.mTvStatus.setVisibility(View.VISIBLE);
                holder.mTvStatus.setText("已上传");
                break;
            case SelectPicBean.Type_Uploading:
                holder.mIvDelete.setVisibility(View.GONE);
                holder.mTvStatus.setVisibility(View.VISIBLE);
                holder.mTvStatus.setText("上传中...");
                break;
            case SelectPicBean.Type_UploadFailed:
                holder.mIvDelete.setVisibility(View.VISIBLE);
                holder.mTvStatus.setVisibility(View.VISIBLE);
                holder.mTvStatus.setText("上传失败");
                break;
        }

        if (!StringUtil.isBlank(item.getUrl())) {
            holder.mIvPic.setPadding(1, 1, 1, 1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.mIvPic.setTransitionName(Constant.TRANSITION_ANIMATION_SHOW_PIC+position);
            }
            Glide.with(activity)
                    .load(item.getUrl())
                    .centerCrop()
                    .placeholder(R.drawable.image_default)
                    .error(R.drawable.image_default)
                    .crossFade()
                    .into(holder.mIvPic);
        }
        holder.mIvDelete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                deleteSelectPic(holder.getLayoutPosition(),data.get(holder.getLayoutPosition()).getUrl());
            }
        });
    }

    private void deleteSelectPic(final int pos, final String imgPath) {
        new AlertFragmentDialog.Builder(activity)
        .setContent("是否要删除？")
                .setLeftBtnText("取消")
                .setRightBtnText("删除")
                .setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
                    @Override
                    public void dialogRightBtnClick() {
                        ConvertUtil.deleteFile(imgPath);
                        data.remove(pos);
                        notifyDataSetChanged();
                    }
                }).build();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.iv_pic)
        ImageView mIvPic;
        @BindView(R.id.tv_status)
        TextView mTvStatus;
        @BindView(R.id.iv_delete)
        ImageView mIvDelete;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
