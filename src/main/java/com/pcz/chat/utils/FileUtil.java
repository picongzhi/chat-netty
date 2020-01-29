package com.pcz.chat.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author picongzhi
 */
@Slf4j
public class FileUtil {
    public static File createFileByUrl(String url, String suffix) {
        byte[] bytes = getImageFromNetByUrl(url);
        return bytes == null ? null : getFileFromBytes(bytes, suffix);
    }

    private static byte[] getImageFromNetByUrl(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5 * 1000);
            InputStream inputStream = connection.getInputStream();
            return readInputStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] readInputStream(InputStream inputStream) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        inputStream.close();

        return outputStream.toByteArray();
    }

    private static File getFileFromBytes(byte[] bytes, String suffix) {
        BufferedOutputStream outputStream = null;
        File file = null;
        try {
            file = File.createTempFile("pattern", "." + suffix);
            log.info("临时文件位置: " + file.getCanonicalPath());

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            outputStream = new BufferedOutputStream(fileOutputStream);
            outputStream.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return file;
    }

    public static boolean base64ToFile(String filePath, String base64Data) throws Exception {
        if (StringUtils.isBlank(base64Data)) {
            return false;
        }

        String[] arr = base64Data.split("base64,");
        if (ArrayUtils.isEmpty(arr) || arr.length != 2) {
            return false;
        }

        byte[] bytes = Base64Utils.decodeFromString(arr[1]);
        FileUtils.writeByteArrayToFile(new File(filePath), bytes);

        return true;
    }

    public static MultipartFile fileToMultipart(String filePath) {
        try {
            File file = new File(filePath);
            FileInputStream inputStream = new FileInputStream(file);
            return new MockMultipartFile(file.getName(),
                    "png", "image/png", inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
