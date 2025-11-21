package com.elfadouaki.darelkilani.download;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.elfadouaki.darelkilani.model.VideoItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class VideoDownloadManager {
    private static final String TAG = "VideoDownloadManager";
    private static VideoDownloadManager instance;
    private Context context;
    private Map<String, DownloadTask> activeDownloads;

    public interface DownloadListener {
        void onDownloadStarted(String videoId);
        void onDownloadProgress(String videoId, int progress);
        void onDownloadCompleted(String videoId, String filePath);
        void onDownloadFailed(String videoId, String error);
    }

    private VideoDownloadManager(Context context) {
        this.context = context.getApplicationContext();
        this.activeDownloads = new HashMap<>();
    }

    public static VideoDownloadManager getInstance(Context context) {
        if (instance == null) {
            instance = new VideoDownloadManager(context);
        }
        return instance;
    }

    public void downloadVideo(VideoItem video, DownloadListener listener) {
        if (video.directVideoUrl == null || video.directVideoUrl.isEmpty()) {
            listener.onDownloadFailed(video.videoId, "No direct URL available");
            return;
        }

        if (activeDownloads.containsKey(video.videoId)) {
            listener.onDownloadFailed(video.videoId, "Download already in progress");
            return;
        }

        File downloadsDir = getDownloadsDirectory();
        if (!downloadsDir.exists()) {
            downloadsDir.mkdirs();
        }

        String fileName = "video_" + video.videoId + ".mp4";
        File outputFile = new File(downloadsDir, fileName);

        DownloadTask task = new DownloadTask(video, outputFile, listener);
        activeDownloads.put(video.videoId, task);
        task.execute(video.directVideoUrl);
    }

    public void cancelDownload(String videoId) {
        DownloadTask task = activeDownloads.get(videoId);
        if (task != null) {
            task.cancel(true);
            activeDownloads.remove(videoId);
        }
    }

    public boolean isDownloading(String videoId) {
        return activeDownloads.containsKey(videoId);
    }

    public File getDownloadsDirectory() {
        File appDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_MOVIES), "DarElkilani");
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        return appDir;
    }

    public void deleteDownloadedVideo(VideoItem video) {
        if (video.localFilePath != null) {
            File file = new File(video.localFilePath);
            if (file.exists()) {
                file.delete();
                video.markAsNotDownloaded();
            }
        }
    }

    private class DownloadTask extends AsyncTask<String, Integer, String> {
        private VideoItem video;
        private File outputFile;
        private DownloadListener listener;
        private String errorMessage;

        public DownloadTask(VideoItem video, File outputFile, DownloadListener listener) {
            this.video = video;
            this.outputFile = outputFile;
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {
            listener.onDownloadStarted(video.videoId);
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    errorMessage = "Server returned HTTP " + connection.getResponseCode();
                    return null;
                }

                int fileLength = connection.getContentLength();
                InputStream input = connection.getInputStream();
                FileOutputStream output = new FileOutputStream(outputFile);

                byte[] buffer = new byte[4096];
                long total = 0;
                int count;

                while ((count = input.read(buffer)) != -1) {
                    if (isCancelled()) {
                        input.close();
                        output.close();
                        outputFile.delete();
                        return null;
                    }

                    total += count;
                    if (fileLength > 0) {
                        publishProgress((int) (total * 100 / fileLength));
                    }
                    output.write(buffer, 0, count);
                }

                output.close();
                input.close();
                connection.disconnect();

                return outputFile.getAbsolutePath();

            } catch (Exception e) {
                Log.e(TAG, "Download failed", e);
                errorMessage = e.getMessage();
                return null;
            }
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            listener.onDownloadProgress(video.videoId, progress[0]);
        }

        @Override
        protected void onPostExecute(String filePath) {
            activeDownloads.remove(video.videoId);

            if (filePath != null) {
                video.markAsDownloaded(filePath);
                listener.onDownloadCompleted(video.videoId, filePath);
            } else {
                if (outputFile.exists()) {
                    outputFile.delete();
                }
                listener.onDownloadFailed(video.videoId, errorMessage != null ? errorMessage : "Unknown error");
            }
        }

        @Override
        protected void onCancelled() {
            activeDownloads.remove(video.videoId);
            if (outputFile.exists()) {
                outputFile.delete();
            }
        }
    }
}