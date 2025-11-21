package com.elfadouaki.darelkilani;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import com.elfadouaki.darelkilani.model.VideoItem;

public class OfflineVideoPlayerActivity extends AppCompatActivity {

    private PlayerView playerView;
    private ExoPlayer player;
    private ImageButton btnBack;
    private TextView videoTitleOverlay;
    private VideoItem videoItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        hideSystemUI();

        setContentView(R.layout.activity_offline_video_player);

        initializeViews();
        setupVideo();
        setupClickListeners();
    }

    private void initializeViews() {
        playerView = findViewById(R.id.exoPlayerView);
        btnBack = findViewById(R.id.btnBackOffline);
        videoTitleOverlay = findViewById(R.id.videoTitleOverlay);
    }

    private void setupVideo() {
        String videoId = getIntent().getStringExtra("videoId");
        String title = getIntent().getStringExtra("title");
        String filePath = getIntent().getStringExtra("filePath");

        if (videoId != null && title != null && filePath != null) {
            videoTitleOverlay.setText(title);
            initializePlayer(filePath);
        } else {
            finish();
        }
    }

    private void initializePlayer(String filePath) {
        player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);

        Uri videoUri = Uri.parse("file://" + filePath);
        MediaItem mediaItem = MediaItem.fromUri(videoUri);

        player.setMediaItem(mediaItem);
        player.prepare();
        player.setPlayWhenReady(true);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        // Hide title overlay when video is playing
        playerView.setOnClickListener(v -> {
            if (videoTitleOverlay.getVisibility() == View.VISIBLE) {
                videoTitleOverlay.setVisibility(View.GONE);
                btnBack.setVisibility(View.GONE);
            } else {
                videoTitleOverlay.setVisibility(View.VISIBLE);
                btnBack.setVisibility(View.VISIBLE);
            }
        });
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

    @Override
    protected void onStart() {
        super.onStart();
        if (player != null) {
            player.setPlayWhenReady(true);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (player != null) {
            player.setPlayWhenReady(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
            player = null;
        }
    }
}