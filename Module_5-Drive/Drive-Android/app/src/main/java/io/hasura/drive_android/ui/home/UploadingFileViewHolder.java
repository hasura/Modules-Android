package io.hasura.drive_android.ui.home;

import android.support.v7.widget.RecyclerView;
import android.view.View;
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

public class UploadingFileViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.file_title)
    TextView fileTitle;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.button_stop_upload)
    ImageButton buttonStopUpload;

    @BindView(R.id.bottom_line)
    FrameLayout bottomLine;

    @BindView(R.id.image_view)
    ImageView imageView;

    public UploadingFileViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

}
