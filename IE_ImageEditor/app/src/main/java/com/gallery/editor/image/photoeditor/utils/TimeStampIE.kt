package com.gallery.editor.image.photoeditor.utils

import android.text.format.DateFormat
import java.util.*

class TimeStampIE {
    companion object {
//        fun getCurrentTime(): String {
//            var calendar = Calendar.getInstance()
//            var dateFormat = DateFormat.format("hh:mm a\ndd.MM.yy", calendar)
//            return dateFormat.toString()
//        }
//

        fun getCurrentTime(): String {
            var calendar = Calendar.getInstance()
            var time = DateFormat.format("hh:mm a", calendar)
            return time.toString()
        }

        fun getCurrentDate(): String {
            var calendar = Calendar.getInstance()
            var dateFormat = DateFormat.format("dd.MM.yyyy", calendar)
            return dateFormat.toString()
        }

        fun convertTimeToHtml(): String {
            var html = "<h5>${getCurrentTime()}<br><small><small>${getCurrentDate()}</small></small></h5>"
            return html
        }

        fun getTimeSaveImage():String{
            var calendar = Calendar.getInstance()
            var time = DateFormat.format("hh_mm", calendar)
            var date= DateFormat.format("dd_MM_yy", calendar)
            return "${time}${date}"
        }
    }
}