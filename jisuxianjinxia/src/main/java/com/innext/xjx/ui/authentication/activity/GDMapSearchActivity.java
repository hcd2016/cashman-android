package com.innext.xjx.ui.authentication.activity;

import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

import com.innext.xjx.R;
import com.innext.xjx.base.BaseActivity;
import com.innext.xjx.ui.authentication.fragment.GDSearchFragment;
import com.innext.xjx.util.KeyBordUtil;
import com.innext.xjx.util.StringUtil;
import com.innext.xjx.util.ToastUtil;
import com.innext.xjx.util.Tool;
import com.innext.xjx.widget.ClearEditText;

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
