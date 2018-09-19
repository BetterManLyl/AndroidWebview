package com.iflytek.webviewtest.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

/**
 * @author: ylli10
 * @date: 2018/8/31.
 * Email:ylli10@iflytek.com
 * Description:
 * 获取机型信息
 */
public class Device {
    public static final String TAG = "Device";

    public static String platform;                            // Device OS
    public static String uuid;                                // Device UUID

    private static final String ANDROID_PLATFORM = "Android";
    private static final String AMAZON_PLATFORM = "amazon-fireos";
    private static final String AMAZON_DEVICE = "Amazon";
    private Context mContext = null;

    public Device(Context context) {
        this.mContext = context;
    }


    /**
     * android.os.Build.BOARD：获取设备基板名称
     * android.os.Build.BOOTLOADER:获取设备引导程序版本号
     * android.os.Build.BRAND：获取设备品牌
     * android.os.Build.CPU_ABI：获取设备指令集名称（CPU的类型）
     * android.os.Build.CPU_ABI2：获取第二个指令集名称
     * android.os.Build.DEVICE：获取设备驱动名称
     * android.os.Build.DISPLAY：获取设备显示的版本包（在系统设置中显示为版本号）和ID一样
     * android.os.Build.FINGERPRINT：设备的唯一标识。由设备的多个信息拼接合成。
     * android.os.Build.HARDWARE：设备硬件名称,一般和基板名称一样（BOARD）
     * android.os.Build.HOST：设备主机地址
     * android.os.Build.ID:设备版本号。
     * android.os.Build.MODEL ：获取手机的型号 设备名称。
     * android.os.Build.MANUFACTURER:获取设备制造商
     * android.os.Build.PRODUCT：整个产品的名称
     * android.os.Build.RADIO：无线电固件版本号，通常是不可用的 显示unknown
     * android.os.Build.TAGS：设备标签。如release-keys 或测试的 test-keys
     * android.os.Build.TIME：时间
     * android.os.Build.TYPE:设备版本类型 主要为”user” 或”eng”.
     * android.os.Build.USER:设备用户名 基本上都为android-build
     * android.os.Build.VERSION.RELEASE：获取系统版本字符串。如4.1.2 或2.2 或2.3等
     * android.os.Build.VERSION.CODENAME：设备当前的系统开发代号，一般使用REL代替
     * android.os.Build.VERSION.INCREMENTAL：系统源代码控制值，一个数字或者git hash值
     * android.os.Build.VERSION.SDK：系统的API级别 一般使用下面大的SDK_INT 来查看
     * android.os.Build.VERSION.SDK_INT：系统的API级别 数字表示
     * android.os.Build.VERSION_CODES类 中有所有的已公布的Android版本号。全部是Int常亮。可用于与SDK_INT进行比较来判断当前的系统版本
     */


    /**
     * 获取IMEI码
     * @return
     */
    public String getIMEI() {
        TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") String imei = telephonyManager.getDeviceId();
        if (imei == null) {
            imei = "";
        }
        return imei;
    }

    /**
     * 获取当前本地apk的版本
     *
     * @param
     * @return
     */
    public  int getAppVersionCode() {
        int versionCode = 0;
        try {
            //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = mContext.getPackageManager().
                    getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取ip地址
     *
     * @return
     */
    public String getIpAddress() {
        if (mContext != null) {
            String ipAddress = IpGetUtil.getIPAddress(mContext);
            return ipAddress;
        } else {
            return " ";
        }
    }

    /**
     * @return 系统的API级别 数字表示
     */
    public int getVersionSdk() {
        return Build.VERSION.SDK_INT;
    }


    /**
     * 获取androidId
     *
     * @return
     */
    public String getAndroidId() {
        String androidID = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidID;
    }

    /**
     * 获取设备号
     * sn码
     * @return
     */
    public String getSerialNumber() {
        String SerialNumber = android.os.Build.SERIAL;
        return SerialNumber;
    }

    /**
     * 设备用户名 基本上都为android-build
     *
     * @return
     */
    public String getUser() {
        return Build.USER;
    }

    /**
     * @return 设备版本类型 主要为”user” 或”eng”.
     */
    public String getType() {
        return Build.TYPE;
    }

    /**
     * @return 时间
     */
    public long getTime() {
        return Build.TIME;
    }

    /**
     * @return 整个产品的名称
     */
    public String getProduct() {
        return Build.PRODUCT;
    }


    /**
     * @return 获取设备基板名称
     */
    public String getBoard() {
        return Build.BOARD;
    }

    /**
     * @return 获取设备引导程序版本号
     */
    public static String getBootloader() {
        return Build.BOOTLOADER;
    }

    /**
     * @return 获取设备品牌
     */
    public String getBrand() {
        return Build.BRAND;
    }

    /**
     * @return 获取设备驱动名称
     */
    public String getDeviceName() {
        return Build.DEVICE;
    }

    /**
     * @return 获取设备显示的版本包（在系统设置中显示为版本号）和ID一样
     */
    public String getDisplay() {
        return Build.DISPLAY;
    }

    /**
     * @return 设备的唯一标识。由设备的多个信息拼接合成。
     */
    public String getFingerPrint() {
        return Build.FINGERPRINT;
    }

    /**
     * @return 设备硬件名称, 一般和基板名称一样（BOARD）
     */
    public String getHardWare() {
        return Build.HARDWARE;
    }

    /**
     * @return 设备主机地址
     */
    public String getHost() {
        return Build.HOST;
    }

    /**
     * @return 设备版本号。
     */
    public String getID() {
        return Build.ID;
    }

    /**
     * @return 获取手机的型号 设备名称。
     */
    public String getModel() {
        return Build.MODEL;
    }

    /**
     * @return 获取设备制造商
     */
    public String getManufacturer() {
        return Build.MANUFACTURER;
    }


    /**
     * @return 获取系统版本
     */
    public String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * @return 获取固件版本
     */
    public String getFirmwareVersionCode() {
        return Build.VERSION.CODENAME;
    }

    /**
     * 获取mac地址
     *
     * @return
     */
    public String getLocalMacAddress() {
        WifiManager wifi = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }


    /**
     * Gets the mac address.
     *
     * @return the mac address
     */
    public String getMacAddress() {

        if (Build.VERSION.SDK_INT >= 23) { // Build.VERSION_CODES.M
            return getMMacAddress();
        }
        return getLegacyMacAddress();
    }

    /**
     * Gets the mac address on version < Marshmallow.
     *
     * @return the mac address
     */
    private String getLegacyMacAddress() {
        String macAddress = null;
        WifiManager wm = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        macAddress = wm.getConnectionInfo().getMacAddress();

        if (macAddress == null || macAddress.length() == 0) {
            macAddress = "02:00:00:00:00:00";
        }
        return macAddress;
    }

    /**
     * Gets the mac address on version >= Marshmallow.
     *
     * @return the mac address
     */
    private String getMMacAddress() {

        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());

            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02x", (b & 0xFF)) + ":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }

                return res1.toString();
            }
        } catch (Exception ex) {
        }

        return "02:00:00:00:00:00";
    }

    /**
     * Function to check if the device is manufactured by Amazon
     *
     * @return
     */
    public boolean isAmazonDevice() {
        if (Build.MANUFACTURER.equals(AMAZON_DEVICE)) {
            return true;
        }
        return false;
    }

    /**
     * 是否是虚拟的
     *
     * @return
     */
    public boolean isVirtual() {
        return Build.FINGERPRINT.contains("generic") ||
                Build.PRODUCT.contains("sdk");
    }
}
