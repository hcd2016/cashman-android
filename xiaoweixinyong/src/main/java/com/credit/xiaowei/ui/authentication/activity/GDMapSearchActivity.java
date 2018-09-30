package com.credit.xiaowei.ui.authentication.activity;

import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

import com.credit.xiaowei.R;
import com.credit.xiaowei.base.BaseActivity;
import com.credit.xiaowei.ui.authentication.fragment.GDSearchFragment;
import com.credit.xiaowei.util.KeyBordUtil;
import com.credit.xiaowei.util.StringUtil;
import com.credit.xiaowei.util.ToastUtil;
import com.credit.xiaowei.util.Tool;
import com.credit.xiaowei.widget.ClearEditText;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 地图搜索列表
 */
public class GDMapSearchActivity extends BaseActivity {
    public static final int SEARCH_REQUEST_CODE = 1002;
    @BindView(R.id.et_search)
    ClearEditText mEtSearch;
    @BindView(R.id.container)
    FrameLayout mContainer;
    private GDSearchFragment gdSearchFragment;
    private String cityCode;
    @Override
    public int getLayoutId() {
        return R.layout.activity_gdmap_search;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void loadData() {
        cityCode = getIntent().getStringExtra("cityCode");
        gdSearchFragment = GDSearchFragment.getInstance(cityCode);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(mContainer.getId(), gdSearchFragment);
        transaction.commitAllowingStateLoss();
    }


    @OnClick({R.id.iv_back, R.id.tv_search_btn})
    public void onClick(View view) {
        if (Tool.isFastDoubleClick())
            return;
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_search_btn:
                if (StringUtil.isBlankEdit(mEtSearch)) {
                    ToastUtil.showToast("请输入要搜索的位置");
                } else {
                    gdSearchFragment.loadSearchData(0, mEtSearch.getText().toString());
                    KeyBordUtil.hideSoftKeyboard(mEtSearch);
                }
                break;
        }
    }
}
