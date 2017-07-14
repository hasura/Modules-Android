package com.example.android.chatmodule;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by amogh on 29/5/17.
 */
public class ConversationsListViewHolder extends RecyclerView.ViewHolder {

    public TextView name;
    public TextView contents;
    public TextView time;
    public ImageView sent_or_received;

    public ConversationsListViewHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.contact_name);
        contents = (TextView) itemView.findViewById(R.id.contact_message_contents);
        time = (TextView) itemView.findViewById(R.id.contact_message_time);
        sent_or_received = (ImageView) itemView.findViewById(R.id.contact_tick);
    }
}
