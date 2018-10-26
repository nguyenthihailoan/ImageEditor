package com.gallery.editor.image.photoeditor.screens.fragments

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gallery.editor.image.photoeditor.R
import com.gallery.editor.image.photoeditor.adapters.ColorAdapter
import com.gallery.editor.image.photoeditor.interfaces.OnClickImageListener
import com.gallery.editor.image.photoeditor.interfaces.OnListenerBrushFragment
import com.gallery.editor.image.photoeditor.objects.TypeEditor
import com.gallery.editor.image.photoeditor.utils.IniterData
import com.gallery.editor.image.photoeditor.utils.SpaceItemDecorationHorizontal
import com.media.converter.photogif.videogif.gifmaker.adapters.GliterAdapter
import kotlinx.android.synthetic.main.fragment_brush.*

class BrushFragment : Fragment(), View.OnClickListener {
    private var onListener: OnListenerBrushFragment? = null
    private lateinit var colorAdapter: ColorAdapter
    private lateinit var gliterAdapter: GliterAdapter
    private var index = 0
    private var color = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_brush, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            cancel_brush.id -> {
                onListener?.onremoveFragment()
            }
            apply_brush.id -> {
                onListener?.onSaveWithDrawLine()
            }

        }
    }

    fun setOnListenerBrush(onListener: OnListenerBrushFragment) {
        this.onListener = onListener
    }

    /**
     * get current color
     */
    fun getCurrentColor(): Int {
        return color
    }

    /**
     * initView
     */
    fun initView() {
        cancel_brush.setOnClickListener(this)
        apply_brush.setOnClickListener(this)
        color = Color.parseColor("#ffffff")
        image_brush_other.setOnClickListener(onClickbrush)
        image_brush_line.setOnClickListener(onClickbrush)
        initList()
    }

    /**
     * init recycler color, gliter
     */
    fun initList() {
        recycler_brush.addItemDecoration(SpaceItemDecorationHorizontal(
                context!!.resources.getDimensionPixelSize(R.dimen.dp_1)))
        recycler_brush.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        colorAdapter = ColorAdapter(context!!, object : ColorAdapter.OnClickItemColor {
            override fun onClickColor(pos: Int) {
                color = colorAdapter.getItem(pos).color
                onListener?.changeColorBrush(color)

            }

        })
        gliterAdapter = GliterAdapter(context!!, object : OnClickImageListener {
            override fun onClickImage(pos: Int) {
                if (index == pos) return
                gliterAdapter.changeStatus(index, pos)
                index=pos
                onListener?.changeGliter(gliterAdapter.getItemGliter(pos).path)

            }
        })
        gliterAdapter.setListGliter(IniterData.initGliter(context!!.assets))
        recycler_brush.adapter = colorAdapter
        colorAdapter.notifyDataSetChanged()
    }


    private val onClickbrush = object : View.OnClickListener {
        override fun onClick(v: View?) {
            resetUIBrush()
            when (v?.id) {
                image_brush_line.id -> {
                    onListener?.changeStatusBrush(TypeEditor.DRAWLINE)
                    recycler_brush.adapter = colorAdapter
                    colorAdapter.notifyDataSetChanged()
                    image_brush_line.setImageResource(R.drawable.ic_brush_line_click)
                }
                image_brush_other.id -> {
                    onListener?.changeStatusBrush(TypeEditor.GLITER)
                    recycler_brush.adapter = gliterAdapter
                    gliterAdapter.notifyDataSetChanged()
                    image_brush_other.setImageResource(R.drawable.ic_brush_other_click)
                }
            }
        }
    }

    fun resetUIBrush() {
        image_brush_line.setImageResource(R.drawable.brush_line_selector)
        image_brush_other.setImageResource(R.drawable.brush_other_selector)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onListener?.onDestroyView()
    }
}
