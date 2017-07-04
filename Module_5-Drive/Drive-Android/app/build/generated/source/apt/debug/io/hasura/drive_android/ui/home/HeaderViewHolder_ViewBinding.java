// Generated code from Butter Knife. Do not modify!
package io.hasura.drive_android.ui.home;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import io.hasura.drive_android.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class HeaderViewHolder_ViewBinding implements Unbinder {
  private HeaderViewHolder target;

  @UiThread
  public HeaderViewHolder_ViewBinding(HeaderViewHolder target, View source) {
    this.target = target;

    target.header = Utils.findRequiredViewAsType(source, R.id.header, "field 'header'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    HeaderViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.header = null;
  }
}
