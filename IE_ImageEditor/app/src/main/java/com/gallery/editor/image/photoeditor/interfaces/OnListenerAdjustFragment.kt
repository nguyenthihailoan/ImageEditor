package com.gallery.editor.image.photoeditor.interfaces

import com.gallery.editor.image.photoeditor.objects.TypeEditor
import com.gallery.editor.image.photoeditor.widgets.simplecropview.CropImageView

interface OnListenerAdjustFragment {
    fun onClickAdjust(@TypeEditor type: Int)

    fun onDestroyView()

    fun onremoveFragment()

    fun onRotateLeft()

    fun onRotateRight()

    fun onFlipVertical()

    fun onFlipHorizontal()

    fun onSaveRotate()

    fun onChangeCropMode(mode: CropImageView.CropMode)

    fun onCancelCrop()

    fun onSaveCrop()

    fun hidenSeekbar()

    fun hidenGlView()

    fun saveAdjust()
}