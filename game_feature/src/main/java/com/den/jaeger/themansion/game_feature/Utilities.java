package com.den.jaeger.themansion.game_feature;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.InputStream;

public class Utilities {
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

    public static void openNote(Activity activity) {
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
                dialog.dismiss();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefs.edit().putString(Constants.PREFS_NOTE, formNote.getText().toString()).commit();
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
