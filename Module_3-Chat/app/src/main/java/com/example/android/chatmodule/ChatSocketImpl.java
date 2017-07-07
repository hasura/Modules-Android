package com.example.android.chatmodule;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.hasura.sdk.Hasura;
import io.socket.client.IO;
import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.Transport;

public class ChatSocketImpl implements ChatSocket {

    private static final String TAG = "ChatSocketImpl";
    SocketEventHandler eventHandler;

    private static final String CHATCHANNELNAME = "chatMessage";

    int socketReconnectTime = 0;

    private Socket socket;

    {
        try {
            socket = IO.socket("https://socket.hello70.hasura-app.io/");
        } catch (URISyntaxException e) {
            Log.i(TAG, "URI Syntax Exception");
            e.printStackTrace();
        }
    }

    public ChatSocketImpl(SocketEventHandler eventHandler) {
        this.eventHandler = eventHandler;
        initializeSocket();
    }

    private void initializeSocket() {
        socket.on(CHATCHANNELNAME, chatMessageListener);
        socket.on(Socket.EVENT_CONNECT, socketConnectionListener);
        socket.on(Socket.EVENT_DISCONNECT, socketDisconnectionListener);
        socket.on(Socket.EVENT_CONNECT_ERROR, socketErrorListener);
        socket.on(Socket.EVENT_CONNECT_TIMEOUT, socketErrorListener);

        // Called upon transport creation.
        socket.io().on(Manager.EVENT_TRANSPORT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Transport transport = (Transport) args[0];
                transport.on(Transport.EVENT_REQUEST_HEADERS, new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        @SuppressWarnings("unchecked")
                        Map<String, List<String>> headers = (Map<String, List<String>>) args[0];
                        headers.put("Authorization", Arrays.asList("Bearer " + Hasura.getClient().getUser().getAuthToken()));
                    }
                });
            }
        });
    }

    private Emitter.Listener chatMessageListener = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {
            new Handler(Looper.getMainLooper())
                    .post(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("ChatSocket", "" + args[0]);
                            try {
                                Log.d(TAG, (String) args[0]);
                                ChatMessage chatMessage = new Gson().fromJson((String) args[0], ChatMessage.class);
                                eventHandler.onNewChatMessageArrived(chatMessage);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });



        }
    };

    private Emitter.Listener socketConnectionListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            new Handler(Looper.getMainLooper())
                    .post(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("ChatSocket", "Socket Connected");
                            socketReconnectTime = 1000;
                            eventHandler.onSocketConnected();
                        }
                    });


        }
    };

    private Emitter.Listener socketDisconnectionListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

            new Handler(Looper.getMainLooper())
                    .post(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("ChatSocket", "Socket DisConnected");
                            eventHandler.onSocketDisconnected();
                        }
                    });
        }
    };

    private Emitter.Listener socketErrorListener = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

            new Handler(Looper.getMainLooper())
                    .post(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("ChatSocket", "Socket Error: " + args[0]);
                            eventHandler.onSocketError(args);
                        }
                    });
        }
    };

    @Override
    public void connect() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                socket.connect();
            }
        }, socketReconnectTime);

        socketReconnectTime += 1000;
    }

    @Override
    public void disconnect() {

        new Handler(Looper.getMainLooper())
                .post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("ChatSocket", "Disconnect called");
                        socket.disconnect();
                        socket.off(CHATCHANNELNAME, chatMessageListener);
                    }
                });
    }

    @Override
    public void emitMessage(String message) {
        socket.emit(CHATCHANNELNAME, message);
    }

}