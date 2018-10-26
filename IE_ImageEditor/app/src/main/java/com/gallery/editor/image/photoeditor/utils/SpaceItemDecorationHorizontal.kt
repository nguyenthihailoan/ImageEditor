package com.gallery.editor.image.photoeditor.utils

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class SpaceItemDecorationHorizontal(left: Int) : RecyclerView.ItemDecoration() {
    private val left: Int

    init {
        this.left = left
    }

    override fun getItemOffsets(outRect: Rect, view: View,
                                parent: RecyclerView, state: RecyclerView.State) {
        outRect.left = left
    }
}