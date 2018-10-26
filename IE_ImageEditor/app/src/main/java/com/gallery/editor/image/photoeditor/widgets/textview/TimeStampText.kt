package com.gallery.editor.image.photoeditor.widgets.textview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.FrameLayout
import android.widget.TextView


class TimeStampText : TextView {
    val NONE = 0
    val DRAG = 1
    val SCALE = 2
    var sizeText = 30f
    var scaleText = 1f
    var maxScale = 3f
    private var xDelta: Int = 0
    private var yDelta: Int = 0
    private var mode = 0
    lateinit var scaleDetector: ScaleGestureDetector

    constructor(context: Context?) : super(context) {
        initTextview()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initTextview()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initTextview()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        super.onTouchEvent(event)
        val X = event?.getRawX()?.toInt()
        val Y = event?.getRawY()?.toInt()
        when (event?.getAction()!! and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                val lParams = getLayoutParams() as FrameLayout.LayoutParams
                xDelta = X!! - lParams.leftMargin
                yDelta = Y!! - lParams.topMargin
                mode = DRAG
                Log.d("DEBUG", "down")
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                mode = NONE
                Log.d("DEBUG", "up")
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                var oldist = spacing(event)
                if (oldist > 10f) {
                    mode = SCALE
                }
                Log.d("DEBUG", "pointerdown")

            }
            MotionEvent.ACTION_MOVE -> {
                if (mode == DRAG) {
                    val layoutParams = getLayoutParams() as FrameLayout.LayoutParams
                    layoutParams.leftMargin = X!! - xDelta
                    layoutParams.topMargin = Y!! - yDelta
                    setLayoutParams(layoutParams)
                } else if (mode == SCALE && event.pointerCount == 2) {
                    scaleDetector.onTouchEvent(event)
                }
                Log.d("DEBUG", "move")
            }
        }
        return true
    }

    fun initTextview() {
        sizeText = sizeText
        scaleDetector = ScaleGestureDetector(context, ScaleListener())
    }

    private fun spacing(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return Math.sqrt((x * x + y * y).toDouble()).toFloat()
    }

    inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector?): Boolean {
            scaleText *= detector?.scaleFactor!!
            scaleText = Math.max(1.0f, Math.min(scaleText, maxScale));
            setTextSize(TypedValue.COMPLEX_UNIT_SP, sizeText * scaleText);
            return true;
        }
    }

}