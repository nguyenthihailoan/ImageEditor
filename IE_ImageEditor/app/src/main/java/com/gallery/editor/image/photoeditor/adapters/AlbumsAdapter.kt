package com.media.converter.photogif.videogif.gifmaker.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.gallery.editor.image.photoeditor.R
import com.gallery.editor.image.photoeditor.interfaces.OnClickImageListener
import com.gallery.editor.image.photoeditor.objects.ImageMedia

class AlbumsAdapter(context: Context, onListener: OnClickImageListener)
    : RecyclerView.Adapter<AlbumsAdapter.ImageHolder>() {
    private var albums: ArrayList<ImageMedia>
    private var context: Context
    private var onListener: OnClickImageListener

    init {
        this.context = context
        this.onListener = onListener
        albums = ArrayList<ImageMedia>()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ImageHolder {
        return ImageHolder(LayoutInflater
                .from(context)
                .inflate(R.layout.item_album, p0, false))
    }

    override fun getItemCount(): Int {
        return albums.size
    }

    override fun onBindViewHolder(p0: ImageHolder, p1: Int) {
        p0.bindData(p1)
    }

    fun getImages(): List<ImageMedia> {
        return albums
    }

    fun getItemAlbum(position: Int): ImageMedia {
        return albums.get(position)
    }

    fun setListImage(images: ArrayList<ImageMedia>) {
        this.albums = images
        notifyDataSetChanged()
    }

    inner class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var iv_album: ImageView
        private var tv_name: TextView

        init {
            iv_album = itemView.findViewById(R.id.album)
            tv_name = itemView.findViewById(R.id.name_album)


        }

        fun bindData(position: Int) {
            Glide.with(context).load(albums.get(position).pathImage).into(iv_album)
            tv_name.setText(getItemAlbum(position).nameAlbum)
            iv_album.setOnClickListener(object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    onListener.onClickImage(position)
                }
            })
        }

    }
}