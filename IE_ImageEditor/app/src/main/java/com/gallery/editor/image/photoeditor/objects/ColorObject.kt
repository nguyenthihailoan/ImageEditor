package com.gallery.editor.image.photoeditor.objects

class ColorObject(color: Int) {
    var id:Int = 0
    var color: Int
    var isAnimationIn: Boolean
    var isAnimationOut: Boolean

    init {
        this.color = color
        this.isAnimationIn = false
        this.isAnimationOut = false
    }

}