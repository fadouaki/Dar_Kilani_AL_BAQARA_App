package com.elfadouaki.darelkilani.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elfadouaki.darelkilani.OfflineVideoPlayerActivity;
import com.elfadouaki.darelkilani.R;
import com.elfadouaki.darelkilani.VideoPlayerActivity;
import com.elfadouaki.darelkilani.download.VideoDownloadManager;
import com.elfadouaki.darelkilani.model.VideoItem;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private final List<VideoItem> originalVideos;
    private List<VideoItem> filteredVideos;

    public VideoAdapter(List<VideoItem> videos) {
        this.originalVideos = new ArrayList<>(videos);
        this.filteredVideos = new ArrayList<>(videos);
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_video, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        holder.bind(filteredVideos.get(position));
    }

    @Override
    public int getItemCount() {
        return filteredVideos.size();
    }

    public void filter(String query) {
        filteredVideos.clear();

        if (query == null || query.trim().isEmpty()) {
            filteredVideos.addAll(originalVideos);
        } else {
            String lowerCaseQuery = query.toLowerCase().trim();
            for (VideoItem video : originalVideos) {
                if (video.title != null &&
                    (video.title.toLowerCase().contains(lowerCaseQuery) ||
                     video.title.contains(query))) {
                    filteredVideos.add(video);
                }
            }
        }
        notifyDataSetChanged();
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle;
        WebView webViewEmbed;
        Button btnLandscape;
        Button btnDownload;
        ProgressBar downloadProgress;
        ImageButton btnOfflinePlay;
        Context context;
        private ShimmerFrameLayout shimmerFrameLayout;
        VideoDownloadManager downloadManager;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            textTitle = itemView.findViewById(R.id.textTitle);
            webViewEmbed = itemView.findViewById(R.id.webViewEmbed);
            btnLandscape = itemView.findViewById(R.id.btnLandscape);
            btnDownload = itemView.findViewById(R.id.btnDownload);
            shimmerFrameLayout= itemView.findViewById(R.id.shimmerLayout);
            downloadProgress = itemView.findViewById(R.id.downloadProgress);
            btnOfflinePlay = itemView.findViewById(R.id.btnOfflinePlay);
            downloadManager = VideoDownloadManager.getInstance(context);
        }

        void bind(VideoItem video) {
            // Add download indicator to title if video is downloadable
            String title = video.title;
            if (video.isDownloadable) {
                title = "📥 " + title + " (قابل للتحميل)";
            }
            textTitle.setText(title);

            WebSettings settings = webViewEmbed.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setDomStorageEnabled(true);
            settings.setUseWideViewPort(true);
            settings.setLoadWithOverviewMode(true);
            settings.setMediaPlaybackRequiresUserGesture(false);
            settings.setAllowContentAccess(true);
            settings.setAllowFileAccess(true);
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

            webViewEmbed.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);

                    webViewEmbed.setVisibility(View.VISIBLE);

                    // Inject JavaScript to unmute video
                    String script = "javascript:(function() {" +
                            "setTimeout(function() {" +
                            "var videos = document.querySelectorAll('video');" +
                            "for(var i = 0; i < videos.length; i++) {" +
                            "videos[i].muted = false;" +
                            "}" +
                            "var iframes = document.querySelectorAll('iframe');" +
                            "for(var j = 0; j < iframes.length; j++) {" +
                            "try {" +
                            "var iframeDoc = iframes[j].contentDocument || iframes[j].contentWindow.document;" +
                            "var iframeVideos = iframeDoc.querySelectorAll('video');" +
                            "for(var k = 0; k < iframeVideos.length; k++) {" +
                            "iframeVideos[k].muted = false;" +
                            "}" +
                            "} catch(e) {}" +
                            "}" +
                            "}, 1000);" +
                            "})()";
                    view.loadUrl(script);
                }
            });

            webViewEmbed.loadUrl(video.getEmbedUrl());

            btnLandscape.setOnClickListener(v -> {
                Intent intent = new Intent(context, VideoPlayerActivity.class);
                intent.putExtra("videoId", video.videoId);
                intent.putExtra("title", video.title);
                intent.putExtra("pageId", video.pageId);
                context.startActivity(intent);
            });

            // Setup download UI based on video state
            setupDownloadUI(video);

            btnDownload.setOnClickListener(v -> downloadVideo(video));

            btnOfflinePlay.setOnClickListener(v -> {
                Intent intent = new Intent(context, OfflineVideoPlayerActivity.class);
                intent.putExtra("videoId", video.videoId);
                intent.putExtra("title", video.title);
                intent.putExtra("filePath", video.localFilePath);
                context.startActivity(intent);
            });
        }

        private void setupDownloadUI(VideoItem video) {
            if (video.isDownloaded) {
                // Video is downloaded - show offline play button
                btnDownload.setVisibility(View.GONE);
                downloadProgress.setVisibility(View.GONE);
                btnOfflinePlay.setVisibility(View.VISIBLE);
            } else if (video.canDownload()) {
                // Video can be downloaded - show download button
                btnDownload.setVisibility(View.VISIBLE);
                downloadProgress.setVisibility(View.GONE);
                btnOfflinePlay.setVisibility(View.GONE);
            } else {
                // Video cannot be downloaded - hide all download controls
                btnDownload.setVisibility(View.GONE);
                downloadProgress.setVisibility(View.GONE);
                btnOfflinePlay.setVisibility(View.GONE);
            }
        }

        private void downloadVideo(VideoItem video) {
            if (!video.canDownload()) {

                Toast.makeText(context, "هذا الفيديو غير متاح للتحميل", Toast.LENGTH_SHORT).show();
                return;
            }

            // Show progress, hide download button
            btnDownload.setVisibility(View.GONE);
            downloadProgress.setVisibility(View.VISIBLE);

            downloadManager.downloadVideo(video, new VideoDownloadManager.DownloadListener() {
                @Override
                public void onDownloadStarted(String videoId) {
                    // Progress is already shown
                }

                @Override
                public void onDownloadProgress(String videoId, int progress) {
                    // Update progress if needed (can add percentage text)
                }

                @Override
                public void onDownloadCompleted(String videoId, String filePath) {
                    // Update UI to show offline play button
                    btnDownload.setVisibility(View.GONE);
                    downloadProgress.setVisibility(View.GONE);
                    btnOfflinePlay.setVisibility(View.VISIBLE);
                    Toast.makeText(context, "تم تحميل الفيديو بنجاح", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onDownloadFailed(String videoId, String error) {
                    // Restore download button, hide progress
                    btnDownload.setVisibility(View.VISIBLE);
                    downloadProgress.setVisibility(View.GONE);
                    btnOfflinePlay.setVisibility(View.GONE);
                    Toast.makeText(context, "فشل في تحميل الفيديو: " + error, Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}