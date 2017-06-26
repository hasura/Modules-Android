package com.example.android.chatmodule;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

        /*List<ChatMessage> sampleData = new ArrayList<>();
        sampleData.add(new ChatMessage("hello there",time,user.getId(),3,user.getId()));
        sampleData.add(new ChatMessage("Hey to different!!",time,3,user.getId(),user.getId()));
        sampleData.add(new ChatMessage("Hey!!",time,user.getId(),2,user.getId()));
        sampleData.add(new ChatMessage("this is some random text!!",time,user.getId(),4,user.getId()));
        sampleData.add(new ChatMessage("Hello World!!",time,3,user.getId(),user.getId()));

        db.insertMessage(sampleData.get(0));
        db.insertMessage(sampleData.get(1));
        db.insertMessage(sampleData.get(2));
        db.insertMessage(sampleData.get(3));
        db.insertMessage(sampleData.get(4));*/




        recyclerView = (RecyclerView) findViewById(R.id.contacts_recyclerview);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        adapter = new ContactsListAdapter(new ContactsListAdapter.Interactor(){
            @Override
            public void onChatClicked(int position, ChatMessage contact) {

                if(contact.getSender() == user.getId())
                    Global.receiverId = contact.getReceiver();
                else
                    Global.receiverId = contact.getSender();

                getSupportFragmentManager().beginTransaction().add(R.id.frame_layout,new ChattingActivity()).commit();


                /*Intent i = new Intent(ContactsActivity.this,ChattingActivity.class);
                startActivity(i);
                finish();*/
            }

            @Override
            public void onChatLongClicked(final int position, final ChatMessage contact) {
                checkForDeleteContact(position,contact);
            }

        });
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setContacts(db.getAllContacts());
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
}
