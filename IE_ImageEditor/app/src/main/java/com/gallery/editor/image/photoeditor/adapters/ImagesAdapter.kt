package com.media.converter.photogif.videogif.gifmaker.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.gallery.editor.image.photoeditor.R
import com.gallery.editor.image.photoeditor.interfaces.OnClickImageListener
import com.gallery.editor.image.photoeditor.objects.ImageMedia

class ImagesAdapter(context: Context, onListener: OnClickImageListener)
    : RecyclerView.Adapter<ImagesAdapter.ImageHolder>() {
    private var images: ArrayList<ImageMedia>
    private var context: Context
    private var onListener: OnClickImageListener
    private var isImageEditor = false

    init {
        this.context = context
        this.onListener = onListener
        images = ArrayList<ImageMedia>()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ImageHolder {
        return ImageHolder(LayoutInflater
                .from(context)
                .inflate(R.layout.item_image, p0, false))
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(p0: ImageHolder, p1: Int) {
        p0.bindData(p1)
    }

    fun setImageEditor(check: Boolean) {
        this.isImageEditor = check
    }

    fun getImages(): ArrayList<ImageMedia> {
        return images
    }

    fun getItemImage(position: Int): ImageMedia {
        return images.get(position)
    }

    fun setListImage(images: ArrayList<ImageMedia>) {
        this.images = images
        notifyDataSetChanged()
    }

    inner class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var iv_image: ImageView

        init {
            iv_image = itemView.findViewById(R.id.image)
        }

        fun bindData(position: Int) {
            var image = getItemImage(position)
            Glide.with(context).load(image.pathImage).into(iv_image)
            iv_image.setOnClickListener(object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    onListener.onClickImage(position)
                }
            })
        }

    }
}