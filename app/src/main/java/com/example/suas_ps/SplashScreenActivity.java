package com.example.suas_ps;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {
    private MySharedPreferences mySharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Tools.setSystemBarColor(this, R.color.colorPrimaryDark);
        mySharedPreferences = new MySharedPreferences(this);
        doSomethingAfter(2, new Runnable() {
            @Override
            public void run() {
                Intent intent;

                if (mySharedPreferences.isLoggedIn()){
                    intent = (new Intent(SplashScreenActivity.this, DashboardActivity.class));
                }else {
                    intent = (new Intent(SplashScreenActivity.this, SingInActivity.class));
                }
                startActivity(intent);
                finish();

            }
        });

    }


    public static Handler handler = new Handler();
    private static Runnable mRunnable;

    public static void doSomethingAfter(double seconds, Runnable runnable) {
        handler.removeCallbacks(mRunnable);
        mRunnable = runnable;
        handler.postDelayed(runnable, (long) (seconds * 1000));

    }


}