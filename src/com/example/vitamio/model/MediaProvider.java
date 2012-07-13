package com.example.vitamio.model;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: william-work from mobicloud.com.cn
 * Date: 12-7-11
 * Time: 下午4:50
 */
public class MediaProvider extends ContentProvider {

    public static final String TAG = MediaProvider.class.getSimpleName();

    private DatabaseHelper mOPenHelper;
    private static final UriMatcher uriMatcher;
    private static HashMap<String, String> sMediaProjectMap;
    private static final int MEDIAS = 1;
    private static final int MEDIA_ID = 2;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MediaFile.AUTHORITY, "media", MEDIAS);
        uriMatcher.addURI(MediaFile.AUTHORITY, "media/#", MEDIA_ID);

        sMediaProjectMap = new HashMap<String, String>();
        sMediaProjectMap.put(MediaFile.Medias._ID, MediaFile.Medias._ID);
        sMediaProjectMap.put(MediaFile.Medias.TITLE, MediaFile.Medias.TITLE);
        sMediaProjectMap.put(MediaFile.Medias.TITLE_PINYIN, MediaFile.Medias.TITLE_PINYIN);
        sMediaProjectMap.put(MediaFile.Medias.PATH, MediaFile.Medias.PATH);
        sMediaProjectMap.put(MediaFile.Medias.LAST_ACCESS_TIME, MediaFile.Medias.LAST_ACCESS_TIME);
        sMediaProjectMap.put(MediaFile.Medias.DURATION, MediaFile.Medias.DURATION);
        sMediaProjectMap.put(MediaFile.Medias.POSITION, MediaFile.Medias.POSITION);
        sMediaProjectMap.put(MediaFile.Medias.THUMB, MediaFile.Medias.THUMB);
    }

    @Override
    public boolean onCreate() {
        mOPenHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch (uriMatcher.match(uri)) {
            case MEDIAS:
                qb.setTables(MediaFile.Medias.TABLE_NAME);
                qb.setProjectionMap(sMediaProjectMap);
                break;
            case MEDIA_ID:
                qb.setTables(MediaFile.Medias.TABLE_NAME);
                qb.setProjectionMap(sMediaProjectMap);
                qb.appendWhere(MediaFile.Medias._ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI" + uri);
        }
        String orderBy;
        //返回true，如果字符串为空或0长度
        if (TextUtils.isEmpty(sortOrder)) {
            orderBy = MediaFile.Medias.DEFAULT_SORT_ORDER;
        } else {
            orderBy = sortOrder;
        }
        SQLiteDatabase db = mOPenHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, null);
        return c;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case MEDIAS:
                return MediaFile.Medias.CONTENT_TYPE;
            case MEDIA_ID:
                return MediaFile.Medias.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown Uri +" + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
//        Log.d(TAG, "The Uri is: " + uri + "; " + uriMatcher.match(uri));
        if (uriMatcher.match(uri) != MEDIAS) {
            throw new IllegalArgumentException("Unknown uri " + uri);
        }
        ContentValues values_temp;
        if (values != null) {
            values_temp = new ContentValues(values);
        } else {
            values_temp = new ContentValues();
        }
//        Log.d(TAG, values_temp.toString());
        Long now = Long.valueOf(java.lang.System.currentTimeMillis());

        if (values_temp.containsKey(MediaFile.Medias.TITLE) == false) {
            values_temp.put(MediaFile.Medias.LAST_ACCESS_TIME, now);
        }
        SQLiteDatabase db = mOPenHelper.getWritableDatabase();
        long rowId = db.insert(MediaFile.Medias.TABLE_NAME, MediaFile.Medias.TABLE_NAME, values_temp);
        if (rowId > 0) {
            Uri mediaUri = ContentUris.withAppendedId(MediaFile.Medias.CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(mediaUri, null);
            return mediaUri;
        }
        db.close();
        mOPenHelper.close();
        throw new SQLException("Failed to insert row into" + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOPenHelper.getWritableDatabase();
        int count;
        switch (uriMatcher.match(uri)) {
            case MEDIAS:
                count = db.delete(MediaFile.Medias.TABLE_NAME, selection, selectionArgs);
                break;
            case MEDIA_ID:
                String mediaId = uri.getPathSegments().get(1);
                count = db.delete(MediaFile.Medias.TABLE_NAME, MediaFile.Medias._ID + "=" + mediaId + (!TextUtils.isEmpty(selection) ? " AND ( " + selection + ")" : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOPenHelper.getWritableDatabase();
        int count;
        switch (uriMatcher.match(uri)) {
            case MEDIAS:
                count = db.update(MediaFile.Medias.TABLE_NAME, values, selection, selectionArgs);
                break;
            case MEDIA_ID:
                String mediaId = uri.getPathSegments().get(1);
                count = db.update(MediaFile.Medias.TABLE_NAME, values, MediaFile.Medias._ID + "=" + mediaId + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri " + uri);
        }
        return count;
    }
}
