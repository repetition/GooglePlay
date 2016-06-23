package com.example.googleplay.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by LIANGSE on 2016/5/27.
 */
public class IOUtils {
    /**
     * 关闭 IO流
     * @param io
     */
    public static void close(Closeable io){
        if (io!=null){
            try {
                io.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
