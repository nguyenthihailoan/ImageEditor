package com.gallery.editor.image.photoeditor.interfaces

import com.gallery.editor.image.photoeditor.objects.TypeEditor

interface OnListenerBrushFragment {
    fun changeStatusBrush(@TypeEditor type: Int)

    fun changeColorBrush(color: Int)

    fun changeGliter(gliter:String)

    fun onSaveWithDrawLine()

    fun onDestroyView()

    fun onremoveFragment()

    fun hidenViewDraw()

}