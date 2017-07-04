// Generated code from Butter Knife. Do not modify!
package io.hasura.drive_android.ui.fileDetail;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import io.hasura.drive_android.R;
import java.lang.IllegalStateException;
import java.lang.Override;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class FileDetailActivity_ViewBinding implements Unbinder {
  private FileDetailActivity target;

  @UiThread
  public FileDetailActivity_ViewBinding(FileDetailActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public FileDetailActivity_ViewBinding(FileDetailActivity target, View source) {
    this.target = target;

    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
    target.progressBar = Utils.findRequiredViewAsType(source, R.id.progress_bar, "field 'progressBar'", MaterialProgressBar.class);
    target.image = Utils.findRequiredViewAsType(source, R.id.image, "field 'image'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    FileDetailActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.toolbar = null;
    target.progressBar = null;
    target.image = null;
  }
}
