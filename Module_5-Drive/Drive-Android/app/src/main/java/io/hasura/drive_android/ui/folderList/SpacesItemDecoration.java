package io.hasura.drive_android.ui.folderList;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpacesItemDecoration() {
        this.space = 10;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildLayoutPosition(view) == 0)
            return;
        outRect.bottom = space/2;
        outRect.top = space/2;
        if (parent.getChildLayoutPosition(view) % 2 == 0) {
            //Right
            outRect.left = space/2;
            outRect.right = space;
        } else {
            //Left
            outRect.left = space;
            outRect.right = space/2;
        }

    }
}