package com.keltapps.testkitcard;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.keltapps.testkitcard.fragments.BikePointsListFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        BikePointsListFragment bikePointsListFragment = new BikePointsListFragment();
        fragmentTransaction.add(R.id.mainActivity_fragment_container, bikePointsListFragment, getString(R.string.fragment_bikePointsListFragment));
        fragmentTransaction.commit();
    }
}
