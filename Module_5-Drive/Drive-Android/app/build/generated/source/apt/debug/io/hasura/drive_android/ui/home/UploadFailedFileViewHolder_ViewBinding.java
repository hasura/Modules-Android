// Generated code from Butter Knife. Do not modify!
package io.hasura.drive_android.ui.home;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import io.hasura.drive_android.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class UploadFailedFileViewHolder_ViewBinding implements Unbinder {
  private UploadFailedFileViewHolder target;

  @UiThread
  public UploadFailedFileViewHolder_ViewBinding(UploadFailedFileViewHolder target, View source) {
    this.target = target;

    target.fileTitle = Utils.findRequiredViewAsType(source, R.id.file_title, "field 'fileTitle'", TextView.class);
    target.bottomLine = Utils.findRequiredViewAsType(source, R.id.bottom_line, "field 'bottomLine'", FrameLayout.class);
    target.imageView = Utils.findRequiredViewAsType(source, R.id.image_view, "field 'imageView'", ImageView.class);
    target.deleteButton = Utils.findRequiredViewAsType(source, R.id.delete_button, "field 'deleteButton'", Button.class);
    target.retryButton = Utils.findRequiredViewAsType(source, R.id.retry_button, "field 'retryButton'", Button.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    UploadFailedFileViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.fileTitle = null;
    target.bottomLine = null;
    target.imageView = null;
    target.deleteButton = null;
    target.retryButton = null;
  }
}
