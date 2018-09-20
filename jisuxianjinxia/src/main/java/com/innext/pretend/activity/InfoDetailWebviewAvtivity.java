package com.innext.pretend.activity;

import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.innext.xjx.R;
import com.innext.xjx.base.BaseActivity;
import com.innext.xjx.widget.DrawableCenterTextView;
import com.loan.loading.spinkit.SpinKitView;

import butterknife.BindView;

public class InfoDetailWebviewAvtivity extends BaseActivity {
    @BindView(R.id.tv_left)
    DrawableCenterTextView tvLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.web_view)
    WebView webView;
    @BindView(R.id.tv_tag_content)
    TextView tvTagContent;
    @BindView(R.id.load_view)
    SpinKitView loadView;
    @BindView(R.id.dialog_view)
    LinearLayout dialogView;
    @BindView(R.id.progressbar)
    ProgressBar mProgressBar;

    @Override
    public int getLayoutId() {
        return R.layout.ptd_activity_webview;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void loadData() {
        String title = getIntent().getStringExtra("title");
        String url = getIntent().getStringExtra("url");
        mTitle.setTitle(title);
        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        WebSettings settings = webView.getSettings();
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        //WebView属性设置！！！
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setDomStorageEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDatabaseEnabled(true);

        //webview在安卓5.0之前默认允许其加载混合网络协议内容
        // 在安卓5.0之后，默认不允许加载http与https混合内容，需要设置webview允许其加载混合网络协议内容
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        webView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (webView.canGoBack() && !isFinishing()) {
            webView.goBack();
        } else {
            finish();
        }
    }
}
