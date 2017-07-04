// Generated code from Butter Knife. Do not modify!
package io.hasura.drive_android.ui.editFile;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
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

public class EditFileActivity_ViewBinding implements Unbinder {
  private EditFileActivity target;

  private View view2131558532;

  private View view2131558531;

  @UiThread
  public EditFileActivity_ViewBinding(EditFileActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public EditFileActivity_ViewBinding(final EditFileActivity target, View source) {
    this.target = target;

    View view;
    target.imageView = Utils.findRequiredViewAsType(source, R.id.image_view, "field 'imageView'", ImageView.class);
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
    target.collapsingToolbar = Utils.findRequiredViewAsType(source, R.id.collapsing_toolbar, "field 'collapsingToolbar'", CollapsingToolbarLayout.class);
    target.filename = Utils.findRequiredViewAsType(source, R.id.filename, "field 'filename'", TextInputEditText.class);
    target.tilFilename = Utils.findRequiredViewAsType(source, R.id.til_filename, "field 'tilFilename'", TextInputLayout.class);
    target.number = Utils.findRequiredViewAsType(source, R.id.number, "field 'number'", TextInputEditText.class);
    target.tilNumber = Utils.findRequiredViewAsType(source, R.id.til_number, "field 'tilNumber'", TextInputLayout.class);
    target.expiry = Utils.findRequiredViewAsType(source, R.id.expiry, "field 'expiry'", TextInputEditText.class);
    target.tilExpiry = Utils.findRequiredViewAsType(source, R.id.til_expiry, "field 'tilExpiry'", TextInputLayout.class);
    view = Utils.findRequiredView(source, R.id.update, "field 'update' and method 'onUpdateButtonClicked'");
    target.update = Utils.castView(view, R.id.update, "field 'update'", Button.class);
    view2131558532 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onUpdateButtonClicked();
      }
    });
    target.progressLayout = Utils.findRequiredViewAsType(source, R.id.progress_layout, "field 'progressLayout'", LinearLayout.class);
    view = Utils.findRequiredView(source, R.id.cancel, "field 'cancel' and method 'onCancelButtonClicked'");
    target.cancel = Utils.castView(view, R.id.cancel, "field 'cancel'", Button.class);
    view2131558531 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onCancelButtonClicked();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    EditFileActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.imageView = null;
    target.toolbar = null;
    target.collapsingToolbar = null;
    target.filename = null;
    target.tilFilename = null;
    target.number = null;
    target.tilNumber = null;
    target.expiry = null;
    target.tilExpiry = null;
    target.update = null;
    target.progressLayout = null;
    target.cancel = null;

    view2131558532.setOnClickListener(null);
    view2131558532 = null;
    view2131558531.setOnClickListener(null);
    view2131558531 = null;
  }
}
