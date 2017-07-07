package com.example.android.chatmodule;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amogh on 6/7/17.
 */

public class AllContactsAdapter extends RecyclerView.Adapter<AllContactsViewHolder> {

    List<Contact> allContacts = new ArrayList<>();

    Context context;

    Interactor interactor;

    public AllContactsAdapter(Interactor interactor){
        this.interactor = interactor;
    }

    public interface Interactor{
        public void onContactClicked(Contact contact,int position);
    }

    @Override
    public AllContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_allcontact,parent,false);
        return new AllContactsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AllContactsViewHolder holder, final int position) {
        final Contact contact = allContacts.get(position);
        holder.name.setText(contact.getName());

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interactor.onContactClicked(contact,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allContacts.size();
    }

    public void setContacts(List<Contact> contactList){
        this.allContacts = contactList;
        notifyDataSetChanged();
    }
}
