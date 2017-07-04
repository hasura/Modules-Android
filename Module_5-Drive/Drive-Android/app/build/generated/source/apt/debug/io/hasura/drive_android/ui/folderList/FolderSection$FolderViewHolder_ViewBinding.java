// Generated code from Butter Knife. Do not modify!
package io.hasura.drive_android.ui.folderList;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import io.hasura.drive_android.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class FolderSection$FolderViewHolder_ViewBinding implements Unbinder {
  private FolderSection.FolderViewHolder target;

  @UiThread
  public FolderSection$FolderViewHolder_ViewBinding(FolderSection.FolderViewHolder target,
      View source) {
    this.target = target;

    target.imageView = Utils.findRequiredViewAsType(source, R.id.image_view, "field 'imageView'", ImageView.class);
    target.fileTitle = Utils.findRequiredViewAsType(source, R.id.file_title, "field 'fileTitle'", TextView.class);
    target.parentCard = Utils.findRequiredViewAsType(source, R.id.parent_card, "field 'parentCard'", CardView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    FolderSection.FolderViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.imageView = null;
    target.fileTitle = null;
    target.parentCard = null;
  }
}
