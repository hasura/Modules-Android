package io.hasura.drive_android.ui.fileDetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.hasura.drive_android.models.HasuraFile;
import io.hasura.drive_android.R;
import io.hasura.drive_android.stores.FileDataStore;
import io.hasura.drive_android.ui.editFile.EditFileActivity;
import io.hasura.drive_android.ui.utils.FileOptionBottomSheet;
import io.hasura.drive_android.utils.CustomGlide;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class FileDetailActivity extends AppCompatActivity implements FileOptionBottomSheet.Listener {

    private static final String HASURA_FILE_KEY = "HasuraFileKey";
    private HasuraFile file;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progress_bar)
    MaterialProgressBar progressBar;
    @BindView(R.id.image)
    ImageView image;

    public static void startActivity(Activity startingActivity, HasuraFile file) {
        Intent intent = new Intent(startingActivity, FileDetailActivity.class);
        intent.putExtra(HASURA_FILE_KEY, file);
        startingActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_detail);
        ButterKnife.bind(this);
        file = getIntent().getParcelableExtra(HASURA_FILE_KEY);
        progressBar.setVisibility(View.VISIBLE);
        CustomGlide.with(this)
                .load(file.getImageString())
                .addListener(new CustomGlide.ResponseListener() {
                    @Override
                    public void onException(Exception e, GlideUrl model, Target<GlideDrawable> target, boolean isFirstResource) {

                    }

                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideUrl model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                })
                .into(image);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.file_detail,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_info:
                FileOptionBottomSheet.newInstance(file).show(getSupportFragmentManager(), "bottomSheet");
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onEditFileClicked(HasuraFile file) {
        EditFileActivity.startActivity(this,file);
    }

    @Override
    public void onDeleteFileClicked(HasuraFile file) {
        FileDataStore.getInstance().deleteFile(file);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        file = FileDataStore.getInstance().getFileDetailsForId(file.getId());
        getSupportActionBar().setTitle(file.getName());
    }
}
