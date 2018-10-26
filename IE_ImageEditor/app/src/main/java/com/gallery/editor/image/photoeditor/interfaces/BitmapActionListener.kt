package com.gallery.editor.image.photoeditor.interfaces

import android.graphics.Bitmap

interface BitmapActionListener {
    fun saveBitmap(path: String)

    fun saveErrorBitmap()

}