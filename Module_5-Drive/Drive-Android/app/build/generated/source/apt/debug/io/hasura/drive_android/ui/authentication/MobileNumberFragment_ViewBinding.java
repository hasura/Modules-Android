// Generated code from Butter Knife. Do not modify!
package io.hasura.drive_android.ui.authentication;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import io.hasura.drive_android.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MobileNumberFragment_ViewBinding implements Unbinder {
  private MobileNumberFragment target;

  private View view2131558567;

  @UiThread
  public MobileNumberFragment_ViewBinding(final MobileNumberFragment target, View source) {
    this.target = target;

    View view;
    target.mobileNumber = Utils.findRequiredViewAsType(source, R.id.mobile_number, "field 'mobileNumber'", TextInputEditText.class);
    target.tilMobileNumber = Utils.findRequiredViewAsType(source, R.id.til_mobile_number, "field 'tilMobileNumber'", TextInputLayout.class);
    target.tilUsername = Utils.findRequiredViewAsType(source, R.id.til_user_name, "field 'tilUsername'", TextInputLayout.class);
    target.username = Utils.findRequiredViewAsType(source, R.id.user_name, "field 'username'", TextInputEditText.class);
    view = Utils.findRequiredView(source, R.id.next_button, "field 'nextButton' and method 'onViewClicked'");
    target.nextButton = Utils.castView(view, R.id.next_button, "field 'nextButton'", Button.class);
    view2131558567 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    MobileNumberFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mobileNumber = null;
    target.tilMobileNumber = null;
    target.tilUsername = null;
    target.username = null;
    target.nextButton = null;

    view2131558567.setOnClickListener(null);
    view2131558567 = null;
  }
}
