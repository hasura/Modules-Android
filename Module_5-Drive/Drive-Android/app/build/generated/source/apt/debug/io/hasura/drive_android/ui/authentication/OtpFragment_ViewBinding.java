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

public class OtpFragment_ViewBinding implements Unbinder {
  private OtpFragment target;

  private View view2131558572;

  private View view2131558571;

  @UiThread
  public OtpFragment_ViewBinding(final OtpFragment target, View source) {
    this.target = target;

    View view;
    target.otp = Utils.findRequiredViewAsType(source, R.id.otp, "field 'otp'", TextInputEditText.class);
    target.tilOtp = Utils.findRequiredViewAsType(source, R.id.til_otp, "field 'tilOtp'", TextInputLayout.class);
    view = Utils.findRequiredView(source, R.id.submit_button, "field 'submitButton' and method 'onSubmitButtonClicked'");
    target.submitButton = Utils.castView(view, R.id.submit_button, "field 'submitButton'", Button.class);
    view2131558572 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onSubmitButtonClicked();
      }
    });
    view = Utils.findRequiredView(source, R.id.resend_button, "field 'resendButton' and method 'onResendButtonClicked'");
    target.resendButton = Utils.castView(view, R.id.resend_button, "field 'resendButton'", Button.class);
    view2131558571 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onResendButtonClicked();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    OtpFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.otp = null;
    target.tilOtp = null;
    target.submitButton = null;
    target.resendButton = null;

    view2131558572.setOnClickListener(null);
    view2131558572 = null;
    view2131558571.setOnClickListener(null);
    view2131558571 = null;
  }
}
