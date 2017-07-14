package com.example.android.chatmodule;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.hasura.sdk.Callback;
import io.hasura.sdk.Hasura;
import io.hasura.sdk.HasuraClient;
import io.hasura.sdk.HasuraUser;
import io.hasura.sdk.exception.HasuraException;

/**
 * Created by amogh on 29/5/17.
 */

public class ConversationsFragment extends Fragment {

    ConversationsListAdapter adapter;
    RecyclerView recyclerView;
    String latestTime;
    FloatingActionButton floatingActionButton;
    LinearLayout linearLayout;

    ProgressDialog progressDialog;

    View parentViewHolder;
    Context context;

    ChatSocket chatSocket;

    private static String TAG = "ConversationsFragment";

    HasuraUser user = Hasura.getClient().getUser();
    HasuraClient client = Hasura.getClient();

    DataBaseHandler db;
    private static final String DATABASE_NAME = "chatapp";
    private static final int DATABASE_VERSION = 2;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parentViewHolder = inflater.inflate(R.layout.fragment_contacts, container, false);
        context = parentViewHolder.getContext();

        linearLayout = (LinearLayout) parentViewHolder.findViewById(R.id.contacts_fragment);
        linearLayout.setVisibility(View.GONE);

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Connecting to Server");
        progressDialog.show();

        db = new DataBaseHandler(context,DATABASE_NAME,null,DATABASE_VERSION);

        chatSocket = new ChatSocketImpl(new SocketEventHandler() {
            @Override
            public void onNewChatMessageArrived(ChatMessage chatMessage) {
                db.insertMessage(chatMessage);
                adapter.setContacts(db.getAllContacts());
                recyclerView.scrollToPosition(adapter.getItemCount()-1);
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


        Long tsLong = System.currentTimeMillis();
        String time = getRequiredTime(tsLong.toString());
        latestTime = db.getLatest();

        client
                .useDataService()
                .setRequestBody(new SelectMessagesQuery(latestTime))
                .expectResponseTypeArrayOf(ChatMessage.class)
                .enqueue(new Callback<List<ChatMessage>, HasuraException>() {
                    @Override
                    public void onSuccess(List<ChatMessage> chatMessages) {
                        int i;
                        for(i = 0; i < chatMessages.size(); i++)
                            db.insertMessage(chatMessages.get(i));
                        adapter.setContacts(db.getAllContacts());
                        recyclerView.scrollToPosition(adapter.getItemCount()-1);
                    }

                    @Override
                    public void onFailure(HasuraException e) {
                        Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

        floatingActionButton = (FloatingActionButton) parentViewHolder.findViewById(R.id.fab);
        recyclerView = (RecyclerView) parentViewHolder.findViewById(R.id.contacts_recyclerview);

        floatingActionButton.setVisibility(View.VISIBLE);



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        adapter = new ConversationsListAdapter(new ConversationsListAdapter.Interactor(){
            @Override
            public void onChatClicked(int position, ChatMessage contact) {

                if(contact.getSender() == user.getId())
                    Global.receiverId = contact.getReceiver();
                else
                    Global.receiverId = contact.getSender();

                floatingActionButton.setVisibility(View.GONE);
                //getActivity().getSupportFragmentManager().beginTransaction().add(R.id.frame_layout,new ChattingActivity()).addToBackStack(null).commit();
                Intent i = new Intent(getActivity().getApplicationContext(),ChattingActivity.class);
                startActivity(i);
                getActivity().finish();
            }

            @Override
            public void onChatLongClicked(final int position, final ChatMessage contact) {
                checkForDeleteContact(position,contact);
            }

        });
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setContacts(db.getAllContacts());
        recyclerView.scrollToPosition(adapter.getItemCount()-1);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                final EditText mobile = new EditText(context);
                mobile.setInputType(InputType.TYPE_CLASS_NUMBER);
                alert.setView(mobile);
                alert.setMessage("Enter Mobile number");
                alert.setPositiveButton("Send Message", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newChat(mobile.getText().toString());
                    }
                });
                alert.show();
            }
        });
        return parentViewHolder;
    }

    public void checkForDeleteContact(final int position, final ChatMessage contact){
        final DataBaseHandler db = new DataBaseHandler(context,DATABASE_NAME,null,DATABASE_VERSION);

        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage("Are you sure you want to delete this chat?");
        alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int id = contact.getReceiver();
                db.deleteContact(id);
                adapter.deleteContact(position);
            }
        });
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

    public int getNumber(String string){
        char[] input = string.toCharArray();
        int i;
        int id = 0;
        for (i = 0;i < input.length;i++){
            id = id * 10;
            id = id + (input[i] - '0');
        }

        return id;
    }

    public void newChat(String mobile){
        //First I have to get User Id
        client.useDataService()
                .setRequestBody(new CheckUserQuery(mobile))
                .expectResponseTypeArrayOf(UserDetails.class)
                .enqueue(new Callback<List<UserDetails>, HasuraException>() {
                    @Override
                    public void onSuccess(List<UserDetails> userDetails) {
                        Global.receiverId = userDetails.get(0).getId();
                        if (adapter.checkForContact(Global.receiverId)) {
                            floatingActionButton.setVisibility(View.GONE);
                            Intent i = new Intent(getActivity().getApplicationContext(),ChattingActivity.class);
                            startActivity(i);
                            getActivity().finish();
                        }else
                            Toast.makeText(context, "Chat with user already exists!!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(HasuraException e) {
                        Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //chatSocket.disconnect();
    }
}
