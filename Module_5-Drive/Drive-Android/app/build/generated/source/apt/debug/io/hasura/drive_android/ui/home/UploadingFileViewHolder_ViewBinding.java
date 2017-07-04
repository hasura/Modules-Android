// Generated code from Butter Knife. Do not modify!
package io.hasura.drive_android.ui.home;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import io.hasura.drive_android.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class UploadingFileViewHolder_ViewBinding implements Unbinder {
  private UploadingFileViewHolder target;

  @UiThread
  public UploadingFileViewHolder_ViewBinding(UploadingFileViewHolder target, View source) {
    this.target = target;

    target.fileTitle = Utils.findRequiredViewAsType(source, R.id.file_title, "field 'fileTitle'", TextView.class);
    target.progressBar = Utils.findRequiredViewAsType(source, R.id.progress_bar, "field 'progressBar'", ProgressBar.class);
    target.buttonStopUpload = Utils.findRequiredViewAsType(source, R.id.button_stop_upload, "field 'buttonStopUpload'", ImageButton.class);
    target.bottomLine = Utils.findRequiredViewAsType(source, R.id.bottom_line, "field 'bottomLine'", FrameLayout.class);
    target.imageView = Utils.findRequiredViewAsType(source, R.id.image_view, "field 'imageView'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    UploadingFileViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.fileTitle = null;
    target.progressBar = null;
    target.buttonStopUpload = null;
    target.bottomLine = null;
    target.imageView = null;
  }
}
