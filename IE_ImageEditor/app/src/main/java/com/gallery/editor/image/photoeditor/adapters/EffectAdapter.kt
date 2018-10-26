package com.media.converter.photogif.videogif.gifmaker.adapters

import android.content.Context
import android.graphics.Color
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.gallery.editor.image.photoeditor.R
import com.gallery.editor.image.photoeditor.interfaces.OnClickEffectListener
import com.gallery.editor.image.photoeditor.objects.EffectChildObject

class EffectAdapter(context: Context, onListener: OnClickEffectListener)
    : RecyclerView.Adapter<EffectAdapter.EffectHolder>() {
    private var effectChildren: ArrayList<EffectChildObject>
    private var context: Context
    private var onListener: OnClickEffectListener

    init {
        this.context = context
        this.onListener = onListener
        effectChildren = ArrayList<EffectChildObject>()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): EffectHolder {
        return EffectHolder(LayoutInflater
                .from(context)
                .inflate(R.layout.item_effect, p0, false))
    }

    override fun getItemCount(): Int {
        return effectChildren.size
    }

    override fun onBindViewHolder(p0: EffectAdapter.EffectHolder, p1: Int) {
        p0.bindData()
    }

    fun getItemEffect(position: Int): EffectChildObject {
        return effectChildren.get(position)
    }

    fun setListEffect(effectChildren: ArrayList<EffectChildObject>) {
        this.effectChildren = effectChildren
        notifyDataSetChanged()
    }

    /**
     * change status clickk item
     */
    fun changeStatus(before: Int, current: Int) {
        if (before != -1) {
            effectChildren.get(before).status = false
        }
        effectChildren.get(current).status = true
        notifyDataSetChanged()
    }

    inner class EffectHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var image_effect: ImageView
        private var image_click: ImageView
        private var text_effect: TextView
        private var item_effect: ConstraintLayout

        init {
            image_effect = itemView.findViewById(R.id.image_effect)
            image_click = itemView.findViewById(R.id.image_click)
            text_effect = itemView.findViewById(R.id.text_effect)
            item_effect = itemView.findViewById(R.id.item_effect)
        }

        fun bindData() {
            var effect = getItemEffect(adapterPosition)
            text_effect.setText(effect.name)
            Glide.with(context).load("file:///android_asset/${effect.pathPreview}").into(image_effect)
            if (effect.status) {
                image_click.visibility = View.VISIBLE
                text_effect.setTextColor(Color.parseColor("#5980ff"))
            } else {
                image_click.visibility = View.GONE
                text_effect.setTextColor(Color.WHITE)
            }
            image_effect.setOnClickListener(onClick)
        }

        val onClick = object : View.OnClickListener {
            override fun onClick(v: View?) {
                onListener.onClickEffect(adapterPosition)
            }
        }
    }
}