package com.tugasakhir.remindme.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import com.tugasakhir.remindme.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    TextView tvAppname;
    private static int splash_time = 5000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        tvAppname = findViewById(R.id.tv_appName);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent splashIntent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(splashIntent);
                finish();
            }
        },splash_time);
        Animation myAnimation = AnimationUtils.loadAnimation(SplashActivity.this,R.anim.splash_animation);
        tvAppname.startAnimation(myAnimation);

    }
}
