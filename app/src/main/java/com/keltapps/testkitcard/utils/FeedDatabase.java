package com.keltapps.testkitcard.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.keltapps.testkitcard.models.BikePoint;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sergio on 6/04/16 for KelpApps.
 */
public class FeedDatabase extends SQLiteOpenHelper {
    private static final String TAG = FeedDatabase.class.getSimpleName();
    private static final String KEY_AVAILABLE_BIKES = "NbBikes";
    private static final String KEY_TOTAL_DOCKS = "NbDocks";

    public static final String DATABASE_NAME = "Feed.db";

    public static final int DATABASE_VERSION = 1;

    private static FeedDatabase ourInstance;

    public static synchronized FeedDatabase getInstance(Context context) {
        if (ourInstance == null)
            ourInstance = new FeedDatabase(context.getApplicationContext());
        return ourInstance;
    }

    private FeedDatabase(Context context) {
        super(context,
                DATABASE_NAME,
                null,
                DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ScriptDatabase.CREATE_BIKE_POINT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ScriptDatabase.BIKE_POINT_TABLE_NAME);
        onCreate(db);
    }

    public Cursor getBikesPoints() {
        return getWritableDatabase().rawQuery(
                "select * from " + ScriptDatabase.BIKE_POINT_TABLE_NAME, null);
    }


    private void insertBikePoint(
            String idBikePoint,
            String commonName,
            int availableBikes,
            int totalDocks,
            double lat,
            double lon) {

        ContentValues values = new ContentValues();
        values.put(ScriptDatabase.ColumnBikePoint.ID_BIKE_POINT, idBikePoint);
        values.put(ScriptDatabase.ColumnBikePoint.COMMON_NAME, commonName);
        values.put(ScriptDatabase.ColumnBikePoint.AVAILABLE_BIKES, availableBikes);
        values.put(ScriptDatabase.ColumnBikePoint.TOTAL_DOCKS, totalDocks);
        values.put(ScriptDatabase.ColumnBikePoint.LATITUDE, lat);
        values.put(ScriptDatabase.ColumnBikePoint.LONGITUDE, lon);

        getWritableDatabase().insert(
                ScriptDatabase.BIKE_POINT_TABLE_NAME,
                null,
                values
        );
    }


    private void updateBikePoint(int id,
                                 String idBikePoint,
                                 String commonName,
                                 int availableBikes,
                                 int totalDocks,
                                 double lat,
                                 double lon) {

        ContentValues values = new ContentValues();
        values.put(ScriptDatabase.ColumnBikePoint.ID_BIKE_POINT, idBikePoint);
        values.put(ScriptDatabase.ColumnBikePoint.COMMON_NAME, commonName);
        values.put(ScriptDatabase.ColumnBikePoint.AVAILABLE_BIKES, availableBikes);
        values.put(ScriptDatabase.ColumnBikePoint.TOTAL_DOCKS, totalDocks);
        values.put(ScriptDatabase.ColumnBikePoint.LATITUDE, lat);
        values.put(ScriptDatabase.ColumnBikePoint.LONGITUDE, lon);

        getWritableDatabase().update(
                ScriptDatabase.BIKE_POINT_TABLE_NAME,
                values,
                ScriptDatabase.ColumnBikePoint.ID + "=?",
                new String[]{String.valueOf(id)});
    }

    public void synchronizeBikePoints(String data) {
        Type fooType = new TypeToken<ArrayList<BikePoint>>() {
        }.getType();
        List<BikePoint> listBikePoint = new GsonBuilder().create().fromJson(data, fooType);
        HashMap<String, BikePoint> entryMap = new HashMap<>();
        for (BikePoint bikePoint : listBikePoint) {
            for (BikePoint.AdditionalProperty additionalProperty : bikePoint.getAdditionalPropertyLinkedList()) {
                if (additionalProperty.getKey().equals(KEY_AVAILABLE_BIKES))
                    bikePoint.setAvailableBikes(additionalProperty.getValue());
                else if (additionalProperty.getKey().equals(KEY_TOTAL_DOCKS))
                    bikePoint.setTotalDocks(additionalProperty.getValue());
            }
            entryMap.put(bikePoint.getIdBikePoint(), bikePoint);
        }
        Cursor c = getBikesPoints();
        assert c != null;

        while (c.moveToNext()) {
            String bikePointId = c.getString(c.getColumnIndex(ScriptDatabase.ColumnBikePoint.ID_BIKE_POINT));
            BikePoint match = entryMap.get(bikePointId);
            if (match != null) {
                entryMap.remove(bikePointId);
                if (!match.getIdBikePoint().equals(bikePointId)) {
                    updateBikePoint(
                            c.getInt(c.getColumnIndex(ScriptDatabase.ColumnBikePoint.ID)),
                            match.getIdBikePoint(),
                            match.getCommonName(),
                            match.getAvailableBikes(),
                            match.getTotalDocks(),
                            match.getLat(),
                            match.getLon()
                    );

                }
            }
        }
        c.close();
        for (BikePoint bikePoint : entryMap.values()) {
            insertBikePoint(
                    bikePoint.getIdBikePoint(),
                    bikePoint.getCommonName(),
                    bikePoint.getAvailableBikes(),
                    bikePoint.getTotalDocks(),
                    bikePoint.getLat(),
                    bikePoint.getLon()
            );
        }
    }


}
