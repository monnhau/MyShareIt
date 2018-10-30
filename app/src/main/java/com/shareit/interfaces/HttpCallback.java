package com.shareit.interfaces;

import java.io.IOException;

import okhttp3.Request;

public interface HttpCallback {
    void onSucess(String s);
    void onStart();
    void onFailure(Request request, IOException e);
}
