package com.elfadouaki.darelkilani.model;

public class VideoItem {
    public String videoId;
    public String title;
    public String pageId;
    public String directVideoUrl; // For downloadable videos
    public boolean isDownloadable;
    public boolean isDownloaded;
    public String localFilePath;

    public VideoItem(String videoId, String title, String pageId) {
        this.videoId = videoId;
        this.title = title;
        this.pageId = pageId;
        this.directVideoUrl = null;
        this.isDownloadable = false;
        this.isDownloaded = false;
        this.localFilePath = null;
    }

    public VideoItem(String videoId, String title, String pageId, String directVideoUrl) {
        this.videoId = videoId;
        this.title = title;
        this.pageId = pageId;
        this.directVideoUrl = directVideoUrl;
        this.isDownloadable = directVideoUrl != null && !directVideoUrl.isEmpty();
        this.isDownloaded = false;
        this.localFilePath = null;
    }

    public String getEmbedUrl() {
        String videoUrl = "https://www.facebook.com/" + pageId + "/videos/" + videoId;
        return "https://www.facebook.com/plugins/video.php?href=" +
                android.net.Uri.encode(videoUrl) +
                "&show_text=false" +
                "&autoplay=true" +
                "&muted=false" +
                "&allowfullscreen=true";
    }

    public String getPlaybackUrl() {
        // Return local file if downloaded, otherwise direct URL or embed URL
        if (isDownloaded && localFilePath != null) {
            return localFilePath;
        } else if (directVideoUrl != null && !directVideoUrl.isEmpty()) {
            return directVideoUrl;
        } else {
            return getEmbedUrl();
        }
    }

    public boolean canDownload() {
        return isDownloadable && !isDownloaded;
    }

    public void markAsDownloaded(String filePath) {
        this.isDownloaded = true;
        this.localFilePath = filePath;
    }

    public void markAsNotDownloaded() {
        this.isDownloaded = false;
        this.localFilePath = null;
    }

}