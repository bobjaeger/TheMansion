package com.den.jaeger.themansion.game_feature;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
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

    public static String currentFragment = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        rootView = findViewById(R.id.rootView);
        prefs = getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE);
        Utilities.startGame(this);

        readJSON(); //For example
        toLobbyInterface();
    }

    private void toLobbyInterface() {
        Game.currentFragment = Constants.FRAGMENT_LOBBY;
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
    public void onBackPressed() {
        if(Game.currentFragment.equals(Constants.FRAGMENT_ROOM)){
            final AlertDialog.Builder alertDialog = Utilities.showAlertDialog(this, Constants.MESSAGE_EXIT_ROOM,
                    "Your progress on this room won't be saved, are you sure?", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            toLobbyInterface();
                        }
                    });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            alertDialog.show();
        } else if(Game.currentFragment.equals(Constants.FRAGMENT_LOBBY)){
            final AlertDialog.Builder alertDialog = Utilities.showAlertDialog(this, Constants.MESSAGE_EXIT_GAME,
                    "Your progress on this game won't be saved, are you sure?", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Game.super.onBackPressed();
                        }
                    });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            alertDialog.show();
            //super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        prefs.edit().clear().commit();
    }
}
