package com.innext.pretend.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.innext.pretend.bean.QueryResultBean;
import com.innext.xjx.R;
import com.innext.xjx.base.BaseActivity;
import com.innext.xjx.widget.DrawableCenterTextView;

import java.util.List;

import butterknife.BindView;

/**
 * 查询结果页
 */
public class QueryResultActivity extends BaseActivity {

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
    @BindView(R.id.iv_emotion)
    ImageView ivEmotion;
    @BindView(R.id.tv_desc_1)
    TextView tvDesc1;
    @BindView(R.id.tv_credit_points)
    TextView tvCreditPoints;
    @BindView(R.id.tv_id_info)
    TextView tvIdInfo;
    @BindView(R.id.tv_phone_num_desc)
    TextView tvPhoneNumDesc;
    @BindView(R.id.ll_risk_item_container)
    LinearLayout llRiskItemContainer;
    @BindView(R.id.ll_risk_container)
    LinearLayout llRiskContainer;

    @Override
    public int getLayoutId() {
        return R.layout.ptd_activity_query_result;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void loadData() {
        mTitle.setTitle("报告详情");
        Bundle bundle = getIntent().getExtras();
        QueryResultBean.QueryDetailBean queryDetailBean = (QueryResultBean.QueryDetailBean) bundle.getSerializable("queryDetailBean");
        setData(queryDetailBean);
    }

    private void setData(QueryResultBean.QueryDetailBean queryDetailBean) {
        if (queryDetailBean == null) return;
        String score = queryDetailBean.message.final_score;
        if (!TextUtils.isEmpty(score)) {
            tvCreditPoints.setText(score);
            int i = Integer.parseInt(score);
            if (i == 750) {//满分
                ivEmotion.setImageResource(R.mipmap.ptd_emoji_best);
                tvDesc1.setText("恭喜您，您的信用无可挑剔~~~");
            } else if (i >= 700) {//非常好
                ivEmotion.setImageResource(R.mipmap.ptd_emoji_better);
                tvDesc1.setText("恭喜您，您的信用打败了90%的人，真可谓一人之下万人之上~~~");
            } else if (i >= 600) {//好
                ivEmotion.setImageResource(R.mipmap.ptd_emoji_good);
                tvDesc1.setText("恭喜您，您的信用打败了80%的人，继续保持哦~~~");
            } else {//一般
                ivEmotion.setImageResource(R.mipmap.ptd_emoji_general);
                tvDesc1.setText("作为追求完美的您，信用有待提高哦~~~");
            }
        }

        List<QueryResultBean.QueryDetailBean.MessageBean.RiskItemBean> riskItems = queryDetailBean.message.risk_item;
        if (riskItems.size() == 0) {
            llRiskContainer.setVisibility(View.GONE);
        } else {
            llRiskContainer.setVisibility(View.VISIBLE);
            llRiskItemContainer.removeAllViews();
            for (int i = 0; i < riskItems.size(); i++) {
                if(i == 10) {//超过10项的不展示
                    return;
                }
                View view = View.inflate(this, R.layout.ptd_item_risk, null);
                TextView tv_risk_desc = (TextView) view.findViewById(R.id.tv_risk_desc);
                TextView tv_score = (TextView) view.findViewById(R.id.tv_score);
                tv_risk_desc.setText(riskItems.get(i).risk_name);
                tv_score.setText("-" + riskItems.get(i).score);
                llRiskItemContainer.addView(view);
            }
        }
    }
}
