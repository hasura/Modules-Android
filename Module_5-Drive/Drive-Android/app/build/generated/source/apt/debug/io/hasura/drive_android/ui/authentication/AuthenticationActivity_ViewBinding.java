// Generated code from Butter Knife. Do not modify!
package io.hasura.drive_android.ui.authentication;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import io.hasura.drive_android.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AuthenticationActivity_ViewBinding implements Unbinder {
  private AuthenticationActivity target;

  @UiThread
  public AuthenticationActivity_ViewBinding(AuthenticationActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public AuthenticationActivity_ViewBinding(AuthenticationActivity target, View source) {
    this.target = target;

    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
    target.collapsingToolbar = Utils.findRequiredViewAsType(source, R.id.collapsing_toolbar, "field 'collapsingToolbar'", CollapsingToolbarLayout.class);
    target.container = Utils.findRequiredViewAsType(source, R.id.container, "field 'container'", FrameLayout.class);
    target.progressLayout = Utils.findRequiredViewAsType(source, R.id.progress_layout, "field 'progressLayout'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    AuthenticationActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar = null;
    target.collapsingToolbar = null;
    target.container = null;
    target.progressLayout = null;
  }
}
