package com.example.android.chatmodule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.hasura.sdk.Callback;
import io.hasura.sdk.Hasura;
import io.hasura.sdk.HasuraClient;
import io.hasura.sdk.HasuraUser;
import io.hasura.sdk.exception.HasuraException;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ChattingActivity extends Fragment {

    View parentViewHolder;

    EditText message;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    Button send;
    ChatRecyclerViewAdapter adapter;
    String time;
    int totalItemCount;
    int displayedsize = 0;
    int paginationNumber = 6;

    String latestTime;

    boolean canFetch = true;

    boolean isLoading = false;

    HasuraClient client = Hasura.getClient();
    HasuraUser user = Hasura.getClient().getUser();

    DataBaseHandler db;

    int receiverId;
    int senderId;

    private static final String DATABASE_NAME = "chatapp";
    private static final int DATABASE_VERSION = 2;

    List<ChatMessage> allData;
    List<ChatMessage> displayData;

    Socket socket;


    @Override
    public void onDetach(){
        super.onDetach();
        Intent i = new Intent(getActivity().getApplicationContext(),ContactsActivity.class);
        startActivity(i);
        getActivity().finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        parentViewHolder = inflater.inflate(R.layout.activity_chatting,container,false);

        socket = getSocket();

        socket.on("sendMessage", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ChatMessage incomingMessage = new Gson().fromJson((String) args[0], ChatMessage.class);

                        db.insertMessage(incomingMessage);
                        if(incomingMessage.getReceiver() == Global.receiverId || incomingMessage.getSender() == Global.receiverId){
                            adapter.addMessage(incomingMessage);
                        }
                    }
                });
            }
        });


        db = new DataBaseHandler(getActivity().getApplicationContext(),DATABASE_NAME,null,DATABASE_VERSION);

        message = (EditText) parentViewHolder.findViewById(R.id.chat_message);
        recyclerView = (RecyclerView) parentViewHolder.findViewById(R.id.chat_recyclerview);
        send = (Button) parentViewHolder.findViewById(R.id.chat_sendButton);

        linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        //linearLayoutManager.setReverseLayout(true);
        adapter = new ChatRecyclerViewAdapter();
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        totalItemCount = linearLayoutManager.getItemCount();

        //setRecyclerViewScrollListener();

        receiverId = Global.receiverId;
        senderId = user.getId();

        allData = db.getAllMessages();
        //latestTime = allData.get(paginationNumber-1).getTime();
        totalItemCount = allData.size();
            if(allData.size() != 0)
                //loadChats(0);
                adapter.setChatMessages(allData);

        recyclerView.scrollToPosition(adapter.getItemCount() - 1);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Long tsLong = System.currentTimeMillis();
                time = getRequiredTime(tsLong.toString());
                //time = tsLong.toString();

                if(message.getText().toString().isEmpty() || message.getText().toString().length() == 0){}
                 else {
                    final ChatMessage chat = new ChatMessage();
                    chat.setContent(message.getText().toString());
                    chat.setTime(time);
                    chat.setSender(senderId);
                    chat.setReceiver(receiverId);
                    chat.setUserId(user.getId());

                    client
                            .useDataService()
                            .setRequestBody(new InsertMessageQuery(message.getText().toString(),time,senderId,receiverId,user.getId()))
                            .expectResponseType(MessageResponse.class)
                            .enqueue(new Callback<MessageResponse, HasuraException>() {
                                @Override
                                public void onSuccess(MessageResponse messageResponse) {
                                    adapter.addMessage(chat);
                                    db.insertMessage(chat);
                                    socket.emit("chatMessage",new Gson().toJson(chat), Global.receiverId);
                                }

                                @Override
                                public void onFailure(HasuraException e) {
                                    Toast.makeText(getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                message.setText("");
            }
        });

        return parentViewHolder;
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        //socket.disconnect();
    }
    public String getRequiredTime(String timeStampStr){
        try{
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date netDate = (new Date(Long.parseLong(timeStampStr)));
            return sdf.format(netDate);
        } catch (Exception ignored) {
            return "xx";
        }
    }

    /*private void setRecyclerViewScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                super.onScrolled(recyclerView, dx, dy);

                int total = linearLayoutManager.getItemCount();
                int lastVisibleItemCount = linearLayoutManager.findLastVisibleItemPosition();

                if (!isLoading && displayedsize < totalItemCount) {
                    if (total - 1 == lastVisibleItemCount)
                        if (dy < 0){
                            loadChats(displayedsize);
                        }
                }
            }
        });
    }*/

    private void loadChats(int start){
        if(totalItemCount > paginationNumber) {

            if(displayedsize + paginationNumber > totalItemCount){
                displayData = allData.subList(displayedsize,totalItemCount-1);
                /*displayData = db.getAllMessages(paginationNumber,latestTime);
                latestTime = displayData.get(displayData.size()-1).getTime();

                if (displayData.size() < paginationNumber)
                    canFetch = false;*/

                adapter.addMessage(displayData);
                displayedsize = totalItemCount + 1;
            }else{
                displayData = allData.subList(start, start + paginationNumber);
                /*displayData = db.getAllMessages(paginationNumber,latestTime);
                latestTime = displayData.get(displayData.size()-1).getTime();

                if (displayData.size() < paginationNumber)
                    canFetch = false;*/

                adapter.addMessage(displayData);
                displayedsize = displayedsize + paginationNumber;
            }
        }else{
            /*displayData = db.getAllMessages(paginationNumber,latestTime);
            latestTime = displayData.get(displayData.size()-1).getTime();

            if (displayData.size() < paginationNumber)
                canFetch = false;*/

            adapter.setChatMessages(allData);
            displayedsize = displayedsize + totalItemCount;
        }
    }

    public Socket getSocket() {
        return Global.socket;
    }
}
