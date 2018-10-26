package com.gallery.editor.image.photoeditor.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.media.ExifInterface
import android.view.View
import android.widget.FrameLayout
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class BitmapImage {
    companion object {

        /**
         * save bitmap to file
         */
        fun saveBitmapToFile(bitmap: Bitmap, folder: String, name: String): String? {
            var path = "${folder}/${name}${Calendar.getInstance().timeInMillis}.jpg"
            try {
                FileOutputStream(path).use({ out ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out) // bmp is your Bitmap instance
                    // PNG is a lossless format, the compression factor (100) is ignored
                })
                return path
            } catch (e: IOException) {
                e.printStackTrace()
                return null
            }
        }

        /**
         * get direction image
         */
        fun getDirectionImage(pathImage: String): Int {
            var exif: ExifInterface? = null
            try {
                exif = ExifInterface(pathImage)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            val orientation = exif!!.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED)
            return orientation
        }

        fun rotateBitmap(bitmap: Bitmap, orientation: Int): Bitmap? {
            val matrix = Matrix()
            when (orientation) {
                ExifInterface.ORIENTATION_NORMAL -> return bitmap
                ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.setScale(-1f, 1f)
                ExifInterface.ORIENTATION_ROTATE_180 -> matrix.setRotate(180f)
                ExifInterface.ORIENTATION_FLIP_VERTICAL -> {
                    matrix.setRotate(180f)
                    matrix.postScale(-1f, 1f)
                }
                ExifInterface.ORIENTATION_TRANSPOSE -> {
                    matrix.setRotate(90f)
                    matrix.postScale(-1f, 1f)
                }
                ExifInterface.ORIENTATION_ROTATE_90 -> matrix.setRotate(90f)
                ExifInterface.ORIENTATION_TRANSVERSE -> {
                    matrix.setRotate(-90f)
                    matrix.postScale(-1f, 1f)
                }
                ExifInterface.ORIENTATION_ROTATE_270 -> matrix.setRotate(-90f)
                else -> return bitmap
            }
            try {
                val bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
                bitmap.recycle()
                return bmRotated
            } catch (e: OutOfMemoryError) {
                e.printStackTrace()
                return null
            }

        }

        fun drawBitmapWithText(bitmap: Bitmap, view: View): Bitmap? {
            val workingBitmap = Bitmap.createBitmap(bitmap)
            val mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true)
            var canvas = Canvas(mutableBitmap)
            var text = drawBitmapFromView(view)
            var textScale = Bitmap.createScaledBitmap(text, bitmap.width, bitmap.height, true)
            canvas.drawBitmap(textScale, 0f, 0f, Paint())
            return mutableBitmap
        }

        fun drawBitmapWithText(framlayout: FrameLayout, bitmap: Bitmap, arrStack: Stack<View>): Bitmap? {
            val workingBitmap = Bitmap.createBitmap(bitmap)
            val mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true)
            var canvas = Canvas(mutableBitmap)
            var bmp = drawBitmapFromView(framlayout, arrStack)
            var bmpScale = Bitmap.createScaledBitmap(bmp, bitmap.width, bitmap.height, true)
            canvas.drawBitmap(bmpScale, 0f, 0f, Paint())
            return mutableBitmap
        }

        fun drawBitmapFromView(view: View): Bitmap? {
            var text = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            var canvas = Canvas(text)
            view.draw(canvas)
            return text
        }

        fun drawBitmapFromView(framlayout: FrameLayout, arrStack: Stack<View>): Bitmap? {
            var arrView = Utils.reverseStackToArr(arrStack)
            var bmpView = Bitmap.createBitmap(arrView.get(0).width, arrView.get(0).height, Bitmap.Config.ARGB_8888)
            var canvas = Canvas(bmpView)
            for (view in arrView) {
                view.draw(canvas)
                framlayout.removeView(view)
            }
            return bmpView
        }

        fun drawBitmapWithAlpha(bitmap: Bitmap, paint: Paint): Bitmap {
            var bmp = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
            var canvas = Canvas(bmp)
            canvas.drawBitmap(bitmap, 0f, 0f, paint)
            return bmp
        }
    }
}