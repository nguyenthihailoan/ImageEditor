package com.gallery.editor.image.photoeditor.interfaces

import com.gallery.editor.image.photoeditor.objects.ImageMedia

interface ImageCallback {
    fun onClickAlbum(idAlbum: String,nameAlbum:String)

    fun onClickImage(image: ImageMedia)

    fun onUpdateNameAlbum()

    fun onLoadedImage(albums:HashMap<String,ArrayList<ImageMedia>>)
    fun onLoadedFailImage()

}