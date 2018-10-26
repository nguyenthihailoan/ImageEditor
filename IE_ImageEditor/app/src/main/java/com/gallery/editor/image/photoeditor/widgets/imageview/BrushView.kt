package com.gallery.editor.image.photoeditor.widgets.imageview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import com.gallery.editor.image.photoeditor.interfaces.OnListenerStopDraw


class BrushView : View {
    private var paint: Paint? = null
    private var gestureDetector: GestureDetector? = null
    private var color: Int = 0
    private var size: Float = 0f
    private var pathCurrent: Path? = null
    private lateinit var onListener: OnListenerStopDraw

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }


    private fun init() {
        gestureDetector = GestureDetector(context, GestureListener())
        setUpPath()
    }

    private fun setUpPath() {
        pathCurrent = Path()
        paint = Paint()
        paint?.setAntiAlias(true)
        paint?.setStrokeWidth(6f)
        paint?.setColor(Color.WHITE)
        paint?.setStyle(Paint.Style.STROKE)
        paint?.setStrokeJoin(Paint.Join.ROUND)

    }

    /**
     * set onlistener
     */
    fun setOnListener(onListener: OnListenerStopDraw) {
        this.onListener = onListener
    }

    /**
     * set color
     */
    fun setColor(color: Int) {
        this.color = color
        paint?.color = color
    }

    /**
     * set size
     */
    fun setSize(size: Float) {
        this.size = size
        paint?.strokeWidth = size
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawPath(pathCurrent, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        var eventX = event?.x
        var eventY = event?.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                pathCurrent?.moveTo(eventX, eventY)
            }
            MotionEvent.ACTION_MOVE -> {
                pathCurrent?.lineTo(eventX, eventY)
            }
            MotionEvent.ACTION_UP -> {
                onListener.onStopDrawLineBrush()
                return false
            }
            else -> {
                return false
            }
        }
        gestureDetector?.onTouchEvent(event)
        // Schedules a repaint.
        invalidate()
        return true
    }

    inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDoubleTap(e: MotionEvent): Boolean {
            var x = e.getX();
            var y = e.getY();
            // clean drawing area on double tap
            pathCurrent?.reset();
            Log.d("Double Tap", "Tapped at: (" + x + "," + y + ")");
            return true;
        }
    }
}