package com.gallery.editor.image.photoeditor.utils

import android.content.*
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Point
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.WindowManager
import java.io.File
import java.util.*

class Utils {
    companion object {
        fun checkPermission(context: Context, permissions: Array<String>): Int {
            var permissionCheck = PackageManager.PERMISSION_GRANTED
            for (permission in permissions) {
                permissionCheck += ContextCompat.checkSelfPermission(context, permission)
            }
            return permissionCheck
        }

        fun rateApp(context: Context) {
            val uri = Uri.parse("market://details?id=" + context.packageName)
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            try {
                context.startActivity(goToMarket)
            } catch (e: ActivityNotFoundException) {
                context.startActivity(Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + context.packageName)))
            }

        }

        /**
         * create folder
         */
        fun creatFolder() {
            var folder = File(Constants.FOLDER)
            if (!folder.exists()) {
                folder.mkdirs()
            }
            var temp = File(Constants.TEMP)
            if (temp.exists()) {
                removeAllFileTemp(temp)
            } else {
                temp.mkdirs()
                createFileNomedia(Constants.TEMP)
            }
        }

        /**
         * remve all file into folder temp
         */
        fun removeAllFileTemp(temp: File) {
            for (f in temp.listFiles()) {
                if (!f.name.equals(".nomedia")) {
                    f.delete()
                }
            }
        }

        fun createFileNomedia(folder: String) {
            var f = File(folder, ".nomedia")
            f.createNewFile()
        }

        /**
         * creat image
         */
        fun creatFileImage(folder: String, name: String): String {
            return "${folder}/${name}.jpg"
        }

        /**
         * get full width of screen
         */
        fun getWidthScreen(context: Context): Int {
            val x: Int
            val y: Int
            val orientation = context.resources.configuration.orientation
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                val screenSize = Point()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    display.getRealSize(screenSize)
                    x = screenSize.x
                    y = screenSize.y
                } else {
                    display.getSize(screenSize)
                    x = screenSize.x
                    y = screenSize.y
                }
            } else {
                x = display.width
                y = display.height
            }

            return getWidth(x, y, orientation)

        }

        /**
         * get full width of screen
         */
        fun getHeightScreen(context: Context): Int {
            val x: Int
            val y: Int
            val orientation = context.resources.configuration.orientation
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                val screenSize = Point()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    display.getRealSize(screenSize)
                    x = screenSize.x
                    y = screenSize.y
                } else {
                    display.getSize(screenSize)
                    x = screenSize.x
                    y = screenSize.y
                }
            } else {
                x = display.width
                y = display.height
            }

            return getHeight(x, y, orientation)

        }

        private fun getWidth(x: Int, y: Int, orientation: Int): Int {
            return if (orientation == Configuration.ORIENTATION_PORTRAIT) x else y
        }

        private fun getHeight(x: Int, y: Int, orientation: Int): Int {
            return if (orientation == Configuration.ORIENTATION_PORTRAIT) y else x
        }

        /**
         * convert stack to arraylist
         */
        fun reverseStackToArr(stack: Stack<View>): ArrayList<View> {
            var arr = java.util.ArrayList<View>()
            while (!stack.isEmpty()) {
                arr.add(stack.pop())
            }
            return arr
        }

        /**
         * update file into con
         */
        fun updatefile(contentResolver: ContentResolver, path: String) {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.DISPLAY_NAME, getNanePath(path))
            values.put(MediaStore.Images.Media.DESCRIPTION, "image edit from application Photo Editor")
            values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis())
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
            values.put(MediaStore.Images.Media.DATA, path)
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        }

        /**
         * get name image
         */
        fun getNanePath(path: String): String {
            return path.substring(path.lastIndexOf("/" )+ 1)
        }

    }
}