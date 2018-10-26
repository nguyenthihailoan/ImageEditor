package com.gallery.editor.image.photoeditor.screens.fragments

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gallery.editor.image.photoeditor.R
import com.gallery.editor.image.photoeditor.interfaces.OnClickEffectListener
import com.gallery.editor.image.photoeditor.interfaces.OnListenerEffectFragment
import com.gallery.editor.image.photoeditor.objects.TypeBlendEffect
import com.gallery.editor.image.photoeditor.objects.TypeEditor
import com.gallery.editor.image.photoeditor.utils.IniterData
import com.gallery.editor.image.photoeditor.utils.SpaceItemDecorationHorizontal
import com.media.converter.photogif.videogif.gifmaker.adapters.EffectAdapter
import com.media.converter.photogif.videogif.gifmaker.adapters.EffectParentAdapter
import kotlinx.android.synthetic.main.fragment_effect.*

class EffectFragment : Fragment(), View.OnClickListener {
    private var onListener: OnListenerEffectFragment? = null
    private lateinit var adapterEffect: EffectAdapter
    private lateinit var adapterEffectParent: EffectParentAdapter
    private var index = -1
    private var typeEffect = TypeEditor.PORTRAIT
    private var typeBlend = TypeBlendEffect.SCREEN
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_effect, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEffects()

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            cancel_effect.id -> {
                onListener?.onremoveFragment()
            }
            apply_effect.id -> {
                onListener?.saveImageWithBlende()
            }
            image_return.id -> {
                ln_filter_child.visibility = View.GONE
                recycler_effects_parent.visibility = View.VISIBLE
                if (ln_blend.visibility == View.VISIBLE) {
                    ln_blend.visibility = View.GONE
                }
                onListener?.removeEffect()
            }

        }
    }

    /**
     * set listener
     */
    fun setOnListener(onListener: OnListenerEffectFragment) {
        this.onListener = onListener
    }

    /**
     * init recycler item adjusts
     */
    fun initEffects() {
        initBlendEffect()
        cancel_effect.setOnClickListener(this)
        image_return.setOnClickListener(this)
        apply_effect.setOnClickListener(this)
        recycler_effects.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recycler_effects.addItemDecoration(SpaceItemDecorationHorizontal(resources.getDimensionPixelSize(R.dimen.dp_25)))
        recycler_effects_parent.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recycler_effects_parent.addItemDecoration(SpaceItemDecorationHorizontal(resources.getDimensionPixelSize(R.dimen.dp_25)))
        adapterEffect = EffectAdapter(context!!, object : OnClickEffectListener {
            override fun onClickEffect(pos: Int) {
                adapterEffect.changeStatus(index, pos)
                index = pos
                if (index == 0) {
                    onListener?.removeEffect()
                } else {
                    onListener?.onClickEffect(adapterEffect.getItemEffect(pos).pathFilter, typeEffect!!)
                }
            }
        })
        adapterEffectParent = EffectParentAdapter(context!!, object : OnClickEffectListener {
            override fun onClickEffect(pos: Int) {
                onListener?.onClickTypeEffect(adapterEffectParent.getItemEffect(pos).type)
                onClickEffectParent(adapterEffectParent.getItemEffect(pos).type)
            }
        })
        adapterEffectParent.setListEffect(IniterData.initEffects()!!)
        recycler_effects_parent.adapter = adapterEffectParent
        adapterEffect.setListEffect(IniterData.initProtrait()!!)
        recycler_effects.adapter = adapterEffect
    }

    /**
     * on click effect parent
     */
    fun onClickEffectParent(@TypeEditor type: Int) {
        typeEffect = type
        recycler_effects_parent.visibility = View.GONE
        ln_filter_child.visibility = View.VISIBLE
        when (type) {
            TypeEditor.PORTRAIT -> {
                adapterEffect.setListEffect(IniterData.initProtrait()!!)
            }
            TypeEditor.GALAXY -> {
                ln_blend.visibility = View.VISIBLE
                adapterEffect.setListEffect(IniterData.initGalaxy()!!)
            }
            TypeEditor.FOOD -> {
                adapterEffect.setListEffect(IniterData.initFood()!!)
            }
            TypeEditor.BROKEN -> {
                ln_blend.visibility = View.VISIBLE
                adapterEffect.setListEffect(IniterData.initBokeh()!!)
            }
            TypeEditor.LIGHT -> {
                ln_blend.visibility = View.VISIBLE
                adapterEffect.setListEffect(IniterData.initLight()!!)
            }
            TypeEditor.SNOW -> {
                ln_blend.visibility = View.VISIBLE
                adapterEffect.setListEffect(IniterData.initSnow()!!)
            }

        }
        adapterEffect.notifyDataSetChanged()

    }

    /**
     * init click blend
     */
    fun initBlendEffect() {
        text_normal.setOnClickListener(onClickBlend)
        text_divide.setOnClickListener(onClickBlend)
        text_multiply.setOnClickListener(onClickBlend)
        text_screen.setOnClickListener(onClickBlend)
        text_overlay.setOnClickListener(onClickBlend)
    }

    /**
     * onclick blend effect
     */
    private val onClickBlend = object : View.OnClickListener {
        override fun onClick(v: View?) {
            resetUITypeBlend()
            when (v?.id) {
                text_normal.id -> {
                    text_normal.setBackgroundColor(Color.parseColor("#5980ff"))
                    if (typeBlend == TypeBlendEffect.NORMAL) return
                    else {
                        typeBlend = TypeBlendEffect.NORMAL
                    }
                }
                text_divide.id -> {
                    text_divide.setBackgroundColor(Color.parseColor("#5980ff"))
                    if (typeBlend == TypeBlendEffect.DIVIDE) return
                    else {
                        typeBlend = TypeBlendEffect.DIVIDE
                    }
                }
                text_multiply.id -> {
                    text_multiply.setBackgroundColor(Color.parseColor("#5980ff"))
                    if (typeBlend == TypeBlendEffect.MULTIPLY) return
                    else {
                        typeBlend = TypeBlendEffect.MULTIPLY
                    }
                }
                text_screen.id -> {
                    text_screen.setBackgroundColor(Color.parseColor("#5980ff"))
                    if (typeBlend == TypeBlendEffect.SCREEN) return
                    else {
                        typeBlend = TypeBlendEffect.SCREEN
                    }
                }
                text_overlay.id -> {
                    text_overlay.setBackgroundColor(Color.parseColor("#5980ff"))
                    if (typeBlend == TypeBlendEffect.OVERLAY) return
                    else {
                        typeBlend = TypeBlendEffect.OVERLAY
                    }
                }
            }
            onListener?.onClickEffectType(typeBlend)
        }
    }

    /**
     * resetUI Blend Type
     */
    fun resetUITypeBlend() {
        text_divide.setBackgroundColor(Color.TRANSPARENT)
        text_multiply.setBackgroundColor(Color.TRANSPARENT)
        text_normal.setBackgroundColor(Color.TRANSPARENT)
        text_screen.setBackgroundColor(Color.TRANSPARENT)
        text_overlay.setBackgroundColor(Color.TRANSPARENT)
    }

    /**
     * apply adjust
     */
    fun actionApplyEffect() {

    }


    override fun onDestroyView() {
        super.onDestroyView()
        onListener?.onDestroyView()
    }

}
