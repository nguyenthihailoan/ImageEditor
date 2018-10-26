package com.gallery.editor.image.photoeditor.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gallery.editor.image.photoeditor.R
import com.gallery.editor.image.photoeditor.objects.ColorObject
import com.gallery.editor.image.photoeditor.utils.IniterData
import com.gallery.editor.image.photoeditor.widgets.textview.CircularColor

class ColorAdapter(context: Context, onListener: OnClickItemColor) : RecyclerView.Adapter<ColorAdapter.ColorHolder>() {
   val DURATION_ANI=200.toLong()
    var context: Context
    var colors: ArrayList<ColorObject>
    var onListener: OnClickItemColor
    var index: Int = 0

    init {
        this.context = context
        colors =IniterData.getListColor(context)
        this.onListener = onListener
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ColorHolder {
        return ColorHolder(LayoutInflater.from(context).inflate(R.layout.item_color, p0, false))
    }

    override fun getItemCount(): Int {
        return colors.size
    }

    override fun onBindViewHolder(holder: ColorHolder, p1: Int) {
        holder.bindData()
    }

    fun getItem(pos: Int): ColorObject {
        return colors.get(pos)
    }

    inner class ColorHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var colorCircleSmall: CircularColor
        var colorCircleLarge: CircularColor

        init {
            colorCircleSmall = itemView.findViewById(R.id.color_small)
            colorCircleLarge = itemView.findViewById(R.id.color_large)
        }

        fun bindData() {
            var color = colors.get(adapterPosition)
            colorCircleSmall.setBackgroundColor(color.color)
            colorCircleLarge.setBackgroundColor(color.color)
            if (color.isAnimationIn) {
                colorCircleLarge.animate()!!.translationY(-16f)
                        .setDuration(DURATION_ANI)
                colorCircleSmall.visibility = View.GONE
                colorCircleLarge.visibility = View.VISIBLE
            } else {
                colorCircleLarge.animate()!!.translationY(16f)
                        .setDuration(DURATION_ANI)
                colorCircleLarge.visibility = View.GONE
                colorCircleSmall.visibility = View.VISIBLE
            }
            colorCircleSmall.setOnClickListener(object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    colors.get(index).isAnimationIn = false
                    colors.get(adapterPosition).isAnimationIn = true
                    notifyItemChanged(adapterPosition)
                    notifyItemChanged(index)
                    index = adapterPosition
                    onListener.onClickColor(adapterPosition)
                }

            })
        }
    }

    interface OnClickItemColor {
        fun onClickColor(pos: Int)
    }
}