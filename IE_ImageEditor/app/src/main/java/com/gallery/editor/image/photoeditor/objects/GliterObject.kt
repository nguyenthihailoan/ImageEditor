package com.gallery.editor.image.photoeditor.objects

class GliterObject(path: String) {
    var path: String
    var status: Boolean

    init {
        this.path = path
        this.status = false
    }
}