package com.innext.pretend.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

/**
 * fragment选择管理(ptd)
 * xiejingwen
 */
public class PtdFragmentFactory {

    private static List<Fragment> list;
    private static Fragment lastFragment;
    private static FragmentManager manager;

    public enum FragmentStatus {
        None,
        HOME_TAB,
        INFO_TAB,
        MINE_TAB
    }


    public static void clear() {
        if (list != null)
            list.clear();
        if (manager != null) {
            int count = manager.getBackStackEntryCount();
            while (count >= 0) {
                manager.popBackStackImmediate();
                count--;
            }
        }
        manager = null;
    }

    public static void changeFragment(FragmentManager manager, FragmentStatus status, int id) {
        PtdFragmentFactory.manager = manager;
        FragmentTransaction transaction = manager.beginTransaction();
        if (list == null)
            list = new ArrayList<Fragment>();
        Fragment selectFragment = null;
        switch (status) {
            case None:
                return;
            case HOME_TAB:
                selectFragment = PtdHomeFragment.getInstance();
                break;
            case INFO_TAB:
                selectFragment = PtdInfoFragment.getInstance();
                break;
            case MINE_TAB:
                selectFragment = PtdMineFragment.getInstance();
                break;
            default:
                break;
        }

        //change
        if (list.contains(selectFragment)) {
            transaction.hide(lastFragment).show(selectFragment).commitAllowingStateLoss();
        } else {
            if (list.size() == 0)
                transaction.add(id, selectFragment).commitAllowingStateLoss();
            else
                transaction.hide(lastFragment).add(id, selectFragment).commitAllowingStateLoss();
            list.add(selectFragment);
        }
        lastFragment = selectFragment;
    }

}
