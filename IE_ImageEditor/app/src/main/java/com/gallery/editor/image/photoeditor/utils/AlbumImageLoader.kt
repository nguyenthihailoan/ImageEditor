package com.gallery.editor.image.photoeditor.utils

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.AsyncTask
import android.provider.MediaStore
import android.util.Log
import com.gallery.editor.image.photoeditor.interfaces.ImageCallback
import com.gallery.editor.image.photoeditor.objects.ImageMedia

class AlbumImageLoader(context: Context, callback: ImageCallback) : AsyncTask<Void, Void, HashMap<String, ArrayList<ImageMedia>>>() {
    val BUCKET_ID = 0
    val BUKET_ALBUM = 1
    val NAME_IMAGE = 2
    val PATH_IMAGE = 3
    private var context: Context? = null
    private var callback: ImageCallback? = null
    private var isDebug=true

    init {
        this.context = context
        this.callback = callback
    }

    override fun doInBackground(vararg params: Void?): HashMap<String, ArrayList<ImageMedia>> {
        return getAlbumImage(context!!)
    }

    override fun onPostExecute(result: HashMap<String, ArrayList<ImageMedia>>?) {
        if (result != null) {
            callback?.onLoadedImage(result)
        } else {
            callback?.onLoadedFailImage()
        }
    }

    fun getAlbumImage(context: Context): HashMap<String, ArrayList<ImageMedia>> {
        var mapImage = java.util.HashMap<String, ArrayList<ImageMedia>>()
        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Images.ImageColumns.BUCKET_ID,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATA)
        var cursor: Cursor = context.contentResolver.query(uri, projection, null, null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC")
        if (cursor != null) {
            while (cursor.moveToNext()) {
                var idAlbum = cursor.getString(BUCKET_ID)
                var nameAlbum: String = cursor.getString(BUKET_ALBUM)
                var pathImage: String = cursor.getString(PATH_IMAGE)
                var nameImage: String = cursor.getString(NAME_IMAGE)
               if (isDebug) Log.d("DEBUG", nameImage)
                var image = ImageMedia(idAlbum, nameAlbum, nameImage, pathImage)
                if (mapImage.containsKey(idAlbum)) {
                    if (!nameImage.endsWith(".gif")) {
                        mapImage.get(idAlbum)?.add(image)
                    }

                } else {
                    if (!nameImage.endsWith(".gif")) {
                        var images = ArrayList<ImageMedia>()
                        images.add(image)
                        mapImage.put(idAlbum, images)
                    }
                }
            }
        }
        return mapImage
    }
}