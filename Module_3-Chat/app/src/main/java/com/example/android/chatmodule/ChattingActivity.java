package com.example.android.chatmodule;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.hasura.sdk.Hasura;
import io.hasura.sdk.HasuraUser;

public class ChattingActivity extends AppCompatActivity {

    EditText message;
    RecyclerView recyclerView;
    LinearLayout linearLayout;
    LinearLayoutManager linearLayoutManager;
    Button send;
    ChatRecyclerViewAdapter adapter;
    ProgressDialog progressDialog;

    String time;
    int totalItemCount;

    HasuraUser user = Hasura.getClient().getUser();

    DataBaseHandler db;

    int receiverId;
    int senderId;

    private static final String DATABASE_NAME = "chatapp";
    private static final int DATABASE_VERSION = 2;

    List<ChatMessage> allData;

    ChatSocket chatSocket;

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ChattingActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        linearLayout = (LinearLayout) findViewById(R.id.message_type_area);
        linearLayout.setVisibility(View.GONE);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Connecting to Server");
        progressDialog.show();

        chatSocket = new ChatSocketImpl(new SocketEventHandler() {
            @Override
            public void onNewChatMessageArrived(ChatMessage chatMessage) {
                db.insertMessage(chatMessage);
                adapter.addMessage(chatMessage);
                recyclerView.scrollToPosition(adapter.getItemCount() - 1);
            }

            @Override
            public void onSocketConnected() {
                linearLayout.setVisibility(View.VISIBLE);
                progressDialog.dismiss();
            }

            @Override
            public void onSocketDisconnected() {

            }

            @Override
            public void onSocketError(Object... args) {
            }
        });

        chatSocket.connect();

        db = new DataBaseHandler(this, DATABASE_NAME, null, DATABASE_VERSION);

        message = (EditText) findViewById(R.id.chat_message);
        recyclerView = (RecyclerView) findViewById(R.id.chat_recyclerview);
        send = (Button) findViewById(R.id.chat_sendButton);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);


        linearLayoutManager = new LinearLayoutManager(ChattingActivity.this);
        adapter = new ChatRecyclerViewAdapter();
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        totalItemCount = linearLayoutManager.getItemCount();

        receiverId = Global.receiverId;
        senderId = user.getId();

        allData = db.getAllMessages();
        totalItemCount = allData.size();
        if (allData.size() != 0)
            adapter.setChatMessages(allData);

        recyclerView.scrollToPosition(adapter.getItemCount() - 1);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Long tsLong = System.currentTimeMillis();
                time = getRequiredTime(tsLong.toString());

                if (message.getText().toString().isEmpty() || message.getText().toString().length() == 0) {
                } else {
                    final ChatMessage chat = new ChatMessage();
                    chat.setContent(message.getText().toString());
                    chat.setTime(time);
                    chat.setSender(senderId);
                    chat.setReceiver(receiverId);
                    chat.setUserId(user.getId());

                    chatSocket.emitMessage(new Gson().toJson(chat));
                    adapter.addMessage(chat);
                    db.insertMessage(chat);
                }
                message.setText("");

                recyclerView.scrollToPosition(adapter.getItemCount()-1);

                InputMethodManager imm = (InputMethodManager)getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(message.getWindowToken(), 0);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public String getRequiredTime(String timeStampStr) {
        try {
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date netDate = (new Date(Long.parseLong(timeStampStr)));
            return sdf.format(netDate);
        } catch (Exception ignored) {
            return "xx";
        }
    }
    public ChatSocket getSocket() {
        return chatSocketProvider.getSocket();
    }

    public interface ChatSocketProvider {
        ChatSocket getSocket();
    }

    ChatSocketProvider chatSocketProvider;

}
