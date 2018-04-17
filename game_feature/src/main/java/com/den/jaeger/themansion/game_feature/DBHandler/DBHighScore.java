package com.den.jaeger.themansion.game_feature.DBHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.den.jaeger.themansion.game_feature.HighScore;
import com.den.jaeger.themansion.game_feature.model.HighScoreModel;

import java.util.ArrayList;
import java.util.List;

public class DBHighScore extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "highscore.db",
            TABLE_NAME = "highScores",
            KEY_NAME = "name",
            KEY_TIME_FINISHED = "timeFinised";

    public DBHighScore(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME+"("+KEY_NAME+" TEXT,"+ KEY_TIME_FINISHED+" INTEGER"+")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
    }

    public void addHighScore(HighScoreModel highScoreModel){
        SQLiteDatabase db = getReadableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_NAME, highScoreModel.getName());
        values.put(KEY_TIME_FINISHED, highScoreModel.getTimeFinished());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public List<HighScoreModel> getHighScores(){
        List<HighScoreModel> highScoreModelList = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME, null);
        if(cursor.moveToFirst()){
            do{
                HighScoreModel highScoreModel =
                        new HighScoreModel(cursor.getString(0), cursor.getInt(1));
                highScoreModelList.add(highScoreModel);
            }
            while(cursor.moveToNext());
        }

        return highScoreModelList;
    }

    public void dropTable(){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
    }
}
