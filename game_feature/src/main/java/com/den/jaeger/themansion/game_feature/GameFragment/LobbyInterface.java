package com.den.jaeger.themansion.game_feature.GameFragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.den.jaeger.themansion.game_feature.Constants;
import com.den.jaeger.themansion.game_feature.Game;
import com.den.jaeger.themansion.game_feature.R;
import com.den.jaeger.themansion.game_feature.Utilities;
import com.den.jaeger.themansion.game_feature.model.RoomModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

public class LobbyInterface extends Fragment {
    TextView textLevelName, textNote, textLifeNumber, textChangeLevel;
    TextView textRoom1Name, textRoom2Name, textRoom3Name, textRoom4Name;
    TextView textRoom1VisitStatus, textRoom2VisitStatus, textRoom3VisitStatus, textRoom4VisitStatus;
    RelativeLayout room1, room2, room3, room4;
    View v;
    SharedPreferences prefs;
    int floorNumber, lifeNumber;

    RoomModel room1Model, room2Model, room3Model, room4Model;

    Set<String> roomSet = new HashSet<>();
    String TAG = "LobbyInterface";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_lobby_interface, container, false);
        prefs = getActivity().getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE);
        floorNumber = prefs.getInt(Constants.PREFS_FLOOR_NUMBER, 1);
        lifeNumber = prefs.getInt(Constants.PREFS_LIFE_NUMBER, 3);
        roomSet = prefs.getStringSet(Constants.PREFS_ROOM_SET, new HashSet<String>());

        textLevelName = v.findViewById(R.id.textLevelName);
        textNote = v.findViewById(R.id.textNote);
        textLifeNumber = v.findViewById(R.id.textLifeNumber);
        textChangeLevel = v.findViewById(R.id.textChangeLevel);
        textRoom1Name = v.findViewById(R.id.textRoom1Name);
        textRoom2Name = v.findViewById(R.id.textRoom2Name);
        textRoom3Name = v.findViewById(R.id.textRoom3Name);
        textRoom4Name = v.findViewById(R.id.textRoom4Name);
        textRoom1VisitStatus = v.findViewById(R.id.textRoom1VisitStatus);
        textRoom2VisitStatus = v.findViewById(R.id.textRoom2VisitStatus);
        textRoom3VisitStatus = v.findViewById(R.id.textRoom3VisitStatus);
        textRoom4VisitStatus = v.findViewById(R.id.textRoom4VisitStatus);
        room1 = v.findViewById(R.id.room1);
        room2 = v.findViewById(R.id.room2);
        room3 = v.findViewById(R.id.room3);
        room4 = v.findViewById(R.id.room4);

        textLifeNumber.setText(String.valueOf(lifeNumber));

        parseRoom();

        room1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toRoomInterface(room1Model);
            }
        });

        room2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toRoomInterface(room2Model);
            }
        });

        room3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toRoomInterface(room3Model);
            }
        });

        room4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toRoomInterface(room4Model);
            }
        });

        textChangeLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangeLevelDialog();
            }
        });

        textNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utilities.openNote(getActivity());
            }
        });

        showNewPlayerTutorial();

        return v;
    }

    private void showNewPlayerTutorial() {
        boolean newPlayer = prefs.getBoolean(Constants.PREFS_TUTORIAL_READ, true);
        if(newPlayer){
            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_tutorial);
            Button buttonProceed = dialog.findViewById(R.id.buttonProceed);
            TextView textName = dialog.findViewById(R.id.textName);
            String name = prefs.getString(Constants.PREFS_NAME, "");

            textName.setText(name);

            buttonProceed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    prefs.edit().putBoolean(Constants.PREFS_TUTORIAL_READ, false).commit();
                    dialog.dismiss();
                }
            });
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
    }

    private void showChangeLevelDialog() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
        builderSingle.setTitle("Please Select Floor: ");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item);
        arrayAdapter.add("Upper Ground");
        arrayAdapter.add("Ground");
        arrayAdapter.add("Lower Ground");

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                String strName = arrayAdapter.getItem(i);
                switch(i){
                    case 0:
                        prefs.edit().putInt(Constants.PREFS_FLOOR_NUMBER, 3).commit();
                        break;
                    case 1:
                        prefs.edit().putInt(Constants.PREFS_FLOOR_NUMBER, 2).commit();
                        break;
                    case 2:
                        prefs.edit().putInt(Constants.PREFS_FLOOR_NUMBER, 1).commit();
                        break;
                }
                floorNumber = prefs.getInt(Constants.PREFS_FLOOR_NUMBER, 1);
                Log.d(TAG, "onClick: "+strName);
                parseRoom();
                dialog.dismiss();
            }
        });
        builderSingle.show();
    }

    private void toRoomInterface(RoomModel roomModel) {
        Game.currentFragment = Constants.FRAGMENT_ROOM;
        int id = roomModel.getId();
        String roomName = roomModel.getRoomName();
        Log.d(TAG, "toRoomInterface: "+id);
        roomSet.add(String.valueOf(id));
        prefs.edit().putStringSet(Constants.PREFS_ROOM_SET, roomSet).commit();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        RoomInterface roomInterface = new RoomInterface();
        roomInterface.setRoomId(id);
        roomInterface.setRoomName(roomName);
        fragmentTransaction.replace(R.id.rootView, roomInterface);
        fragmentTransaction.commit();
    }

    private void parseRoom() {
        try {
            room1.setVisibility(View.INVISIBLE);
            room2.setVisibility(View.INVISIBLE);
            room3.setVisibility(View.INVISIBLE);
            room4.setVisibility(View.INVISIBLE);
            JSONObject jsonObject = new JSONObject(Utilities.loadJSONFromAsset(getActivity(), Constants.JSON_LEVELS_ROOM));
            JSONArray room = new JSONArray();
            switch(floorNumber){
                case 1:
                    textLevelName.setText(Constants.LEVEL_NAME_LG);
                    room = jsonObject.getJSONArray(Constants.LEVEL_LG);
                    break;
                case 2:
                    textLevelName.setText(Constants.LEVEL_NAME_G);
                    room = jsonObject.getJSONArray(Constants.LEVEL_G);
                    break;
                case 3:
                    textLevelName.setText(Constants.LEVEL_NAME_UG);
                    room = jsonObject.getJSONArray(Constants.LEVEL_UG);
                    break;
            }
            for(int i=0; room.length()>i; i++){
                JSONObject obj = room.getJSONObject(i);
                int id = obj.getInt("id");
                String roomName = obj.getString("name");
                boolean exist = roomSet.contains(String.valueOf(id));
                switch(i){
                    case 0:
                        room1.setVisibility(View.VISIBLE);
                        room1Model = new RoomModel(id, roomName);
                        textRoom1Name.setText(roomName);
                        if(exist){
                            textRoom1VisitStatus.setText("Visited");
                        } else{
                            textRoom1VisitStatus.setText("Unvisited");
                            textRoom1Name.setText("?");
                        }
                        break;
                    case 1:
                        room2.setVisibility(View.VISIBLE);
                        room2Model = new RoomModel(id, roomName);
                        textRoom2Name.setText(roomName);
                        if(exist){
                            textRoom2VisitStatus.setText("visited");
                        } else{
                            textRoom2VisitStatus.setText("unvisited");
                            textRoom2Name.setText("?");
                        }
                        break;
                    case 2:
                        room3.setVisibility(View.VISIBLE);
                        room3Model = new RoomModel(id, roomName);
                        textRoom3Name.setText(roomName);
                        if(exist){
                            textRoom3VisitStatus.setText("Visited");
                        } else{
                            textRoom3VisitStatus.setText("Unvisited");
                            textRoom3Name.setText("?");
                        }
                        break;
                    case 3:
                        room4.setVisibility(View.VISIBLE);
                        room4Model = new RoomModel(id, roomName);
                        textRoom4Name.setText(roomName);
                        if(exist){
                            textRoom4VisitStatus.setText("Visited");
                        } else{
                            textRoom4VisitStatus.setText("Unvisited");
                            textRoom4Name.setText("?");
                        }
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
