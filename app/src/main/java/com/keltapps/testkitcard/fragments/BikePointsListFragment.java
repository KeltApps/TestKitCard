package com.keltapps.testkitcard.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.keltapps.testkitcard.R;
import com.keltapps.testkitcard.network.VolleySingleton;
import com.keltapps.testkitcard.utils.BikePointProvider;
import com.keltapps.testkitcard.utils.FeedDatabase;
import com.keltapps.testkitcard.utils.ScriptDatabase;
import com.keltapps.testkitcard.views.adapters.BikePointCursorAdapter;


public class BikePointsListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, Response.Listener<String>, Response.ErrorListener {
    private BikePointCursorAdapter bikePointCursorAdapter;
    private SwipeRefreshLayout swipeRefreshLayout = null;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bike_points_list, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.fragment_bikePointList_swipeRefresh);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(),R.color.colorAccent));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestBikePoints();
            }
        });

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_bikePointList_recyclerView);
        bikePointCursorAdapter = new BikePointCursorAdapter(getActivity());
        recyclerView.setAdapter(bikePointCursorAdapter);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        getLoaderManager().initLoader(0, null, this);
        requestBikePoints();
        return rootView;
    }

    private void requestBikePoints() {
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(new StringRequest(Request.Method.GET,
                VolleySingleton.URL_BIKE_POINT,
                BikePointsListFragment.this, BikePointsListFragment.this));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bikePointCursorAdapter = null;
        swipeRefreshLayout = null;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        getLoaderManager().initLoader(1, null, this);
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onResponse(String response) {
        FeedDatabase.getInstance(getActivity()).synchronizeBikePoints(response);
        getLoaderManager().initLoader(1, null, this);
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), BikePointProvider.CONTENT_URI, null, null, null,
                ScriptDatabase.ColumnBikePoint.COMMON_NAME + " ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        bikePointCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (bikePointCursorAdapter != null)
            bikePointCursorAdapter.changeCursor(null);
    }
}
