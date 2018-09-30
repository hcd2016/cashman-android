package com.credit.xiaowei.ui.my.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.credit.xiaowei.R;
import com.credit.xiaowei.app.App;
import com.credit.xiaowei.base.BaseActivity;
import com.credit.xiaowei.base.PermissionsListener;
import com.credit.xiaowei.dialog.AlertFragmentDialog;
import com.credit.xiaowei.ui.my.contract.FeedBackContract;
import com.credit.xiaowei.ui.my.presenter.FeedBackPresenter;
import com.credit.xiaowei.util.ToastUtil;
import com.credit.xiaowei.util.Tool;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

import static com.credit.xiaowei.R.id.input_feedback;

/**
 * Created by gyl on 2017/2/15 0015.
 */

public class FeedBackActivity extends BaseActivity<FeedBackPresenter> implements FeedBackContract.View {
    @BindView(input_feedback)
    EditText mInputFeedback;
    @BindView(R.id.tv_submit)
    TextView mTvSubmit;
    @BindView(R.id.tv_online_service)
    TextView mTvOnlineService;
    @BindView(R.id.tv_phone_service)
    TextView mTvPhoneService;
    @BindView(R.id.number)
    TextView mNumber;
    @BindView(R.id.tv_worktime)
    TextView mWorktime;
    @BindView(R.id.tv_holiday)
    TextView mHoliday;
    @BindView(R.id.tv_qq_group)
    TextView mQQgroup;
    /**
     * 反馈意见是否为空
     */
    private boolean sign1 = false;
    private String[] qqList;//客服qq
    private Random random;
    private String holiday, peacetime, qq_group, service_phone;
    private ArrayList<String> qqGroupList;

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting_feed_back;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        mTitle.setTitle(true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }, "意见反馈");
        mInputFeedback.addTextChangedListener(inputWatcher);
        holiday = getIntent().getStringExtra("holiday");
        peacetime = getIntent().getStringExtra("peacetime");
        qq_group = getIntent().getStringExtra("qq_group");
        service_phone = getIntent().getStringExtra("service_phone");
        qqGroupList = getIntent().getStringArrayListExtra("services_qq");
        init();
    }

    public void init() {
        if (!Tool.isBlank(peacetime) && !Tool.isBlank(holiday) && !Tool.isBlank(qq_group)) {
            mWorktime.setText("平时: " + peacetime);
            mHoliday.setText("节假: " + holiday);
            mQQgroup.setText("客服QQ: " + qq_group);
        }
    }

    private TextWatcher inputWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (mInputFeedback.getText().length() > 160) {
                mNumber.setText("0/160");
            } else {
                mNumber.setText((160 - mInputFeedback.getText().length()) + "/160");
            }

            sign1 = mInputFeedback.getText().length() > 0;

            if (sign1) {
                mTvSubmit.setEnabled(true);
            } else {
                mTvSubmit.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /**
     * 判断是否有特殊字符
     *
     * @param content
     * @return
     */
    public boolean hasEmoji(String content) {

        Pattern pattern = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]");
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            return true;
        }
        return false;
    }

    @OnClick({R.id.tv_submit, R.id.tv_online_service, R.id.tv_phone_service})
    public void onClick(View view) {
        if (Tool.isFastDoubleClick())
            return;
        switch (view.getId()) {
            case R.id.tv_submit:
                String inPutFeedbackStr = mInputFeedback.getText().toString().trim();
                if (TextUtils.isEmpty(inPutFeedbackStr)) {
                    ToastUtil.showToast("请输入您的反馈内容");
                    return;
                }
                if (hasEmoji(inPutFeedbackStr) == true) {
                    ToastUtil.showToast("请勿输入特殊字符及表情");
                    return;
                }
                App.loadingDefault(this);
                mPresenter.feedBack(inPutFeedbackStr);
                break;
            case R.id.tv_online_service:
                // 跳转到客服
                String qq_url = "mqqwpa://im/chat?chat_type=wpa&uin=" + getQQ();//
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(qq_url)));
                } catch (Exception e) {
                    ToastUtil.showToast("请确认安装了QQ客户端");
                }
                break;
            case R.id.tv_phone_service://"021-56350010"
                phoneService();
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }

    private void phoneService() {
        if (Tool.isBlank(service_phone)) {
            service_phone = "0571-85099655";
        }
        new AlertFragmentDialog.Builder(this).setContent(service_phone)
                .setLeftBtnText("取消").setLeftCallBack(new AlertFragmentDialog.LeftClickCallBack() {
            @Override
            public void dialogLeftBtnClick() {

            }
        }).setRightBtnText("呼叫").setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
            @Override
            public void dialogRightBtnClick() {
                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, new PermissionsListener() {
                    @Override
                    public void onGranted() {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + service_phone.replace("-", "")));
                        startActivity(intent);
                    }

                    @Override
                    public void onDenied(List<String> deniedPermissions, boolean isNeverAsk) {

                    }
                });

            }
        }).build();
    }

    @Override
    public void feedBackSuccess() {
        ToastUtil.showToast("反馈成功");
        mInputFeedback.setText("");
        finish();
    }

    @Override
    public void showLoading(String content) {
        App.loadingContent(this, content);
    }

    @Override
    public void stopLoading() {
        App.hideLoading();
    }

    @Override
    public void showErrorMsg(String msg, String type) {
        ToastUtil.showToast(msg);
    }

    public String getQQ() {
        if (null == random) {
            random = new Random();
        }
        if (qqGroupList != null) {
            return qqGroupList.get(random.nextInt(qqGroupList.size()));
        } else {
            if (null == qqList) {
                qqList = new String[]{"592472157"};
            }
            return qqList[random.nextInt(qqList.length)];
        }

    }
}
