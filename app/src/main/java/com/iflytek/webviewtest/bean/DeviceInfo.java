package com.iflytek.webviewtest.bean;

import java.io.Serializable;

/**
 * @author: ylli10
 * @date: 2018/9/14.
 * Email:ylli10@iflytek.com
 * Description:
 */
public class DeviceInfo implements Serializable {
    private String id;

    private String brand;

    private String appVersion;


    private String sn;

    private String sysVersion;

    private String ipAddress = "";

    private String mac;

    public DeviceInfo(String id, String board, String appVersion,
                      String sn, String sysVersion, String ipAddress, String mac) {
        this.id = id;
        this.brand = board;
        this.appVersion = appVersion;
        this.sn = sn;
        this.sysVersion = sysVersion;
        this.ipAddress = ipAddress;
        this.mac = mac;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getSysVersion() {
        return sysVersion;
    }

    public void setSysVersion(String sysVersion) {
        this.sysVersion = sysVersion;
    }


}
