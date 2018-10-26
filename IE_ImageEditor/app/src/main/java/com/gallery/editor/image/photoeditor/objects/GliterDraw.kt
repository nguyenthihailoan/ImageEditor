package com.gallery.editor.image.photoeditor.objects

import android.graphics.Bitmap

class GliterDraw(bitmap: Bitmap, x: Float, y: Float) {
    var bitmap: Bitmap
    var x: Float
    var y: Float

    init {
        this.bitmap = bitmap
        this.x = x
        this.y = y
    }
}