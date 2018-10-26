package com.gallery.editor.image.photoeditor.widgets.textview

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView

class TextViewMedium :TextView {
    val FONT="Quicksand-Medium.ttf"
    constructor(context: Context?) : super(context) {
        appplyFont(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        appplyFont(context)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        appplyFont(context)
    }

    fun appplyFont(context: Context?) {
        val customFont = FontCache().getTypeface(context, FONT)
        setTypeface(customFont)
    }
}