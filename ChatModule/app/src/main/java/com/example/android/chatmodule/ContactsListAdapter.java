package com.example.android.chatmodule;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import io.hasura.sdk.Hasura;
import io.hasura.sdk.HasuraUser;

/**
 * Created by amogh on 29/5/17.
 */

public class ContactsListAdapter extends RecyclerView.Adapter<ContactsListViewHolder> {

    List<ChatMessage> contacts = new ArrayList<>();
    int receiverId;
    String timeStampStr;

    HasuraUser user = Hasura.getClient().getUser();

    public ContactsListAdapter(Interactor interactor) {
        this.interactor = interactor;
    }

    public interface Interactor{
        void onChatClicked(int position, ChatMessage contact);
        void onChatLongClicked(int position, ChatMessage contact);
    }

    Interactor interactor;

    @Override
    public ContactsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item,parent,false);
        return new ContactsListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactsListViewHolder holder, final int position) {

        final ChatMessage contact = contacts.get(position);

        receiverId = contact.getReceiver();
        //Call a select query on the hasura auth  table to get username where receiverId = user_id
        holder.name.setText("User");
        holder.contents.setText(contact.getContent());

        timeStampStr = contact.getTime();
        String[] time = timeStampStr.split(" ");
        String[] date = timeStampStr.split(" ");
        date = date[0].split("-");
        Long tsLong = System.currentTimeMillis();
        String currentTime = getRequiredTime(tsLong.toString());
        String[] currentDate = currentTime.split(" ");
        currentDate = currentDate[0].split("-");

        if(currentDate[2].equals(date[2])) {
            holder.time.setText((time[1]));
        } else {
            holder.time.setText(time[0]);
        }


        if(contact.getSender() == user.getId() ) {
                holder.sent_or_received.setImageResource(R.mipmap.double_tick_30);
            }else {
                holder.sent_or_received.setImageResource(R.mipmap.single_tick_25);
            }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interactor.onChatClicked(position,contact);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                interactor.onChatLongClicked(position,contact);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public void setContacts(List<ChatMessage> contacts){
        this.contacts = contacts;
        notifyDataSetChanged();
    }

    public void addContact(ChatMessage contact){
        contacts.add(contact);
        notifyDataSetChanged();
    }

    public void deleteContact(int position){
        contacts.remove(contacts.get(position));
        notifyDataSetChanged();
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
