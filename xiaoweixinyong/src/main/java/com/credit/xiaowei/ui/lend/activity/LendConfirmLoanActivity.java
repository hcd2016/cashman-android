package com.credit.xiaowei.ui.lend.activity;

import android.Manifest;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.credit.xiaowei.R;
import com.credit.xiaowei.base.BaseActivity;
import com.credit.xiaowei.base.PermissionsListener;
import com.credit.xiaowei.config.Constant;
import com.credit.xiaowei.dialog.AlertFragmentDialog;
import com.credit.xiaowei.events.LoanEvent;
import com.credit.xiaowei.events.PayLeanResultEvent;
import com.credit.xiaowei.events.RefreshUIEvent;
import com.credit.xiaowei.events.UIBaseEvent;
import com.credit.xiaowei.ui.lend.bean.ConfirmLoanBean;
import com.credit.xiaowei.ui.main.WebViewActivity;
import com.credit.xiaowei.ui.my.activity.ForgetPayPwdActivity;
import com.credit.xiaowei.ui.my.activity.SetTradePwdActivity;
import com.credit.xiaowei.util.SpUtil;
import com.credit.xiaowei.util.TextViewUtil;
import com.credit.xiaowei.util.Tool;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.credit.xiaowei.R.id.btn_next;
/**
 * 借款确认页面
 * Created by xiejingwen at 2017/2/15 0015
 */
public class LendConfirmLoanActivity extends BaseActivity {

    ConfirmLoanBean bean;
    @BindView(R.id.tv_loan_amount)
    TextView mTvLoanAmount;
    @BindView(R.id.tv_loan_period)
    TextView mTvLoanPeriod;
    @BindView(R.id.tv_real_amount)
    TextView mTvRealAmount;
    @BindView(R.id.tv_service_amount)
    TextView mTvServiceAmount;
    @BindView(R.id.tv_bank_card)
    TextView mTvBankCard;
    @BindView(R.id.tv_bank_card_num)
    TextView mTvBankCardNum;
    @BindView(R.id.content)
    LinearLayout mContent;
    @BindView(R.id.tv_tips)
    TextView mTvTips;
    @BindView(R.id.btn_next)
    TextView mBtnNext;
    @BindView(R.id.ck_agreement)
    CheckBox mCkAgreement;
    @BindView(R.id.tv_loan_agreement)
    TextView mTvLoanAgreement;
    @BindView(R.id.tv_service_agreement)
    TextView mTvServiceAgreement;
    @BindView(R.id.tv_deduct_agreement)
    TextView mTvDeductAgreement;


    @Override
    public int getLayoutId() {
        return R.layout.activity_lend_confirm_loan;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void loadData() {
        EventBus.getDefault().register(this);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
        bean = getIntent().getParcelableExtra(BankInputPwdActivity.TAG_OPERATE_BEAN);
        mTitle.setTitle("借款");
        if (bean != null) {
            mTvLoanAmount.setText(bean.getMoney() + "(元)");
            mTvLoanPeriod.setText(bean.getPeriod() + "(天)");
            mTvRealAmount.setText(bean.getTrue_money() + "(元)");
            mTvServiceAmount.setText(bean.getCounter_fee() + "(元)");
            mTvBankCard.setText(bean.getBank_name());
            mTvBankCardNum.setText(bean.getCard_no());
            mTvTips.setText(bean.getTips());
            TextViewUtil.setPartialColor(mTvLoanAgreement,7,mTvLoanAgreement.getText().length(), ContextCompat.getColor(mContext,R.color.theme_color));
        }
        mCkAgreement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mBtnNext.setEnabled(isChecked);
            }
        });
    }

    private void toLoan() {
        EventBus.getDefault().post(new LoanEvent(LendConfirmLoanActivity.this));
        Intent intent = new Intent(LendConfirmLoanActivity.this, BankInputPwdActivity.class);
        intent.putExtra(BankInputPwdActivity.TAG_OPERATE, BankInputPwdActivity.TAG_OPERATE_LEND);
        intent.putExtra(BankInputPwdActivity.TAG_OPERATE_BEAN, getIntent().getParcelableExtra(BankInputPwdActivity.TAG_OPERATE_BEAN));
        startActivity(intent);
    }

    /***********
     * eventBus 监听
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(RefreshUIEvent event) {
        //设置交易密码成功
        if (UIBaseEvent.EVENT_SET_PAYPWD == event.getType()) {
            bean.setReal_pay_pwd_status("1");
        } else if (UIBaseEvent.EVENT_LOAN_SUCCESS == event.getType()) {
            /*Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);*/
            SpUtil.putString(Constant.SHARE_TAG_COUNT_DOWN,"");
            finish();
        } else if (UIBaseEvent.EVENT_LOAN_FAILED == event.getType()) {
            if (((PayLeanResultEvent) event).getErrMessage().equals("支付密码错误")) {
                new AlertFragmentDialog.Builder(mActivity)
                        .setContent(((PayLeanResultEvent) event).getErrMessage())
                        .setLeftBtnText("忘记密码")
                        .setLeftCallBack(new AlertFragmentDialog.LeftClickCallBack() {
                            @Override
                            public void dialogLeftBtnClick() {
                                Intent intent = new Intent(LendConfirmLoanActivity.this, ForgetPayPwdActivity.class);
                                startActivity(intent);
                            }

                        }).setRightBtnText("重新输入")
                        .setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
                            @Override
                            public void dialogRightBtnClick() {
                                Intent intent = new Intent(LendConfirmLoanActivity.this, BankInputPwdActivity.class);
                                intent.putExtra(BankInputPwdActivity.TAG_OPERATE, BankInputPwdActivity.TAG_OPERATE_LEND);
                                intent.putExtra(BankInputPwdActivity.TAG_OPERATE_BEAN, getIntent().getParcelableExtra(BankInputPwdActivity.TAG_OPERATE_BEAN));
                                startActivity(intent);
                            }
                        }).setCancel(true).build();
            } else {
                new AlertFragmentDialog.Builder(mActivity)
                        .setContent(((PayLeanResultEvent) event).getErrMessage())

                        .setRightBtnText("确定")
                        .build();
            }
        }
    }

    @OnClick({btn_next, R.id.rl_agreement,R.id.tv_service_agreement,R.id.tv_deduct_agreement})
    public void onClick(View view) {
        if (Tool.isFastDoubleClick()) return;
        switch (view.getId()) {
            case btn_next:
                if (!bean.getReal_pay_pwd_status().equals("1")) {
                    Intent intent = new Intent(LendConfirmLoanActivity.this, SetTradePwdActivity.class);
                    startActivity(intent);
                } else {
                    requestPermissions(new String[]{Manifest.permission.READ_SMS}, new PermissionsListener() {
                        @Override
                        public void onGranted() {
                            toLoan();
                        }

                        @Override
                        public void onDenied(List<String> deniedPermissions, boolean isNeverAsk) {
                            if (isNeverAsk) {
                                toAppSettings("缺少短信权限", false);
                            }
                        }
                    });
                }
                break;
            case R.id.rl_agreement:
                Intent intent = new Intent(LendConfirmLoanActivity.this, WebViewActivity.class);
                intent.putExtra("title", "借款协议");
                intent.putExtra("url", bean.getProtocol_url());
                startActivity(intent);
                break;
            case R.id.tv_service_agreement:
                //TODO 平台服务协议
                Intent platformservice_url = new Intent(LendConfirmLoanActivity.this, WebViewActivity.class);
                Log.e("这里是日子","输出当前的平台服务协议地址==="+bean.getPlatformservice_url());
                platformservice_url.putExtra("url", bean.getPlatformservice_url());
                startActivity(platformservice_url);
                break;
            case R.id.tv_deduct_agreement:
                //TODO 授权扣款协议
                Intent withholding_url = new Intent(LendConfirmLoanActivity.this, WebViewActivity.class);
                Log.e("这里是日子","输出当前的授权扣款协议地址==="+bean.getWithholding_url());
                withholding_url.putExtra("url", bean.getWithholding_url());
                startActivity(withholding_url);
                break;
        }
    }
}
