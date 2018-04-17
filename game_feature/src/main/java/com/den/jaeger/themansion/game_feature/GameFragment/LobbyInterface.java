package com.den.jaeger.themansion.game_feature.GameFragment;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.den.jaeger.themansion.game_feature.Constants;
import com.den.jaeger.themansion.game_feature.R;
import com.den.jaeger.themansion.game_feature.Utilities;
import com.den.jaeger.themansion.game_feature.model.RoomModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LobbyInterface extends Fragment {
    TextView textLevelName, textNote, textLifeNumber, textChangeLevel;
    TextView textRoom1Name, textRoom2Name, textRoom3Name, textRoom4Name;
    TextView textRoom1VisitStatus, textRoom2VisitStatus, textRoom3VisitStatus, textRoom4VisitStatus;
    RelativeLayout room1, room2, room3, room4;
    View v;
    SharedPreferences prefs;
    int floorNumber;

    RoomModel room1Model, room2Model, room3Model, room4Model;

    String TAG = "LobbyInterface";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_lobby_interface, container, false);
        prefs = getActivity().getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE);
        floorNumber = prefs.getInt(Constants.PREFS_FLOOR_NUMBER, 1);

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

        return v;
    }

    private void showChangeLevelDialog() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
        builderSingle.setTitle("Please Select Floor: ");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item);
        arrayAdapter.add("Lower Ground");
        arrayAdapter.add("Ground");
        arrayAdapter.add("Upper Ground");

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                String strName = arrayAdapter.getItem(i);
                switch(i){
                    case 0:
                        prefs.edit().putInt(Constants.PREFS_FLOOR_NUMBER, 1).commit();
                        break;
                    case 1:
                        prefs.edit().putInt(Constants.PREFS_FLOOR_NUMBER, 2).commit();
                        break;
                    case 2:
                        prefs.edit().putInt(Constants.PREFS_FLOOR_NUMBER, 3).commit();
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
        int id = roomModel.getId();
        Log.d(TAG, "toRoomInterface: "+id);
    }

    private void parseRoom() {
        try {
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
                switch(i){
                    case 0:
                        room1Model = new RoomModel(id, roomName);
                        textRoom1Name.setText(roomName);
                        break;
                    case 1:
                        room2Model = new RoomModel(id, roomName);
                        textRoom2Name.setText(roomName);
                        break;
                    case 2:
                        room3Model = new RoomModel(id, roomName);
                        textRoom3Name.setText(roomName);
                        break;
                    case 3:
                        room4Model = new RoomModel(id, roomName);
                        textRoom4Name.setText(roomName);
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
