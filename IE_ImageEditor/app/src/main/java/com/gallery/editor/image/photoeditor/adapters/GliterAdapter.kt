package com.media.converter.photogif.videogif.gifmaker.adapters

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.gallery.editor.image.photoeditor.R
import com.gallery.editor.image.photoeditor.interfaces.OnClickImageListener
import com.gallery.editor.image.photoeditor.objects.GliterObject

class GliterAdapter(context: Context, onListener: OnClickImageListener)
    : RecyclerView.Adapter<GliterAdapter.ImageHolder>() {
    private var gliters: ArrayList<GliterObject>
    private var context: Context
    private var onListener: OnClickImageListener
    private var isGliter = false

    init {
        this.context = context
        this.onListener = onListener
        gliters = ArrayList<GliterObject>()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ImageHolder {
        return ImageHolder(LayoutInflater
                .from(context)
                .inflate(R.layout.item_brush, p0, false))
    }

    override fun getItemCount(): Int {
        return gliters.size
    }

    override fun onBindViewHolder(p0: ImageHolder, p1: Int) {
        p0.bindData(p1)
    }


    /**
     * change status clickk item
     */
    fun changeStatus(before: Int, current: Int) {
        if (before != -1) {
            gliters.get(before).status = false
        }
        gliters.get(current).status = true
        notifyDataSetChanged()
    }

    fun getGliters(): ArrayList<GliterObject> {
        return gliters
    }

    fun getItemGliter(position: Int): GliterObject {
        return gliters.get(position)
    }

    fun setListGliter(gliters: ArrayList<GliterObject>) {
        this.gliters = gliters
        notifyDataSetChanged()
    }

    inner class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var iv_gliter: ImageView
        private var item_gliter: ConstraintLayout
        private var click_gliter: ImageView

        init {
            iv_gliter = itemView.findViewById(R.id.image_brush)
            item_gliter = itemView.findViewById(R.id.item_brush)
            click_gliter = itemView.findViewById(R.id.brush_click)
        }

        fun bindData(position: Int) {
            var image = getItemGliter(position)
            Glide.with(context).load("file:///android_asset/${image.path}").into(iv_gliter)
            if (image.status) {
                click_gliter.visibility = View.VISIBLE
            } else {
                click_gliter.visibility = View.GONE
            }
            item_gliter.setOnClickListener(object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    onListener.onClickImage(position)
                }
            })
        }

    }
}