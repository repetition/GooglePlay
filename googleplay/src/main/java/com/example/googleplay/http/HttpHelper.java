package com.example.googleplay.http;

import android.util.Log;

import com.example.googleplay.utils.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by LIANGSE on 2016/5/7.
 */
public class HttpHelper {

    public static String URL_HOST = "http://127.0.0.1:8090/"; //主机名

    /**
     * 访问网络,请求数据
     *
     * @param url
     * @return
     */
    public static String Get(String url) {

        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        HttpURLConnection connection = null;
        String result = "";
        try {
            URL getUrl = new URL(url);
            connection = (HttpURLConnection) getUrl.openConnection();
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                is = connection.getInputStream();
                isr = new InputStreamReader(is);
                br = new BufferedReader(isr);
                String line;
                while ((line = br.readLine()) != null) {
                    result += line;
                }

                Log.w("HttpHelper", "网络请求数据:" + result);
                return result;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(is);
            IOUtils.close(isr);
            IOUtils.close(br);
        }
        return null;
    }


    public static void res() {

    }
}
