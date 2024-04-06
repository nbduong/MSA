package com.example.msa;


import android.annotation.SuppressLint;

import android.content.DialogInterface;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AlertDialog;
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

    private TextView rateCount,showRating;
    private EditText review;
    private Button submit;
    private DatabaseHelper dbHelper;
    private RatingBar ratingBar;
    float rateValue; String temp;
    Cursor cursor;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_player);

        //thanh điều hướng



        frameLayout = findViewById(R.id.frameLayout);
        videoPlayer = findViewById(R.id.videoView);
        fullScreenOp = findViewById(R.id.fullScreenOp);
        progressBar = findViewById(R.id.videoProgressBar);


        rateCount=findViewById(R.id.rateCount);
        showRating=findViewById(R.id.showRating);
        review=findViewById(R.id.review);
        submit=findViewById(R.id.submitBtn);
        ratingBar=findViewById(R.id.ratingBar);
        dbHelper = new DatabaseHelper(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //lấy dữ liệu về phim
        Intent i = getIntent();
        Bundle data = i.getExtras();
        Video v = (Video) data.getSerializable("videoData");


        TextView title = findViewById(R.id.videoTitle);
        TextView desc = findViewById(R.id.videoDesc);
        title.setText(v.getTitle());
        desc.setText(v.getDescription());


        Uri videoUrl = Uri.parse("https" + v.getVideoUrl().substring(4));
        videoPlayer.setVideoURI(videoUrl);
        MediaController mc = new MediaController(this);
        videoPlayer.setMediaController(mc);
        showResumeDialog();
        videoPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoPlayer.start();
                progressBar.setVisibility(View.GONE);
            }
        });


        // xử lý full màn hình
        fullScreenOp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFullScreen) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    isFullScreen = true;
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                    isFullScreen = false;
                }
            }
        });

        // xử lý các view để khớp với màn hình sau khi xoay
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








        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rateValue = ratingBar.getRating();

                // Corrected if-else conditions for clarity and accuracy
                if (rateValue <= 1 && rateValue>0) {
                    rateCount.setText("Bad " + rateValue + "/5");
                } else if (rateValue <= 2 && rateValue>1) {
                    rateCount.setText("Ok " + rateValue + "/5");
                } else if (rateValue <= 3 && rateValue>2) {
                    rateCount.setText("Good " + rateValue + "/5");
                } else if (rateValue <= 4 && rateValue>3) {
                    rateCount.setText("Very " + rateValue + "/5");
                } else { // rateValue is 5
                    rateCount.setText("Best " + rateValue + "/5");
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temp=rateCount.getText().toString();
                showRating.setText("đánh giá của bạn:\n"+temp+"\n"+review.getText());

                rateValue=ratingBar.getRating();
                String name = "Your Name"; // Thay bằng tên người dùng thực tế
                boolean result = dbHelper.addRating(name, rateValue, review.getText().toString());
                if (result) {
                    Toast.makeText(Player.this, "Đã gửi đánh giá", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Player.this, "Lỗi khi gửi đánh giá", Toast.LENGTH_SHORT).show();
                }
                String ratingsText = dbHelper.getAllRatingsAsString();
                showRating.setText(ratingsText);
                review.setText("");
                ratingBar.setRating(0);
                rateCount.setText("");
            }

        });
        String ratingsText = dbHelper.getAllRatingsAsString();
        showRating.setText(ratingsText);






    }

    // xử lý nút phóng to sẽ di chuyển sau khi ấn
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


    // sự kiện quay về trang home
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    // sự kiện xem tiếp từ 1 vị trí đã dừng trước đó
    private void showResumeDialog() {
        Intent i = getIntent();
        Bundle data = i.getExtras();
        Video v = (Video) data.getSerializable("videoData");
        SharedPreferences preferences = getSharedPreferences("VideoPositions", MODE_PRIVATE);
        int savedPosition = preferences.getInt(v.getTitle().replaceAll("\\s+", ""), 0);
        if (savedPosition != 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Resume video");
            builder.setMessage("Bạn có muốn xem tiếp phim từ vị trí bạn dừng trước đó không?");
            builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences preferences = getSharedPreferences("VideoPositions", MODE_PRIVATE);
                    int savedPosition = preferences.getInt(v.getTitle().replaceAll("\\s+", ""), 0);
                    videoPlayer.seekTo(savedPosition);
                    videoPlayer.start();
                }
            });
            builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing or dismiss dialog
                }
            });
            builder.show();
        }
    }


    // sự kiện bắt vị trí đang xem khi ấn dừng video
    @Override
    protected void onPause() {
        super.onPause();
        Intent i = getIntent();
        Bundle data = i.getExtras();
        Video v = (Video) data.getSerializable("videoData");
        SharedPreferences preferences = getSharedPreferences("VideoPositions", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(v.getTitle().replaceAll("\\s+", ""), videoPlayer.getCurrentPosition());
        editor.apply();
    }


}
