package com.gallery.editor.image.photoeditor.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.IOException
import java.io.InputStream


class BitmapFactoryL {
    companion object {
        /**
         * get bitmap from uri
         */
        fun getBitmapFromLocal(path: String): Bitmap {
            var bitmap = BitmapFactory.decodeFile(path)

            if (bitmap.width >= bitmap.height) {
                if (bitmap.width > 1024) {
                    var scale = 1024f / bitmap.width
                    var h = scale * bitmap.height
                    return Bitmap.createScaledBitmap(bitmap, 1024, h.toInt(), true)
                }
            } else {
                if (bitmap.height > 1024) {
                    var scale = 1024f / bitmap.height
                    var w = scale * bitmap.width
                    return Bitmap.createScaledBitmap(bitmap, w.toInt(), 1024, true)
                }
            }
            return bitmap
        }

        /**
         * get bitmap from asset
         */
        fun getBitmapFromAsset(context: Context, filePath: String): Bitmap? {
            val assetManager = context.getAssets()
            val istr: InputStream
            var bitmap: Bitmap? = null
            try {
                istr = assetManager.open(filePath)
                bitmap = BitmapFactory.decodeStream(istr)
            } catch (e: IOException) {
                // handle exception
            }
            return bitmap
        }
        fun getBitmapFromAsset(context: Context, filePath: String,size:Int): Bitmap? {

            val assetManager = context.getAssets()
            val istr: InputStream
            var bitmap: Bitmap? = null
            var sizeB=size
            if (sizeB==0) sizeB=1
            try {
                istr = assetManager.open(filePath)
                bitmap = BitmapFactory.decodeStream(istr)
                bitmap=Bitmap.createScaledBitmap(bitmap,sizeB,sizeB,true)
            } catch (e: IOException) {
                // handle exception
            }
            return bitmap
        }
    }
}