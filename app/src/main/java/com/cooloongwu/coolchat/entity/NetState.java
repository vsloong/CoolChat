package com.cooloongwu.coolchat.entity;

/**
 * Created by CooLoongWu on 2017-1-11 18:44.
 */

public class NetState {

    private boolean isAvailable;

    public NetState(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
