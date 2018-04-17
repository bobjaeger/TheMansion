package com.den.jaeger.themansion.game_feature;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class Game extends AppCompatActivity {

    RelativeLayout rootView;
    String TAG = "Game";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        rootView = findViewById(R.id.rootView);

        readJSON(); //For example
    }

    private void readJSON() {
        try {
            JSONObject jsonObject = new JSONObject(Utilities.loadJSONFromAsset(this, Constants.JSON_LEVELS_ROOM));
            Log.d(TAG, "readJSON: "+jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
