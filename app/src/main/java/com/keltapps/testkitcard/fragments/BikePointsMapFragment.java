package com.keltapps.testkitcard.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.keltapps.testkitcard.R;
import com.keltapps.testkitcard.utils.BikePointProvider;
import com.keltapps.testkitcard.utils.ScriptDatabase;


public class BikePointsMapFragment extends Fragment implements
        OnMapReadyCallback, LoaderManager.LoaderCallbacks<Cursor> {
    MapView mapView;
    private GoogleMap map; // Might be null if Google Play services APK is not available.


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bike_points_map, container, false);
        MapsInitializer.initialize(getActivity());

        switch (GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity())) {
            case ConnectionResult.SUCCESS:
                mapView = (MapView) rootView.findViewById(R.id.fragment_map_mapView);
                mapView.onCreate(savedInstanceState);
                if (mapView != null)
                    mapView.getMapAsync(this);
                break;
            case ConnectionResult.SERVICE_MISSING:
                Toast.makeText(getActivity(), getString(R.string.toast_error_serviceMissing), Toast.LENGTH_SHORT).show();
                break;
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                Toast.makeText(getActivity(), getString(R.string.toast_error_updateRequired), Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(getActivity(), GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity()), Toast.LENGTH_SHORT).show();
        }

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap mMap) {
        map = mMap;
    }


    public void onMyErrorResponse() {
        getLoaderManager().initLoader(0, null, this);
    }


    public void onMyResponse() {
        getLoaderManager().initLoader(0, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), BikePointProvider.CONTENT_URI, null, null, null,
                ScriptDatabase.ColumnBikePoint.COMMON_NAME + " ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null)
            return;

        while (data.moveToNext()) {
            int availableDocks = data.getInt(data.getColumnIndex(ScriptDatabase.ColumnBikePoint.AVAILABLE_DOCKS));
            int totalDocks = data.getInt(data.getColumnIndex(ScriptDatabase.ColumnBikePoint.TOTAL_DOCKS));
            String information = getActivity().getString(R.string.bikePointList_information, availableDocks, totalDocks);
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(information);
            spannableStringBuilder.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, information.indexOf(" "), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableStringBuilder.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), information.lastIndexOf(" "), information.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            map.addMarker(new MarkerOptions()
                    .position(new LatLng(data.getDouble(data.getColumnIndex(ScriptDatabase.ColumnBikePoint.LATITUDE)),
                            data.getDouble(data.getColumnIndex(ScriptDatabase.ColumnBikePoint.LONGITUDE))))
                    .title(data.getString(data.getColumnIndex(ScriptDatabase.ColumnBikePoint.COMMON_NAME)))
                    .snippet(information));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

}
