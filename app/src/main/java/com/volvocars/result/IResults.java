package com.volvocars.result;

import android.content.Context;

public interface IResults {

    int getRelation();

    void setDescription(String description);
    String getDescription();

    void setMsgType(int msgType);
    int getMsgType();

    int startAction();
    int stopAction();
}
