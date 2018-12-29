package com.qixiu.lejia.core;

import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.qixiu.lejia.R;
import com.qixiu.lejia.core.home.HomeFragment;
import com.qixiu.lejia.core.me.MeFragment;
import com.qixiu.lejia.core.service.LifeServicesFragment;

/**
 * Created by Long on 2018/4/20
 * <pre>
 *     首页fragment切换导航
 * </pre>
 */
final class MainNavigator {

    private static final String TAG_HOME = "HOME";
    private static final String TAG_SERVICES = "SERVICES";
    private static final String TAG_ME = "ME";


    private Fragment mHomeFrag;
    private Fragment mServicesFrag;
    private Fragment mMeFrag;

    private final FragmentManager fm;

    MainNavigator(FragmentManager fm) {
        this.fm = fm;
    }

    void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mHomeFrag = fm.findFragmentByTag(TAG_HOME);
            mServicesFrag = fm.findFragmentByTag(TAG_SERVICES);
            mMeFrag = fm.findFragmentByTag(TAG_ME);
        }
        //显示首页
        show(0);
    }

    void onDestroy() {
        mHomeFrag = null;
        mServicesFrag = null;
        mMeFrag = null;
    }

    void show(@IntRange(from = 0) int position) {
        FragmentTransaction ft = fm.beginTransaction();
        //防止出现重影，先隐藏所有fragment
        hideAllFragments(ft);

        final int container = R.id.container;
        switch (position) {
            case 0: //home
                if (mHomeFrag == null) {
                    mHomeFrag = HomeFragment.newInstance();
                    ft.add(container, mHomeFrag, TAG_HOME);
                }
                ft.show(mHomeFrag);
                break;
            case 1: //services
                if (mServicesFrag == null) {
                    mServicesFrag = LifeServicesFragment.newInstance();
                    ft.add(container, mServicesFrag, TAG_SERVICES);
                }
                ft.show(mServicesFrag);
                break;
            case 2: //me
                if (mMeFrag == null) {
                    mMeFrag = MeFragment.newInstance();
                    ft.add(container, mMeFrag, TAG_ME);
                }
                ft.show(mMeFrag);
                break;
        }

        //提交
        ft.commitAllowingStateLoss();
    }

    /**
     * 隐藏所有Fragments
     *
     * @param ft FragmentTransaction
     */
    private void hideAllFragments(FragmentTransaction ft) {
        if (mHomeFrag != null) {
            ft.hide(mHomeFrag);
        }
        if (mServicesFrag != null) {
            ft.hide(mServicesFrag);
        }
        if (mMeFrag != null) {
            ft.hide(mMeFrag);
        }
    }

}
