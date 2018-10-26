package com.gallery.editor.image.photoeditor.objects

class EditorObject(idResource: Int,
                   @TypeEditor type: Int) {
    var idResource: Int
    var idResourceClick: Int = 0
    lateinit var name: String
    var type: Int
    var status: Boolean

    init {
        this.idResource = idResource
        this.type = type
        this.status = false
    }

    constructor(idResource: Int,
                name: String,
                @TypeEditor type: Int) : this(idResource, type) {
        this.name = name
    }

    constructor(idResource: Int,
                idResourceClick: Int,
                name: String,
                @TypeEditor type: Int) : this(idResource, name, type) {
        this.idResourceClick = idResourceClick
    }
}