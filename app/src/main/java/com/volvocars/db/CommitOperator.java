package com.volvocars.db;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.List;

public interface CommitOperator {

    int STATE_RUN = 1;
    int STATE_STOP = 0;

    List<String> queryAllDB();

    void createDB(String fileName);

    Cursor queryDB(String fileName);

    int insertDB(String fileName, ContentValues values);

    void updateDBItem(String oldfileName, String newFileName, ContentValues values);

    void updateDBState(String fileName, int state);

    void deleteDB(String fileName);
}
