// Generated code from Butter Knife. Do not modify!
package io.hasura.drive_android.ui.home;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import io.hasura.drive_android.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class FileViewHolder_ViewBinding implements Unbinder {
  private FileViewHolder target;

  @UiThread
  public FileViewHolder_ViewBinding(FileViewHolder target, View source) {
    this.target = target;

    target.cardView = Utils.findRequiredViewAsType(source, R.id.parent_card, "field 'cardView'", CardView.class);
    target.imageView = Utils.findRequiredViewAsType(source, R.id.image_view, "field 'imageView'", ImageView.class);
    target.fileTitle = Utils.findRequiredViewAsType(source, R.id.file_title, "field 'fileTitle'", TextView.class);
    target.fileDescription = Utils.findRequiredViewAsType(source, R.id.file_description, "field 'fileDescription'", TextView.class);
    target.bottomLine = Utils.findRequiredViewAsType(source, R.id.bottom_line, "field 'bottomLine'", FrameLayout.class);
    target.optionsButton = Utils.findRequiredViewAsType(source, R.id.options, "field 'optionsButton'", ImageButton.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    FileViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.cardView = null;
    target.imageView = null;
    target.fileTitle = null;
    target.fileDescription = null;
    target.bottomLine = null;
    target.optionsButton = null;
  }
}
