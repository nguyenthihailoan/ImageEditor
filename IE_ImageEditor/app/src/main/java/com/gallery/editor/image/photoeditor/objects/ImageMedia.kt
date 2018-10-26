package com.gallery.editor.image.photoeditor.objects

import android.os.Parcel
import android.os.Parcelable

class ImageMedia() : Parcelable {
    lateinit var idAlbum: String
    lateinit var nameAlbum: String
    lateinit var nameImage: String
    lateinit var pathImage: String
    var isChecked: Boolean = false

    constructor(parcel: Parcel) : this() {
        nameAlbum = parcel.readString()
        nameImage = parcel.readString()
        pathImage = parcel.readString()
        isChecked = parcel.readByte() != 0.toByte()
    }

    constructor(nameImage: String,
                pathImage: String) : this() {
        this.nameImage = nameImage
        this.pathImage = pathImage
    }

    constructor(idAlbum: String, nameAlbum: String, nameImage: String, pathImage: String) : this() {
        this.idAlbum = idAlbum
        this.nameAlbum = nameAlbum
        this.nameImage = nameImage
        this.pathImage = pathImage
        this.isChecked = false
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nameAlbum)
        parcel.writeString(nameImage)
        parcel.writeString(pathImage)
        parcel.writeByte(if (isChecked) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ImageMedia> {
        override fun createFromParcel(parcel: Parcel): ImageMedia {
            return ImageMedia(parcel)
        }

        override fun newArray(size: Int): Array<ImageMedia?> {
            return arrayOfNulls(size)
        }
    }

}