package io.hasura.drive_android.ui.editFile;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.hasura.drive_android.R;
import io.hasura.drive_android.enums.FileEditState;
import io.hasura.drive_android.models.HasuraFile;
import io.hasura.drive_android.models.ServerError;
import io.hasura.drive_android.stores.FileDataStore;
import io.hasura.drive_android.ui.launcher.LauncherActivity;
import io.hasura.drive_android.utils.CustomGlide;
import io.hasura.drive_android.utils.DateManager;
import io.hasura.drive_android.utils.DateSelectorListener;

public class EditFileActivity extends AppCompatActivity {

    private static final String HASURA_FILE_KEY = "HasuraFileKey";
    private HasuraFile file;
    private HasuraFile updatedFile;

    @BindView(R.id.image_view)
    ImageView imageView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.filename)
    TextInputEditText filename;
    @BindView(R.id.til_filename)
    TextInputLayout tilFilename;
    @BindView(R.id.number)
    TextInputEditText number;
    @BindView(R.id.til_number)
    TextInputLayout tilNumber;
    @BindView(R.id.expiry)
    TextInputEditText expiry;
    @BindView(R.id.til_expiry)
    TextInputLayout tilExpiry;
    @BindView(R.id.update)
    Button update;
    @BindView(R.id.progress_layout)
    LinearLayout progressLayout;
    @BindView(R.id.cancel)
    Button cancel;

    public static void startActivity(Activity startingActivity, HasuraFile file) {
        Intent intent = new Intent(startingActivity, EditFileActivity.class);
        intent.putExtra(HASURA_FILE_KEY, file);
        startingActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_file);
        ButterKnife.bind(this);
        collapsingToolbar.setTitle("Edit File");

        file = getIntent().getParcelableExtra(HASURA_FILE_KEY);
        updatedFile = new HasuraFile();
        updatedFile.setId(file.getId());
        updatedFile.setCreated(file.getCreated());
        updatedFile.setUser_id(file.getUser_id());

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        showProgressBar(false);

        CustomGlide.with(this)
                .load(file.getImageString())
                .into(imageView);

        filename.setText(file.getName());
        number.setText(file.getFile_number() != null ? file.getFile_number() : "");
        expiry.setText(file.getFile_expiry() != null ? DateManager.getFormattedExpiryDate(file.getFile_expiry()) : "");

        expiry.setOnClickListener(new DateSelectorListener(expiry, "MM/yyyy", true));
        expiry.setFocusable(false);
    }

    @OnClick(R.id.update)
    public void onUpdateButtonClicked() {
        if (filename.getText().toString().trim().isEmpty()) {
            tilFilename.setError("Filename cannot be empty");
            return;
        }
        tilFilename.setError(null);

        updatedFile.setName(filename.getText().toString());
        if (!expiry.getText().toString().trim().isEmpty())
            updatedFile.setFile_expiry(DateManager.getHasuraFormatExpiryDate(expiry.getText().toString()));
        updatedFile.setFile_number(number.getText().toString());
        updatedFile.setLast_modified(DateManager.getHasuraFormattedModifiedDate(new Date()));

        if (updatedFile.isSame(file)) {
            finish();
        } else {
            FileDataStore.getInstance().editFile(updatedFile, new FileDataStore.FileEditListener() {
                @Override
                public void onFileEditStateChanged(FileEditState editState, Object object) {
                    switch (editState) {
                        case EDIT_STARTED:
                            showProgressBar(true);
                            break;
                        case EDIT_COMPLETED:
                            showProgressBar(false);
                            finish();
                            break;
                        case EDIT_FAILED:
                            ServerError error = (ServerError) object;
                            if (error != null) {
                                handleErrorResponse(error);
                            }

                    }
                }
            });
        }
    }

    private void handleErrorResponse(ServerError error) {
        switch (error.getType()) {
            case INTERNET:
                showErrorAlert("No Internet Connection", "Please ensure that you have a working internet connection and try again", "Dismiss", null);
                break;
            case INVALID_AUTH:
                showErrorAlert("Session Expired", "Your login session has expired. Please login again.", "Login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //UserAuthStore.clearSession();
                        LauncherActivity.startActivity(EditFileActivity.this);
                    }
                });
                break;
            case UNKNOWN:
                showErrorAlert("Something went wrong", "", "Dismiss", null);
                break;
        }
    }

    private void showErrorAlert(String title, String message, String buttonName, final DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(buttonName, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (listener != null)
                    listener.onClick(dialog, which);
            }
        });
        builder.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showProgressBar(boolean show) {
        progressLayout.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    @OnClick(R.id.cancel)
    public void onCancelButtonClicked() {
        finish();
    }
}
