package com.keltapps.testkitcard.utils;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by sergio on 6/04/16 for KelpApps.
 */
public class BikePointProvider extends ContentProvider {
    private static final String prefix_content = "content://";
    private static final String prefix_uri = "com.keltapps.testkitcard.contentproviders";
    private static final String uri = prefix_content + prefix_uri + "/" + ScriptDatabase.BIKE_POINT_TABLE_NAME;
    public static final Uri CONTENT_URI = Uri.parse(uri);
    private static final String MIME_LIST = "vnd.android.cursor.dir/vnd.testkitcard.bikepoint";
    private static final String MIME_UNIQUE = "vnd.android.cursor.item/vnd.testkitcard.bikepoint";

    private FeedDatabase feedDatabase;

    //UriMatcher
    private static final int BIKE_POINTS = 1;
    private static final UriMatcher uriMatcher;

    //Initialize UriMatcher
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(prefix_uri, ScriptDatabase.BIKE_POINT_TABLE_NAME, BIKE_POINTS);
    }


    @Override
    public boolean onCreate() {
        feedDatabase = FeedDatabase.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        switch (uriMatcher.match(uri)) {
            case BIKE_POINTS:
                return feedDatabase.getWritableDatabase().query(ScriptDatabase.BIKE_POINT_TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case BIKE_POINTS:
                return MIME_LIST;
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long regId;
        switch (uriMatcher.match(uri)) {
            case BIKE_POINTS:
                regId = feedDatabase.getWritableDatabase().insert(ScriptDatabase.BIKE_POINT_TABLE_NAME, null, values);
                break;
            default:
                return null;
        }
        return ContentUris.withAppendedId(CONTENT_URI, regId);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case BIKE_POINTS:
                return feedDatabase.getWritableDatabase().delete(ScriptDatabase.BIKE_POINT_TABLE_NAME, selection, selectionArgs);
            default:
                return 0;
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case BIKE_POINTS:
                return feedDatabase.getWritableDatabase().update(ScriptDatabase.BIKE_POINT_TABLE_NAME, values, selection, selectionArgs);
            default:
                return 0;
        }
    }
}
