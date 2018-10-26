package com.gallery.editor.image.photoeditor.widgets.imageview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.gallery.editor.image.photoeditor.interfaces.OnListenerStopDraw
import com.gallery.editor.image.photoeditor.objects.GliterDraw
import com.gallery.editor.image.photoeditor.utils.BitmapImage
import java.util.*


class GliterView : View {
    private var currentGliter: Bitmap? = null
    private var paint: Paint? = null
    private var size: Float = 0f
    private lateinit var onListener: OnListenerStopDraw
    private var arrBitmap = ArrayList<GliterDraw>()
    private var random = Random()
    private var paintAlpha = Paint()

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
        setUpPath()
    }

    private fun setUpPath() {
        paint = Paint()
        paint?.setAntiAlias(true)
    }

    /**
     * set gliter
     */
    fun setBitmapGliter(gliter: Bitmap) {
        this.currentGliter = gliter
    }

    /**
     * set onlistener
     */
    fun setOnListener(onListener: OnListenerStopDraw) {
        this.onListener = onListener
    }

    /**
     * set size
     */
    fun setSize(size: Float) {
        this.size = size
//        paint?.strokeWidth = size
    }

    override fun onDraw(canvas: Canvas?) {
        if (currentGliter != null) {
            for (gliter in arrBitmap) {
//                paint?.alpha = (alpha * 255).toInt()
                canvas?.drawBitmap(gliter.bitmap, gliter.x, gliter.y, paint)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        var eventX = event?.x
        var eventY = event?.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.d("DEBUG", "start")
            }
            MotionEvent.ACTION_MOVE -> {
                Log.d("DEBUG", "move")
                Log.d("DEBUG", eventX.toString())
                Log.d("DEBUG", eventY.toString())
                randomBitmapGliter(eventX.toInt(), eventY.toInt())
            }
            MotionEvent.ACTION_UP -> {
                onListener.onStopDrawLineBrush()
                return false
            }
            else -> {
                return false
            }
        }
        invalidate()
        return true
    }

    fun randomBitmapGliter(x: Int, y: Int) {
        var number = Random().nextInt(3)
        for (i in 0..number - 1) {
            var addsub = random.nextInt(2)
            var scale = random.nextFloat() + 0.2f
            var rotate = random.nextInt(360)
            var xR = x
            var yR = y
            if (addsub == 0) {
                xR = random.nextInt(size.toInt()) + 1 + x
                yR = random.nextInt(size.toInt()) + 1 + y
            } else {
                xR = random.nextInt(size.toInt()) + 1 - x
                yR = random.nextInt(size.toInt()) + 1 - y
            }
            var matrix = Matrix()
            matrix.postScale(scale, scale)
            matrix.postRotate(rotate.toFloat())
            var alpha = random.nextFloat() + 0.2f
            paintAlpha.alpha = (alpha * 255).toInt()
            var bitmap = Bitmap.createBitmap(currentGliter,
                    0, 0,
                    currentGliter!!.width, currentGliter!!.height, matrix, true)
            arrBitmap.add(GliterDraw(BitmapImage.drawBitmapWithAlpha(bitmap, paintAlpha), xR.toFloat(), yR.toFloat()))
        }
    }


}