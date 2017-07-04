// Generated code from Butter Knife. Do not modify!
package io.hasura.drive_android.ui.launcher;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import io.hasura.drive_android.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LauncherActivity_ViewBinding implements Unbinder {
  private LauncherActivity target;

  private View view2131558540;

  private View view2131558544;

  @UiThread
  public LauncherActivity_ViewBinding(LauncherActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public LauncherActivity_ViewBinding(final LauncherActivity target, View source) {
    this.target = target;

    View view;
    target.viewPager = Utils.findRequiredViewAsType(source, R.id.view_pager, "field 'viewPager'", ViewPager.class);
    view = Utils.findRequiredView(source, R.id.signInButton, "field 'signInButton' and method 'onSignInButtonClicked'");
    target.signInButton = Utils.castView(view, R.id.signInButton, "field 'signInButton'", Button.class);
    view2131558540 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onSignInButtonClicked();
      }
    });
    target.firstIndicator = Utils.findRequiredViewAsType(source, R.id.firstIndicator, "field 'firstIndicator'", ImageView.class);
    target.secondIndicator = Utils.findRequiredViewAsType(source, R.id.secondIndicator, "field 'secondIndicator'", ImageView.class);
    target.thirdIndicator = Utils.findRequiredViewAsType(source, R.id.thirdIndicator, "field 'thirdIndicator'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.nextButton, "field 'nextButton' and method 'onNextButtonClicked'");
    target.nextButton = Utils.castView(view, R.id.nextButton, "field 'nextButton'", Button.class);
    view2131558544 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onNextButtonClicked();
      }
    });
    target.parent = Utils.findRequiredViewAsType(source, R.id.parent, "field 'parent'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    LauncherActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.viewPager = null;
    target.signInButton = null;
    target.firstIndicator = null;
    target.secondIndicator = null;
    target.thirdIndicator = null;
    target.nextButton = null;
    target.parent = null;

    view2131558540.setOnClickListener(null);
    view2131558540 = null;
    view2131558544.setOnClickListener(null);
    view2131558544 = null;
  }
}
