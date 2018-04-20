package com.den.jaeger.themansion.game_feature.GameFragment;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.den.jaeger.themansion.game_feature.Constants;
import com.den.jaeger.themansion.game_feature.R;
import com.den.jaeger.themansion.game_feature.Utilities;
import com.den.jaeger.themansion.game_feature.model.AnswerModel;
import com.den.jaeger.themansion.game_feature.model.QuestionModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RoomInterface extends Fragment {
    View v;
    TextView textRoomName, textNote, textExitRoom, textLifeNumber;
    TextView textQuestion;
    RelativeLayout answer1, answer2, answer3, answer4;
    TextView textAnswer1, textAnswer2, textAnswer3, textAnswer4;
    SharedPreferences prefs;
    int lifeNumber;
    int roomId;
    QuestionModel currentQuestion;
    String TAG = "RoomInterface";

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_room_interface, container, false);
        prefs = getActivity().getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE);
        lifeNumber = prefs.getInt(Constants.PREFS_LIFE_NUMBER, 3);

        textRoomName = v.findViewById(R.id.textRoomName);
        textNote = v.findViewById(R.id.textNote);
        textExitRoom = v.findViewById(R.id.textExitRoom);
        textLifeNumber = v.findViewById(R.id.textLifeNumber);
        textQuestion = v.findViewById(R.id.textQuestion);

        answer1 = v.findViewById(R.id.answer1);
        answer2 = v.findViewById(R.id.answer2);
        answer3 = v.findViewById(R.id.answer3);
        answer4 = v.findViewById(R.id.answer4);

        textAnswer1 = v.findViewById(R.id.textAnswer1);
        textAnswer2 = v.findViewById(R.id.textAnswer2);
        textAnswer3 = v.findViewById(R.id.textAnswer3);
        textAnswer4 = v.findViewById(R.id.textAnswer4);

        textLifeNumber.setText(String.valueOf(lifeNumber));

        textNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utilities.openNote(getActivity());
            }
        });

        textExitRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        answer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        answer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        answer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        answer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        loadFirstQuestion();

        return v;
    }

    private void loadFirstQuestion() {
        try {
            String jsonFileName = Constants.JSON_QUESTION+String.valueOf(roomId)+".json";
            JSONArray jsonArray = new JSONArray(Utilities.loadJSONFromAsset(getActivity(), jsonFileName));
            JSONObject obj = jsonArray.getJSONObject(0);

            int qid = obj.getInt("qid");
            String question = obj.getString("question");
            JSONArray answersArray = obj.getJSONArray("answers");
            List<AnswerModel> answerModelList = new ArrayList<>();
            for(int i=0; answersArray.length()>i; i++){
                JSONObject answersObj = answersArray.getJSONObject(i);
                String answer = null;
                int nextQid = 0;
                String anotherDialog = null;
                int effect = 0;
                if(answersObj.has("answer")){
                    answer = answersObj.getString("answer");
                }
                if(answersObj.has("next_qid")){
                    nextQid = answersObj.getInt("next_qid");
                }
                if(answersObj.has("another_dialog")){
                    anotherDialog = answersObj.getString("another_dialog");
                }
                if(answersObj.has("effect")){
                    effect = answersObj.getInt("effect");
                }
                answerModelList.add(new AnswerModel(nextQid, answer, anotherDialog, effect));
            }
            currentQuestion = new QuestionModel(qid, question, answerModelList);
            parseQuestion();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseQuestion() {
        textQuestion.setText(currentQuestion.getQuestion());
        List<AnswerModel> answerModelList = currentQuestion.getAnswerLists();
        answer1.setVisibility(View.INVISIBLE);
        answer2.setVisibility(View.INVISIBLE);
        answer3.setVisibility(View.INVISIBLE);
        answer4.setVisibility(View.INVISIBLE);
        for(int i=0; currentQuestion.getAnswerLists().size()>i; i++){
            AnswerModel answerModel = answerModelList.get(i);
            Log.d(TAG, "parseQuestion: "+i);
            switch(i){
                case 0:
                    answer1.setVisibility(View.VISIBLE);
                    textAnswer1.setText(answerModel.getAnswer());
                    break;
                case 1:
                    answer2.setVisibility(View.VISIBLE);
                    textAnswer2.setText(answerModel.getAnswer());
                    break;
                case 2:
                    answer3.setVisibility(View.VISIBLE);
                    textAnswer3.setText(answerModel.getAnswer());
                    break;
                case 3:
                    answer4.setVisibility(View.VISIBLE);
                    textAnswer4.setText(answerModel.getAnswer());
                    break;
            }
        }
    }
}
