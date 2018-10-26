package com.gallery.editor.image.photoeditor.utils

import android.content.Context
import android.os.AsyncTask
import com.gallery.editor.image.photoeditor.interfaces.ImageCollectionCallback
import com.gallery.editor.image.photoeditor.objects.ImageMedia
import java.io.File
import java.util.*

class CollectionImageLoader(context: Context, callback: ImageCollectionCallback) : AsyncTask<Void, Void, ArrayList<ImageMedia>>() {
    private var context: Context? = null
    private var callback: ImageCollectionCallback? = null

    init {
        this.context = context
        this.callback = callback
    }

    override fun doInBackground(vararg params: Void?): ArrayList<ImageMedia> {
        return getImages()
    }

    override fun onPostExecute(result: ArrayList<ImageMedia>?) {
        if (result != null) {
            callback?.onLoadedImage(result)
        } else {
            callback?.onLoadedFailImage()
        }
    }

    /**
     * load all video from folder editor
     */
    fun getImages(): ArrayList<ImageMedia> {
        var images = java.util.ArrayList<ImageMedia>()
        var folder = File(Constants.FOLDER)
        for (f in folder.listFiles()) {
            var pathImage = f.absolutePath
            var nameImage = f.name
            if (!f.endsWith(".gif") && !File(pathImage).isDirectory
                    && !nameImage.equals(".nomedia")) {
                images.add(ImageMedia(nameImage, pathImage))
            }
        }
        Collections.reverse(images)
        return images
    }
}