package com.keltapps.testkitcard.views.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.keltapps.testkitcard.R;
import com.keltapps.testkitcard.fragments.BikePointsListFragment;
import com.keltapps.testkitcard.fragments.BikePointsMapFragment;


public class MainTabsAdapter extends FragmentPagerAdapter {
    private static final int NUMBER_TABS = 2;
    public static final int TAB_LIST = 0;
    public static final int TAB_MAP = 1;
    private final Context context;

    public MainTabsAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case TAB_LIST:

                return new BikePointsListFragment();
            case TAB_MAP:
                return new BikePointsMapFragment();
            default:
                return new BikePointsListFragment();
        }
    }

    @Override
    public int getCount() {
        return NUMBER_TABS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case TAB_LIST:
                return context.getResources().getString(R.string.tab_list);
            case TAB_MAP:
                return context.getResources().getString(R.string.tab_map);
            default:
                return null;
        }
    }



}