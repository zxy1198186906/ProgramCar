package com.volvocars.model;

public interface onButtonStateListener {
    boolean onStateChecked(int itemId, String itemName);

    void onStateChanged(int itemId, String itemName, boolean itemChanged);
}
