package com.example.msa;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.widget.Toolbar;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.net.URI;
import java.util.Objects;


public class Player extends AppCompatActivity {

    private static final String TAG = "Player";
    private boolean isFullScreen = false;
    private int currentPosition = 0;
    private Toolbar toolbar;
    private FrameLayout frameLayout;
    private VideoView videoPlayer;
    private ImageView fullScreenOp;

    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_player);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        frameLayout = findViewById(R.id.frameLayout);
        videoPlayer = findViewById(R.id.videoView);
        fullScreenOp = findViewById(R.id.fullScreenOp);
        progressBar = findViewById(R.id.videoProgressBar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        Bundle data = i.getExtras();
        Video v = (Video) data.getSerializable("videoData");
        getSupportActionBar().setTitle(v.getTitle());

        TextView title = findViewById(R.id.videoTitle);
        TextView desc = findViewById(R.id.videoDesc);

        title.setText(v.getTitle());
        desc.setText(v.getDescription());
        Uri videoUrl = Uri.parse("https" + v.getVideoUrl().substring(4));
        videoPlayer.setVideoURI(videoUrl);
        MediaController mc = new MediaController(this);
        videoPlayer.setMediaController(mc);

        videoPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoPlayer.start();
                progressBar.setVisibility(View.GONE);
            }
        });

        fullScreenOp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFullScreen) {
                    toolbar.setVisibility(View.INVISIBLE);
                    currentPosition = videoPlayer.getCurrentPosition();
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    isFullScreen = true;
                } else {
                    toolbar.setVisibility(View.VISIBLE);
                    videoPlayer.seekTo(currentPosition);
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                    isFullScreen = false;
                }
            }
        });

        OrientationEventListener orientationEventListener = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int orientation) {
                if (isFullScreen) {
                    frameLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
                    videoPlayer.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
                } else {
                    frameLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
                    videoPlayer.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
                }
            }
        };
        orientationEventListener.enable();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updateFullScreenButtonMargin();
    }

    private void updateFullScreenButtonMargin() {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) fullScreenOp.getLayoutParams();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            params.setMargins(32, 32, 128, 32);
        } else {
            params.setMargins(16, 16, 16, 16);
        }
        fullScreenOp.setLayoutParams(params);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
