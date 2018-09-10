package com.innext.xjx.ui.lend.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innext.xjx.R;
import com.innext.xjx.app.App;
import com.innext.xjx.base.BaseActivity;
import com.innext.xjx.base.PermissionsListener;
import com.innext.xjx.config.Constant;
import com.innext.xjx.dialog.AlertFragmentDialog;
import com.innext.xjx.events.PayLeanResultEvent;
import com.innext.xjx.events.RefreshUIEvent;
import com.innext.xjx.events.UIBaseEvent;
import com.innext.xjx.ui.lend.bean.ConfirmLoanBean;
import com.innext.xjx.ui.lend.contract.ApplyLoanContract;
import com.innext.xjx.ui.lend.presenter.ApplyLoanPresenter;
import com.innext.xjx.util.CalendarUtil;
import com.innext.xjx.util.SpUtil;
import com.innext.xjx.util.StatusBarUtil;
import com.innext.xjx.util.ToastUtil;
import com.innext.xjx.util.ViewUtil;
import com.innext.xjx.widget.keyboard.KeyboardNumberUtil;
import com.innext.xjx.widget.keyboard.PwdInputController;

import org.greenrobot.eventbus.EventBus;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.util.List;

import butterknife.BindView;

import static com.innext.xjx.R.id.iv_close;
import static com.innext.xjx.R.id.tv_money;

/**
 * 借款密码输入
 * Created by xiejingwen at 2017/2/15 0015
 */
public class BankInputPwdActivity extends BaseActivity<ApplyLoanPresenter> implements ApplyLoanContract.View {

    //操作的key值
    public static final String TAG_OPERATE = "OPERATE";
    public static final String TAG_OPERATE_BEAN = "BEAN";
    public static final String TAG_OPERATE_LEND = "LEND";
    @BindView(iv_close)
    ImageView mIvClose;
    @BindView(R.id.tv_tip)
    TextView mTvTip;
    @BindView(R.id.view_line)
    View mViewLine;
    @BindView(R.id.tv_pay_type)
    TextView mTvPayType;
    @BindView(R.id.iv_bank_logo)
    ImageView mIvBankLogo;
    @BindView(R.id.tv_bank_info)
    TextView mTvBankInfo;
    @BindView(R.id.iv_choose_bank)
    ImageView mIvChooseBank;
    @BindView(R.id.layout_choose_bank)
    LinearLayout mLayoutChooseBank;
    @BindView(R.id.input_controller)
    PwdInputController mInputController;
    @BindView(tv_money)
    TextView mTvMoney;
    @BindView(R.id.ivGlkHide)
    ImageView mIvGlkHide;
    @BindView(R.id.llCustomerKb)
    LinearLayout mLlCustomerKb;
    @BindView(R.id.view_line1)
    View mViewLine1;
    private String type = "";
    //借款
    ConfirmLoanBean lendBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_bank_input_pwd;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        initView();
        initLisenter();
    }

    public void initView() {
        StatusBarUtil.setStatusBarColor(this,R.color.transparent_33);
        ViewUtil.expandViewTouchDelegate(mIvClose, 50, 50, 50, 50);
        mInputController.initKeyBoard(mLlCustomerKb, KeyboardNumberUtil.CUSTOMER_KEYBOARD_TYPE.NUMBER);
        mLayoutChooseBank.setVisibility(View.GONE);
        mTvPayType.setText("借款金额");
        mViewLine1.setVisibility(View.INVISIBLE);
        lendBean = getIntent().getParcelableExtra(TAG_OPERATE_BEAN);
        mTvMoney.setText(replaceXmlMoney(lendBean.getMoney()));
        type = getIntent().getStringExtra(TAG_OPERATE);
        mInputController.showKeyBoard();
    }

    /******
     * 处理xml标签
     *
     * @param xmlMoney
     * @return
     */
    private String replaceXmlMoney(String xmlMoney) {
        if (!xmlMoney.toLowerCase().contains("<font"))
            return xmlMoney;

        String money = "";

        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(xmlMoney));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if ("font".equals(parser.getName().toLowerCase())) {

                            money = parser.nextText();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return money;
    }

    public void initLisenter() {
        mIvClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mIvChooseBank.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }

        });
        mInputController.setOnPwdInputEvent(new PwdInputController.OnPwdInputEvent() {

            @Override
            public void inputComplete(String pwd) {
                if (type.equals(TAG_OPERATE_LEND)) {
                    mPresenter.applyLoan(lendBean.getMoney(), String.valueOf(lendBean.getPeriod()), pwd);
                }
            }
        });
    }

    @Override
    public void applyLoanSuccess() {
        showCalenderDialog();
    }

    private void showCalenderDialog() {
         AlertFragmentDialog.Builder dialog = new AlertFragmentDialog.Builder(mActivity);
        boolean permissions = ContextCompat.checkSelfPermission(mContext,Manifest.permission.WRITE_CALENDAR)== PackageManager.PERMISSION_DENIED;
        //第一次弹出请求框，请求用户授权
        if (TextUtils.isEmpty(SpUtil.getString(Constant.SHARE_CALENDAR_PERMISSIONS))&&permissions) {
            dialog.setLeftBtnText("不允许")
                        .setLeftCallBack(new AlertFragmentDialog.LeftClickCallBack() {
                            @Override
                            public void dialogLeftBtnClick() {
                                SpUtil.putString(Constant.SHARE_CALENDAR_PERMISSIONS, "no");
                                EventBus.getDefault().post(new RefreshUIEvent(UIBaseEvent.EVENT_LOAN_SUCCESS));
                                finish();
                            }
                        })
                        .setRightBtnText("允许")
                        .setContent("申请成功,开启日历还款提醒，不再忘记还钱，更快提升额度。")
                        .setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
                            @Override
                            public void dialogRightBtnClick() {
                                requestPermissions(new String[]{Manifest.permission.WRITE_CALENDAR}, new PermissionsListener() {
                                    @Override
                                    public void onGranted() {
                                        //vivo权限机制与IOS相似，需要调用日历相关代码才能请求权限。
                                        CalendarUtil.deleteCalendarEvent(mContext,"XJX");

                                        SpUtil.putString(Constant.SHARE_CALENDAR_PERMISSIONS, "yes");
                                        EventBus.getDefault().post(new RefreshUIEvent(UIBaseEvent.EVENT_LOAN_SUCCESS));
                                        finish();
                                    }

                                    @Override
                                    public void onDenied(List<String> deniedPermissions, boolean isNeverAsk) {
                                        SpUtil.putString(Constant.SHARE_CALENDAR_PERMISSIONS, "no");
                                        EventBus.getDefault().post(new RefreshUIEvent(UIBaseEvent.EVENT_LOAN_SUCCESS));
                                        ToastUtil.showToast("日历提醒已被禁止,如有需要,请手动开启");
                                        finish();
                                    }
                                });
                            }
                        });

        }  else {
            dialog.setContent("申请成功")
                    .setRightBtnText("确定")
                    .setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
                        @Override
                        public void dialogRightBtnClick() {
                            EventBus.getDefault().post(new RefreshUIEvent(UIBaseEvent.EVENT_LOAN_SUCCESS));
                            finish();
                        }
                    });
        }
        dialog.build();
    }
    @Override
    public void applyLoanFaild(int code, String message) {
        EventBus.getDefault().post(new PayLeanResultEvent(UIBaseEvent.EVENT_LOAN_FAILED, code, message, Constant.PAY_RESULT_LEND_FAILED, message));
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

    }
}
