package com.example.android.chatmodule;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by amogh on 6/7/17.
 */

class AllContactsViewHolder extends RecyclerView.ViewHolder {

    TextView name;

    public AllContactsViewHolder(View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.name);
    }
}
