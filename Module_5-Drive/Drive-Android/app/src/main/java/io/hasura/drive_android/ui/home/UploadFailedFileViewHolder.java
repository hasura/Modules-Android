package io.hasura.drive_android.ui.home;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.hasura.drive_android.R;

/**
 * Created by jaison on 31/03/17.
 */

public class UploadFailedFileViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.file_title)
    TextView fileTitle;

    @BindView(R.id.bottom_line)
    FrameLayout bottomLine;

    @BindView(R.id.image_view)
    ImageView imageView;

    @BindView(R.id.delete_button)
    Button deleteButton;

    @BindView(R.id.retry_button)
    Button retryButton;

    public UploadFailedFileViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

}
