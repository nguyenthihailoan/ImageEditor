package com.gallery.editor.image.photoeditor.widgets.textview

import android.content.Context
import android.graphics.Typeface

class FontCache {
    private val fontCache = HashMap<String, Typeface>()
    fun getTypeface(context: Context?, fontname: String): Typeface? {
        var typeface = fontCache[fontname]
        if (typeface == null) {
            typeface = Typeface.createFromAsset(context?.assets, "fonts/$fontname")
            fontCache[fontname] = typeface
        }
        return typeface
    }
}