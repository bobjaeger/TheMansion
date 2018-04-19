package com.den.jaeger.themansion.game_feature;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;

import com.den.jaeger.themansion.game_feature.GameFragment.LobbyInterface;

import org.json.JSONException;
import org.json.JSONObject;

public class Game extends AppCompatActivity {

    RelativeLayout rootView;
    SharedPreferences prefs;

    String TAG = "Game";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        rootView = findViewById(R.id.rootView);
        prefs = getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE);

        readJSON(); //For example
        toRoomInterface();
    }

    private void toRoomInterface() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.rootView, new LobbyInterface());
        fragmentTransaction.commit();
    }

    private void readJSON() {
        try {
            JSONObject jsonObject = new JSONObject(Utilities.loadJSONFromAsset(this, Constants.JSON_LEVELS_ROOM));
            Log.d(TAG, "readJSON: "+jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        prefs.edit().clear().commit();
    }
}
