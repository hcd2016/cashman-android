package com.credit.pretend.activity;

import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.credit.pretend.fragment.PtdHomeFragment;
import com.credit.pretend.fragment.PtdInfoFragment;
import com.credit.pretend.fragment.PtdMineFragment;
import com.credit.xiaowei.R;
import com.credit.xiaowei.base.BaseActivity;
import com.credit.xiaowei.util.ToastUtil;
import com.credit.xiaowei.widget.MyRadioButton;

import butterknife.BindView;

/**
 * 伪首页,你懂了.
 */
public class PretendMainActivity extends BaseActivity {
    @BindView(R.id.container)
    FrameLayout container;
    @BindView(R.id.group)
    RadioGroup group;
    @BindView(R.id.home_tab)
    MyRadioButton homeTab;
    @BindView(R.id.info_tab)
    MyRadioButton infoTab;
    @BindView(R.id.mine_tab)
    MyRadioButton mineTab;
    PtdHomeFragment ptdHomeFragment = null;
    PtdInfoFragment ptdInfoFragment = null;
    PtdMineFragment ptdMineFragment = null;

    @Override
    public int getLayoutId() {
        return R.layout.mainactivity_pretend;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void loadData() {
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
                switch (i) {
                    case R.id.home_tab:
                        ptdHomeFragment = (PtdHomeFragment) getSupportFragmentManager().findFragmentByTag("home_tag");//获取当前点击的fragment
                        if (null == ptdHomeFragment) {//不存在,添加
                            ptdHomeFragment = new PtdHomeFragment();
                            beginTransaction.add(R.id.container, ptdHomeFragment, "home_tag");
                        }
                        hideAll(beginTransaction);//隐藏所有fragment
                        beginTransaction.show(ptdHomeFragment);//显示当前fragment
                        beginTransaction.commitAllowingStateLoss();//提交事务
                        break;
                    case R.id.info_tab:
                        ptdInfoFragment = (PtdInfoFragment) getSupportFragmentManager().findFragmentByTag("info_tag");
                        if (null == ptdInfoFragment) {
                            ptdInfoFragment = new PtdInfoFragment();
                            beginTransaction.add(R.id.container, ptdInfoFragment, "info_tag");
                        }
                        hideAll(beginTransaction);
                        beginTransaction.show(ptdInfoFragment);
                        beginTransaction.commitAllowingStateLoss();
                        break;
                    case R.id.mine_tab:
                        ptdMineFragment = (PtdMineFragment) getSupportFragmentManager().findFragmentByTag("mine_tag");
                        if (null == ptdMineFragment) {
                            ptdMineFragment = new PtdMineFragment();
                            beginTransaction.add(R.id.container, ptdMineFragment, "mine_tag");
                        }
                        hideAll(beginTransaction);
                        beginTransaction.show(ptdMineFragment);
                        beginTransaction.commitAllowingStateLoss();
                        break;
                }
            }
        });

        //第一次选中,保证第一次会回调OnCheckedChangeListener
        homeTab.setChecked(true);
    }

    //隐藏所有
    public void hideAll(FragmentTransaction beginTransaction) {
        if (null != ptdHomeFragment) {
            beginTransaction.hide(ptdHomeFragment);
        }
        if (null != ptdInfoFragment) {
            beginTransaction.hide(ptdInfoFragment);
        }
        if (null != ptdMineFragment) {
            beginTransaction.hide(ptdMineFragment);
        }
    }

    private long exitTime = 0;
    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ToastUtil.showToast("再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }
}
