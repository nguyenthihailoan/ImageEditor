package com.gallery.editor.image.photoeditor.objects

class EffectChildObject(pathPreview: String, name: String, pathFilter: String)
    : EffectParentObject(pathPreview, name) {
    var pathFilter: String
    var status: Boolean

    init {
        this.pathFilter = pathFilter
        this.status = false
    }
}