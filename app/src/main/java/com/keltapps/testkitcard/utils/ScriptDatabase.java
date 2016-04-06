package com.keltapps.testkitcard.utils;

import android.provider.BaseColumns;

/**
 * Created by sergio on 6/04/16 for KelpApps.
 */
public class ScriptDatabase {

    public static final String BIKE_POINT_TABLE_NAME = "bikePointTable";
    private static final String STRING_TYPE = "TEXT";
    private static final String INT_TYPE = "INTEGER";
    private static final String DOUBLE_TYPE = "REAL";

    public static class ColumnBikePoint implements BaseColumns {
        public static final String ID = _ID;
        public static final String ID_BIKE_POINT = "id_bike_point";
        public static final String COMMON_NAME = "commonName";
        public static final String AVAILABLE_BIKES = "availableBikes";
        public static final String TOTAL_DOCKS = "totalDocks";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
    }

    public static final String CREATE_BIKE_POINT_TABLE =
            "CREATE TABLE " + BIKE_POINT_TABLE_NAME + "(" +
                    ColumnBikePoint.ID + " " + INT_TYPE + " primary key autoincrement," +
                    ColumnBikePoint.ID_BIKE_POINT + " " + STRING_TYPE + "," +
                    ColumnBikePoint.COMMON_NAME + " " + STRING_TYPE +
                    ColumnBikePoint.AVAILABLE_BIKES + " " + INT_TYPE + "," +
                    ColumnBikePoint.LATITUDE + " " + DOUBLE_TYPE + "," +
                    ColumnBikePoint.LONGITUDE + " " + DOUBLE_TYPE + ")";

}
