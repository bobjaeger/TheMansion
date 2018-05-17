package com.den.jaeger.themansion.game_feature;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

public class MenuActivity extends AppCompatActivity {

    Button buttonStartGame, buttonHighScore, buttonAbout;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        prefs = getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE);
        prefs.edit().clear().commit();

        buttonStartGame = findViewById(R.id.buttonStartGame);
        buttonHighScore = findViewById(R.id.buttonHighScore);
        buttonAbout = findViewById(R.id.buttonAbout);

        buttonStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNameDialog();
            }
        });
        buttonHighScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toHighScore();
            }
        });
        buttonAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toAbout();
            }
        });
    }

    private void showNameDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_name);

        final SharedPreferences prefs =  getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE);

        final EditText formName = dialog.findViewById(R.id.formName);
        Button buttonClose = dialog.findViewById(R.id.buttonClose);
        Button buttonProceed = dialog.findViewById(R.id.buttonProceed);

        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        buttonProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefs.edit().putString(Constants.PREFS_NAME, formName.getText().toString()).commit();
                //toStartGame();
                showDifficultyDialog();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showDifficultyDialog() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        builderSingle.setTitle("Please Select Difficulty: ");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item);
        arrayAdapter.add("Easy: 5 Life");
        arrayAdapter.add("Medium: 4 Life");
        arrayAdapter.add("Hard: 3 Life");

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                switch(i){
                    case 0:
                        prefs.edit().putInt(Constants.PREFS_LIFE_NUMBER, 5).commit();
                        break;
                    case 1:
                        prefs.edit().putInt(Constants.PREFS_LIFE_NUMBER, 4).commit();
                        break;
                    case 2:
                        prefs.edit().putInt(Constants.PREFS_LIFE_NUMBER, 3).commit();
                        break;
                }
                toStartGame();
                dialog.dismiss();
            }
        });
        builderSingle.show();
    }

    private void toStartGame() {
        Intent intent = new Intent(this, Game.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void toHighScore() {
        Intent intent = new Intent(this, HighScore.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void toAbout() {
        Intent intent = new Intent(this, About.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }
}
