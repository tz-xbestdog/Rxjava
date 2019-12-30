package com.io.rx.util.util;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownLoadUtil {
    final static int DOWNLOAD_VERSION_PROGRESS = 0;
    final static int DOWNLOAD_VERSION_FAILED = 1;
    final static int DOWNLOAD_VERSION_FINISH = 2;
    final static int DOWNLOAD_VERSION_CANCEL = 3;
    final static int NOTIFICATION_DOWNLOAD = 4;

    /**
     * 下载文件
     *
     * @return
     */
    public static void downloadUpdateFile(String down_url, String file, Handler uiHandler) {
//        long totalSize = versionFileSize;// 文件总大小
        long totalSize = 0;
        long downloadCount = 0;// 已经下载好的大小
        InputStream inputStream = null;
        OutputStream outputStream = null;
        URL url = null;
        HttpURLConnection httpURLConnection = null;
        try {
            url = new URL(down_url);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            // 获取下载文件的size
            if (httpURLConnection.getResponseCode() == 404) {
                throw new Exception("fail!");
            }
            inputStream = httpURLConnection.getInputStream();
            totalSize = httpURLConnection.getContentLength();
            outputStream = new FileOutputStream(file, false);// 文件存在则覆盖掉
            byte buffer[] = new byte[4 * 1024];
            int readsize = 0;
            long lastTime = 0L;
            while ((readsize = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, readsize);
                downloadCount += readsize;// 时时获取下载到的大小
                long currentTime = SystemClock.elapsedRealtime();
                if (currentTime - lastTime > 600L) {
                    Message message = uiHandler
                            .obtainMessage(DOWNLOAD_VERSION_PROGRESS);
                    message.obj = new long[]{downloadCount, totalSize};
                    uiHandler.sendMessage(message);
                    lastTime = currentTime;
                }
            }
            if (downloadCount == totalSize) {
                Object[] data = {file, false};
                Message message = uiHandler
                        .obtainMessage(DOWNLOAD_VERSION_FINISH, data);
                uiHandler.sendMessage(message);
            }
        } catch (Exception e) {
            Message message = uiHandler.obtainMessage(DOWNLOAD_VERSION_FAILED);
            uiHandler.sendMessage(message);
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            try {
                if (inputStream != null)
                    inputStream.close();
                if (outputStream != null)
                    outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
