package com.media.converter.photogif.videogif.gifmaker.adapters

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.gallery.editor.image.photoeditor.R
import com.gallery.editor.image.photoeditor.interfaces.OnClickEditorListener
import com.gallery.editor.image.photoeditor.objects.EditorObject

class EditorAdapter(context: Context, onListener: OnClickEditorListener)
    : RecyclerView.Adapter<EditorAdapter.EditorHolder>() {
    private var editors: ArrayList<EditorObject>
    private var context: Context
    private var onListener: OnClickEditorListener
    private var isShowText: Boolean = true

    init {
        this.context = context
        this.onListener = onListener
        editors = ArrayList<EditorObject>()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): EditorHolder {
        return EditorHolder(LayoutInflater
                .from(context)
                .inflate(R.layout.item_editor, p0, false))
    }

    override fun getItemCount(): Int {
        return editors.size
    }

    override fun onBindViewHolder(p0: EditorHolder, p1: Int) {
        p0.bindData()
    }

    fun getItemEditor(position: Int): EditorObject {
        return editors.get(position)
    }

    fun setListEditor(editors: ArrayList<EditorObject>) {
        this.editors = editors
        notifyDataSetChanged()
    }

    /**
     * change value show text
     */
    fun changeShowText(value: Boolean) {
        this.isShowText = value
    }

    /**
     * change status clickk item
     */
    fun changeStatus(before: Int, current: Int) {
        if (before != -1) {
            editors.get(before).status = false
        }
        editors.get(current).status = true
        notifyDataSetChanged()
    }

    inner class EditorHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var image_editor: ImageView
        private var text_editor: TextView
        private var item_editor: LinearLayout

        init {
            image_editor = itemView.findViewById(R.id.image_editor)
            text_editor = itemView.findViewById(R.id.text_editor)
            item_editor = itemView.findViewById(R.id.item_editor)
        }

        fun bindData() {
            var editor = getItemEditor(adapterPosition)
            if (isShowText) {
                text_editor.visibility = View.VISIBLE
                text_editor.setText(editor.name)
                text_editor.setOnClickListener(onClick)
            } else {
                text_editor.visibility = View.INVISIBLE
            }
            if (editor.status) {
                text_editor.setTextColor(Color.parseColor("#5980ff"))
                image_editor.setImageResource(editor.idResourceClick)
            } else {
                image_editor.setImageResource(editor.idResource)
                text_editor.setTextColor(Color.WHITE)
            }
            item_editor.setOnClickListener(onClick)
            image_editor.setOnClickListener(onClick)
        }

        val onClick = object : View.OnClickListener {
            override fun onClick(v: View?) {
                onListener.onClickEditor(adapterPosition)
            }
        }
    }
}