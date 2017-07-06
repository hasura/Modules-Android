package com.example.android.chatmodule;

public interface ChatSocket {
    void connect();
    void disconnect();
    void emitMessage(String message);
}