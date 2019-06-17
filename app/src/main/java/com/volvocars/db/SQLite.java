package com.volvocars.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class SQLite extends SQLiteOpenHelper implements CommitOperator{

    private static final String TAG = "SQLiteManager";

    private static final String DB_NAME = "programcar.db";
    private static final int DB_VERSION = 2;
    private static final String ID = "_id";
    public static final String VERSION = "version";
    public static final String ITEM_TYPE = "type";
    public static final String ITEM_TEXT = "destext";
    public static final String ITEM_RELATION = "relation";
    public static final String ITEM_RUN = "state";


    private static SQLite instance;


    private SQLite(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static SQLite getInstance(Context context){
        if (null == instance){
            synchronized (SQLite.class){
                if (null == instance)
                    instance = new SQLite(context);
            }
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) { }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }


    @Override
    public List<String> queryAllDB() {
        List<String> result = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select name from sqlite_master where type='table' order by name", null);
        while(cursor.moveToNext()){
            result.add(cursor.getString(0));
            Log.i(TAG, "find "+cursor.getString(0));
        }
        return result;
    }

    @Override
    public void createDB(String fileName) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL("create table if not exists " + fileName +
                    "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + VERSION + " INTEGER, "  + ITEM_TYPE + " INTEGER, "
                    + ITEM_TEXT + " TEXT, " + ITEM_RELATION +" INTEGER, "+ITEM_RUN+ " INTEGER);");
        }catch (Exception e){
            Log.e(TAG, e.toString());
        }
    }

    @Override
    public Cursor queryDB(String fileName) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return  sqLiteDatabase.query(fileName, null, null, null,
                null, null, null);
    }

    @Override
    public int insertDB(String fileName, ContentValues values) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return (int)sqLiteDatabase.insert(fileName, null, values);
    }

    @Override
    public void updateDBItem(String oldfileName, @NonNull String newFileName, ContentValues values) {
        if (null == oldfileName){
            createDB(newFileName);
        }else {
            deleteDB(oldfileName);
            createDB(newFileName);
        }
        insertDB(newFileName, values);
    }

    @Override
    public void updateDBState(String fileName, int state) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        if (STATE_RUN == state)
            sqLiteDatabase.execSQL("update "+fileName+" set "+ITEM_RUN+"= '1'");
        else if (STATE_STOP == state)
            sqLiteDatabase.execSQL("update "+fileName+" set "+ITEM_RUN+"= '0'");
        else
            throw new IllegalStateException();
    }

    @Override
    public void deleteDB(String fileName) {
        try{
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL("drop table if exists "+ fileName);
        }catch (Exception e){
            Log.e(TAG, e.toString());
        }
    }
}
