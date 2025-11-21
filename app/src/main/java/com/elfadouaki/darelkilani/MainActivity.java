package com.elfadouaki.darelkilani;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.BuildConfig;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private LinearLayout[] thumunLayouts;
    private LinearLayout[] hizbButtons;
    private boolean[] isExpanded = {false, false, false, false, false};
    private ScrollView scrollView;
    private CardView aboutApp;
    private RequestQueue requestQueue;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        setupClickListeners();

        requestQueue = Volley.newRequestQueue(this);
    }

    private void initializeViews() {
        scrollView = findViewById(R.id.scrollView);
        aboutApp = findViewById(R.id.aboutApp);

        thumunLayouts = new LinearLayout[5];
        hizbButtons = new LinearLayout[5];

        thumunLayouts[0] = findViewById(R.id.layoutThumun1);
        thumunLayouts[1] = findViewById(R.id.layoutThumun2);
        thumunLayouts[2] = findViewById(R.id.layoutThumun3);
        thumunLayouts[3] = findViewById(R.id.layoutThumun4);
        thumunLayouts[4] = findViewById(R.id.layoutThumun5);

        hizbButtons[0] = findViewById(R.id.btnHizb1);
        hizbButtons[1] = findViewById(R.id.btnHizb2);
        hizbButtons[2] = findViewById(R.id.btnHizb3);
        hizbButtons[3] = findViewById(R.id.btnHizb4);
        hizbButtons[4] = findViewById(R.id.btnHizb5);
    }

    private void setupClickListeners() {
        for (int i = 0; i < 5; i++) {
            final int hizbIndex = i;
            hizbButtons[i].setOnClickListener(v -> toggleThumunList(hizbIndex));
        }

        aboutApp.setOnClickListener(v -> showAboutDialog());

        setupThumunClickListeners();
    }

    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_about, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        ImageView facebookIcon = dialogView.findViewById(R.id.facebook_icon);
        ImageView closeButton= dialogView.findViewById(R.id.ic_close);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              dialog.cancel();
            }
        });
        facebookIcon.setOnClickListener(v -> {
            String url = "https://www.facebook.com/profile.php?id=100086541783034";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        });


        dialog.show();
    }

    private void setupThumunClickListeners() {
        int[] thumunIds = {
                R.id.btnThumun1_1, R.id.btnThumun1_2, R.id.btnThumun1_3, R.id.btnThumun1_4,
                R.id.btnThumun1_5, R.id.btnThumun1_6, R.id.btnThumun1_7, R.id.btnThumun1_8,
                R.id.btnThumun2_1, R.id.btnThumun2_2, R.id.btnThumun2_3, R.id.btnThumun2_4,
                R.id.btnThumun2_5, R.id.btnThumun2_6, R.id.btnThumun2_7, R.id.btnThumun2_8,
                R.id.btnThumun3_1, R.id.btnThumun3_2, R.id.btnThumun3_3, R.id.btnThumun3_4,
                R.id.btnThumun3_5, R.id.btnThumun3_6, R.id.btnThumun3_7, R.id.btnThumun3_8,
                R.id.btnThumun4_1, R.id.btnThumun4_2, R.id.btnThumun4_3, R.id.btnThumun4_4,
                R.id.btnThumun4_5, R.id.btnThumun4_6, R.id.btnThumun4_7, R.id.btnThumun4_8,
                R.id.btnThumun5_1, R.id.btnThumun5_2, R.id.btnThumun5_3, R.id.btnThumun5_4,
                R.id.btnThumun5_5, R.id.btnThumun5_6, R.id.btnThumun5_7, R.id.btnThumun5_8
        };

        for (int i = 0; i < thumunIds.length; i++) {
            View thumunButton = findViewById(thumunIds[i]);
            final int hizbNumber = (i / 8) + 1;
            final int thumunNumber = (i % 8) + 1;

            if (i == 39) { // Skip Thumun 8 in Hizb 5
                thumunButton.setOnClickListener(v -> checkForUpdate());
                continue;
            }

            thumunButton.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, PageVideosActivity.class);
                intent.putExtra("hizb", hizbNumber);
                intent.putExtra("thumun", thumunNumber);
                startActivity(intent);
            });
        }
    }

    private void checkForUpdate() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Checking for updates...");
        progressDialog.show();

        String url = getString(R.string.update_url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        int latestVersionCode = jsonObject.getInt("versionCode");
                        String downloadUrl = jsonObject.getString("downloadUrl");

                        int currentVersionCode = BuildConfig.VERSION_CODE;

                        if (latestVersionCode > currentVersionCode) {
                            new AlertDialog.Builder(this)
                                    .setTitle("Update Available")
                                    .setMessage("A new version of the app is available. Please update to the latest version.")
                                    .setPositiveButton("Update", (dialog, which) -> {
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        intent.setData(Uri.parse(downloadUrl));
                                        startActivity(intent);
                                    })
                                    .setNegativeButton("Cancel", null)
                                    .show();
                        } else {
                            Toast.makeText(this, "You have the latest version.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(this, "Error parsing update information.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Failed to check for updates.", Toast.LENGTH_SHORT).show();
                });

        requestQueue.add(stringRequest);
    }

    private void toggleThumunList(int hizbIndex) {
        LinearLayout layout = thumunLayouts[hizbIndex];

        if (isExpanded[hizbIndex]) {
            Animation slideUp = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
            layout.startAnimation(slideUp);
            layout.setVisibility(View.GONE);
            isExpanded[hizbIndex] = false;
        } else {
            layout.setVisibility(View.VISIBLE);
            Animation slideDown = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
            layout.startAnimation(slideDown);
            isExpanded[hizbIndex] = true;

            // Auto-scroll for Hizb 4 and 5 (index 3 and 4)
            if (hizbIndex == 3 || hizbIndex == 4) {
                scrollView.postDelayed(() -> {
                    scrollView.fullScroll(View.FOCUS_DOWN);
                }, 300); // Delay to allow animation to start
            }
        }
    }
}