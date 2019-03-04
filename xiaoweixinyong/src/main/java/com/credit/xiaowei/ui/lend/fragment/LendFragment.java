package com.credit.xiaowei.ui.lend.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.credit.pretend.ptd_util.RetrofitUtil;
import com.credit.xiaowei.R;
import com.credit.xiaowei.app.App;
import com.credit.xiaowei.base.BaseFragment;
import com.credit.xiaowei.base.PermissionsListener;
import com.credit.xiaowei.config.Constant;
import com.credit.xiaowei.dialog.AlertFragmentDialog;
import com.credit.xiaowei.dialog.CostDetailsDialog;
import com.credit.xiaowei.events.AuthenticationRefreshEvent;
import com.credit.xiaowei.events.FragmentRefreshEvent;
import com.credit.xiaowei.events.UIBaseEvent;
import com.credit.xiaowei.ui.authentication.activity.PerfectInformationActivity;
import com.credit.xiaowei.ui.authentication.bean.OtherProductBean;
import com.credit.xiaowei.ui.lend.activity.BankInputPwdActivity;
import com.credit.xiaowei.ui.lend.activity.LendConfirmLoanActivity;
import com.credit.xiaowei.ui.lend.bean.ConfirmLoanBean;
import com.credit.xiaowei.ui.lend.bean.HomeIndexResponseBean;
import com.credit.xiaowei.ui.lend.contract.LendContract;
import com.credit.xiaowei.ui.lend.presenter.LendPresenter;
import com.credit.xiaowei.ui.main.MainActivity;
import com.credit.xiaowei.ui.main.WebViewActivity;
import com.credit.xiaowei.util.CalendarUtil;
import com.credit.xiaowei.util.SpUtil;
import com.credit.xiaowei.util.TimeUtil;
import com.credit.xiaowei.util.ToastUtil;
import com.credit.xiaowei.util.Tool;
import com.credit.xiaowei.util.ViewUtil;
import com.credit.xiaowei.widget.DrawableCenterTextView;
import com.credit.xiaowei.widget.HomeSeekBar;
import com.credit.xiaowei.widget.LockableScrollView;
import com.credit.xiaowei.widget.RollView;
import com.credit.xiaowei.widget.loading.LoadingLayout;
import com.google.gson.JsonObject;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**********
 * 借款
 */
public class LendFragment extends BaseFragment<LendPresenter> implements OnClickListener, LendContract.View {

    @BindView(R.id.loading_layout)
    LoadingLayout mLoadingLayout;
    @BindView(R.id.roll_view)
    RollView mRollView; //滚动消息view
    @BindView(R.id.tv_credit_money)
    TextView mTvCreditMoney;  //信用额度
    @BindView(R.id.rl_credit)
    RelativeLayout mRlCredit;  //信用额度布局
    @BindView(R.id.scroll_view)
    LockableScrollView mScrollView; //滚动view
    @BindView(R.id.refresh)
    SwipeRefreshLayout mRefresh;    //刷新view
    @BindView(R.id.ll_loan_content)
    LinearLayout mLlLoanContent;   //未借款时布局
    @BindView(R.id.tv_money)
    TextView mTvMoney;  //借款金额
    @BindView(R.id.tv_bigMoney)
    TextView mTvBigMoney;   //"最大可借"与"可借额度"文字
    @BindView(R.id.hsb_selected_money)
    HomeSeekBar mHsbSelectedMoney;  //金额拖动view
    @BindView(R.id.rg_days)
    RadioGroup mRgDays; //借款日期容器
    @BindView(R.id.tv_home_limit1)
    TextView mTvHomeLimit1; //到账金额
    @BindView(R.id.tv_home_limit2)
    TextView mTvHomeLimit2; //服务费用
    @BindView(R.id.ll_verify)
    LinearLayout mLlVerify; //认证步骤布局
    @BindView(R.id.tv_phone_auth)
    TextView mTvPhoneAuth;     //显示当前认证多少步骤
    @BindView(R.id.tv_rent_btn)
    TextView mTvRentBtn;        //登录or借款按钮
    @BindView(R.id.ll_status_content)
    LinearLayout mLlStatusContent;  //借款后布局
    @BindView(R.id.tv_loan_tag)
    TextView mTvLoanTag;    //标题
    @BindView(R.id.ll_status_item_view)
    LinearLayout mLlStatusItemView; //借款状态view
    //    @BindView(R.id.activity_recycler)
//    RecyclerView mActivityRecycler; //首页活动列表
//    @BindView(R.id.ll_service_fee)
//    LinearLayout mLlServiceFee; //登录时查看服务费按钮
//    @BindView(R.id.fl_check_service_fee)
//    FrameLayout mFlCheckServiceFee; //未登录时查看服务费按钮
    @BindView(R.id.ll_surplus_status)
    LinearLayout mLlSurplusStatus;
    @BindView(R.id.tv_surplus_title)
    TextView mTvSurplusTitle;

    @BindView(R.id.tv_surplus_txt)
    TextView mTvSurplusTxt;

    @BindView(R.id.tv_surplus_day)
    TextView mTvSurplusDay;

    @BindView(R.id.tv_surplus_btn)
    DrawableCenterTextView mTvSurplusBtn;

    public static LendFragment lendFragment;
    @BindView(R.id.tv_warning_desc)
    TextView tvWarningDesc;
    Unbinder unbinder;
    @BindView(R.id.tv_earnings_desc)
    TextView tvEarningsDesc;
    @BindView(R.id.tv_service_desc)
    TextView tvServiceDesc;
    @BindView(R.id.view_line)
    View viewLine;
    @BindView(R.id.ll_money)
    LinearLayout llMoney;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_message)
    ImageView ivMessage;
    @BindView(R.id.ll_days_container)
    LinearLayout llDaysContainer;
    @BindView(R.id.ll_day)
    LinearLayout llDay;
    @BindView(R.id.v_day_line)
    View vDayLine;
    @BindView(R.id.fl_process_container)
    FrameLayout flProcessContainer;
    //    @BindView(R.id.tv_days)
//    TextView tvDays;
    private HomeIndexResponseBean.ItemBean bean;
    private int maxMoney, loanMoney;
    private int loanDay;
    private double realMoney;
    private int realMaxMoney;
    private double serviceMoney;
    private int intoMoney; //还款金额
    private String loanEndTime; //还款时间
    private int surplusDay; //剩余还款天数
    private int nextLoanDay; //剩余可借款时间倒计时
    private HomeIndexResponseBean.AmountDaysListBean moneyPeriodBean;
    private List<HomeIndexResponseBean.IndexImagesBean> mActivityListBean = new ArrayList<>();

//    private ActivityListAdapter mActivityListAdapter;

    private MainActivity mainActivity;
    private OtherProductBean otherProductBean;

    public static LendFragment getInstance() {
        if (lendFragment == null) {
            lendFragment = new LendFragment();
        }
        return lendFragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_lend_main_new;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        mainActivity = (MainActivity) mActivity;
        EventBus.getDefault().register(this);
        initView();
        initListener();
    }

    private void initView() {
        tvTitle.setText(App.getAPPName());
        setWindowTranslucentFlags();
//        mTitle.setTitle(false,App.getAPPName() );
//        mTitle.setRightTitle(R.mipmap.icon_message, new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String url = App.getConfig().ACTIVITY_CENTER;
//                Intent intent = new Intent(getActivity(), WebViewActivity.class);
//                intent.putExtra("url", url);
//                startActivity(intent);
//            }
//        });

        mRefresh.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.theme_color));
//        mActivityRecycler.setLayoutManager(new LinearLayoutManager(mContext));
//        mActivityRecycler.setFocusable(false);
//        mActivityListAdapter = new ActivityListAdapter();
//        mActivityRecycler.setAdapter(mActivityListAdapter);
//        mActivityListAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClick() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Intent intent = new Intent(mActivity, WebViewActivity.class);
//                intent.putExtra("url", mActivityListAdapter.getData().get(position).getReUrl() + "?clientType=andriod&app=1");
//                startActivity(intent);
//            }
//        });
        //加载数据
        mLoadingLayout.setStatus(LoadingLayout.Loading);
        mPresenter.loadIndex();
        if (App.getConfig().getLoginStatus()) {
//            mLlServiceFee.setVisibility(View.VISIBLE);
//            mFlCheckServiceFee.setVisibility(View.GONE);
            mTvRentBtn.setText("我要借款");
            tvWarningDesc.setVisibility(View.VISIBLE);//不向学生提供借款提醒.
        } else {//未登录
//            mLlServiceFee.setVisibility(View.VISIBLE);
            llMoney.setVisibility(View.GONE);//隐藏到账金额
            tvServiceDesc.setVisibility(View.GONE);//隐藏服务费
            tvEarningsDesc.setVisibility(View.VISIBLE);//显示借款利息
//            tvDays.setText("7/14/30天");

//            mFlCheckServiceFee.setVisibility(View.GONE);
            mTvRentBtn.setText("马上登录");
            tvWarningDesc.setVisibility(View.GONE);
        }
    }


    @Override
    public void indexSuccess(HomeIndexResponseBean result) {
        mLoadingLayout.setStatus(LoadingLayout.Success);
        bean = result.getItem();
        //金额集合
        List<Integer> amounts = result.getAmount_days_list().getAmounts();
        if (amounts != null && amounts.size() > 2) {
            //需求：只能借整百
            //如果最后一个值与倒数第二位相等或者最后一个值除100不等于0，则去除最后一个值
            if (amounts.get(amounts.size() - 1).equals(amounts.get(amounts.size() - 2)) || amounts.get(amounts.size() - 1) % 10000 != 0) {
                amounts.remove(amounts.size() - 1);
            }
            //获取最大金额值
            realMaxMoney = amounts.get(amounts.size() - 1);
        }
        //金额集合
        moneyPeriodBean = result.getAmount_days_list();
        // 借款滚动信息
        mRollView.setContent(result.getUser_loan_log_list());
        //动态加载借款时间
        setDays(result.getAmount_days_list().getDays());
//        if (App.getConfig().getLoginStatus()) {
//            tvDays.setText(result.getAmount_days_list().getDays().get(0)+"天");
//        }else {
//            tvDays.setText("7/14/30天");
//        }


//        //热门活动集合
//        mActivityListBean = result.getIndex_images();
//        mActivityListAdapter.clearData();
//        mActivityListAdapter.addData(mActivityListBean);
        //获取数据后进行显示在界面上
        setData();

    }

    /**
     * 设置状态栏和导航栏透明
     * 为沉浸式状态栏的必要一步
     */
    public void setWindowTranslucentFlags() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            int statusBarHeight = getStatusBarHeight();
//            vStatus.setPadding(0, statusBarHeight + 16, 0, 0);
            //透明状态栏
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    public void toLoanSuccess(final ConfirmLoanBean result) {
        //验证借款成功,判断借款今日金额是否已达上限
        Call<JsonObject> call = RetrofitUtil.create().checkLimit();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONObject object = new JSONObject(response.body().toString());
                    String code = object.optString("code");
                    if (code != null && code.equals("Y")) {//已达上限
                        String msg = object.optString("msg");
                        String title = object.optString("title");
                        new AlertFragmentDialog.Builder(getActivity())
                                .setTitle(title)
                                .setContent(msg)
                                .setLeftBtnText("确定")
                                .setCancel(true)
                                .build();
                    } else {
                        Intent intent = new Intent(getActivity(), LendConfirmLoanActivity.class);
                        intent.putExtra(BankInputPwdActivity.TAG_OPERATE_BEAN, result);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Intent intent = new Intent(getActivity(), LendConfirmLoanActivity.class);
                intent.putExtra(BankInputPwdActivity.TAG_OPERATE_BEAN, result);
                startActivity(intent);
            }
        });
    }

    @Override
    public void confirmFailedSuccess() {
        mPresenter.loadIndex();
        EventBus.getDefault().post(new FragmentRefreshEvent(UIBaseEvent.EVENT_LOAN_FAILED));
    }

//    @Override
//    public void showOtherProductSuccess(OtherProductBean otherProductBean) {
//        this.otherProductBean = otherProductBean;
//    }

    @Override
    public void moreCommitSucess(JsonObject jsonObject, String product_url) {
        if (jsonObject != null) {
            try {
                JSONObject object = new JSONObject(jsonObject.toString());
                String error = object.optString("error");
                if (error.equals("N")) {
                    Bundle message = new Bundle();
                    message.putString("url", product_url);
                    startActivity(WebViewActivity.class, message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void showLoading(String content) {
        if (bean != null) {
            App.loadingContent(mActivity, content);
        }
    }

    @Override
    public void stopLoading() {
        App.hideLoading();
        if (mRefresh.isRefreshing()) {
            mRefresh.setRefreshing(false);
        }
    }

    /**
     * @param msg  请求异常信息
     * @param type 若有多个请求，用于区分不同请求（不同请求失败或有不同的处理）
     */
    @Override
    public void showErrorMsg(String msg, String type) {
        if (TextUtils.isEmpty(type)) {
            return;
        }
        if (type.equals(mPresenter.TYPE_INDEX)) {//首页接口
            ToastUtil.showToast(msg);
            loadingStatusView(msg);
        } else if (type.equals(mPresenter.TYPE_FAILED)) { //借款失败调用
            mPresenter.loadIndex();
        } else if (type.equals(mPresenter.TYPE_LOAN)) {   //验证借款信息
            new AlertFragmentDialog.Builder(getActivity())
                    .setContent(msg)
                    .setRightBtnText("确定")
                    .setCancel(false)
                    .build();
//            ToastUtil.showToast(msg);
        } else if (type.equals(mPresenter.TYPE_OTHER_PRODUCT)) {
            ToastUtil.showToast(msg);
        }
    }

    //为view设置数据
    private void setData() {
        if (bean != null) {
            //认证状态
            if (bean.getVerify_loan_pass() == 1 && moneyPeriodBean.getNewflag() == 2) {//已认证并已出额度
                mLlVerify.setVisibility(View.GONE);
                mRlCredit.setVisibility(View.GONE);
                mTvCreditMoney.setText("" + bean.getCard_amount() / 100);
                mTvBigMoney.setText("可借额度");
                llDaysContainer.setVisibility(View.VISIBLE);
                llDay.setVisibility(View.GONE);
                vDayLine.setVisibility(View.GONE);
                flProcessContainer.setVisibility(View.VISIBLE);
            } else {
                mRlCredit.setVisibility(View.GONE);
                mTvBigMoney.setText("最高借款额度");
                if (bean.getVerify_loan_nums() > 0 && bean.getVerify_loan_nums() < 5) {
                    mLlVerify.setVisibility(View.VISIBLE);
                } else if (bean.getVerify_loan_nums() == 0) {

                    mLlVerify.setVisibility(View.GONE);
                }
                llDaysContainer.setVisibility(View.GONE);
                llDay.setVisibility(View.VISIBLE);
                vDayLine.setVisibility(View.VISIBLE);
                flProcessContainer.setVisibility(View.GONE);
                mTvPhoneAuth.setText("您已" + bean.getCard_verify_step() + ",完成认证即可拿钱，加油！");
            }
            //借款状态  bean.getLoan_infos()不为空时为已登录并借款
            if (bean.getLoan_infos() != null) {
                showView(mLlStatusContent);
                setLoanStatus();
            } else {//没有借款
                nextLoanDay = bean.getNext_loan_day();
                if (nextLoanDay > 0 && App.getConfig().getLoginStatus()) {
                    String countDownTag = SpUtil.getString(Constant.SHARE_TAG_COUNT_DOWN);
                    if (TextUtils.isEmpty(countDownTag) || !countDownTag.equals(Constant.SHARE_TAG_NEXT_LOAN)) {
                        SpUtil.putString(Constant.SHARE_TAG_COUNT_DOWN, Constant.SHARE_TAG_NEXT_LOAN);
                    }
                    showView(mLlSurplusStatus);
                } else {
                    showView(mLlLoanContent);
                    if (moneyPeriodBean != null) {
                        List<Integer> amounts = moneyPeriodBean.getAmounts();
                        if (amounts != null && amounts.size() >= 2) {
                            maxMoney = (amounts.size() - 1) * 100;
                        } else {
                            maxMoney = 0;
                        }
                    }
                    mHsbSelectedMoney.setMax(maxMoney);
                    mHsbSelectedMoney.setAnimProgress(maxMoney, false);
                }

            }
        }

        //没验证的隐藏到账金额,显示日利率
        if (moneyPeriodBean.getNewflag() != 2) {
            tvEarningsDesc.setVisibility(View.VISIBLE);
            tvServiceDesc.setVisibility(View.GONE);
            viewLine.setVisibility(View.GONE);
            llMoney.setVisibility(View.GONE);
        } else {
            tvEarningsDesc.setVisibility(View.GONE);
            tvServiceDesc.setVisibility(View.VISIBLE);
            viewLine.setVisibility(View.VISIBLE);
            llMoney.setVisibility(View.VISIBLE);
        }
    }

    //审核状态改变
    private void setLoanStatus() {
        mLlStatusItemView.removeAllViews();
        HomeIndexResponseBean.ItemBean.LoanInfosBean loaninfo = bean.getLoan_infos();
        mTvLoanTag.setText(loaninfo.getHeader_tip());
        List<HomeIndexResponseBean.ItemBean.LoanInfosBean.ListsBean> loans = loaninfo.getLists();
        if (loans != null && loans.size() > 0) {
            for (int i = 0; i < loans.size(); i++) {
                HomeIndexResponseBean.ItemBean.LoanInfosBean.ListsBean loan = loans.get(i);
                if (i == 0) {
                    addLoanStatusView(loan, R.layout.layout_loan_status_item1, i);
                } else if (i == loans.size() - 1) {
                    addLoanStatusView(loan, R.layout.layout_loan_status_item3, i);
                } else {
                    addLoanStatusView(loan, R.layout.layout_loan_status_item2, i);
                }
            }
        }
        HomeIndexResponseBean.ItemBean.LoanInfosBean.ButtonBean button = loaninfo.getButton();
        if (button != null) {
            final String orderId = button.getId();
            View btnLayout = LayoutInflater.from(getActivity()).inflate(R.layout.layout_loan_status_button, null);
            TextView tv_ikonw_btn = (TextView) btnLayout.findViewById(R.id.tv_ikonw_btn);
            tv_ikonw_btn.setText(button.getMsg());
            tv_ikonw_btn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.confirmFailed(orderId);
                }
            });
            mLlStatusItemView.addView(btnLayout);
            if (otherProductBean != null) {//添加贷超布局
                mLlStatusItemView.addView(loadOtherProductView(otherProductBean));
            }

        }

        intoMoney = loaninfo.getIntoMoney();//还款金额
        loanEndTime = loaninfo.getLoanEndTime();//还款日期 格式:2016-12-12
        surplusDay = loaninfo.getLastRepaymentD();//还款倒计时天数
        if (intoMoney > 0 && !TextUtils.isEmpty(loanEndTime)) {//满足这个条件,表示当前为还款阶段
            //通知还款页面列表刷新
            EventBus.getDefault().post(new FragmentRefreshEvent(UIBaseEvent.EVENT_BORROW_MONEY_SUCCESS));

            if (!TextUtils.isEmpty(SpUtil.getString(Constant.SHARE_TAG_COUNT_DOWN)) &&
                    SpUtil.getString(Constant.SHARE_TAG_COUNT_DOWN).equals(Constant.SHARE_TAG_REPAY_COUNT_DOWN)) {
                showView(mLlSurplusStatus);
            } else {
                //切换倒计时页面
                View btnLayout = LayoutInflater.from(getActivity()).inflate(R.layout.layout_loan_status_button, null);
                TextView tv_ikonw_btn = (TextView) btnLayout.findViewById(R.id.tv_ikonw_btn);
                tv_ikonw_btn.setText("我知道了");
                tv_ikonw_btn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SpUtil.putString(Constant.SHARE_TAG_COUNT_DOWN, Constant.SHARE_TAG_REPAY_COUNT_DOWN);
                        showView(mLlSurplusStatus);
                    }
                });
                mLlStatusItemView.addView(btnLayout);
            }

            showCalenderDialog();
        }
    }

    /**
     * 被拒后展示其他产品(贷超)
     *
     * @param otherProductBean
     */
    public View loadOtherProductView(OtherProductBean otherProductBean) {
        if (otherProductBean == null) return null;
        //申请被拒展示 其他产品视图
        final List<OtherProductBean.ItemsBean> items = otherProductBean.items;
        View view = View.inflate(getActivity(), R.layout.other_products, null);
        LinearLayout ll_icon_container = (LinearLayout) view.findViewById(R.id.ll_icon_container);
        view.findViewById(R.id.tv_btn_more).setOnClickListener(new OnClickListener() {//更多产品
            @Override
            public void onClick(View v) {
                Bundle message = new Bundle();
                message.putString("url", App.getConfig().MORE_PRODUCT_DC + "?deviceId=" + ViewUtil.getDeviceId(App.getContext()));
                startActivity(WebViewActivity.class, message);
            }
        });
//        ll_icon_container.removeAllViews();
        if (items != null && items.size() != 0) {
            for (int i = 0; i < items.size(); i++) {
                final OtherProductBean.ItemsBean itemsBean = items.get(i);
                View op_item_icon_view = View.inflate(getActivity(), R.layout.op_item_icon, null);
                final LinearLayout ll_item_container = (LinearLayout) op_item_icon_view.findViewById(R.id.ll_item_container);
                ll_item_container.setOnClickListener(new OnClickListener() {//产品点击跳转
                    @Override
                    public void onClick(View v) {
                        mPresenter.moreCommit(itemsBean.id, itemsBean.product_url);
                    }
                });
                ImageView iv_icon = (ImageView) op_item_icon_view.findViewById(R.id.iv_icon);
                TextView tv_range_amount = (TextView) op_item_icon_view.findViewById(R.id.tv_range_amount);
                Glide.with(mContext).load(App.getConfig().getBaseUrl() + "getImages?url=" + itemsBean.icon_url)
                        .placeholder(R.mipmap.banner)
                        .error(R.mipmap.banner)
                        .centerCrop()
                        .into(iv_icon); //设置图片
                tv_range_amount.setText(itemsBean.loan_min + "-" + itemsBean.loan_max + "元");
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
                ll_item_container.setLayoutParams(layoutParams);
                ll_icon_container.addView(op_item_icon_view);
//
            }
        }
        return view;
    }

    /**
     * 切换 未借款、借款中。倒计时布局
     *
     * @param view
     */
    private void showView(View view) {
        switch (view.getId()) {
            case R.id.ll_loan_content:
                mLlLoanContent.setVisibility(View.VISIBLE);
                mLlStatusContent.setVisibility(View.GONE);
                mLlSurplusStatus.setVisibility(View.GONE);
                break;
            case R.id.ll_status_content:
                mLlLoanContent.setVisibility(View.GONE);
                mLlStatusContent.setVisibility(View.VISIBLE);
                mLlSurplusStatus.setVisibility(View.GONE);
                break;
            case R.id.ll_surplus_status:
                mLlLoanContent.setVisibility(View.GONE);
                mLlStatusContent.setVisibility(View.GONE);
                mLlSurplusStatus.setVisibility(View.VISIBLE);
                if (SpUtil.getString(Constant.SHARE_TAG_COUNT_DOWN).equals(Constant.SHARE_TAG_REPAY_COUNT_DOWN)) {

                    if (surplusDay < 0) {
                        mTvSurplusTxt.setText("逾 期");
                        mTvSurplusTitle.setText("本次已逾期时间");
                    } else {
                        mTvSurplusTxt.setText("还 剩");
                        mTvSurplusTitle.setText("距离本次还款时间");
                    }
                    mTvSurplusBtn.setText("现在就还");
                    mTvSurplusBtn.setEnabled(true);
                    mTvSurplusBtn.setGravity(Gravity.CENTER);
                    mTvSurplusBtn.setCompoundDrawablePadding(0);
                    mTvSurplusBtn.setCompoundDrawables(null, null, null, null);
                    mTvSurplusDay.setText(String.valueOf(Math.abs(surplusDay)).length() == 1 ? (surplusDay == 0 ? String.valueOf(surplusDay) : "0" + Math.abs(surplusDay)) : String.valueOf(Math.abs(surplusDay)));
                } else if (SpUtil.getString(Constant.SHARE_TAG_COUNT_DOWN).equals(Constant.SHARE_TAG_NEXT_LOAN)) {
                    mTvSurplusTitle.setText("距离下次申请借款时间");
                    mTvSurplusTxt.setText("还 剩");
                    String nowLoanDate = TimeUtil.getCurrentDateByOffset("yyyy-MM-dd 00:00:00", Calendar.DAY_OF_MONTH, nextLoanDay);
                    if (TextUtils.isEmpty(SpUtil.getString(Constant.SHARE_CALENDAR_LOAN_DATE)) ||
                            !SpUtil.getString(Constant.SHARE_CALENDAR_LOAN_DATE).equals(nowLoanDate)) {
                        mTvSurplusBtn.setText("提醒我");
                        mTvSurplusBtn.setEnabled(true);
                    } else {
                        mTvSurplusBtn.setText("已添加提醒");
                        mTvSurplusBtn.setEnabled(false);
                    }
                    mTvSurplusBtn.setGravity(Gravity.CENTER_VERTICAL);
                    Drawable left = ContextCompat.getDrawable(mActivity, R.mipmap.remind);
                    left.setBounds(0, 0, left.getMinimumWidth(), left.getMinimumHeight());
                    mTvSurplusBtn.setCompoundDrawables(left, null, null, null);
                    mTvSurplusBtn.setCompoundDrawablePadding(Tool.dip2px(mContext, 5));
                    mTvSurplusDay.setText(String.valueOf(nextLoanDay).length() == 1 ? (nextLoanDay == 0 ? String.valueOf(nextLoanDay) : "0" + nextLoanDay) : String.valueOf(nextLoanDay));
                }
                break;
        }
    }

    private AlertFragmentDialog dialog;

    /**
     * 日历功能
     */
    private void showCalenderDialog() {
        String calendarPermissions = SpUtil.getString(Constant.SHARE_CALENDAR_PERMISSIONS);
        boolean permissions = ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_DENIED;
        //未授权则弹出框
        if (TextUtils.isEmpty(calendarPermissions) && permissions) {
            if (dialog == null) {
                dialog = new AlertFragmentDialog.Builder(mActivity).setLeftBtnText("不允许")
                        .setLeftCallBack(new AlertFragmentDialog.LeftClickCallBack() {
                            @Override
                            public void dialogLeftBtnClick() {
                                SpUtil.putString(Constant.SHARE_CALENDAR_PERMISSIONS, "no");
                            }
                        })
                        .setRightBtnText("允许")
                        .setContent("开启日历还款提醒，不再忘记还钱，更快提升额度。")
                        .setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
                            @Override
                            public void dialogRightBtnClick() {
                                requestPermissions(new String[]{Manifest.permission.WRITE_CALENDAR}, new PermissionsListener() {
                                    @Override
                                    public void onGranted() {
                                        insetCalender();
                                    }

                                    @Override
                                    public void onDenied(List<String> deniedPermissions, boolean isNeverAsk) {
                                        SpUtil.putString(Constant.SHARE_CALENDAR_PERMISSIONS, "no");
                                    }
                                });
                            }
                        }).build();
            } else if (!dialog.getShowsDialog()) {
                dialog.show(getChildFragmentManager(), AlertFragmentDialog.TAG);
            }
        } else if (calendarPermissions.equals("yes") || !permissions) {
            insetCalender();
        }
    }

    private void insetCalender() {
        //往日历中插数据
        final String mLoanEndTime = SpUtil.getString(Constant.SHARE_CALENDAR_REPAY_DATE);//获取缓存的还款日期
        int mLoanEndMoney = SpUtil.getInt(Constant.SHARE_CALENDAR_REPAY_MONEY); //获取缓存的还款金额
        if (TextUtils.isEmpty(mLoanEndTime) || !mLoanEndTime.equals(loanEndTime) ||
                mLoanEndMoney != intoMoney) {//缓存的日期为空或者与返回的日期不相等，则去日历插入
            mActivity.requestPermissions(new String[]{Manifest.permission.WRITE_CALENDAR}, new PermissionsListener() {
                @Override
                public void onGranted() {
                    SpUtil.putString(Constant.SHARE_CALENDAR_PERMISSIONS, "yes");
                    CalendarUtil.deleteCalendarEvent(mContext, App.getAPPName());
                    String refundDate = loanEndTime;//还款日期 2017-01-06格式
                    String refundAmount = String.valueOf(intoMoney);//还款金额
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
                    String time = refundDate + " 00:00:00";
                    Date date = null;
                    long mFinalTime = 0;
                    try {
                        date = format.parse(time);
                        long dateTime = date.getTime();
                        mFinalTime = dateTime - 14 * 60 * 60 * 1000;//还款日的前一天10:00提示
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String fomater = CalendarUtil.dateFomater(refundDate);
                    if (CalendarUtil.addCalendarEvent(getContext(),
                            App.getAPPName(), "明天" + fomater + ",小微信用需还款" + refundAmount + "元(如已提前还款请忽略)",
                            mFinalTime)) {
                        SpUtil.putString(Constant.SHARE_CALENDAR_REPAY_DATE, loanEndTime);
                        SpUtil.putInt(Constant.SHARE_CALENDAR_REPAY_MONEY, intoMoney);
                    }
                }

                @Override
                public void onDenied(List<String> deniedPermissions, boolean isNeverAsk) {
                    SpUtil.putString(Constant.SHARE_CALENDAR_PERMISSIONS, "no");
                    ToastUtil.showToast("日历提醒已被禁止,如有需要,请手动开启");
                }
            });
        }
    }

    /**
     * 加载借款时状态布局
     *
     * @param loan
     * @param layout
     */
    private void addLoanStatusView(HomeIndexResponseBean.ItemBean.LoanInfosBean.ListsBean loan, int layout, int position) {
        View content = LayoutInflater.from(getActivity()).inflate(layout, null);
        ImageView item_icon = (ImageView) content.findViewById(R.id.item_icon);
        View item_line = content.findViewById(R.id.item_line);
        if (position == 0 || position == 1) {
            item_line.setBackgroundResource(R.mipmap.icon_dashed);
        }
        TextView tv_title = (TextView) content.findViewById(R.id.tv_title);
        TextView tv_info = (TextView) content.findViewById(R.id.tv_info);
        tv_title.setText(Html.fromHtml(loan.getTitle()));
        tv_info.setText(loan.getBody());
        if (loan.getTag() == 0) {
            item_icon.setImageResource(R.mipmap.icon_loan_status_passed);
            tv_title.setTextColor(ContextCompat.getColor(mContext, R.color.global_label_color));
            tv_info.setTextColor(ContextCompat.getColor(mContext, R.color.global_label_color));
        } else if (loan.getTag() == 1) {
            item_icon.setImageResource(R.mipmap.state_underway);
            tv_title.setTextColor(ContextCompat.getColor(mContext, R.color.global_black_color));
            tv_info.setTextColor(ContextCompat.getColor(mContext, R.color.global_black_color));
        } else if (loan.getTag() == 2) {
            item_icon.setImageResource(R.mipmap.icon_loan_status_nopassed);
            tv_title.setTextColor(ContextCompat.getColor(mContext, R.color.global_black_color));
            tv_info.setTextColor(ContextCompat.getColor(mContext, R.color.global_black_color));
        } else if (loan.getTag() == 3) {
            item_icon.setImageResource(R.mipmap.state_done);
            tv_title.setTextColor(ContextCompat.getColor(mContext, R.color.global_black_color));
            tv_info.setTextColor(ContextCompat.getColor(mContext, R.color.global_black_color));
        }
        mLlStatusItemView.addView(content);
    }

    private void loadingStatusView(String message) {
        if ("网络不可用".equals(message)) {
            mLoadingLayout.setStatus(LoadingLayout.No_Network);
        } else {
            mLoadingLayout.setErrorText(message)
                    .setStatus(LoadingLayout.Error);
        }
        mLoadingLayout.setOnReloadListener(new LoadingLayout.OnReloadListener() {
            @Override
            public void onReload(View v) {
                mPresenter.loadIndex();
            }
        });
    }

    private void initListener() {

        //选择金钱的seekBar
        mHsbSelectedMoney.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            /**
             * 拖动条进度改变的时候调用
             */
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (moneyPeriodBean == null) {
                    return;
                }
                int position = (progress + 50) / 100;
                if (position >= moneyPeriodBean.getAmounts().size()) {
                    position = moneyPeriodBean.getAmounts().size() - 1;
                }
                loanMoney = moneyPeriodBean.getAmounts().get(position) / 100;
                //下面到账金额的改变
                calculateRate(moneyPeriodBean.getDays());
                //字体的改变
                mTvMoney.setText(loanMoney + "");
            }

            /**
             * 开始拖动条进度的时候调用
             */
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mRefresh.setEnabled(false);//拖动时禁止刷新
            }

            /**
             * 结束拖动条进度的时候调用
             */
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mRefresh.setEnabled(true);//拖动结束解锁刷新
                mHsbSelectedMoney.setProgress((mHsbSelectedMoney.getProgress() + 50) / 100 * 100);
            }
        });

        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadIndex();
            }
        });
    }

    @OnClick({R.id.tv_improveMoney, R.id.tv_rent_btn, R.id.tv_phone_auth, R.id.ll_service_fee_check, R.id.tv_surplus_btn, R.id.iv_message})
    public void onClick(View v) {
        if (Tool.isFastDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.iv_message://活动中心h5
                String url = App.getConfig().ACTIVITY_CENTER;
                Intent intent1 = new Intent(getActivity(), WebViewActivity.class);
                intent1.putExtra("url", url);
                startActivity(intent1);
                break;
            case R.id.tv_improveMoney://提额链接跳转
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("title", "提额");
                intent.putExtra("improveUrl", Constant.LIMIT_URL);
                startActivity(intent);
                break;
            case R.id.tv_rent_btn:  //借款按钮
                if (!App.getConfig().getLoginStatus()) {
                    App.toLogin(getActivity());
                } else {
                    if (bean.getVerify_loan_pass() == 1) {
                        toLoan();
                    } else {
                        Intent liftingQuotaIntent = new Intent(getActivity(), PerfectInformationActivity.class);
                        startActivityToLogin(liftingQuotaIntent);
                    }
                }
                break;
            case R.id.tv_phone_auth:    //步骤认证
                Intent liftingQuotaIntent = new Intent(getActivity(), PerfectInformationActivity.class);
                startActivityToLogin(liftingQuotaIntent);
                break;
//            case R.id.fl_check_service_fee: //未登录时查看服务费
//                App.toLogin(getActivity());
//                break;
            case R.id.ll_service_fee_check: //登录后查看服务费
                if (moneyPeriodBean.getNewflag() != 2) {
                    return;
                }
                CostDetailsDialog.newInstance(loanMoney + "", loanDay + "").show(getChildFragmentManager(), CostDetailsDialog.TAG);
                break;
            case R.id.tv_surplus_btn:
                String status = SpUtil.getString(Constant.SHARE_TAG_COUNT_DOWN);
                if (TextUtils.isEmpty(status)) {
                    return;
                }
                if (status.contains(Constant.SHARE_TAG_REPAY_COUNT_DOWN)) {//还款倒计时
                    mainActivity.getGroup().check(R.id.rb_rent_lend);
                } else if (status.contains(Constant.SHARE_TAG_NEXT_LOAN)) {//下次可借款倒计时
                    //往日历中插数据
                    final String mLoanStartTime = SpUtil.getString(Constant.SHARE_CALENDAR_LOAN_DATE);//获取缓存的下次可借款日期
                    Log.e("----", "mLoanStartTime=" + mLoanStartTime + "loanEndTime =" + loanEndTime);
                    if (TextUtils.isEmpty(mLoanStartTime) || !mLoanStartTime.equals(loanEndTime)) {//缓存的日期为空或者与返回的日期不相等，则去日历插入
                        mActivity.requestPermissions(new String[]{Manifest.permission.WRITE_CALENDAR}, new PermissionsListener() {
                            @Override
                            public void onGranted() {
                                CalendarUtil.deleteCalendarEvent(mContext, App.getAPPName());
                                //下次可借款日期 格式:yyyy-MM-dd 00:00:00
                                String refundDate = TimeUtil.getCurrentDateByOffset("yyyy-MM-dd 00:00:00", Calendar.DAY_OF_MONTH, nextLoanDay);

                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
                                Date date;
                                long mFinalTime = 0;
                                try {
                                    date = format.parse(refundDate);
                                    long dateTime = date.getTime();
                                    mFinalTime = dateTime + 10 * 60 * 60 * 1000;//可借款日的当天10:00提示
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                if (CalendarUtil.addCalendarEvent(getContext(),
                                        App.getAPPName(), "今天" + App.getAPPName() + "可以申请借款啦",
                                        mFinalTime)) {
                                    SpUtil.putString(Constant.SHARE_CALENDAR_LOAN_DATE, refundDate);
                                    mTvSurplusBtn.setEnabled(false);
                                    mTvSurplusBtn.setText("已添加提醒");
                                }
                            }

                            @Override
                            public void onDenied(List<String> deniedPermissions, boolean isNeverAsk) {
                                SpUtil.putString(Constant.SHARE_CALENDAR_PERMISSIONS, "no");
                                if (isNeverAsk) {
                                    mActivity.toAppSettings("日历权限已被禁止", false);
                                }
                            }
                        });
                    }
                }
                break;
            default:
                break;
        }
    }

    private void setDays(final List<Integer> days) {
        if (days == null || days.isEmpty()) {
            return;
        }
        if (mRgDays.getChildCount() > 0) {
            mRgDays.removeAllViews();
        }
        int margin = Tool.dip2px(mContext, 25);
        for (int i = 0; i < days.size(); i++) {
            RadioButton btn = new RadioButton(mContext);
            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(0, Tool.dip2px(mContext, 40), 1);
            if (i > 0) {
                params.leftMargin = margin;
            }
            btn.setLayoutParams(params);
            btn.setText(days.get(i) + "天");
            btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            btn.setTextAppearance(mContext, R.style.homeDayTextColor);
            btn.setGravity(Gravity.CENTER);
            //4.4和以下系统去除radioButton样式
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                try {
                    Field field = btn.getClass().getSuperclass().getDeclaredField("mButtonDrawable");
                    field.setAccessible(true);
                    field.set(btn, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                btn.setButtonDrawable(null);
            }
            btn.setBackgroundResource(R.drawable.selector_home_time_btn);
            mRgDays.addView(btn);
        }
        mRgDays.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < group.getChildCount(); i++) {
                    if (group.getChildAt(i).getId() == checkedId) {
                        loanDay = days.get(i);
                        calculateRate(days);
                        break;
                    }
                }
            }
        });
        if (loanDay == 0) {
            mRgDays.check(mRgDays.getChildAt(days.size() - 1).getId());
        } else {
            for (int i = 0; i < days.size(); i++) {
                if (days.get(i) == loanDay) {
                    mRgDays.check(mRgDays.getChildAt(i).getId());
                    break;
                }
            }
        }
    }

    /**
     * 当天数选择的时候，计算费用
     */
    private void calculateRate(List<Integer> amountDaysList) {

        if (loanMoney == 0) {
            mTvHomeLimit1.setText("0.00元");
            if (moneyPeriodBean.getNewflag() != 2) {
                mTvHomeLimit2.setText(moneyPeriodBean.getRate() + "/日");
            } else {
                mTvHomeLimit2.setText("0.00元");
            }
        } else {
            List<Double> interests = moneyPeriodBean.getInterests();
            if (amountDaysList.size() != interests.size()) {
                return;
            }

            for (int i = 0; i < interests.size(); i++) {
                if (loanDay == amountDaysList.get(i)) {
                    double maxInterests = moneyPeriodBean.getInterests().get(i) / 100;
                    double offset = ((double) loanMoney * 100 / (double) realMaxMoney);
                    serviceMoney = maxInterests * offset;
                    realMoney = loanMoney - serviceMoney;
                    mTvHomeLimit1.setText(new DecimalFormat("0.00").format(realMoney) + "元");
//                    if (!TextUtils.isEmpty(moneyPeriodBean.getAuthstatus()) && moneyPeriodBean.getAuthstatus().equals("N")) {
                    if (moneyPeriodBean.getNewflag() != 2) {
                        mTvHomeLimit2.setText(moneyPeriodBean.getRate() + "/日");
                    } else {
                        mTvHomeLimit2.setText(new DecimalFormat("0.00").format(serviceMoney) + "元");
                    }
                    break;
                }
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(FragmentRefreshEvent event) {
        //当借款申请成功、还款或续期成功、登录成、退出功时刷新数据
        if (event.getType() == UIBaseEvent.EVENT_LOAN_SUCCESS ||
                event.getType() == UIBaseEvent.EVENT_LOGIN ||
                event.getType() == UIBaseEvent.EVENT_REPAY_SUCCESS ||
                event.getType() == UIBaseEvent.EVENT_LOGOUT) {
            if (App.getConfig().getLoginStatus()) {
//            mLlServiceFee.setVisibility(View.VISIBLE);
//                mFlCheckServiceFee.setVisibility(View.GONE);
                mTvRentBtn.setText("我要借款");
                tvWarningDesc.setVisibility(View.VISIBLE);//不向学生提供借款提醒.
            } else {//未登录
//            mLlServiceFee.setVisibility(View.VISIBLE);
                llMoney.setVisibility(View.GONE);//隐藏到账金额
                tvServiceDesc.setVisibility(View.GONE);//隐藏服务费
                tvEarningsDesc.setVisibility(View.VISIBLE);//显示借款利息
//            tvDays.setText("7/14/30天");

//                mFlCheckServiceFee.setVisibility(View.GONE);
                mTvRentBtn.setText("马上登录");
                tvWarningDesc.setVisibility(View.GONE);
            }
            // 数据刷新
            mPresenter.loadIndex();


//            if (App.getConfig().getLoginStatus()) {
////                mLlServiceFee.setVisibility(View.VISIBLE);
//                mFlCheckServiceFee.setVisibility(View.GONE);
//                mTvRentBtn.setText("我要借款");
//                tvWarningDesc.setVisibility(View.VISIBLE);//不向学生提供借款提醒.
//            } else {
////                mLlServiceFee.setVisibility(View.GONE);
//                mFlCheckServiceFee.setVisibility(View.VISIBLE);
//                mTvRentBtn.setText("马上登录");
//                tvWarningDesc.setVisibility(View.GONE);
//            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAuthenticationRefresh(AuthenticationRefreshEvent event) {
        // 认证成功 数据刷新
        mPresenter.loadIndex();
    }

    private void toLoan() {
        if (loanMoney <= 0) {
            ToastUtil.showToast("借款额度为0,暂不可借款");
            return;
        }
        if (loanDay <= 0) {
            ToastUtil.showToast("借款期限错误");
            return;
        }
        if (loanMoney > (bean.getCard_amount() / 100)) {
            ToastUtil.showToast("您的最高可借额度仅为" + bean.getCard_amount() / 100 + "哦");
            return;
        }
        mPresenter.toLoan(String.valueOf(loanMoney), String.valueOf(loanDay));
    }

    @Override
    public void onResume() {
        super.onResume();
        mRollView.play();
        if (!App.getConfig().isDebug())
            MobclickAgent.onPageStart("首页"); //统计页面，"MainScreen"为页面名称，可自定义
        mPresenter.loadIndex();
        requestOtherProductList();
    }

    public void requestOtherProductList() {
        RetrofitUtil.create().getOtherProductList().enqueue(new Callback<OtherProductBean>() {
            @Override
            public void onResponse(Call<OtherProductBean> call, Response<OtherProductBean> response) {
                otherProductBean = response.body();
            }

            @Override
            public void onFailure(Call<OtherProductBean> call, Throwable t) {
                t.toString();
            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();
        mRollView.onStop();
        if (!App.getConfig().isDebug())
            MobclickAgent.onPageEnd("首页");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
        lendFragment = null;
        unbinder.unbind();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }
}
