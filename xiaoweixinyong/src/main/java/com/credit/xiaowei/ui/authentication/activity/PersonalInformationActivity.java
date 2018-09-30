package com.credit.xiaowei.ui.authentication.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.credit.xiaowei.R;
import com.credit.xiaowei.app.App;
import com.credit.xiaowei.base.BaseActivity;
import com.credit.xiaowei.base.PermissionsListener;
import com.credit.xiaowei.config.Constant;
import com.credit.xiaowei.dialog.ActionSheetDialog;
import com.credit.xiaowei.dialog.AlertFragmentDialog;
import com.credit.xiaowei.dialog.PickerViewFragmentDialog;
import com.credit.xiaowei.events.AuthenticationRefreshEvent;
import com.credit.xiaowei.events.FragmentRefreshEvent;
import com.credit.xiaowei.events.UIBaseEvent;
import com.credit.xiaowei.ui.authentication.bean.EnterTimeAndSalaryBean;
import com.credit.xiaowei.ui.authentication.bean.FaceResultBean;
import com.credit.xiaowei.ui.authentication.bean.IdCardResultBean;
import com.credit.xiaowei.ui.authentication.bean.ImageDataBean;
import com.credit.xiaowei.ui.authentication.bean.PersonalInformationBean;
import com.credit.xiaowei.ui.authentication.bean.PersonalInformationRequestBean;
import com.credit.xiaowei.ui.authentication.contract.PersonalInformationContract;
import com.credit.xiaowei.ui.authentication.presenter.PersonalInformationPresenter;
import com.credit.xiaowei.util.ConvertUtil;
import com.credit.xiaowei.util.LogUtils;
import com.credit.xiaowei.util.SpUtil;
import com.credit.xiaowei.util.StatusViewUtil;
import com.credit.xiaowei.util.StringUtil;
import com.credit.xiaowei.util.ToastUtil;
import com.credit.xiaowei.util.Tool;
import com.credit.xiaowei.widget.ClearEditText;
import com.credit.xiaowei.widget.loading.LoadingLayout;
import com.megvii.idcardlib.IDCardScanActivity;
import com.megvii.idcardquality.IDCardQualityLicenseManager;
import com.megvii.licensemanager.Manager;
import com.megvii.livenessdetection.LivenessLicenseManager;
import com.megvii.livenesslib.LivenessActivity;
import com.megvii.livenesslib.util.ConUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * 个人信息
 */
public class PersonalInformationActivity extends BaseActivity<PersonalInformationPresenter> implements PersonalInformationContract.View {
    @BindView(R.id.loading_layout)
    LoadingLayout mLoadingLayout;
    @BindView(R.id.tv_tag)
    TextView mTvTag;
    @BindView(R.id.iv_face_img) //人脸识别
    ImageView mIvFaceImg;
    @BindView(R.id.layout_face_pic)
    LinearLayout mLayoutFacePic;
    @BindView(R.id.face_recognition)
    LinearLayout mFaceRecognition;
    @BindView(R.id.iv_idcard_facade)//身份证正面
    ImageView mIvIdcardFacade;
    @BindView(R.id.iv_idcard_contrary)//身份证反面
    ImageView mIvIdcardContrary;
    @BindView(R.id.layout_idcard_pic)
    LinearLayout mLayoutIdcardPic;
    @BindView(R.id.et_card_name)//姓名
    ClearEditText mEtCardName;
    @BindView(R.id.et_card_number)
    ClearEditText mEtCardNumber;//银行卡名称
    @BindView(R.id.tv_degree)
    TextView mTvDegree;//学历
    @BindView(R.id.layout_choose_degree)
    LinearLayout mLayoutChooseDegree;
    @BindView(R.id.tv_home_area)
    TextView mTvHomeArea;//选择居住地址
    @BindView(R.id.layout_choose_home_area)
    LinearLayout mLayoutChooseHomeArea;
    @BindView(R.id.et_home_address)
    ClearEditText mEtHomeAddress;//街道和门牌号
    @BindView(R.id.tv_marriage)
    TextView mTvMarriage;//婚姻
    @BindView(R.id.layout_choose_marriage)
    LinearLayout mLayoutChooseMarriage;
    @BindView(R.id.tv_live_time)
    TextView mTvLiveTime;//居住时间
    @BindView(R.id.layout_choose_live_time)
    LinearLayout mLayoutChooseLiveTime;


    private PersonalInformationBean mBean;
    //认证状态
    private int realVerifyStatus;


    //学历id   居住时间id 婚姻状况id
    private String degree, live_time, marriage;
    private int degree_pos, live_time_pos, marriage_pos;
    private final int TYPE_DEGREE = 1; //学历
    private final int TYPE_LIVE_TIME = 2;   //居住时长
    private final int TYPE_MARRIAGE = 3;    //婚姻状态
    // 学历列表 居住列表 婚姻列表
    private List<EnterTimeAndSalaryBean> degree_list, live_time_list, marriage_list;


    // 人脸识别头像
    private String facePic, IdCardFrontPic, IdCardBackPic;
    public static final int KEY_UPLOAD_FACE = 10;//人脸识别
    public static final int KEY_UPLOAD_IDCRAD_FRONT = 11;//身份证正面
    public static final int KEY_UPLOAD_IDCRAD_BACK = 12;//身份证反面


    //上传图片类型
    private int imageType;
    //上传图片的ImageView
    private ImageView mImageView;

    // face++
    private static final int HANDLER_WHAT_IDCARD_FAONT = 1;
    private static final int HANDLER_WHAT_IDCARD_BACK = 2;
    private static final int HANDLER_WHAT_IDCARD_FACE = 3;
    private static final int HANDLER_WHAT_IDCARD_NET_ERR = 4;
    private static final int HANDLER_WHAT_IDCARD_OCR_ERR = 5;
    //对应的code
    private static final int PAGE_INTO_LIVENESS = 100;
    private static final int INTO_IDCARDSCAN_PAGE = 101;
    private static final int INTO_IDCARDSCAN_PAGE_BACK = 102;

    //照相文件
    private File newFile;

    //地理位置
    private PoiItem poiItem;

    private boolean isChange=false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_personal_information;
    }

    @Override
    public void initPresenter() {
        mPresenter.init(this);
    }

    @Override
    public void loadData() {
        mActivity = PersonalInformationActivity.this;
        initView();
        setData();
    }

    private void initView() {
        mTitle.setTitle(true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        },R.string.personal_information);
        mTitle.setRightTitle("保存", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    save();
                }
            });
    }
    @OnClick({R.id.iv_face_img, R.id.iv_idcard_facade, R.id.iv_idcard_contrary, R.id.layout_choose_home_area, R.id.layout_choose_live_time, R.id.layout_choose_degree, R.id.layout_choose_marriage})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_face_img:
                imageAction(KEY_UPLOAD_FACE);
                break;
            case R.id.iv_idcard_facade:
                imageAction(KEY_UPLOAD_IDCRAD_FRONT);
                break;
            case R.id.iv_idcard_contrary:
                imageAction(KEY_UPLOAD_IDCRAD_BACK);
                break;
            //高德地图
            case R.id.layout_choose_home_area:
                Intent intent = new Intent(mActivity, GDMapActivity.class);
                startActivityForResult(intent, GDMapActivity.GET_POI_REQUEST_CODE);
                break;
            //选择居住时间
            case R.id.layout_choose_live_time:
                if (live_time_list!=null){
                    selectDialog(TYPE_LIVE_TIME, live_time_pos, live_time_list, mTvLiveTime);
                }else{
                    mPresenter.getPersonalInformation();
                }
                break;
            //选择学历
            case R.id.layout_choose_degree:
                if (degree_list!=null){
                    selectDialog(TYPE_DEGREE, degree_pos, degree_list, mTvDegree);
                }else{
                    mPresenter.getPersonalInformation();
                }
                break;
            //婚姻状况
            case R.id.layout_choose_marriage:
                if (marriage_list!=null){
                    selectDialog(TYPE_MARRIAGE, marriage_pos, marriage_list, mTvMarriage);
                }else{
                    mPresenter.getPersonalInformation();
                }
                break;
        }
    }

    //选择dailog
    private void selectDialog(final int selectType, final int oldPosition, final List<EnterTimeAndSalaryBean> typeList, final TextView textView) {
        ArrayList<String> items = new ArrayList<>();
        for (int i = 0; i < typeList.size(); i++){
            items.add(typeList.get(i).getName());
        }
        new PickerViewFragmentDialog(oldPosition, items, new PickerViewFragmentDialog.OnValueSelectListener() {
            @Override
            public void select(String value, int position) {
                /*if (oldPosition == position) {
                    textView.setText(value);//return之前要把值给上，要么就不做这个position判断
                    return;
                }*/
                isChange = true;
                switch (selectType) {
                    case TYPE_DEGREE:
                        degree = typeList.get(position).getDegrees();
                        degree_pos = position;
                        break;
                    case TYPE_LIVE_TIME:
                        live_time = typeList.get(position).getLive_time_type();
                        live_time_pos = position;
                        break;
                    case TYPE_MARRIAGE:
                        marriage = typeList.get(position).getMarriage();
                        marriage_pos = position;
                        break;
                }
                textView.setText(value);
            }
        }).show(getSupportFragmentManager(), PickerViewFragmentDialog.TAG);
    }

    //设置数据
    private void setData() {
        //获取个人信息请求数据
        mPresenter.getPersonalInformation();
    }

    /**
     * 个人信息保存
     **/
    private void save() {
        if (check()) {
            //如果已经实名认证过，不能提交姓名和身份证
            HashMap<String, String> map = new HashMap<>();
            //现居住地址
            map.put("address_distinct", mTvHomeArea.getText().toString());
            //地址区域
            map.put("address", mEtHomeAddress.getText().toString());
            // 居住时长
            map.put("live_time_type", live_time);
            //个人学历
            map.put("degrees", degree);
            //结婚情况
            map.put("marriage", marriage);
            if (poiItem != null && poiItem.getLatLonPoint() != null) {
                //上传坐标
                map.put("latitude", String.valueOf(poiItem.getLatLonPoint().getLatitude()));
                map.put("longitude", String.valueOf(poiItem.getLatLonPoint().getLongitude()));
            }
            if (mBean.getReal_verify_status() != 1) {
                map.put("name", mEtCardName.getText().toString());
                map.put("id_number", mEtCardNumber.getText().toString());
            }
            //如果当期的认证状态为空 会有问题
            mPresenter.getSaveInformation(mBean.getReal_verify_status(), map);
        }
    }

    private boolean check() {
        if (TextUtils.isEmpty(facePic)) {
            ToastUtil.showToast("请上传人脸识别图片");
        } else if (TextUtils.isEmpty(IdCardFrontPic)) {
            ToastUtil.showToast("请上传正面身份证照");
        } else if (TextUtils.isEmpty(IdCardBackPic)) {
            ToastUtil.showToast("请上传反面身份证照");
        } else if (StringUtil.isBlankEdit(mEtCardName)) {
            ToastUtil.showToast("请输入真实姓名");
        } else if (StringUtil.isBlankEdit(mEtCardNumber)) {
            ToastUtil.showToast("请输入身份证号");
        }else if (StringUtil.isBlankEdit(mTvDegree)) {
            ToastUtil.showToast("请选择学历");
        }else if(StringUtil.isBlankEdit(mTvHomeArea)) {
            ToastUtil.showToast("请选择现居地址");
        }else if (StringUtil.isBlankEdit(mEtHomeAddress)) {
            ToastUtil.showToast("请输入详细地址");
        }else if (!isChange){
            ToastUtil.showToast("请修改内容后再保存");
        }else{
            return true;
        }
        return false;
    }
    private String imgUrl = "";//当前点击的图片url
    //拍照选择
    private void imageAction(final int type) {
        String str = null; //展示的文字
        boolean isShowImage = false;//是否显示查看大图选项
        if (type == KEY_UPLOAD_FACE) {
            str = "拍摄手持身份证照片";
            mImageView = mIvFaceImg;
            if (!TextUtils.isEmpty(facePic)) {
                isShowImage = true;
                imgUrl = facePic;
            }
        } else if (type == KEY_UPLOAD_IDCRAD_FRONT) {
            str = "拍摄身份证正面照片";
            mImageView = mIvIdcardFacade;
            if (!TextUtils.isEmpty(IdCardFrontPic)) {
                isShowImage = true;
                imgUrl = IdCardFrontPic;
            }
        } else if (type == KEY_UPLOAD_IDCRAD_BACK) {
            str = "拍摄身份证反面照片";
            mImageView = mIvIdcardContrary;
            if (!TextUtils.isEmpty(IdCardBackPic)) {
                isShowImage = true;
                imgUrl = IdCardBackPic;
            }
        }
        ActionSheetDialog dialog = new ActionSheetDialog(mActivity).builder();
        if (isShowImage) {
            dialog.addSheetItem("查看大图", ActionSheetDialog.SheetItemColor.Red, new ActionSheetDialog.OnSheetItemClickListener() {
                @Override
                public void onClick(int which) {
                    ShowSinglePictureActivity.startAction(mContext, mImageView,imgUrl);
                }
            });
        }
        if (realVerifyStatus != 1) {//是否实名认证
            dialog.setCancelable(true)
                    .setCanceledOnTouchOutside(false)
                    .addSheetItem(type == KEY_UPLOAD_FACE ? "智能识别（优先）" : "智能扫描（优先）",
                            ActionSheetDialog.SheetItemColor.Red,
                            new ActionSheetDialog.OnSheetItemClickListener() {
                                @Override
                                public void onClick(int which) {
                                    int faceType = 0;
                                    if (type == KEY_UPLOAD_FACE) {
                                        faceType = HANDLER_WHAT_IDCARD_FACE;
                                    } else if (type == KEY_UPLOAD_IDCRAD_FRONT) {
                                        faceType = HANDLER_WHAT_IDCARD_FAONT;
                                    } else if (type == KEY_UPLOAD_IDCRAD_BACK) {
                                        faceType = HANDLER_WHAT_IDCARD_BACK;
                                    }
                                    toFace(faceType);
                                }
                            }).addSheetItem(str, ActionSheetDialog.SheetItemColor.Red,
                    new ActionSheetDialog.OnSheetItemClickListener() {
                        @Override
                        public void onClick(int which) {
                            // 拍照
                            camera(type);
                        }
                    });
        }
        dialog.show();
    }
    /**
     * 跳转face++
     */
    private void toFace(final int type) {
        requestPermissions(new String[]{Manifest.permission.CAMERA}, new PermissionsListener() {
            @Override
            public void onGranted() {
                final String uuid = ConUtil.getUUIDString(mActivity);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Manager manager = new Manager(mActivity);
                        if (type == HANDLER_WHAT_IDCARD_FACE) {
                            LivenessLicenseManager licenseManager = new LivenessLicenseManager(
                                    mActivity);
                            manager.registerLicenseManager(licenseManager);
                            manager.takeLicenseFromNetwork(uuid);
                            if (licenseManager.checkCachedLicense() > 0)
                                mHandler.sendEmptyMessage(HANDLER_WHAT_IDCARD_FACE);
                            else
                                mHandler.sendEmptyMessage(HANDLER_WHAT_IDCARD_NET_ERR);
                        } else if (type == HANDLER_WHAT_IDCARD_FAONT) {
                            IDCardQualityLicenseManager idCardLicenseManager = new IDCardQualityLicenseManager(mActivity);
                            manager.registerLicenseManager(idCardLicenseManager);
                            manager.takeLicenseFromNetwork(uuid);
                            if (idCardLicenseManager.checkCachedLicense() > 0)
                                mHandler.sendEmptyMessage(HANDLER_WHAT_IDCARD_FAONT);
                            else
                                mHandler.sendEmptyMessage(HANDLER_WHAT_IDCARD_NET_ERR);
                        } else if (type == HANDLER_WHAT_IDCARD_BACK) {
                            IDCardQualityLicenseManager idCardLicenseManager = new IDCardQualityLicenseManager(mActivity);
                            manager.registerLicenseManager(idCardLicenseManager);
                            manager.takeLicenseFromNetwork(uuid);
                            if (idCardLicenseManager.checkCachedLicense() > 0)
                                mHandler.sendEmptyMessage(HANDLER_WHAT_IDCARD_BACK);
                            else
                                mHandler.sendEmptyMessage(HANDLER_WHAT_IDCARD_NET_ERR);
                        }
                    }
                }).start();
            }
            @Override
            public void onDenied(List<String> deniedPermissions, boolean isNeverAsk) {
                if (isNeverAsk) {
                    toAppSettings("相机权限已被禁止", false);
                }
            }
        });
    }

    /**
     * 照相的方法
     *
     * @param type
     */
    private void camera(final int type) {
        //判断sd是否存在
        if (Tool.checkSD(mActivity)) {
            // 照相 和 读取 内存权限
            requestPermissions(new String[]{Manifest.permission.CAMERA}, new PermissionsListener() {
                //授权
                @Override
                public void onGranted() {
                    //7.0 处理 重新构造Uri：content://
                    File imagePath = new File(Environment.getExternalStorageDirectory() + "/" + getApplicationInfo().packageName + "/image/");
                    if (!imagePath.exists()) {
                        imagePath.mkdirs();
                    }
                    newFile = new File(imagePath, System.currentTimeMillis() + ".jpg");
                    Uri contentUri = FileProvider.getUriForFile(mContext, "com.credit.xiaowei.provider.fileprovider", newFile);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
                    // 授予目录临时共享权限
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                            | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    startActivityForResult(intent, type);

                }
                //拒绝
                @Override
                public void onDenied(List<String> deniedPermissions, boolean isNeverAsk) {
                    if (isNeverAsk) {
                        toAppSettings("相机权限已被禁止", false);
                    }
                }
            });
        }
    }

    /**
     * 启动face++
     **/
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Intent intent = null;
            switch (msg.what) {
                case HANDLER_WHAT_IDCARD_FACE:
                    //活体检测
                    startActivityForResult(new Intent(mActivity, LivenessActivity.class), PAGE_INTO_LIVENESS);
                    break;
                case HANDLER_WHAT_IDCARD_FAONT:
                    //身份证正面
                    intent = new Intent(mActivity, IDCardScanActivity.class);
                    intent.putExtra("side", 0);
                    intent.putExtra("isvertical", false);
                    startActivityForResult(intent, INTO_IDCARDSCAN_PAGE);
                    break;
                case HANDLER_WHAT_IDCARD_BACK:
                    //身份证反面
                    intent = new Intent(mActivity, IDCardScanActivity.class);
                    intent.putExtra("side", 1);
                    intent.putExtra("isvertical", false);
                    startActivityForResult(intent, INTO_IDCARDSCAN_PAGE_BACK);
                    break;

                case HANDLER_WHAT_IDCARD_NET_ERR:
                    ToastUtil.showToast("联网授权失败，请检查网络！");
                    break;
                case HANDLER_WHAT_IDCARD_OCR_ERR:
                    Tool.setImage(mActivity, facePic, mIvFaceImg);
                    ToastUtil.showToast("身份证识别失败");
                    break;
            }
        }
    };

    @Override
    public void showLoading(String content) {
        if (mBean==null){
            mLoadingLayout.setStatus(LoadingLayout.Loading);
        }else {
            App.loadingContent(mActivity, content);
        }
    }

    //接口回调
    @Override
    public void PersonalInformationSccess(PersonalInformationRequestBean bean) {
        mLoadingLayout.setStatus(LoadingLayout.Success);
        if (bean != null && bean.getItem() != null) {
            StatusViewUtil.hidePopWin();
            mBean = bean.getItem();
            realVerifyStatus = mBean.getReal_verify_status();
            //认证状态为1的时候不可修改
            if (realVerifyStatus == 1) {
                mEtCardName.setFocusable(false);
                mEtCardNumber.setFocusable(false);
            }
            if (!StringUtil.isBlank(mBean.getName()) && !StringUtil.isBlank(mBean.getId_number())) {
                mEtCardName.setText(mBean.getName());
                mEtCardNumber.setText(mBean.getId_number());
                mEtCardNumber.setSelection(mEtCardNumber.length());
                if (TextUtils.isEmpty(SpUtil.getString(Constant.SHARE_TAG_REAL_NAME))) {
                    SpUtil.putString(Constant.SHARE_TAG_REAL_NAME, mBean.getName());
                }
            }

                if (!StringUtil.isBlank(mBean.getFace_recognition_picture())) {
                    facePic = mBean.getFace_recognition_picture();
                    Tool.setImage(mActivity, mBean.getFace_recognition_picture(), mIvFaceImg);
                }
                //设置身份证正面头像
                if (!StringUtil.isBlank(mBean.getId_number_z_picture())) {
                    IdCardFrontPic = mBean.getId_number_z_picture();
                    Tool.setImage(mActivity, IdCardFrontPic, mIvIdcardFacade);
                }
                //设置身份证反面头像
                if (!StringUtil.isBlank(mBean.getId_number_f_picture())) {
                    IdCardBackPic = mBean.getId_number_f_picture();
                    Tool.setImage(mActivity, IdCardBackPic, mIvIdcardContrary);
                }
            }
            //设置街道和门牌号
            if (!StringUtil.isBlank(mBean.getAddress_distinct())) {
                mEtHomeAddress.setText(mBean.getAddress());
                mEtHomeAddress.setSelection(mEtHomeAddress.length());
            }
            //设置居住地址
            if (!StringUtil.isBlank(mBean.getAddress())) {
                mTvHomeArea.setText(mBean.getAddress_distinct());
            }
            degree = mBean.getDegrees();
            live_time = mBean.getLive_period();

            marriage = mBean.getMarriage();
            degree_list = mBean.getDegrees_all();
             LogUtils.loge(degree_list.size()+"degree_list");
            live_time_list = mBean.getLive_time_type_all();
            marriage_list = mBean.getMarriage_all();
            for (EnterTimeAndSalaryBean timeBean : live_time_list) {
                if (timeBean.getLive_time_type().equals(live_time)) {
                    mTvLiveTime.setText(timeBean.getName());
                    live_time_pos = live_time_list.indexOf(timeBean);
                    break;
                }
            }
            for (EnterTimeAndSalaryBean degreeBean : degree_list) {
                if (degreeBean.getDegrees().equals(degree )) {
                    mTvDegree.setText(degreeBean.getName());
                    degree_pos = degree_list.indexOf(degreeBean);
                    break;
                }
            }
            for (EnterTimeAndSalaryBean marriageBean : marriage_list) {
                if (marriageBean.getMarriage().equals(marriage)) {
                    mTvMarriage.setText(marriageBean.getName());
                    marriage_pos = marriage_list.indexOf(marriageBean);
                    break;
                }
            }
            mEtHomeAddress.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}
                @Override
                public void afterTextChanged(Editable s) {
                    isChange = true;
                }
            });
    }

    //上传图片成功
    @Override
    public void UpLoadImageSccess(ImageDataBean imageDataBean,int type, File file, ImageView mImageView) {
        Tool.setImage(mActivity, file, mImageView);
        if (imageDataBean != null) {
            mEtCardName.setText(imageDataBean.getName());
            mEtCardNumber.setText(imageDataBean.getId_card_number());
            SpUtil.putString(Constant.SHARE_TAG_REAL_NAME, mBean.getName());
        }
        isChange = true;
        if (type == KEY_UPLOAD_FACE) {
            facePic = file.getAbsolutePath();
        } else if (type == KEY_UPLOAD_IDCRAD_FRONT) {
            IdCardFrontPic = file.getAbsolutePath();
        } else if (type == KEY_UPLOAD_IDCRAD_BACK) {
            IdCardBackPic = file.getAbsolutePath();
        }
    }
    //保存用户信息成功返回
    @Override
    public void SaveInformationSuccess() {
        //刷新实名认证（在添加银行卡的时候会用到）

        if (TextUtils.isEmpty(SpUtil.getString(Constant.SHARE_TAG_REAL_NAME))&&
                !TextUtils.isEmpty(mEtCardName.getText().toString())) {
            SpUtil.putString(Constant.SHARE_TAG_REAL_NAME,mEtCardName.getText().toString());
            EventBus.getDefault().post(new FragmentRefreshEvent(UIBaseEvent.EVENT_REALNAME_AUTHENTICATION_SUCCESS));
        }
        ToastUtil.showToast("保存成功");
        EventBus.getDefault().post(new AuthenticationRefreshEvent());
        finish();
    }

    @Override
    public void stopLoading() {
        App.hideLoading();
    }

    @Override
    public void showErrorMsg(String msg, String type) {
        ToastUtil.showToast(msg);
        if (type != null && type.equals(mPresenter.TYPE_GET_INFO)) {
            if ("网络不可用".equals(msg)){
                mLoadingLayout.setStatus(LoadingLayout.No_Network);
            }else{
                mLoadingLayout.setErrorText(msg)
                        .setStatus(LoadingLayout.Error);
            }
            mLoadingLayout.setOnReloadListener(new LoadingLayout.OnReloadListener() {
                @Override
                public void onReload(View v) {
                    mPresenter.getPersonalInformation();
                }
            });
        }
    }

    /***
     * 回调
     */
    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("这里是日志","输出日志信息---requestCode="+requestCode+"--resultCode="+resultCode+"---data="+data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (data != null) {
            if ((requestCode == PAGE_INTO_LIVENESS || requestCode == INTO_IDCARDSCAN_PAGE || requestCode == INTO_IDCARDSCAN_PAGE_BACK) && resultCode == RESULT_OK) {
                int code = 0;
                if (requestCode == PAGE_INTO_LIVENESS) {
                    FaceResultBean result = ConvertUtil.toObject(data.getStringExtra("result"), FaceResultBean.class);
                    if (result.getImgs() != null && result.getImgs().size() > 0) {
                        ToastUtil.showToast(result.getResult());
                        int resID = result.getResultcode();
                        if (resID == R.string.verify_success) {
                            // 上传 人脸识别图片
                            imageType =KEY_UPLOAD_FACE;
                            code = 0;
                            saveFile(result.getImgs().get(0), code, imageType);
                        }
                    } else {
                        ToastUtil.showToast("图片获取失败");
                    }
                } else if (requestCode == INTO_IDCARDSCAN_PAGE) {
                    IdCardResultBean result = ConvertUtil.toObject(data.getStringExtra("result"), IdCardResultBean.class);
                    if (result != null) {
                        ToastUtil.showToast(result.getResult());
                        //上传身份证正面照片
                        imageType = KEY_UPLOAD_IDCRAD_FRONT;
                        code = 1;
                        saveFile(result.getIdcardImg(), code, imageType);
                    } else {
                        ToastUtil.showToast("图片获取失败");
                    }
                } else if (requestCode == INTO_IDCARDSCAN_PAGE_BACK) {
                    IdCardResultBean result = ConvertUtil.toObject(data.getStringExtra("result"), IdCardResultBean.class);
                    if (result != null) {
                        ToastUtil.showToast(result.getResult());
                        //上传省份证反面照片
                        imageType = KEY_UPLOAD_IDCRAD_BACK;
                        code = 2;
                        saveFile(result.getIdcardImg(), code, imageType);
                    } else {
                        ToastUtil.showToast("图片获取失败");
                    }
                }
            }else if (requestCode == GDMapActivity.GET_POI_REQUEST_CODE) {//定位
                isChange = true;
                poiItem = data.getParcelableExtra("result");
                mTvHomeArea.setText(poiItem.getTitle() + " — (" + poiItem.getSnippet() + ")");
            }
        } else if (requestCode == KEY_UPLOAD_FACE || requestCode == KEY_UPLOAD_IDCRAD_FRONT || requestCode == KEY_UPLOAD_IDCRAD_BACK) {
            /***
             * 7.0 照相文件File
             *  上传照片类别 10 人脸 11 身份证正面 12 省份证 反面
             *  type 10,11,12
             * */
            if (requestCode == KEY_UPLOAD_FACE) {
                imageType = KEY_UPLOAD_FACE;
            } else if (requestCode == KEY_UPLOAD_IDCRAD_FRONT) {
                imageType = KEY_UPLOAD_IDCRAD_FRONT;
            } else if (requestCode == KEY_UPLOAD_IDCRAD_BACK) {
                imageType =KEY_UPLOAD_IDCRAD_BACK;;
            }
            if (newFile!=null&&!TextUtils.isEmpty(newFile.getPath())){
                String cameimage=newFile.getPath();
                saveFile(cameimage, requestCode, imageType);
            }else {
                ToastUtil.showToast("照片获取失败,请重试");
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (isChange) {
            new AlertFragmentDialog.Builder(mActivity)
                    .setContent("有修改未保存,确定退出？")
                    .setLeftBtnText("取消")
                    .setRightBtnText("退出")
                    .setRightCallBack(new AlertFragmentDialog.RightClickCallBack() {
                        @Override
                        public void dialogRightBtnClick() {
                            finish();
                        }
                    }).build();
        } else {
            super.onBackPressed();
        }

    }
    /***
     * 文件压缩上传
     * @param imagePath 图片地址
     * @param ocrtype  上传类型
     * @param  type 接口是上传类型
     * */
    private void saveFile(final String imagePath, final int ocrtype,final int type) {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                if (new File(imagePath).exists()) {//图片是否存在
                    //获取图片旋转度数
                    int degree = ConvertUtil.getBitmapDegree(imagePath);
                    //压缩图片
                    Bitmap bitmap = ConvertUtil.getCompressedBmp(imagePath);
                    //如果旋转度数大于0则进行校正
                    if (degree > 0){
                        bitmap = ConvertUtil.rotateBitmapByDegree(bitmap, degree);
                    }
                    //保存压缩、校正旋转之后的图片(覆盖掉所拍的图片)
                    ConvertUtil.saveBitmap(imagePath, bitmap);
                    if (bitmap != null){
                        bitmap.recycle();//释放内存
                    }
                    subscriber.onNext(imagePath);
                    subscriber.onCompleted();
                }else{
                    subscriber.onError(new IOException("图片保存失败"));
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        //网络请求上传文件
                        HashMap<String, Integer> map = new HashMap<>();
                        map.put("type", type);
                        map.put("ocrtype", ocrtype);
                        Log.e("输出当前日志信息","type====="+type+"----ocrtyp="+String.valueOf(ocrtype));
                        File file =new File(s);
                        mPresenter.getUpLoadImage(file, map, mImageView);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ToastUtil.showToast(throwable.getMessage());
                    }
                });
    }
}
