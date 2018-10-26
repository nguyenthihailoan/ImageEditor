package com.gallery.editor.image.photoeditor.utils

import android.graphics.Bitmap
import android.os.Environment

class Constants {
    companion object {
        val NAME_FOLDER = "PhotoEditor"
        val FOLDER = Environment.getExternalStorageDirectory().absolutePath + "/$NAME_FOLDER"
        val TEMP = FOLDER + "/.temp"

        const val EXTRA_IMAGE = "extra_video"
        var bitmap: Bitmap? = null
    }
}