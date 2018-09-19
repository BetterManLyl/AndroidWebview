package com.iflytek.webviewtest.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.iflytek.webviewtest.bean.DeviceInfo;

import java.security.PublicKey;

/**
 * @author: ylli10
 * @date: 2018/9/9.
 * Email:ylli10@iflytek.com
 * Description:
 */
public class MyUtils {

    private static String TAG = MyUtils.class.getSimpleName();

    private Context mContext;

    private Activity activity;

    public static int TAKE_PHOTO_REQUEST_CODE = 0X01;

    public MyUtils(Context context) {
        this.mContext = context;
        activity = (Activity) context;
    }

    /**
     * 打电话
     */
    @JavascriptInterface
    public void callPhone(final String phoneNum) {
        //申请权限
        MPermissionUtils.requestPermissionsResult((Activity) mContext, 200,
                new String[]{Manifest.permission.CALL_PHONE}, new MPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        Log.e(TAG, "callPhone: ");
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + phoneNum));
                        activity.startActivity(intent);
                    }

                    @Override
                    public void onPermissionDenied() {
                        MPermissionUtils.showTipsDialog(mContext);
                    }
                });
    }

    /**
     * 拍照
     */
    @JavascriptInterface
    public void takePhoto() {

        //申请权限
        MPermissionUtils.requestPermissionsResult((Activity) mContext, 100,
                new String[]{Manifest.permission.CAMERA}, new MPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        Log.e(TAG, "takePhoto: ");
                        Intent intent = new Intent();
                        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                        activity.startActivityForResult(intent, TAKE_PHOTO_REQUEST_CODE);
                    }

                    @Override
                    public void onPermissionDenied() {
                        MPermissionUtils.showTipsDialog(mContext);
                    }
                });
    }


    /**
     * 获取设备信息
     *
     * @return
     */
    @JavascriptInterface
    public String getDeviceInfo() {
        Device device = new Device(mContext);
        String id = device.getAndroidId();
        String brand = device.getBrand();
        String ipAddress = device.getIpAddress();
        String appVersion = device.getAppVersionCode() + "";
        String sn = device.getSerialNumber();
        String sysVersion = device.getSystemVersion();
        String mac = device.getMacAddress();

        DeviceInfo deviceInfo = new DeviceInfo(id, brand, appVersion, sn, sysVersion, ipAddress, mac);

        String deviceInfojson = GsonUtil.getInstance().toJson(deviceInfo);

        return deviceInfojson;

    }


    /**
     * 获取手机联系人
     *
     * @return
     */
    @JavascriptInterface
    public void getContacts() {
        MPermissionUtils.requestPermissionsResult((Activity) mContext, 300,
                new String[]{Manifest.permission.READ_CONTACTS}, new MPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        //联系人的Uri，也就是content://com.android.contacts/contacts
                        Uri uri = ContactsContract.Contacts.CONTENT_URI;
                        //指定获取_id和display_name两列数据，display_name即为姓名
                        String[] projection = new String[]{
                                ContactsContract.Contacts._ID,
                                ContactsContract.Contacts.DISPLAY_NAME
                        };
                        //根据Uri查询相应的ContentProvider，cursor为获取到的数据集
                        Cursor cursor = mContext.getContentResolver().query(uri, projection, null, null, null);
                        String[] arr = new String[cursor.getCount()];
                        int i = 0;
                        if (cursor != null && cursor.moveToFirst()) {
                            do {
                                Long id = cursor.getLong(0);
                                //获取姓名
                                String name = cursor.getString(1);
                                //指定获取NUMBER这一列数据
                                String[] phoneProjection = new String[]{
                                        ContactsContract.CommonDataKinds.Phone.NUMBER
                                };
                                arr[i] = id + " , 姓名：" + name;

                                //根据联系人的ID获取此人的电话号码
                                Cursor phonesCusor = mContext.getContentResolver().query(
                                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                        phoneProjection,
                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id,
                                        null,
                                        null);

                                //因为每个联系人可能有多个电话号码，所以需要遍历
                                if (phonesCusor != null && phonesCusor.moveToFirst()) {
                                    do {
                                        String num = phonesCusor.getString(0);
                                        arr[i] += " , 电话号码：" + num;
                                    } while (phonesCusor.moveToNext());
                                }
                                i++;
                            } while (cursor.moveToNext());
                        }
                        for (int j = 0; j < arr.length; j++) {
                            Log.e(TAG, "getContacts: " + arr[j].toString());
                        }
                    }

                    @Override
                    public void onPermissionDenied() {

                        MPermissionUtils.showTipsDialog(mContext);
                    }
                });

    }
}
