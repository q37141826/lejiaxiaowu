package com.qixiu.lejia.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * Created by Long on 2017/12/29
 */
public class StreamUtils {

    public static byte[] fileToBytes(File file) {
        FileInputStream fos = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            byte[] buff = new byte[1024];
            int len;
            fos = new FileInputStream(file);
            while ((len = fos.read(buff)) != -1) {
                bos.write(buff, 0, len);
            }
            return bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            CloseUtils.closeIOQuietly(fos, bos);
        }
    }


}
