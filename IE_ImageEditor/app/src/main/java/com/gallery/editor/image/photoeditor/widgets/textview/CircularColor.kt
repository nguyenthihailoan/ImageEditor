package com.gallery.editor.image.photoeditor.widgets.textview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.util.AttributeSet
import android.widget.TextView
import com.gallery.editor.image.photoeditor.R


class CircularColor : TextView {
    private var backgroundDrawable: ShapeDrawable? = null
    private var ovalShape: OvalShape? = null

    private var backgroundColor: Int = 0

    constructor(context: Context?) : super(context) {
        backgroundColor = context!!.resources.getColor(R.color.color1)
        allocateShapes();
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        backgroundColor = context!!.resources.getColor(R.color.color1)
        allocateShapes()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        backgroundColor = context!!.resources.getColor(R.color.color1)
        allocateShapes()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val h = this.measuredHeight
        val w = this.measuredWidth
        val r = Math.max(w, h)

        setMeasuredDimension(r, r)
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        backgroundDrawable!!.setShape(ovalShape);
        backgroundDrawable!!.getPaint().setColor(backgroundColor);

        setBackground(backgroundDrawable);
    }

    private fun allocateShapes() {
        backgroundDrawable = ShapeDrawable()
        ovalShape = OvalShape()
    }

    override fun setBackgroundColor(color: Int) {
        backgroundColor = color
        invalidate()
    }
}