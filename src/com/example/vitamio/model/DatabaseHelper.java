package com.example.vitamio.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;
import com.example.vitamio.VitamioApplication;

/**
 * Created with IntelliJ IDEA.
 * User: william-work from mobicloud.com.cn
 * Date: 12-7-11
 * Time: 下午2:56
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_DATABASE =
            " CREATE TABLE " + MediaFile.Medias.TABLE_NAME + " ("
                    + MediaFile.Medias._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + MediaFile.Medias.TITLE + " TEXT,"
                    + MediaFile.Medias.TITLE_PINYIN + " TEXT,"
                    + MediaFile.Medias.PATH + " TEXT,"
                    + MediaFile.Medias.POSITION +" INTEGER,"
                    + MediaFile.Medias.DURATION + " INTEGER,"
                    + MediaFile.Medias.LAST_ACCESS_TIME + " INTEGER,"
                    + MediaFile.Medias.THUMB + " TEXT"
                    + ")";

    public DatabaseHelper(Context context) {
        super(context, VitamioApplication.DATABASE_NAME, null, VitamioApplication.DATABASE_VERSION);
    }

    public boolean isExist() {

        String sql = "SELECT count(*) count from " + MediaFile.Medias.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                int count = c.getInt(0);
                if (count > 0) {
                    c.close();
                    db.close();
                    return true;
                }
            } while (c.moveToNext());
        }
        db.close();
        c.close();
        return false;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            /**
             *     暂时将锁检查关闭，提升数据库创建速度
             */
            db.setLockingEnabled(false);
        } finally {
            db.execSQL(SQL_CREATE_DATABASE);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + MediaFile.Medias.TABLE_NAME);
            onCreate(db);
        }
    }
}
