package com.gallery.editor.image.photoeditor.utils

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class SpaceItemDecorationGrid(right: Int, bottom: Int) : RecyclerView.ItemDecoration() {
    private val right: Int
    private val bottom: Int

    init {
        this.bottom = bottom
        this.right = right
    }

    override fun getItemOffsets(outRect: Rect, view: View,
                                parent: RecyclerView, state: RecyclerView.State) {
        outRect.bottom = bottom
        outRect.right = right


    }
}