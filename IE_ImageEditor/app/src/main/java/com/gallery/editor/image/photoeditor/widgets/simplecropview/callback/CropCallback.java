package com.gallery.editor.image.photoeditor.widgets.simplecropview.callback;

import android.graphics.Bitmap;

public interface CropCallback extends Callback {
  void onSuccess(Bitmap cropped);
}
