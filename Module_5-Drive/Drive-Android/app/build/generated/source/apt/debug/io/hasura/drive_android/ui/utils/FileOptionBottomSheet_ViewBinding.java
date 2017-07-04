// Generated code from Butter Knife. Do not modify!
package io.hasura.drive_android.ui.utils;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import io.hasura.drive_android.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class FileOptionBottomSheet_ViewBinding implements Unbinder {
  private FileOptionBottomSheet target;

  private View view2131558558;

  private View view2131558561;

  private View view2131558562;

  @UiThread
  public FileOptionBottomSheet_ViewBinding(final FileOptionBottomSheet target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.editButton, "field 'editButton' and method 'onEditButtonClicked'");
    target.editButton = Utils.castView(view, R.id.editButton, "field 'editButton'", ImageView.class);
    view2131558558 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onEditButtonClicked();
      }
    });
    target.fileName = Utils.findRequiredViewAsType(source, R.id.fileName, "field 'fileName'", TextView.class);
    target.fileNumber = Utils.findRequiredViewAsType(source, R.id.fileNumber, "field 'fileNumber'", TextView.class);
    target.fileExpiry = Utils.findRequiredViewAsType(source, R.id.fileExpiry, "field 'fileExpiry'", TextView.class);
    view = Utils.findRequiredView(source, R.id.editInfoCard, "field 'editInfoCard' and method 'onEditInfoCardClicked'");
    target.editInfoCard = Utils.castView(view, R.id.editInfoCard, "field 'editInfoCard'", CardView.class);
    view2131558561 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onEditInfoCardClicked();
      }
    });
    view = Utils.findRequiredView(source, R.id.deleteFileCard, "field 'deleteFileCard' and method 'onDeleteFileCardClicked'");
    target.deleteFileCard = Utils.castView(view, R.id.deleteFileCard, "field 'deleteFileCard'", CardView.class);
    view2131558562 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onDeleteFileCardClicked();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    FileOptionBottomSheet target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.editButton = null;
    target.fileName = null;
    target.fileNumber = null;
    target.fileExpiry = null;
    target.editInfoCard = null;
    target.deleteFileCard = null;

    view2131558558.setOnClickListener(null);
    view2131558558 = null;
    view2131558561.setOnClickListener(null);
    view2131558561 = null;
    view2131558562.setOnClickListener(null);
    view2131558562 = null;
  }
}
