package com.innext.pretend.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.innext.pretend.bean.QueryResultBean;
import com.innext.pretend.ptd_util.RetrofitUtil;
import com.innext.xjx.R;
import com.innext.xjx.base.BaseActivity;
import com.innext.xjx.config.Constant;
import com.innext.xjx.util.SpUtil;
import com.innext.xjx.widget.DrawableCenterTextView;
import com.megvii.idcardlib.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 填写信息
 */
public class FillInInfoAvtivity extends BaseActivity {
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
    @BindView(R.id.et_real_name)
    EditText etRealName;
    @BindView(R.id.et_card_number)
    EditText etCardNumber;
    @BindView(R.id.et_phone_num)
    EditText etPhoneNum;
    @BindView(R.id.et_verification)
    EditText etVerification;
    @BindView(R.id.iv_verification)
    ImageView ivVerification;
    @BindView(R.id.iv_refresh_va)
    ImageView ivRefreshVa;
    @BindView(R.id.btn_query)
    TextView btnQuery;

    @Override
    public int getLayoutId() {
        return R.layout.activity_fill_in_info;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void loadData() {
        mTitle.setTitle(true, "填写信息");
        getImgVerification();
    }

    @OnClick({R.id.iv_refresh_va, R.id.btn_query})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_refresh_va://重新发送验证码
//                loadImg();
                getImgVerification();
                break;
            case R.id.btn_query://立即查询
                String realName = etRealName.getText().toString();
                String cardNum = etCardNumber.getText().toString();
                String phoneNum = etPhoneNum.getText().toString();
                String verification = etVerification.getText().toString();
                if (TextUtils.isEmpty(realName)) {
                    Util.showToast(this, "姓名不能为空!");
                    return;
                }
                if (TextUtils.isEmpty(cardNum)) {
                    Util.showToast(this, "身份证号不能为空!");
                    return;
                }
                if (TextUtils.isEmpty(phoneNum)) {
                    Util.showToast(this, "手机号不能为空!");
                    return;
                }
                if (TextUtils.isEmpty(verification)) {
                    Util.showToast(this, "验证码不能为空!");
                    return;
                }
                requestData(realName, cardNum, phoneNum, verification);

                break;
        }
    }

    //提交
    private void requestData(String realName, String cardNum, String phoneNum, String verification) {
        String key = SpUtil.getString(Constant.CAPTCHA_KEY);
        Call<JsonObject> call = RetrofitUtil.create().commitQuery(realName, cardNum, phoneNum, verification, key, "android");
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                String string = response.body().toString();
                try {
                    JSONObject object = new JSONObject(string);
                    String error = object.optString("error");
                    if (error.equals("N")) {
                        Gson gson = new Gson();
                        QueryResultBean.QueryDetailBean queryDetailBean = gson.fromJson(string,  QueryResultBean.QueryDetailBean.class);
                        if (null != queryDetailBean) {
                            Bundle bundle = new Bundle();
                            int totalScore = calculateTotalScore(queryDetailBean);
                            queryDetailBean.message.final_score = totalScore+"";//改变接口中的总分
                            saveDate(totalScore+"",queryDetailBean);
                            bundle.putSerializable("queryDetailBean", queryDetailBean);
                            startActivity(QueryResultActivity.class, bundle);
                        }
                    } else {
                        String message = object.optString("message");
                        Util.showToast(FillInInfoAvtivity.this, message);
                        getImgVerification();//提交后更换验证码
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.toString();
            }
        });
    }

    //    计算总分
    private int calculateTotalScore( QueryResultBean.QueryDetailBean queryDetailBean) {
        int resultScore = 750;//总分750
        List<QueryResultBean.QueryDetailBean.MessageBean.RiskItemBean> riskItems = queryDetailBean.message.risk_item;
        for (int i = 0; i < riskItems.size(); i++) {
            if (i == 10) {//第10项以后不再扣除
                return resultScore;
            }
            int item = Integer.parseInt(riskItems.get(i).score);
            resultScore -= item;//扣除风险项扣分
        }
        return resultScore;
    }


    //保存查询时间
    private void saveDate(String result, QueryResultBean.QueryDetailBean queryDetailBean) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH时mm分ss秒");
        String currentDate = dateFormat.format(date);//当前日期
        String currentTime = timeFormat.format(date);//当前时间

        QueryResultBean queryResultBean = new QueryResultBean();
        queryResultBean.setTime(currentTime);
        queryResultBean.setScore(result);
        queryResultBean.setQueryDetailBean(queryDetailBean);

        Map<String, List<QueryResultBean>> map = null;
        Gson gson = new Gson();
        String json = SpUtil.getString(Constant.QUERY_DATE_MAP);
        if (!TextUtils.isEmpty(json)) {//之前存储过map
            map = gson.fromJson(json, new TypeToken<Map<String, List<QueryResultBean>>>() {
            }.getType());
            if (map == null) return;

            List<QueryResultBean> timeList = map.get(currentDate);
            if (timeList == null) {//没有这天存储记录
                timeList = new ArrayList<>();
                timeList.add(0,queryResultBean);//在最前面插入
                map.put(currentDate, timeList);
            } else {
                timeList.add(0,queryResultBean);
            }
        } else {//没有存储过
            map = new HashMap<>();
            List<QueryResultBean> timeList = new ArrayList<>();
            timeList.add(0,queryResultBean);
            map.put(currentDate, timeList);
        }
        SpUtil.putString(Constant.QUERY_DATE_MAP, gson.toJson(map));
    }

    /**
     * 获取图形验证码
     */
    public void getImgVerification() {
//        HttpManager.getApi().getImgVerification()
        RetrofitUtil.create().getImgVerification()
                .subscribeOn(Schedulers.newThread())//在新线程中实现该方法
                .map(new Func1<ResponseBody, Bitmap>() {
                    @Override
                    public Bitmap call(ResponseBody responseBody) {
                        byte[] bys = new byte[0];
                        try {
                            bys = responseBody.bytes(); //注意：把byte[]转换为bitmap时，也是耗时操作，也必须在子线程
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bys, 0, bys.length);
                        return bitmap;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Bitmap>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                        ivVerification.setImageBitmap(bitmap);
                    }
                });
    }
}
