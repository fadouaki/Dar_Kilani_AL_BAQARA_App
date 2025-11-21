package com.elfadouaki.darelkilani;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.elfadouaki.darelkilani.model.VideoItem;

public class VideoPlayerActivity extends AppCompatActivity {

    private WebView webView;
    private ImageButton btnBack;
    private VideoItem videoItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        hideSystemUI();

        setContentView(R.layout.activity_video_player);

        initializeViews();
        setupVideo();
        setupClickListeners();
    }

    private void initializeViews() {
        webView = findViewById(R.id.webViewPlayer);
        btnBack = findViewById(R.id.btnBack);
    }

    private void setupVideo() {
        String videoId = getIntent().getStringExtra("videoId");
        String title = getIntent().getStringExtra("title");
        String pageId = getIntent().getStringExtra("pageId");

        if (videoId != null && pageId != null) {
            videoItem = new VideoItem(videoId, title, pageId);
            loadVideo();
        }
    }

    private void loadVideo() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(false);
        webSettings.setUseWideViewPort(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setSupportZoom(false);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // Inject CSS to fix video positioning and JavaScript to unmute
                String script = "javascript:(function() {" +
                        "var style = document.createElement('style');" +
                        "style.innerHTML = 'body { margin: 0 !important; padding: 0 !important; overflow: hidden !important; } " +
                        "iframe, video { position: fixed !important; top: 0 !important; left: 0 !important; " +
                        "width: 100% !important; height: 100% !important; border: none !important; }';" +
                        "document.head.appendChild(style);" +
                        "setTimeout(function() {" +
                        "var videos = document.querySelectorAll('video');" +
                        "for(var i = 0; i < videos.length; i++) {" +
                        "videos[i].muted = false;" +
                        "videos[i].play();" +
                        "}" +
                        "var iframes = document.querySelectorAll('iframe');" +
                        "for(var j = 0; j < iframes.length; j++) {" +
                        "try {" +
                        "var iframeDoc = iframes[j].contentDocument || iframes[j].contentWindow.document;" +
                        "var iframeVideos = iframeDoc.querySelectorAll('video');" +
                        "for(var k = 0; k < iframeVideos.length; k++) {" +
                        "iframeVideos[k].muted = false;" +
                        "iframeVideos[k].play();" +
                        "}" +
                        "} catch(e) {}" +
                        "}" +
                        "}, 1000);" +
                        "})()";
                view.loadUrl(script);
            }
        });
        webView.setInitialScale(100);

        String embedUrl = videoItem.getEmbedUrl();
        webView.loadUrl(embedUrl);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());
    }

    private void hideSystemUI() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    private void showSystemUI() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }

}