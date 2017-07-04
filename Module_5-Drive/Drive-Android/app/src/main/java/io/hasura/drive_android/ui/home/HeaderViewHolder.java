package io.hasura.drive_android.ui.home;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.hasura.drive_android.R;

/**
 * Created by jaison on 31/03/17.
 */

public class HeaderViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.header)
    public TextView header;

    public HeaderViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
