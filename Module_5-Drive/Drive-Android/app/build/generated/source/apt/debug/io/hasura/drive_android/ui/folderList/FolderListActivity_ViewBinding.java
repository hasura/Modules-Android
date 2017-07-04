// Generated code from Butter Knife. Do not modify!
package io.hasura.drive_android.ui.folderList;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import io.hasura.drive_android.R;
import java.lang.IllegalStateException;
import java.lang.Override;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class FolderListActivity_ViewBinding implements Unbinder {
  private FolderListActivity target;

  private View view2131558577;

  @UiThread
  public FolderListActivity_ViewBinding(FolderListActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public FolderListActivity_ViewBinding(final FolderListActivity target, View source) {
    this.target = target;

    View view;
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
    view = Utils.findRequiredView(source, R.id.retry_button, "field 'retryButton' and method 'onViewClicked'");
    target.retryButton = Utils.castView(view, R.id.retry_button, "field 'retryButton'", Button.class);
    view2131558577 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked();
      }
    });
    target.errorLayout = Utils.findRequiredViewAsType(source, R.id.error_layout, "field 'errorLayout'", LinearLayout.class);
    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.recycler_view, "field 'recyclerView'", RecyclerView.class);
    target.progressBar = Utils.findRequiredViewAsType(source, R.id.progress_bar, "field 'progressBar'", MaterialProgressBar.class);
    target.fab = Utils.findRequiredViewAsType(source, R.id.fab_add, "field 'fab'", FloatingActionButton.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    FolderListActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar = null;
    target.retryButton = null;
    target.errorLayout = null;
    target.recyclerView = null;
    target.progressBar = null;
    target.fab = null;

    view2131558577.setOnClickListener(null);
    view2131558577 = null;
  }
}
