package com.credit.xiaowei.ui.main;

import android.content.Intent;
import android.net.Uri;
import android.widget.EditText;

import com.credit.xiaowei.R;
import com.credit.xiaowei.app.App;
import com.credit.xiaowei.base.BaseActivity;
import com.credit.xiaowei.util.SpUtil;
import com.credit.xiaowei.util.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class UrlSelectorActivity extends BaseActivity {
    @BindView(R.id.edt_url)
    EditText mEdtUrl;
    private String urlKey = "Url_Selector";
    @Override
    public int getLayoutId() {
        return R.layout.activity_url_select;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void loadData() {
        mTitle.setTitle(false, "服务器地址");
        String url = SpUtil.getString(urlKey);
        mEdtUrl.setHint("默认服务器地址:"+ App.getConfig().getBaseUrl());
        if (!url.isEmpty()) {
            mEdtUrl.setText(url);
            mEdtUrl.setSelection(url.length());
        }
    }


    @OnClick(R.id.btn_next)
    public void onClick() {
        if (!mEdtUrl.getText().toString().isEmpty()) {
            try {
                Uri.parse(mEdtUrl.getText().toString()).getHost().hashCode();
                App.getConfig().setBaseUrl(mEdtUrl.getText().toString());
                SpUtil.putString(urlKey, mEdtUrl.getText().toString());
            } catch (Exception e) {
                ToastUtil.showToast("服务器地址有误,请重新输入");
                return;
            }
        } else {
            SpUtil.putString(urlKey, "");
        }
        startActivity(new Intent(UrlSelectorActivity.this, SplashActivity.class));
        finish();
    }
}
