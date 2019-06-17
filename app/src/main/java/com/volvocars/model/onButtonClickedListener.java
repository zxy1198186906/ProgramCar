package com.volvocars.model;

public interface onButtonClickedListener {

    void onRunClicked(int itemId, String itemName);

    void onStopClicked(int itemId, String itemName);

    void onEditClicked(int itemId, String itemName);

    void onDeleteClicked(int itemId, String itemName);
}
