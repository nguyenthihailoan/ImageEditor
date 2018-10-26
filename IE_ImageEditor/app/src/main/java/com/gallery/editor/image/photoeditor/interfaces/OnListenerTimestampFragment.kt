package com.gallery.editor.image.photoeditor.interfaces

import android.graphics.Typeface
import com.gallery.editor.image.photoeditor.objects.TypeFont

interface OnListenerTimestampFragment {
    fun onClickFont(@TypeFont font: String)

    fun onClickColor(color:Int)

    fun saveTextWithBitmap(color:Int,typeface:Typeface)

    fun onDestroyView()

    fun onremoveFragment()

}