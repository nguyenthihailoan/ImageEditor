package com.gallery.editor.image.photoeditor.screens.fragments

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.gallery.editor.image.photoeditor.R
import com.gallery.editor.image.photoeditor.adapters.ColorAdapter
import com.gallery.editor.image.photoeditor.interfaces.OnListenerTimestampFragment
import com.gallery.editor.image.photoeditor.utils.SpaceItemDecorationHorizontal
import kotlinx.android.synthetic.main.fragment_timestamp.*

class TimeStampFragment : Fragment(), View.OnClickListener {
    val FONT1 = "timestamp/digital-7_0.ttf"
    val FONT2 = "timestamp/28 Days Later.ttf"
    val FONT3 = "timestamp/UTM Soraya.ttf"
    val FONT4 = "timestamp/Roboto-Condensed.ttf"
    val FONT5 = "timestamp/VNF-Intro Inline.ttf"
    private var onListener: OnListenerTimestampFragment? = null
    private lateinit var colorAdapter: ColorAdapter
    private var index = -1
    private var color = 0
    private var font: String? = null
    private var typeface: Typeface? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timestamp, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cancel_timestamp.setOnClickListener(this)
        apply_timestamp.setOnClickListener(this)
        color = Color.parseColor("#ffffff")
        typeface = Typeface.createFromAsset(context?.assets, FONT1)
        initListColor()
        initListFont()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            cancel_timestamp.id -> {
                onListener?.onremoveFragment()
            }
            apply_timestamp.id -> {
                onListener?.saveTextWithBitmap(color, typeface!!)
            }

        }
    }

    fun setOnListenerTimeStamp(onListener: OnListenerTimestampFragment) {
        this.onListener = onListener
    }

    /**
     * init recycler color
     */
    fun initListColor() {
        recycler_color.addItemDecoration(SpaceItemDecorationHorizontal(
                context!!.resources.getDimensionPixelSize(R.dimen.dp_1)))
        recycler_color.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        colorAdapter = ColorAdapter(context!!, object : ColorAdapter.OnClickItemColor {
            override fun onClickColor(pos: Int) {
                color = colorAdapter.getItem(pos).color
                onListener?.onClickColor(colorAdapter.getItem(pos).color)
            }

        })
        recycler_color.adapter = colorAdapter
        colorAdapter.notifyDataSetChanged()
    }

    /**
     * init font style
     */
    fun initListFont() {
        setTypeFont(text_timestamp1, FONT1)
        setTypeFont(text_timestamp2, FONT2)
        setTypeFont(text_timestamp3, FONT3)
        setTypeFont(text_timestamp4, FONT4)
        setTypeFont(text_timestamp5, FONT5)
        text_timestamp1.setOnClickListener(onClickFont)
        text_timestamp2.setOnClickListener(onClickFont)
        text_timestamp3.setOnClickListener(onClickFont)
        text_timestamp4.setOnClickListener(onClickFont)
        text_timestamp5.setOnClickListener(onClickFont)
    }

    /**
     * set typefont
     */
    fun setTypeFont(v: TextView, font: String) {
        var type = Typeface.createFromAsset(context?.assets, font)
        v.setTypeface(type)
    }

    private val onClickFont = object : View.OnClickListener {
        override fun onClick(v: View?) {
            var currentfont = ""
            resetUIText()
            when (v?.id) {
                text_timestamp1.id -> {
                    typeface = Typeface.createFromAsset(context?.assets, FONT1)
                    currentfont = FONT1
                    text_timestamp1.setBackgroundColor(Color.parseColor("#5980ff"))
                }
                text_timestamp2.id -> {
                    typeface = Typeface.createFromAsset(context?.assets, FONT2)
                    text_timestamp2.setBackgroundColor(Color.parseColor("#5980ff"))
                    currentfont = FONT2

                }
                text_timestamp3.id -> {
                    typeface = Typeface.createFromAsset(context?.assets, FONT3)
                    text_timestamp3.setBackgroundColor(Color.parseColor("#5980ff"))
                    currentfont = FONT3
                }
                text_timestamp4.id -> {
                    typeface = Typeface.createFromAsset(context?.assets, FONT4)
                    text_timestamp4.setBackgroundColor(Color.parseColor("#5980ff"))
                    currentfont = FONT4

                }
                text_timestamp5.id -> {
                    typeface = Typeface.createFromAsset(context?.assets, FONT5)
                    text_timestamp5.setBackgroundColor(Color.parseColor("#5980ff"))
                    currentfont = FONT5

                }
            }
            if (font.equals(currentfont)) {
                return
            } else {
                font=currentfont
                onListener?.onClickFont(font!!)
            }
        }
    }

    fun resetUIText() {
        text_timestamp1.setBackgroundColor(Color.TRANSPARENT)
        text_timestamp2.setBackgroundColor(Color.TRANSPARENT)
        text_timestamp3.setBackgroundColor(Color.TRANSPARENT)
        text_timestamp4.setBackgroundColor(Color.TRANSPARENT)
        text_timestamp5.setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onListener?.onDestroyView()
    }

}
