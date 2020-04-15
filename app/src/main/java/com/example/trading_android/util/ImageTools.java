package com.example.trading_android.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class ImageTools {
    public static Bitmap getIcon(String path) {
        Bitmap bitmap = null;
        try {
            URL url = new URL(path);//创建URL连接
            URLConnection connection = url.openConnection();//打开连接
            InputStream stream = connection.getInputStream();//获取输输入流
            bitmap = BitmapFactory.decodeStream(stream);

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bitmap;
    }
}