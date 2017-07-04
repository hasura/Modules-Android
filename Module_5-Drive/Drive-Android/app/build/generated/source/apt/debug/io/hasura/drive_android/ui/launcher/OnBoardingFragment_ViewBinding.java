// Generated code from Butter Knife. Do not modify!
package io.hasura.drive_android.ui.launcher;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import io.hasura.drive_android.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class OnBoardingFragment_ViewBinding implements Unbinder {
  private OnBoardingFragment target;

  @UiThread
  public OnBoardingFragment_ViewBinding(OnBoardingFragment target, View source) {
    this.target = target;

    target.image = Utils.findRequiredViewAsType(source, R.id.image, "field 'image'", ImageView.class);
    target.title = Utils.findRequiredViewAsType(source, R.id.title, "field 'title'", TextView.class);
    target.description = Utils.findRequiredViewAsType(source, R.id.description, "field 'description'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    OnBoardingFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.image = null;
    target.title = null;
    target.description = null;
  }
}
