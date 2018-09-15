package com.innext.pretend.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.innext.pretend.bean.QueryResultBean;
import com.innext.pretend.ptd_util.RetrofitUtil;
import com.innext.xjx.R;
import com.innext.xjx.base.BaseActivity;
import com.innext.xjx.config.Constant;
import com.innext.xjx.util.SpUtil;
import com.innext.xjx.widget.DrawableCenterTextView;
import com.megvii.idcardlib.util.Util;

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
                //todo
                saveDate("700分");
                startActivity(QueryResultActivity.class);
                break;
        }
    }

    //保存查询时间
    private void saveDate(String result) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH时mm分ss秒");
        String currentDate = dateFormat.format(date);//当前日期
        String currentTime = timeFormat.format(date);//当前时间

        QueryResultBean queryResultBean = new QueryResultBean();
        queryResultBean.setTime(currentTime);
        queryResultBean.setScore(result);

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
                timeList.add(queryResultBean);
                map.put(currentDate, timeList);
            } else {
                timeList.add(queryResultBean);
            }
        } else {//没有存储过
            map = new HashMap<>();
            List<QueryResultBean> timeList = new ArrayList<>();
            timeList.add(queryResultBean);
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
                        Util.showToast(FillInInfoAvtivity.this, SpUtil.getString("RCaptchaKey"));
                    }
                });
    }
}
