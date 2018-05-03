package com.den.jaeger.themansion.game_feature;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.den.jaeger.themansion.game_feature.DBHandler.DBHighScore;
import com.den.jaeger.themansion.game_feature.adapter.HighScoreAdapter;
import com.den.jaeger.themansion.game_feature.model.HighScoreModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HighScore extends AppCompatActivity {

    RecyclerView recyclerViewHighScore;
    DBHighScore dbHighScore;
    HighScoreAdapter adapterHighScore;
    Button buttonHighScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);
        recyclerViewHighScore = findViewById(R.id.recyclerViewHighScore);
        buttonHighScore = findViewById(R.id.buttonDeleteHighScore);

        dbHighScore = new DBHighScore(this);
        //dbHighScore.dropTable(); // To delete database
        //addDummyHighScoreToDB(); // To add dummy data comment out to disable

        getHighScoreFromDB();

        buttonHighScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetHighScore();
            }
        });
    }

    private void resetHighScore() {
        AlertDialog.Builder alertDialog = Utilities.showAlertDialog(this, Constants.MESSAGE_ARE_YOU_SURE,
                "Clicking OK button will permanently delete the High score list.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dbHighScore.dropTable();
                        getHighScoreFromDB();
                    }
                });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    private void getHighScoreFromDB() {
        List<HighScoreModel> highScoreModelList = dbHighScore.getHighScores();
        sortByTimeFinished(highScoreModelList);

        adapterHighScore = new HighScoreAdapter(highScoreModelList);
        recyclerViewHighScore.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewHighScore.setHasFixedSize(true);
        recyclerViewHighScore.setAdapter(adapterHighScore);
        adapterHighScore.notifyDataSetChanged();
    }

    private void sortByTimeFinished(List<HighScoreModel> highScoreModelList) {
        Collections.sort(highScoreModelList, new Comparator<HighScoreModel>() {
            @Override
            public int compare(HighScoreModel lhs, HighScoreModel rhs) {
                return lhs.getTimeFinished() - rhs.getTimeFinished();
            }
        });
    }

    private void addDummyHighScoreToDB() {
        List<HighScoreModel> highScoreModelList = new ArrayList<>();
        highScoreModelList.add(new HighScoreModel("name", 1235235));
        highScoreModelList.add(new HighScoreModel("name1", 10));
        for(HighScoreModel highScoreModel: highScoreModelList){
            dbHighScore.addHighScore(highScoreModel);
        }
    }
}
