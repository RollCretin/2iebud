package com.hxjf.dubei.factory;

import android.support.v4.app.Fragment;

import com.hxjf.dubei.R;
import com.hxjf.dubei.ui.fragment.BookshelfFragment;
import com.hxjf.dubei.ui.fragment.DiscoveryFragment;
import com.hxjf.dubei.ui.fragment.HomeFragment;
import com.hxjf.dubei.ui.fragment.MeFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chen_Zhang on 2017/5/19.
 * 单例创建Fragment
 */

public class FragmentFactory {

    private static FragmentFactory sFragmentFactory;
    List<Fragment> fragmentList;

    private FragmentFactory() {
        fragmentList = new ArrayList<>();
        HomeFragment homeFragment = new HomeFragment();
        BookshelfFragment bookshelfFragment = new BookshelfFragment();
        DiscoveryFragment discoveryFragment = new DiscoveryFragment();
        MeFragment meFragment = new MeFragment();
        fragmentList.add(homeFragment);
        fragmentList.add(bookshelfFragment);
        fragmentList.add(discoveryFragment);
        fragmentList.add(meFragment);
    }

    public static FragmentFactory getInstance() {
        if (sFragmentFactory == null) {
            synchronized (FragmentFactory.class) {
                if (sFragmentFactory == null) {
                    sFragmentFactory = new FragmentFactory();
                }
            }
        }
        return sFragmentFactory;
    }

    public Fragment getFragment(int position) {
        switch (position) {
            case R.id.tab_challenge:
                return fragmentList.get(0);
            case R.id.tab_bookshelf:
                return fragmentList.get(1);
            case R.id.tab_discovery:
                return fragmentList.get(2);
            case R.id.tab_me:
                return fragmentList.get(3);
        }
        return null;
    }

}
