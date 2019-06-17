package com.volvocars.db;

import android.content.Context;

public class OperatorFactory {
    public static CommitOperator getOperator(Context context, String storageType){
        switch (storageType){
            case "SQL":
                return SQLite.getInstance(context);
            default:
                return null;
        }
    }
}
