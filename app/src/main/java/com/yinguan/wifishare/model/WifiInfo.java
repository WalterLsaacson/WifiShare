package com.yinguan.wifishare.model;


public class WifiInfo {
    private String ssid;
    private String password;

    public WifiInfo(String ssid, String password) {
        this.ssid = ssid;
        this.password = password;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "WifiInfo{" +
                "\nssid='" + ssid + '\'' +
                ", \npassword='" + password + '\'' +
                '}';
    }
}
