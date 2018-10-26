package com.gallery.editor.image.photoeditor.widgets.simplecropview.callback;

import android.net.Uri;

public interface SaveCallback extends Callback {
  void onSuccess(Uri uri);
}
