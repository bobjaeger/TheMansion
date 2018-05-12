package com.den.jaeger.themansion.game_feature;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SplashScreen extends AppCompatActivity {

    String TAG = "SplashScreen";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Log.d(TAG, "onCreate: SPLASHSCREEN");
        Intent intent = new Intent(SplashScreen.this, MenuActivity.class);
        startActivity(intent);
        finish();
    }
}
