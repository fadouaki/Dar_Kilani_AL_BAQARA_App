package com.elfadouaki.darelkilani;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SplashScreen extends AppCompatActivity {

    private Handler splashHandler;
    private Runnable splashRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        startAnimations();
        scheduleTransition();
    }

    private void initializeViews() {

    }

    private void startAnimations() {
        LinearLayout mainLayout = findViewById(R.id.mainLayout);
        ProgressBar progressBar = findViewById(R.id.progressBar);

        ObjectAnimator rotationAnimator = ObjectAnimator.ofFloat(progressBar, "rotation", 0f, 360f);
        rotationAnimator.setDuration(2000);
        rotationAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        rotationAnimator.start();
    }

    private void scheduleTransition() {
        splashHandler = new Handler(Looper.getMainLooper());
        splashRunnable = () -> {
            if (!isFinishing()) {
                startActivity(new Intent(SplashScreen.this, MainActivity.class));
                finish();
            }
        };

        splashHandler.postDelayed(splashRunnable, 3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (splashHandler != null && splashRunnable != null) {
            splashHandler.removeCallbacks(splashRunnable);
        }
    }
}
