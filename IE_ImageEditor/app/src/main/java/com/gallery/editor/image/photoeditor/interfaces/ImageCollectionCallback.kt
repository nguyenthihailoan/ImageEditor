package com.gallery.editor.image.photoeditor.interfaces

import com.gallery.editor.image.photoeditor.objects.ImageMedia

interface ImageCollectionCallback {

    fun onLoadedImage(images:ArrayList<ImageMedia>)

    fun onLoadedFailImage()

}