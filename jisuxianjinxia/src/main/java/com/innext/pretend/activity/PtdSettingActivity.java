package com.innext.pretend.activity;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innext.pretend.ptd_util.DataCleanManager;
import com.innext.xjx.R;
import com.innext.xjx.app.App;
import com.innext.xjx.base.BaseActivity;
import com.innext.xjx.widget.DrawableCenterTextView;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设置
 */
public class PtdSettingActivity extends BaseActivity {
    @BindView(R.id.tv_left)
    DrawableCenterTextView tvLeft;
    @BindView(R.id.tv_close)
    DrawableCenterTextView tvClose;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    DrawableCenterTextView tvRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_cache)
    TextView tvCache;
    @BindView(R.id.ll_clear_cache_container)
    LinearLayout llClearCacheContainer;
    @BindView(R.id.ll_abount_us_container)
    LinearLayout llAbountUsContainer;

    @Override
    public int getLayoutId() {
        return R.layout.ptd_activity_setting;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void loadData() {
        mTitle.setTitle(true, "设置");
        try {
            String totalCacheSize = DataCleanManager.getTotalCacheSize(this);
            tvCache.setText(totalCacheSize);
        } catch (Exception e) {
            tvCache.setText("0KB");
            e.printStackTrace();
        }
    }

    @OnClick({R.id.ll_clear_cache_container, R.id.ll_abount_us_container})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_clear_cache_container://清空缓存
                DataCleanManager.cleanApplicationData(this,new File(App.getContext().getCacheDir(), "jsxjxCache").getAbsolutePath(),new File(App.getContext().getCacheDir(), "image_manager_disk_cache").getAbsolutePath());
                tvCache.setText("0KB");
                break;
            case R.id.ll_abount_us_container://关于我们
                startActivity(PtdAboutUsActivity.class);
                break;
        }
    }
}
