package com.gallery.editor.image.photoeditor.utils

import android.graphics.Bitmap
import android.os.AsyncTask
import com.gallery.editor.image.photoeditor.interfaces.BitmapActionListener

class SaveBitmapToLocal(onListener: BitmapActionListener) : AsyncTask<Bitmap, Void, String>() {
    private var onListener: BitmapActionListener

    init {
        this.onListener = onListener
    }

    override fun doInBackground(vararg params: Bitmap?): String? {
        var bitmap = params[0]
        return BitmapImage.saveBitmapToFile(bitmap!!, Constants.FOLDER, "photo-editor")
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        if (result != null) {
            onListener.saveBitmap(result)
        } else {
            onListener.saveErrorBitmap()
        }
    }
}