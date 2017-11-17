package com.yinguan.wifishare.database;

import android.content.Context;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;


public class WifiHelper extends SQLiteOpenHelper {


    private static final int VERSION = 1;
    private static final String WIFI_DATABASE_NAME = "wifi_info";

    private WifiHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int
            version) {
        super(context, name, factory, version);
        // load so file
        SQLiteDatabase.loadLibs(context);
    }

    public WifiHelper(Context context) {
        this(context, WIFI_DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + WifiTable.NAME + "(" +
                WifiTable.Clo.SSID + "," +
                WifiTable.Clo.PWD +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
