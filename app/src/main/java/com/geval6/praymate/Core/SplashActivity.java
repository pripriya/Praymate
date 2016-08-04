package com.geval6.praymate.Core;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends Activity {
    private static int SPLASH_TIME_OUT;

    static {
        SPLASH_TIME_OUT = 2000;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        startActivity(new Intent(this, !isUserLoggedIn() ? LaunchActivity.class : HomeActivity.class));
    }

    private boolean isUserLoggedIn() {
        return getSharedPreferences("LoginPreferences", 0).getBoolean("IsUserLoggedIn", false);
    }
}
