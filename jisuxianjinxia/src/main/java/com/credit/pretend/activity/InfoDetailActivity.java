package com.credit.pretend.activity;

import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.credit.pretend.bean.InfoDetailBean;
import com.credit.pretend.ptd_util.MImageGetter;
import com.credit.pretend.ptd_util.RetrofitUtil;
import com.credit.xiaowei.R;
import com.credit.xiaowei.base.BaseActivity;
import com.credit.xiaowei.widget.DrawableCenterTextView;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InfoDetailActivity extends BaseActivity {

    @BindView(R.id.tv_left)
    DrawableCenterTextView tvLeft;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title_desc)
    TextView tvTitleDesc;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @Override
    public int getLayoutId() {
        return R.layout.ptd_activity_info_detial;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void loadData() {
        mTitle.setTitle("");
        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String id = getIntent().getStringExtra("id");
        RetrofitUtil.create().getInfoDetail(id).enqueue(new Callback<InfoDetailBean>() {
            @Override
            public void onResponse(Call<InfoDetailBean> call, Response<InfoDetailBean> response) {
                InfoDetailBean infoDetailBean = response.body();
                if (null != infoDetailBean) {
                    tvContent.setText(Html.fromHtml(infoDetailBean.content, new MImageGetter(tvContent, InfoDetailActivity.this), null));
                    tvTitleDesc.setText(infoDetailBean.title);
                    tvDate.setText(infoDetailBean.created_date);
                }
            }

            @Override
            public void onFailure(Call<InfoDetailBean> call, Throwable t) {
                t.toString();
            }
        });
    }
}
