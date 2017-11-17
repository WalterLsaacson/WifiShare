package com.yinguan.wifishare.database;

import com.yinguan.wifishare.model.WifiInfo;

import net.sqlcipher.Cursor;
import net.sqlcipher.CursorWrapper;


public class WifiCursorWrapper extends CursorWrapper {
    public WifiCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public WifiInfo getWifiInfo() {
        String ssid = getString(getColumnIndex(WifiTable.Clo.SSID));
        String pwd = getString(getColumnIndex(WifiTable.Clo.PWD));
        return new WifiInfo(ssid, pwd);
    }
}
