package com.keltapps.testkitcard;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.keltapps.testkitcard.fragments.BikePointsListFragment;
import com.keltapps.testkitcard.fragments.BikePointsMapFragment;
import com.keltapps.testkitcard.network.VolleySingleton;
import com.keltapps.testkitcard.utils.FeedDatabase;
import com.keltapps.testkitcard.views.adapters.MainTabsAdapter;

public class MainActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener {

    private int viewPagerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpTabs();

    }


    private static String makeFragmentName(int viewPagerId, int index) {
        return "android:switcher:" + viewPagerId + ":" + index;
    }

    private void setUpTabs() {
        TabLayout tabs = (TabLayout) findViewById(R.id.activity_main_tab_Layout);

        ViewPager viewPager = (ViewPager) findViewById(R.id.activity_main_tab_layout_view_pager);
        viewPagerID = viewPager.getId();
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        PagerAdapter pagerAdapter = new MainTabsAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(pagerAdapter);
        tabs.setupWithViewPager(viewPager);
        tabs.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        tabs.setTabTextColors(ContextCompat.getColor(this, R.color.color_tabs_textNormal), ContextCompat.getColor(this, R.color.color_tabs_textSelected));
        TabLayout.Tab tab = tabs.getTabAt(MainTabsAdapter.TAB_LIST);
        if (tab != null)
            tab.select();
        VolleySingleton.getInstance(this).addToRequestQueue(new StringRequest(Request.Method.GET,
                VolleySingleton.URL_BIKE_POINT,
                this, this));


    }

    @Override
    public void onErrorResponse(VolleyError error) {
        BikePointsListFragment bikePointsListFragment = (BikePointsListFragment) getSupportFragmentManager().findFragmentByTag(makeFragmentName(viewPagerID, MainTabsAdapter.TAB_LIST));
        if (bikePointsListFragment != null)
            bikePointsListFragment.onMyErrorResponse();
        BikePointsMapFragment bikePointsMapFragment = (BikePointsMapFragment) getSupportFragmentManager().findFragmentByTag(makeFragmentName(viewPagerID, MainTabsAdapter.TAB_MAP));
        if (bikePointsMapFragment != null)
            bikePointsMapFragment.onMyErrorResponse();
    }

    @Override
    public void onResponse(String response) {
        FeedDatabase.getInstance(this).synchronizeBikePoints(response);
        BikePointsListFragment bikePointsListFragment = (BikePointsListFragment) getSupportFragmentManager().findFragmentByTag(makeFragmentName(viewPagerID, MainTabsAdapter.TAB_LIST));
        if (bikePointsListFragment != null)
            bikePointsListFragment.onMyResponse();
        BikePointsMapFragment bikePointsMapFragment = (BikePointsMapFragment) getSupportFragmentManager().findFragmentByTag(makeFragmentName(viewPagerID, MainTabsAdapter.TAB_MAP));
        if (bikePointsMapFragment != null)
            bikePointsMapFragment.onMyResponse();
    }

}
