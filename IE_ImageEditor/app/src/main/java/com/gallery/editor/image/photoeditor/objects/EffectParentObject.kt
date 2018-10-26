package com.gallery.editor.image.photoeditor.objects

open class EffectParentObject(pathPreview: String, name: String) {
    var pathPreview: String
    var name: String
    var type: Int = 0

    init {
        this.name = name
        this.pathPreview = pathPreview
    }

    constructor(pathPreview: String, name: String, type: Int) : this(pathPreview, name) {
        this.type = type
    }
}