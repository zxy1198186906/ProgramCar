package com.volvocars.condition;

public interface IConditions {
    void setDescription(String description);
    String getDescription();

    void setMsgType(int msgType);
    int getMsgType();

    void setRelation(int relation);
    int getRelation();

    boolean find(int equation, int number);
}
