package io.hasura.drive_android.ui.home;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.hasura.drive_android.R;

/**
 * Created by jaison on 28/03/17.
 */

class FileViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.parent_card)
    CardView cardView;

    @BindView(R.id.image_view)
    ImageView imageView;

    @BindView(R.id.file_title)
    TextView fileTitle;

    @BindView(R.id.file_description)
    TextView fileDescription;

    @BindView(R.id.bottom_line)
    FrameLayout bottomLine;

    @BindView(R.id.options)
    ImageButton optionsButton;

    FileViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
