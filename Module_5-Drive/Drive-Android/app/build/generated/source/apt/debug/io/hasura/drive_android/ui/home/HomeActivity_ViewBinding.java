// Generated code from Butter Knife. Do not modify!
package io.hasura.drive_android.ui.home;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.flipboard.bottomsheet.BottomSheetLayout;
import io.hasura.drive_android.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class HomeActivity_ViewBinding implements Unbinder {
  private HomeActivity target;

  private View view2131558577;

  @UiThread
  public HomeActivity_ViewBinding(HomeActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public HomeActivity_ViewBinding(final HomeActivity target, View source) {
    this.target = target;

    View view;
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.recycler_view, "field 'recyclerView'", RecyclerView.class);
    target.fabAdd = Utils.findRequiredViewAsType(source, R.id.fab_add, "field 'fabAdd'", FloatingActionButton.class);
    target.bottomSheet = Utils.findRequiredViewAsType(source, R.id.bottomSheet, "field 'bottomSheet'", BottomSheetLayout.class);
    target.swipeRefreshLayout = Utils.findRequiredViewAsType(source, R.id.swipeRefreshLayout, "field 'swipeRefreshLayout'", SwipeRefreshLayout.class);
    view = Utils.findRequiredView(source, R.id.retry_button, "field 'retryButton' and method 'onRetryButtonClicked'");
    target.retryButton = Utils.castView(view, R.id.retry_button, "field 'retryButton'", Button.class);
    view2131558577 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onRetryButtonClicked();
      }
    });
    target.errorLayout = Utils.findRequiredViewAsType(source, R.id.error_layout, "field 'errorLayout'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    HomeActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar = null;
    target.recyclerView = null;
    target.fabAdd = null;
    target.bottomSheet = null;
    target.swipeRefreshLayout = null;
    target.retryButton = null;
    target.errorLayout = null;

    view2131558577.setOnClickListener(null);
    view2131558577 = null;
  }
}
