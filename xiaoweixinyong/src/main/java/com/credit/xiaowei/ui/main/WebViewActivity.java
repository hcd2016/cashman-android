package com.credit.xiaowei.ui.main;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.credit.xiaowei.R;
import com.credit.xiaowei.app.App;
import com.credit.xiaowei.base.BaseActivity;
import com.credit.xiaowei.base.PermissionsListener;
import com.credit.xiaowei.config.Constant;
import com.credit.xiaowei.dialog.AlertFragmentDialog;
import com.credit.xiaowei.events.AuthenticationRefreshEvent;
import com.credit.xiaowei.events.ChangeTabMainEvent;
import com.credit.xiaowei.events.FragmentRefreshEvent;
import com.credit.xiaowei.events.UIBaseEvent;
import com.credit.xiaowei.http.HttpManager;
import com.credit.xiaowei.ui.authentication.activity.PerfectInformationActivity;
import com.credit.xiaowei.ui.authentication.contract.SaveAlipayInfoContract;
import com.credit.xiaowei.ui.authentication.presenter.SaveAlipayInfoPresenter;
import com.credit.xiaowei.ui.login.activity.ForgetPwdActivity;
import com.credit.xiaowei.ui.my.activity.ForgetPayPwdActivity;
import com.credit.xiaowei.ui.my.activity.MyLotteryCodeActivity;
import com.credit.xiaowei.ui.my.bean.CopyTextBean;
import com.credit.xiaowei.ui.my.bean.MoreContentBean;
import com.credit.xiaowei.ui.my.bean.WebShareBean;
import com.credit.xiaowei.ui.my.contract.MyContract;
import com.credit.xiaowei.ui.my.presenter.MyPresenter;
import com.credit.xiaowei.util.ConvertUtil;
import com.credit.xiaowei.util.LogUtils;
import com.credit.xiaowei.util.SpUtil;
import com.credit.xiaowei.util.StringUtil;
import com.credit.xiaowei.util.ToastUtil;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * 网页加载容器
 * xiejingwen
 */
public class WebViewActivity extends BaseActivity<MyPresenter> implements MyContract.View, SaveAlipayInfoContract.View {

    @BindView(R.id.web_view)
    WebView mWebView;
    @BindView(R.id.tv_tag_content)
    TextView mTvTagContent;
    @BindView(R.id.dialog_view)
    LinearLayout mDialogView;
    @BindView(R.id.progressbar)
    ProgressBar mProgressBar;

    private String title;
    private String mUrl;
    private HashMap<String, String> mHashMap;
    private final String AUTHTAG = "认证进度：";
    private boolean isZhbTitle;
    private boolean isFinish;
    private MoreContentBean mMoreContentBean;
    private SaveAlipayInfoPresenter alipayInfoPresenter;
    /**
     * 发送与接收的广播
     **/
    String SENT_SMS_ACTION = "SENT_SMS_ACTION";
    String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";

    @Override
    public int getLayoutId() {
        return R.layout.activity_loan_webview;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        mTitle.setTitle("");
        initView();
        mUrl = HttpManager.getUrl(mUrl);
        mWebView.loadUrl(mUrl);
    }

    public void initView() {
        mHashMap = new HashMap<>();
        if (getIntent() != null) {
            if (!StringUtil.isBlank(getIntent().getStringExtra("title"))) {
                title = getIntent().getStringExtra("title");
                mTitle.setTitle(title);
            }
            if (!StringUtil.isBlank(getIntent().getStringExtra("improveUrl"))) {//该链接是为了提额的改动
                mUrl = getIntent().getStringExtra("improveUrl");
            } else {
                mUrl = getIntent().getStringExtra("url");
            }

        }
        mWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        //WebView属性设置！！！
        WebSettings settings = mWebView.getSettings();
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
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mWebView.addJavascriptInterface(new JavaMethod(), "nativeMethod");
        mWebView.setDownloadListener(new MyWebViewDownLoadListener());
        mWebView.setWebViewClient(new MyWebViewClient());
        mWebView.setWebChromeClient(new MyWebChromeClient());
    }

    private void showDialog() {
        mWebView.setVisibility(View.GONE);
        isZhbTitle = true;
        mDialogView.setVisibility(View.VISIBLE);
        mTvTagContent.setText("正在认证中...");
    }

    private void dismissDialog(String message) {
        isZhbTitle = false;
        mDialogView.setVisibility(View.GONE);
        ToastUtil.showToast(message);
        finish();
    }


    public void toShare() {
        if (mMoreContentBean == null) {
            mPresenter.getInfo();
        } else {
            showShare();
        }
    }

    private void showShare() {
        if (mMoreContentBean != null) {
            UMWeb web = new UMWeb(mMoreContentBean.getShare_url());
            web.setTitle(mMoreContentBean.getShare_title());//标题
            web.setThumb(new UMImage(mActivity, mMoreContentBean.getShare_logo()));  //缩略图
            web.setDescription(mMoreContentBean.getShare_body());//描述

            new ShareAction(mActivity).setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                    .withMedia(web)
                    .setCallback(umShareListener).open();
//            new ShareAction(WebViewActivity.this).setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
//                    .withTitle(mMoreContentBean.getShare_title())
//                    .withText(mMoreContentBean.getShare_body())
//                    .withTargetUrl(mMoreContentBean.getShare_url())
//                    .withMedia(new UMImage(WebViewActivity.this, mMoreContentBean.getShare_logo()))
//                    .setCallback(umShareListener).open();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (alipayInfoPresenter != null) {
            alipayInfoPresenter.onDestroy();
        }
        if (mUrl.contains("repayment/detail")) {
            //通知还款或续期成功
            EventBus.getDefault().post(new FragmentRefreshEvent(UIBaseEvent.EVENT_REPAY_SUCCESS));
        }
        if (mWebView != null) {
            mWebView.clearHistory();
            mWebView.clearCache(true);
            mWebView.destroy();
        }
    }

    @Override
    public void userInfoSuccess(MoreContentBean result) {
        mMoreContentBean = result;
        showShare();
    }

    @Override
    public void showLoading(String content) {
        //认证支付宝因为已经有了dialog，所以不弹出
        if (mWebView.getVisibility() == View.VISIBLE) {
            App.loadingContent(this, content);
        }
    }

    @Override
    public void stopLoading() {
        App.hideLoading();
    }

    @Override
    public void showErrorMsg(String msg, String type) {
        if (TextUtils.isEmpty(type)) {
            ToastUtil.showToast(msg);
            return;
        }
        if (type.equals(alipayInfoPresenter.TYPE_ALIPAY_INFO)) {
            dismissDialog(msg);
        }
    }

    @Override
    public void saveInfoSuccess(String message) {
        //支付宝验证成功，通知完善资料页面刷新
        EventBus.getDefault().post(new AuthenticationRefreshEvent());
        dismissDialog(message);
    }

    public class JavaMethod {
        /*************** START  支付宝和淘宝等第三方数据抓取和认证操作***************/
        /**
         * 隐藏页面 显示认证进度 布局
         *
         * @param type 传入 0
         */
        @JavascriptInterface
        public void goneLayout(int type) {
            switch (type) {
                //隐藏webview
                case 0:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mWebView.setVisibility(View.GONE);
                            isZhbTitle = true;
                            mDialogView.setVisibility(View.VISIBLE);
                            mTvTagContent.setText(AUTHTAG + "0%");
                        }
                    });
                    break;
                default:
                    break;
            }
        }

        /**
         * 保存传入的键值
         *
         * @param key
         * @param text
         */
        @JavascriptInterface
        public void saveText(String key, String text) {
            mHashMap.put(key, text);
        }

        /**
         * 根据键获取值
         *
         * @param key 传入键
         * @return
         */
        @JavascriptInterface
        public String getText(String key) {
            //调用这个方法返回数据
            Log.e("getText", key);
            String result = mHashMap.get(key);
            return result;
        }

        /**
         * 设置当前的认证进度
         *
         * @param progress 当前进度  0-100
         */
        @JavascriptInterface
        public void setProgress(final int progress) {
            Log.e("setProgress", progress + "");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isZhbTitle = true;
                    mTvTagContent.setText(AUTHTAG + progress + "%");
                }
            });

        }

        /**
         * 上传传入的数据 到info-capture/info-upload这个接口 入参： String data
         */
        @JavascriptInterface
        public void submitText(String text) {
            alipayInfoPresenter = new SaveAlipayInfoPresenter();
            alipayInfoPresenter.init(WebViewActivity.this);
            JSONObject mapValue = new JSONObject(mHashMap);
            alipayInfoPresenter.toSaveInfo(mapValue.toString());
        }

        /***************END 支付宝和淘宝等第三方数据抓取和认证操作***************/


        /**
         * 跳转到app的方法
         *
         * @param type json字符串
         *             {
         *             type:"0";
         *             }
         *             0：关闭当前页面
         *             1：跳转到忘记登录密码
         *             2：跳转到忘记支付密码
         *             3：跳转到认证中心
         *             4：跳转到首页
         *             5：跳转到qq，并打开指定号码的客服聊天界面
         */
        @JavascriptInterface
        public void returnNativeMethod(String type) {
            Log.e("TAG", "type:" + type);
            if ("0".equals(type)) {
                finish();
            } else if ("1".equals(type)) {
                Intent intent = new Intent(mContext, ForgetPwdActivity.class);
                intent.putExtra(Constant.SHARE_TAG_USERNAME, SpUtil.getString(Constant.SHARE_TAG_USERNAME));
                startActivity(intent);
            } else if ("2".equals(type)) {
                Intent intent = new Intent(mContext, ForgetPayPwdActivity.class);
                startActivity(intent);
            } else if ("3".equals(type)) {
                Intent intent = new Intent(mContext, PerfectInformationActivity.class);
                startActivity(intent);
            } else if ("4".equals(type)) {
                Intent intent = new Intent(WebViewActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                EventBus.getDefault().post(new ChangeTabMainEvent(FragmentFactory.FragmentStatus.Lend));
            } else if ("5".equals(type)) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTitle.setRightTitle("咨询客服", new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // 这里只能为联系客服特殊处理，转到联系我们的页面
                                String qq_url = "mqqwpa://im/chat?chat_type=crm&uin=2152872885&version=1&src_type=web&web_src=file:://";//938009600
                                try {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(qq_url)));
                                } catch (Exception e) {
                                    ToastUtil.showToast("请确认安装了QQ客户端");
                                }
                            }
                        });
                    }
                });

            }
        }

        /**
         * 调用改方法去发送短信
         *
         * @param phoneNumber 手机号码
         * @param message     短信内容
         **/
        @JavascriptInterface
        public void sendMessage(String phoneNumber, String message) {
            // 注册广播 发送消息
            Log.e("这里是日志", "输出日志===phoneNumber" + phoneNumber + "---message=" + message);
            //发送短信并且到发送短信页面
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
            intent.putExtra("sms_body", message);
            startActivity(intent);
            //直接发送短信不跳转发送短信页面
//            registerReceiver(sendMessage, new IntentFilter(SENT_SMS_ACTION));
//            registerReceiver(receiver, new IntentFilter(DELIVERED_SMS_ACTION));
//            SmsManager sms = SmsManager.getDefault();
//            // create the sentIntent parameter
//            Intent sentIntent = new Intent(SENT_SMS_ACTION);
//            PendingIntent sentPI = PendingIntent.getBroadcast(mContext, 0, sentIntent,
//                    0);
//            // create the deilverIntent parameter
//            Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION);
//            PendingIntent deliverPI = PendingIntent.getBroadcast(mContext, 0,
//                    deliverIntent, 0);
//            //如果短信内容超过70个字符 将这条短信拆成多条短信发送出去
//            if (message.length() > 70) {
//                ArrayList<String> msgs = sms.divideMessage(message);
//                for (String msg : msgs) {
//                    sms.sendTextMessage(phoneNumber, null, msg, sentPI, deliverPI);
//                }
//            } else {
//                sms.sendTextMessage(phoneNumber, null, message, sentPI, deliverPI);
//            }
        }

        /**
         * 发送短信Receiver
         */
        private BroadcastReceiver sendMessage = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //判断短信是否发送成功
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
//                        Toast.makeText(context, "短信发送成功", Toast.LENGTH_SHORT).show();
                        Log.e("发送短信", "短信发送成功");
                        break;
                    default:
//                        Toast.makeText(mContext, "发送失败", Toast.LENGTH_LONG).show();
                        Log.e("发送短信", "短信发送失败");
                        break;
                }
            }
        };
        //接收短信
        private BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //表示对方成功收到短信
//                Toast.makeText(mContext, "对方接收成功",Toast.LENGTH_LONG).show();
                Log.e("发送短信", "对方接收成功");
            }
        };

        /**
         * 调用该方法可以复制文字到手机的粘贴板
         *
         * @param text 需要复制的文字
         *             PS:暂未使用到
         */
        @JavascriptInterface
        public void copyTextMethod(String text) {
            CopyTextBean bean = ConvertUtil.toObject(text, CopyTextBean.class);

            ClipboardManager c = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            c.setPrimaryClip(ClipData.newPlainText("text", bean.getText()));

            ToastUtil.showToast(bean.getTip());
        }

        /**
         * 跳转到拨号页面，拨打传入的手机号码
         *
         * @param tele PS:暂未使用到
         */
        @JavascriptInterface
        public void callPhoneMethod(final String tele) {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, new PermissionsListener() {
                @Override
                public void onGranted() {
                    String mTele = tele.replaceAll("-", "").trim();
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mTele));
                    startActivity(intent);
                }

                @Override
                public void onDenied(List<String> deniedPermissions, boolean isNeverAsk) {

                }
            });

        }

        /**
         * 调用app的分享
         *
         * @param shareBean 分享的json字符串
         *                  int type;   //类型  如需要点击标题右上角“分享"按钮再分享 ，则传入1。  否则传入其他  会直接弹出分享弹窗
         *                  String share_title; //分享标题
         *                  String share_body;  //分享的内容
         *                  String share_url;   //分享后点击打开的地址
         *                  String share_logo;  //分享的图标
         */
        @JavascriptInterface
        public void shareMethod(String shareBean) {
            final WebShareBean bean = ConvertUtil.toObject(shareBean, WebShareBean.class);
            if (bean != null) {
                if (bean.getType() == 1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTitle.setRightTitle("分享", new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    UMWeb web = new UMWeb(bean.getShare_url());
                                    web.setTitle(bean.getShare_title());//标题
                                    web.setThumb(new UMImage(mActivity, bean.getShare_logo()));  //缩略图
                                    web.setDescription(bean.getShare_body());//描述

                                    new ShareAction(mActivity).setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                                            .withMedia(web)
                                            .setCallback(umShareListener).open();
//                                    new ShareAction(mActivity).setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
//                                            .withTitle(bean.getShare_title())
//                                            .withText(bean.getShare_body())
//                                            .withTargetUrl(bean.getShare_url())
//                                            .withMedia(new UMImage(WebViewActivity.this, bean.getShare_logo()))
//                                            .setCallback(umShareListener).open();
                                }
                            });
                        }
                    });
                } else {
                    UMWeb web = new UMWeb(bean.getShare_url());
                    web.setTitle(bean.getShare_title());//标题
                    web.setThumb(new UMImage(mActivity, bean.getShare_logo()));  //缩略图
                    web.setDescription(bean.getShare_body());//描述

                    new ShareAction(mActivity).setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                            .withMedia(web)
                            .setCallback(umShareListener).open();
//                    new ShareAction(mActivity).setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
//                            .withTitle(bean.getShare_title())
//                            .withText(bean.getShare_body())
//                            .withTargetUrl(bean.getShare_url())
//                            .withMedia(new UMImage(mActivity, bean.getShare_logo()))
//                            .setCallback(umShareListener).open();
                }
            }
        }

        /**
         * 跳转到我的抽奖码
         */
        @JavascriptInterface
        public void toLotteryList() {
            if (App.getConfig().getLoginStatus()) {
                Intent intent = new Intent(mContext, MyLotteryCodeActivity.class);
                startActivity(intent);
            } else {
                App.toLogin(mContext);
                ToastUtil.showToast("请先登录");
            }
        }

        @JavascriptInterface
        public void authenticationResult(String message) {
            //手机运营商、收款银行卡、芝麻授信验证成功刷新页面
            EventBus.getDefault().post(new AuthenticationRefreshEvent());
            //通知银行卡绑定或修改成功
            EventBus.getDefault().post(new FragmentRefreshEvent(UIBaseEvent.EVENT_BANK_CARD_SUCCESS));
            ToastUtil.showToast(message);
            finish();
        }

    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            ToastUtil.showToast("分享成功");
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            ToastUtil.showToast("分享失败");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (isZhbTitle) {
            new AlertFragmentDialog.Builder(this)
                    .setContent("返回操作将中断支付宝认证，\n确认要退出吗？")
                    .setLeftBtnText("取消认证")
                    .setRightBtnText("继续认证")
                    .setLeftCallBack(new AlertFragmentDialog.LeftClickCallBack() {
                        @Override
                        public void dialogLeftBtnClick() {
                            finish();
                        }
                    }).build();
        } else {
            if (mWebView.canGoBack() && !isFinish) {
                mWebView.goBack();
            } else {
                finish();
            }
        }
    }


    private class MyWebViewDownLoadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                    long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    class MyWebViewClient extends WebViewClient {

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            //handler.cancel(); // Android默认的处理方式
            handler.proceed();  // 接受所有网站的证书  授权证书()
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(final WebView view, String url) {
            super.onPageFinished(view, url);
            mUrl = url;
            LogUtils.loge(mUrl);
            mProgressBar.setVisibility(View.GONE);
            if (view.canGoBack()) { //如果当前不是初始页面则显示关闭按钮
                mTitle.showClose(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isZhbTitle) {
                            new AlertFragmentDialog.Builder(mActivity)
                                    .setContent("返回操作将中断支付宝认证，\n确认要退出吗？")
                                    .setLeftBtnText("取消认证")
                                    .setRightBtnText("继续认证")
                                    .setLeftCallBack(new AlertFragmentDialog.LeftClickCallBack() {
                                        @Override
                                        public void dialogLeftBtnClick() {
                                            finish();
                                        }
                                    }).build();
                        } else {
                            finish();
                        }
                    }
                });
            } else {
                mTitle.hintClose();
            }
            if (url.contains("repayment/detail")) {
                isFinish = true;
            } else {
                isFinish = false;
            }
            //往当前页面插入一段JS
            if (url.contains("https://my.alipay.com/portal/i.htm")) {
                showDialog();
                String js = "var newscript = document.createElement(\"script\");";
                js += "newscript.src=\"" + App.getConfig().GET_ALIPAY_JS + "\";";
                js += "newscript.onload=function(){toDo();};";  //xxx()代表js中某方法
                js += "document.body.appendChild(newscript);";
                view.loadUrl("javascript:" + js);
            }
        }
    }

    class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            mProgressBar.setProgress(newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (!isZhbTitle) {
                if (StringUtil.isBlank(getIntent().getStringExtra("title"))) {
                    WebViewActivity.this.title = title;
                    mTitle.setTitle(true, new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (isZhbTitle) {
                                new AlertFragmentDialog.Builder(mActivity)
                                        .setContent("返回操作将中断支付宝认证，\n确认要退出吗？")
                                        .setLeftBtnText("取消认证")
                                        .setRightBtnText("继续认证")
                                        .setLeftCallBack(new AlertFragmentDialog.LeftClickCallBack() {
                                            @Override
                                            public void dialogLeftBtnClick() {
                                                finish();
                                            }
                                        }).build();
                            } else {
                                if (mWebView.canGoBack() && !isFinish) {
                                    mWebView.goBack();
                                } else {
                                    finish();
                                }
                            }
                        }
                    }, title);
                    if (title.equals("拒就送现金")) {
                        mTitle.setRightTitle("分享", new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                toShare();
                            }
                        });
                    } else {
                        mTitle.setRightTitle("", null);
                    }
                }
            } else {
                mTitle.setTitle("认证中");
            }

        }
    }
}
