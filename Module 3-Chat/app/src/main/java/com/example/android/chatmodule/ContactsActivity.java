package com.example.android.chatmodule;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.hasura.sdk.Callback;
import io.hasura.sdk.Hasura;
import io.hasura.sdk.HasuraClient;
import io.hasura.sdk.HasuraUser;
import io.hasura.sdk.exception.HasuraException;
import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Created by amogh on 29/5/17.
 */

public class ContactsActivity extends AppCompatActivity {

    ContactsListAdapter adapter;
    RecyclerView recyclerView;
    String latestTime;
    FloatingActionButton floatingActionButton;

    private Socket socket;{
        try{
            socket = IO.socket("http://192.168.0.131:3000");
        }catch(URISyntaxException e){
            throw new RuntimeException(e);
        }
    }

    public void setSocket(Socket socket) {
        Global.socket = socket;
    }

    HasuraUser user = Hasura.getClient().getUser();
    HasuraClient client = Hasura.getClient();

    private static final String DATABASE_NAME = "chatapp";
    private static final int DATABASE_VERSION = 2;

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_activity);

        final DataBaseHandler db = new DataBaseHandler(this,DATABASE_NAME,null,DATABASE_VERSION);

        socket.connect();
        socket.emit("join",user.getId());

        setSocket(socket);


        Long tsLong = System.currentTimeMillis();
        String time = getRequiredTime(tsLong.toString());
        //String time = tsLong.toString();
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
                    }

                    @Override
                    public void onFailure(HasuraException e) {
                        Toast.makeText(ContactsActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        recyclerView = (RecyclerView) findViewById(R.id.contacts_recyclerview);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        adapter = new ContactsListAdapter(new ContactsListAdapter.Interactor(){
            @Override
            public void onChatClicked(int position, ChatMessage contact) {

                if(contact.getSender() == user.getId())
                    Global.receiverId = contact.getReceiver();
                else
                    Global.receiverId = contact.getSender();

                floatingActionButton.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().add(R.id.frame_layout,new ChattingActivity()).addToBackStack(null).commit();
            }

            @Override
            public void onChatLongClicked(final int position, final ChatMessage contact) {
                checkForDeleteContact(position,contact);
            }

        });
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setContacts(db.getAllContacts());


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ContactsActivity.this);
                final EditText id = new EditText(ContactsActivity.this);
                id.setInputType(InputType.TYPE_CLASS_NUMBER);
                alert.setView(id);
                alert.setMessage("Enter User-Id of new user");
                alert.setPositiveButton("Send Message", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (adapter.checkForContact(getNumber(id.getText().toString()))) {
                            Global.receiverId = getNumber(id.getText().toString());
                            floatingActionButton.setVisibility(View.GONE);
                            getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, new ChattingActivity()).commit();
                        }else
                            Toast.makeText(ContactsActivity.this, "Chat with user already exists!!", Toast.LENGTH_SHORT).show();
                    }
                });
                alert.show();
            }
        });
    }

    public void checkForDeleteContact(final int position, final ChatMessage contact){
        final DataBaseHandler db = new DataBaseHandler(this,DATABASE_NAME,null,DATABASE_VERSION);

        AlertDialog.Builder alert = new AlertDialog.Builder(ContactsActivity.this);
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
}
