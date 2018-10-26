package com.gallery.editor.image.photoeditor.interfaces

import com.gallery.editor.image.photoeditor.objects.TypeBlendEffect
import com.gallery.editor.image.photoeditor.objects.TypeEditor

interface OnListenerEffectFragment {
    fun onClickEffect(effect: String, @TypeEditor type: Int)

    fun onClickEffectType(@TypeBlendEffect typeBlend: Int)

    fun onClickTypeEffect(@TypeEditor type: Int)

    fun saveImageWithBlende()

    fun removeEffect()

    fun onDestroyView()

    fun onremoveFragment()

}