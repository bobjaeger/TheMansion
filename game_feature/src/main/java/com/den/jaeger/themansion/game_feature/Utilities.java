package com.den.jaeger.themansion.game_feature;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.den.jaeger.themansion.game_feature.DBHandler.DBHighScore;
import com.den.jaeger.themansion.game_feature.model.HighScoreModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Utilities {
    static String TAG = "Utilities";
    public static String loadJSONFromAsset(Activity activity, String fileName) {
        String json = null;
        try {
            InputStream is = activity.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static void openNote(final Activity activity) {
        Utilities.playSound(activity, Constants.AUDIO_OPEN_NOTE);

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_notes);

        final SharedPreferences prefs =  activity.getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE);
        String notes = prefs.getString(Constants.PREFS_NOTE, "");

        final EditText formNote = dialog.findViewById(R.id.formNote);
        Button buttonClose = dialog.findViewById(R.id.buttonClose);
        Button buttonSave = dialog.findViewById(R.id.buttonSave);
        formNote.setText(notes);

        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utilities.playSound(activity, Constants.AUDIO_CLOSE_NOTE);

                dialog.dismiss();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utilities.playSound(activity, Constants.AUDIO_CLOSE_NOTE);

                prefs.edit().putString(Constants.PREFS_NOTE, formNote.getText().toString()).commit();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void startGame(Activity activity){
        SharedPreferences prefs =  activity.getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE);
        Date currentTime = Calendar.getInstance().getTime();
        Log.d(TAG, "startGame: "+currentTime.getTime());
        prefs.edit().putLong(Constants.PREFS_TIME_START, currentTime.getTime()).commit();
    }

    public static void endGame(Activity activity){
        SharedPreferences prefs =  activity.getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE);
        String name = prefs.getString(Constants.PREFS_NAME, "");

        long endTime = Calendar.getInstance().getTime().getTime();
        long startTime = prefs.getLong(Constants.PREFS_TIME_START, 0);
        int secondsDifference = (int) TimeUnit.MILLISECONDS.toSeconds(endTime-startTime);
        Log.d(TAG, "endGame: "+secondsDifference+" seconds");

        DBHighScore dbHighScore = new DBHighScore(activity);
        dbHighScore.addHighScore(new HighScoreModel(name, secondsDifference));
    }

    public static AlertDialog.Builder showAlertDialog(Activity activity, String title, String message, DialogInterface.OnClickListener clickListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message)
                .setTitle(title);
        builder.setCancelable(false);

        if(clickListener==null){
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                    dialog.dismiss();
                }
            });
        } else{
            Log.d(TAG, "showAlertDialog: exit");
            builder.setPositiveButton("OK", clickListener);
        }

        return builder;
    }

    public static void playSound(Activity activity, int i){
        int audio = 0;
        switch(i){
            case Constants.AUDIO_ANSWER:
                audio = R.raw.answer;
                break;
            case Constants.AUDIO_CLOSE_NOTE:
                audio = R.raw.close_note;
                break;
            case Constants.AUDIO_DEATH:
                audio = R.raw.death;
                break;
            case Constants.AUDIO_KEY:
                audio = R.raw.key;
                break;
            case Constants.AUDIO_LIFE_MINUS:
                audio = R.raw.life_minus;
                break;
            case Constants.AUDIO_LIFE_PLUS:
                audio = R.raw.life_plus;
                break;
            case Constants.AUDIO_MOVE_LVL:
                audio = R.raw.move_lvl;
                break;
            case Constants.AUDIO_OPEN_NOTE:
                audio = R.raw.open_note;
                break;
            case Constants.AUDIO_ROOM_DOOR:
                audio = R.raw.room_door;
                break;
        }
        MediaPlayer mp = MediaPlayer.create(activity, audio);
        mp.setLooping(false);
        mp.start();
        Log.d(TAG, "playSound: audio played");
    }
}
