package com.iflytek.webviewtest.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author: ylli10
 * @date: 2018/9/4.
 * Email:ylli10@iflytek.com
 * Description:
 */
public class FileUtils {
    /**
     * 图片压缩
     * @param image
     * @return
     */
    public  static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
//因为获取到的ByteArrayOutputStream大小基本为50几kb，所以不用压缩，所以以下代码注释掉
//        int options = 90;
//        while (baos.toByteArray().length / 1024 > 200) {// 循环判断如果压缩后图片是否大于200kb,大于继续压缩
//            baos.reset(); // 重置baos即清空baos
//            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options，把压缩后的数据存放到baos中
//            options -= 10;// 每次都减少10
//        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    // bitmap转base64
    public static String bitmapToBase64(Bitmap bitmap) {
        String result = "data:image/png;base64,";//必须加上“data:image/png;base64”图片的数据格式H5才能识别出来
        ByteArrayOutputStream bos = null;
        try {
            if (null != bitmap) {
                bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 30, bos);// 将bitmap放入字节数组流中
                bos.flush();// 将bos流缓存在内存中的数据全部输出，清空缓存
                bos.close();
                byte[] bitmapByte = bos.toByteArray();
                result += Base64.encodeToString(bitmapByte, Base64.DEFAULT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d("it520", "result=" + result);
        Log.d("it520", "size=" + bos.toByteArray().length / 1024);//获取ByteArrayOutputStream的大小，单位kb，
        return result;
    }
    /**
     * 保存bitmap到SD卡
     *
     * @param bitmap
     * @param
     */
    public static String saveBitmapToSDCard(Bitmap bitmap) {
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/watermark/image");
        if (!file.exists()) {
            file.mkdirs();
        }
        File picture = new File(file + "/" + System.currentTimeMillis() + ".jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(picture);
            if (fos != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                fos.flush();
                fos.close();
            }
            return picture.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
