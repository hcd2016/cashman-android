package com.innext.pretend.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.innext.pretend.fragment.PtdFragmentFactory;
import com.innext.xjx.R;
import com.innext.xjx.base.BaseActivity;
import com.innext.xjx.util.ToastUtil;
import com.innext.xjx.widget.MyRadioButton;

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
    private PtdFragmentFactory.FragmentStatus toTabIndex = PtdFragmentFactory.FragmentStatus.None;

    @Override
    public int getLayoutId() {
        return R.layout.mainactivity_pretend;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void loadData() {
        group.setOnCheckedChangeListener(changeListener);
        group.check(getCheckIdByStatus(PtdFragmentFactory.FragmentStatus.HOME_TAB));
    }

    RadioGroup.OnCheckedChangeListener changeListener = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.home_tab:
                    toTabIndex = PtdFragmentFactory.FragmentStatus.HOME_TAB;
                    changeTab(PtdFragmentFactory.FragmentStatus.HOME_TAB);
                    break;
                case R.id.info_tab:
                    toTabIndex = PtdFragmentFactory.FragmentStatus.INFO_TAB;
                    changeTab(PtdFragmentFactory.FragmentStatus.INFO_TAB);
                    break;
                case R.id.mine_tab:
                    toTabIndex = PtdFragmentFactory.FragmentStatus.MINE_TAB;
                    changeTab(PtdFragmentFactory.FragmentStatus.MINE_TAB);
                    break;
                default:
                    break;

            }
        }
    };

    /***********
     * 获取所选状态的checkId
     *
     * @return
     */
    public int getCheckIdByStatus(PtdFragmentFactory.FragmentStatus status) {
        int id = R.id.home_tab;
        switch (status) {
            case HOME_TAB:
                id = R.id.home_tab;
                break;
            case INFO_TAB:
                id = R.id.info_tab;
                break;
            case MINE_TAB:
                id = R.id.mine_tab;
                break;
            default:
                break;
        }
        return id;
    }

    /***********
     * 切换导航栏
     */
    private PtdFragmentFactory.FragmentStatus oldState = PtdFragmentFactory.FragmentStatus.None;

    public void changeTab(PtdFragmentFactory.FragmentStatus status) {
        if (status == oldState) return;
        PtdFragmentFactory.changeFragment(getSupportFragmentManager(), status, R.id.container);
        oldState = status;
    }

    @Override
    protected void onResume() {
        super.onResume();
        group.setOnCheckedChangeListener(changeListener);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }
}
