package com.huahao.serialport.activity;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.huahao.serialport.bean.EventMp4;
import com.huahao.serialport.utils.CommonUtils;
import com.huahao.serialport.utils.UpdateProgressListener;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by Lkn on 2018/2/9.
 */
public class UpdateService extends Service {
    public static final String TAG = "UpdateService";
    public static boolean DEBUG = false;
    private static final String URL = "downloadUrl";
    private String downloadUrl;
    private UpdateProgressListener updateProgressListener;
    private LocalBinder localBinder = new LocalBinder();

    /**
     * Class used for the client Binder.
     */
    public class LocalBinder extends Binder {
        /**
         * set update progress call back
         *
         * @param listener
         */
        public void setUpdateProgressListener(UpdateProgressListener listener) {
            UpdateService.this.setUpdateProgressListener(listener);
        }
    }


    private boolean startDownload;//开始下载
    private DownloadApk downloadApkTask;

    /**
     * whether debug
     */
    public static void debug() {
        DEBUG = true;
    }



    private static String getSaveFileName(String downloadUrl) {
        if (downloadUrl == null || TextUtils.isEmpty(downloadUrl)) {
            return System.currentTimeMillis() + ".mp4";
        }
        return downloadUrl.substring(downloadUrl.lastIndexOf("/"));
    }

    public static File getDownloadDir(Context service) {
        File downloadDir = null;
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            downloadDir = new File(service.getExternalCacheDir(), "download");
        } else {
            downloadDir = new File(service.getCacheDir(), "download");
        }
        if (!downloadDir.exists()) {
            downloadDir.mkdirs();
        }
        return downloadDir;
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!startDownload && intent != null) {
            startDownload = true;
            downloadUrl = intent.getStringExtra(URL);
            downloadApkTask = new DownloadApk(this, this);
            downloadApkTask.execute(downloadUrl);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return localBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    public void setUpdateProgressListener(UpdateProgressListener updateProgressListener) {
        this.updateProgressListener = updateProgressListener;
    }

    @Override
    public void onDestroy() {
        if (downloadApkTask != null) {
            downloadApkTask.cancel(true);
        }
        if (updateProgressListener != null) {
            updateProgressListener = null;
        }
        super.onDestroy();
    }


    private void success(String path) {
        EventBus.getDefault().post(new EventMp4(path));
        stopSelf();
    }


    private void error() {
        if (updateProgressListener != null) {
            updateProgressListener.error();
        }
        stopSelf();
    }

    private static class DownloadApk extends AsyncTask<String, Integer, String> {

        private WeakReference<UpdateService> updateServiceWeakReference;
        private Context context;

        public DownloadApk(UpdateService service, Context context) {
            updateServiceWeakReference = new WeakReference<>(service);
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            updateServiceWeakReference.get();
        }

        @Override
        protected String doInBackground(String... params) {
            final String downloadUrl = params[0];
            Log.i("ywl","doInBackground:"+downloadUrl);
            final File file = new File(UpdateService.getDownloadDir(updateServiceWeakReference.get()),
                    UpdateService.getSaveFileName(downloadUrl));
            if (DEBUG) {
                Log.d(TAG, "download url is " + downloadUrl);
                Log.d(TAG, "download apk cache at " + file.getAbsolutePath());
            }
            File dir = file.getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            HttpURLConnection httpConnection = null;
            InputStream is = null;
            FileOutputStream fos = null;
            int updateTotalSize = 0;
            java.net.URL url;
            try {
                url = new URL(downloadUrl);
                httpConnection = (HttpURLConnection) url.openConnection();
                httpConnection.setConnectTimeout(20000);
                httpConnection.setReadTimeout(20000);

                if (DEBUG) {
                    Log.d(TAG, "download status code: " + httpConnection.getResponseCode());
                }

                if (httpConnection.getResponseCode() != 200) {
                    return null;
                }

                updateTotalSize = httpConnection.getContentLength();

                if (file.exists()) {
                    if (updateTotalSize == file.length()) {
                        // 下载完成
                        return file.getAbsolutePath();
                    } else {
                        file.delete();
                    }
                }
                file.createNewFile();
                is = httpConnection.getInputStream();
                fos = new FileOutputStream(file, false);
                byte buffer[] = new byte[4096];

                int readSize = 0;
                int currentSize = 0;
                while ((readSize = is.read(buffer)) > 0) {
                    Log.i("ywl","currentSize:"+currentSize);
                    fos.write(buffer, 0, readSize);
                    currentSize += readSize;
                    publishProgress((currentSize * 100 / updateTotalSize));
                }
                // download success
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                if (httpConnection != null) {
                    httpConnection.disconnect();
                }
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return file.getAbsolutePath();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (DEBUG) {
                Log.d(TAG, "current progress is " + values[0]);
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            UpdateService service = updateServiceWeakReference.get();
            if (service != null) {
                if (s != null) {
                    service.success(s);
                } else {
                    service.error();
                }
            }
        }
    }


    /**
     * a builder class helper use UpdateService
     */
    public static class Builder {

        private String downloadUrl;

        protected Builder(String downloadUrl) {
            this.downloadUrl = downloadUrl;
        }

        public static Builder create(String downloadUrl) {
            Log.i("ywl","create:"+downloadUrl);
            if (downloadUrl == null) {
                throw new NullPointerException("downloadUrl == null");
            }
            return new Builder(downloadUrl);
        }


        public Builder build(Context context) {
            if (context == null) {
                throw new NullPointerException("context == null");
            }
            Intent intent = new Intent();
            intent.setClass(context, UpdateService.class);
            intent.putExtra(URL, downloadUrl);
            context.startService(intent);

            return this;
        }
    }

}