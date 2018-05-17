package com.den.jaeger.themansion.game_feature.GameFragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.den.jaeger.themansion.game_feature.Constants;
import com.den.jaeger.themansion.game_feature.Game;
import com.den.jaeger.themansion.game_feature.MenuActivity;
import com.den.jaeger.themansion.game_feature.R;
import com.den.jaeger.themansion.game_feature.Utilities;
import com.den.jaeger.themansion.game_feature.model.AnswerModel;
import com.den.jaeger.themansion.game_feature.model.QuestionModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RoomInterface extends Fragment {
    View v;
    TextView textRoomName, textNote, textExitRoom, textLifeNumber;
    TextView textQuestion;
    RelativeLayout answer1, answer2, answer3, answer4;
    TextView textAnswer1, textAnswer2, textAnswer3, textAnswer4;
    SharedPreferences prefs;
    int lifeNumber;
    int roomId;
    String roomName;
    QuestionModel currentQuestion;
    Set<String> visitedQuestion;
    String TAG = "RoomInterface";

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_room_interface, container, false);
        prefs = getActivity().getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE);
        lifeNumber = prefs.getInt(Constants.PREFS_LIFE_NUMBER, 3);
        visitedQuestion = prefs.getStringSet(Constants.PREFS_VISITED_QUESTION, new HashSet<String>());

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

        textRoomName.setText(roomName);
        textLifeNumber.setText(String.valueOf(lifeNumber));

        textNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utilities.openNote(getActivity());
            }
        });

        answer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAnswer(0);
            }
        });
        answer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAnswer(1);
            }
        });
        answer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAnswer(2);
            }
        });
        answer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAnswer(3);
            }
        });

        textExitRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showExitRoomDialog();
            }
        });

        loadFirstQuestion();

        return v;
    }

    private void selectAnswer(int i) {
        Utilities.playSound(getActivity(), Constants.AUDIO_ANSWER);

        List<AnswerModel> answerModelList = currentQuestion.getAnswerLists();
        AnswerModel answerModel = answerModelList.get(i);
        //Log.d(TAG, "selectAnswer: "+answerModel.getAnswer());
        int nextQid = answerModel.getNextQid();
        //Log.d(TAG, "selectAnswer: nextQid: "+nextQid);
        String anotherDialog = answerModel.getAnotherDialog();
        final int effect = answerModel.getEffect();
        if(!anotherDialog.isEmpty()){
            AlertDialog.Builder alertDialog = Utilities.showAlertDialog(getActivity(), Constants.MESSAGE, anotherDialog,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            toEffect(effect);
                            dialogInterface.dismiss();
                        }
                    });
            alertDialog.show();
        } else{
            toEffect(effect);
        }
        if(nextQid!=0){
            loadQuestionById(nextQid);
        }
    }

    private void toEffect(int effect) {
        switch (effect){
            case 1:
                AlertDialog.Builder alertDialog = Utilities.showAlertDialog(getActivity(), Constants.MESSAGE_CANDLE_BLOWN,
                        "You lost your life -1", null);
                alertDialog.show();
                decreaseLife();
                break;
            case 2:
                String title = null;
                String content = null;
                if(visitedQuestion.contains(String.valueOf(currentQuestion.getQid()))){
                    title = "Candle already acquired.";
                    content = "No gain on life";
                } else{
                    Utilities.playSound(getActivity(), Constants.AUDIO_LIFE_PLUS);

                    title = Constants.MESSAGE_CANDLE_ACQUIRED;
                    content = "You gained your life +1";
                    increaseLife();
                }
                alertDialog = Utilities.showAlertDialog(getActivity(), title,
                        content, null);
                alertDialog.show();
                break;
            case 3:
                alertDialog = Utilities.showAlertDialog(getActivity(), Constants.MESSAGE_ROOM_FINISHED,
                        "You finished this room, you can explore the other room or explore further to get the key",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                exitRoom();
                                dialogInterface.dismiss();
                            }
                        });
                alertDialog.show();
                break;
            case 4:
                Utilities.playSound(getActivity(), Constants.AUDIO_KEY);

                alertDialog = Utilities.showAlertDialog(getActivity(), Constants.MESSAGE_KEY_ACQUIRED,
                        "You found the key, you can now escape the mansion",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Utilities.endGame(getActivity());
                                prefs.edit().clear().commit();
                                toMenu();
                                dialogInterface.dismiss();
                            }
                        });
                alertDialog.show();
                break;
        }
        //Log.d(TAG, "selectAnswer: "+visitedQuestion+", "+currentQuestion.getQid());
    }

    private void increaseLife() {
        visitedQuestion.add(String.valueOf(currentQuestion.getQid()));
        prefs.edit().putStringSet(Constants.PREFS_VISITED_QUESTION, visitedQuestion).commit();

        Utilities.playSound(getActivity(), Constants.AUDIO_LIFE_PLUS);

        lifeNumber+=1;
        prefs.edit().putInt(Constants.PREFS_LIFE_NUMBER, lifeNumber).commit();
        textLifeNumber.setText(String.valueOf(lifeNumber));
    }

    private void decreaseLife() {
        lifeNumber-=1;
        prefs.edit().putInt(Constants.PREFS_LIFE_NUMBER, lifeNumber).commit();
        textLifeNumber.setText(String.valueOf(lifeNumber));
        if(lifeNumber==0){
            Utilities.playSound(getActivity(), Constants.AUDIO_DEATH);
            AlertDialog.Builder alertDialog = Utilities.showAlertDialog(getActivity(), Constants.MESSAGE_YOU_DIED,
                    "...",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            prefs.edit().clear().commit();
                            toMenu();
                            dialogInterface.dismiss();
                        }
                    });
            alertDialog.show();
        } else{
            Utilities.playSound(getActivity(), Constants.AUDIO_LIFE_MINUS);
        }
    }

    private void toMenu() {
        Intent intent = new Intent(getActivity(), MenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void loadQuestionById(int id) {
        String jsonFileName = Constants.JSON_QUESTION+String.valueOf(roomId)+".json";
        try {
            JSONArray jsonArray = new JSONArray(Utilities.loadJSONFromAsset(getActivity(), jsonFileName));
            for(int i=0; jsonArray.length()>i; i++){
                JSONObject obj = jsonArray.getJSONObject(i);
                int qid = obj.getInt("qid");
                if(qid==id){
                    String question = obj.getString("question");
                    JSONArray answersArray = obj.getJSONArray("answers");
                    List<AnswerModel> answerModelList = new ArrayList<>();
                    for(int j=0; answersArray.length()>j; j++){
                        JSONObject answersObj = answersArray.getJSONObject(j);
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
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void exitRoom() {
        Game.currentFragment = Constants.FRAGMENT_LOBBY;
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.rootView, new LobbyInterface());
        fragmentTransaction.commit();
    }

    private void showExitRoomDialog(){
        final AlertDialog.Builder alertDialog = Utilities.showAlertDialog(getActivity(), Constants.MESSAGE_EXIT_GAME,
                "Your progress on this game won't be saved, are you sure?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        exitRoom();
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
            //Log.d(TAG, "parseQuestion: "+i);
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