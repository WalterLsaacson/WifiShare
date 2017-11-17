package com.yinguan.wifishare.model;


import android.content.ContentValues;
import android.content.Context;

import com.yinguan.wifishare.database.WifiCursorWrapper;
import com.yinguan.wifishare.database.WifiHelper;
import com.yinguan.wifishare.database.WifiTable;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

public class WifiInfoLab {

    private SQLiteDatabase mSQLiteDatabase;
    private static WifiInfoLab sInstance;

    public WifiInfoLab(Context context) {
        mSQLiteDatabase = new WifiHelper(context.getApplicationContext()).getWritableDatabase
                ("lava");
    }

    public static WifiInfoLab getsInstance(Context context) {
        if (sInstance == null) {
            synchronized (WifiInfoLab.class) {
                if (sInstance == null) {
                    sInstance = new WifiInfoLab(context);
                }
            }
        }
        return sInstance;
    }

    public void updateWifi(WifiInfo wifiInfo) {
        String idString = wifiInfo.getSsid() + "";
        ContentValues values = getContentValues(wifiInfo);

        mSQLiteDatabase.update(WifiTable.NAME, values, WifiTable.Clo.SSID
                + " = ?", new String[]{idString});
    }

    public void addWifi(WifiInfo wifiInfo) {
        ContentValues contentValues = getContentValues(wifiInfo);
        mSQLiteDatabase.insert(WifiTable.NAME, WifiTable.Clo.SSID, contentValues);
    }

    private ContentValues getContentValues(WifiInfo wifiInfo) {
        ContentValues values = new ContentValues();
        values.put(WifiTable.Clo.SSID, wifiInfo.getSsid());
        values.put(WifiTable.Clo.PWD, wifiInfo.getPassword());
        return values;
    }

    public WifiInfo getWifiInfo(String ssid) {
        WifiInfo wifiInfo;
        WifiCursorWrapper wifiCursorWrapper = queryInfo(WifiTable.Clo.SSID, new String[]{ssid});
        if (wifiCursorWrapper.moveToFirst()) {
            wifiInfo = wifiCursorWrapper.getWifiInfo();
            wifiCursorWrapper.close();
        } else {
            wifiInfo = new WifiInfo("error","no password!");
        }
        return wifiInfo;
    }

    private WifiCursorWrapper queryInfo(String whereClause, String[] whereArgs) {
        Cursor cursor = mSQLiteDatabase.query(WifiTable.NAME, null,
                whereClause + "=?",
                whereArgs,
                null, null, null);
        return new WifiCursorWrapper(cursor);
    }
}
