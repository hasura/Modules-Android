package com.example.android.chatmodule;

/**
 * Created by jaison on 06/07/17.
 */

public interface SocketEventHandler {

    void onNewChatMessageArrived(ChatMessage chatMessage);
    void onSocketConnected();
    void onSocketDisconnected();
    void onSocketError(Object... args);

}
