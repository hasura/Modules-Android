package io.hasura.drive_android.ui.folderList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.hasura.drive_android.R;
import io.hasura.drive_android.enums.FolderListState;
import io.hasura.drive_android.models.HasuraFolder;
import io.hasura.drive_android.stores.FileDataStore;
import io.hasura.drive_android.ui.home.HomeActivity;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class FolderListActivity extends AppCompatActivity {
    SectionedRecyclerViewAdapter adapter;
    FolderSection folderSection;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.retry_button)
    Button retryButton;
    @BindView(R.id.error_layout)
    LinearLayout errorLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.progress_bar)
    MaterialProgressBar progressBar;

    public static void startActivity(Activity startingActivity) {
        Intent intent = new Intent(startingActivity, FolderListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startingActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_list);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        showErrorLayout(false);

        adapter = new SectionedRecyclerViewAdapter();
        folderSection = new FolderSection(new FolderSection.Listener() {
            @Override
            public void onFileSelected(HasuraFolder folder) {
                HomeActivity.startActivity(FolderListActivity.this, folder);
            }
        });
        adapter.addSection(folderSection);
        recyclerView.setAdapter(adapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (adapter.getSectionItemViewType(position)) {
                    case SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER:
                        return 2;
                    default:
                        return 1;
                }
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.addItemDecoration(new SpacesItemDecoration());
        fetchFolders();
    }

    private void fetchFolders() {
        FileDataStore.getInstance().getFolderList(new FileDataStore.FolderListListener() {
            @Override
            public void onFolderListStateChanged(FolderListState folderListState, Object object) {
                switch (folderListState) {
                    case SYNC_STARTED:
                        showProgressIndicator(true);
                        break;
                    case SYNC_COMPLETE:
                        showProgressIndicator(false);
                        folderSection.setData((List<HasuraFolder>) object);
                        adapter.notifyDataSetChanged();
                        break;
                    case SYNC_FAILED:
                        showProgressIndicator(false);
                        showErrorLayout(true);
                        break;
                }
            }
        });
    }

    @OnClick(R.id.retry_button)
    public void onViewClicked() {
        showErrorLayout(false);
        fetchFolders();
    }

    private void showErrorLayout(Boolean shouldShow) {
        errorLayout.setVisibility(shouldShow ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(!shouldShow ? View.VISIBLE : View.GONE);
    }

    private void showProgressIndicator(Boolean shouldShow) {
        progressBar.setVisibility(shouldShow ? View.VISIBLE : View.INVISIBLE);
    }
}
