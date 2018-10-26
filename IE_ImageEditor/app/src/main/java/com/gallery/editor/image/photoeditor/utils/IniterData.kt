package com.gallery.editor.image.photoeditor.utils

import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import com.gallery.editor.image.photoeditor.R
import com.gallery.editor.image.photoeditor.objects.*


class IniterData {
    companion object {

        /**
         * init all item ss adjust editor: crop, rotate, contract, .....
         */
        fun initAdjust(res: Resources): ArrayList<EditorObject> {
            var adjusts = ArrayList<EditorObject>()
            adjusts.add(EditorObject(R.drawable.crop_selector, R.drawable.ic_crop_free_click, res.getString(R.string.crop), TypeEditor.CROP))
            adjusts.add(EditorObject(R.drawable.rotate_selector, R.drawable.ic_rotate_click, res.getString(R.string.rotate), TypeEditor.ROTATE))
            adjusts.add(EditorObject(R.drawable.exp_selector, R.drawable.ic_exposure_click, res.getString(R.string.exp), TypeEditor.EXPOSURE))
            adjusts.add(EditorObject(R.drawable.contr_selector, R.drawable.ic_contrast_click, res.getString(R.string.contr), TypeEditor.CONTRAST))
            adjusts.add(EditorObject(R.drawable.temper_selector, R.drawable.ic_temperature_click, res.getString(R.string.temp), TypeEditor.TEMPERATURE))
            adjusts.add(EditorObject(R.drawable.vintage_selector, R.drawable.ic_vintage_click, res.getString(R.string.vintage), TypeEditor.VINTAGE))
            adjusts.add(EditorObject(R.drawable.grain_selector, R.drawable.ic_grain_click, res.getString(R.string.grain), TypeEditor.GRAIN))
            adjusts.add(EditorObject(R.drawable.satur_selector, R.drawable.ic_saturation_click, res.getString(R.string.sat), TypeEditor.SATURATION))
            adjusts.add(EditorObject(R.drawable.shaw_selector, R.drawable.ic_shadowandhighlight_click, res.getString(R.string.shaw), TypeEditor.SHADOW))
            adjusts.add(EditorObject(R.drawable.colortint_selector, R.drawable.ic_adj_colortint_click, res.getString(R.string.colortint), TypeEditor.COLORTINT))
            return adjusts
        }

        /**
         * init item flip
         */
        fun initrotate(): ArrayList<EditorObject> {
            var rotates = ArrayList<EditorObject>()
            rotates.add(EditorObject(R.drawable.rotate_left_selector, TypeEditor.ROTATE_LEFT))
            rotates.add(EditorObject(R.drawable.rotate_right_selector, TypeEditor.ROTATE_RIGHT))
            rotates.add(EditorObject(R.drawable.flip_vertical_selector, TypeEditor.FLIP_VERTICAL))
            rotates.add(EditorObject(R.drawable.flip_horizontal_selector, TypeEditor.FLIP_HORIZONTAL))
            return rotates
        }

        /**
         * init item crop
         */
        fun initcrop(res: Resources): ArrayList<EditorObject> {
            var rotates = ArrayList<EditorObject>()
            rotates.add(EditorObject(R.drawable.crop_free_selector, R.drawable.ic_crop_free_click, res.getString(R.string.crop_free), TypeEditor.CROP_FREE))
            rotates.add(EditorObject(R.drawable.crop_circle_selector, R.drawable.ic_circle_click, res.getString(R.string.crop_circle), TypeEditor.CROP_CIRCLE))
            rotates.add(EditorObject(R.drawable.crop_11_selector, R.drawable.ic_11_click, res.getString(R.string.crop_11), TypeEditor.CROP_11))
            rotates.add(EditorObject(R.drawable.crop_34_selector, R.drawable.ic_34_click, res.getString(R.string.crop_34), TypeEditor.CROP_34))
            rotates.add(EditorObject(R.drawable.crop_43_selector, R.drawable.ic_43_click, res.getString(R.string.crop_43), TypeEditor.CROP_43))
            rotates.add(EditorObject(R.drawable.crop_169_selector, R.drawable.ic_169_click, res.getString(R.string.crop_169), TypeEditor.CROP_169))
            rotates.add(EditorObject(R.drawable.crop_916_selector, R.drawable.ic_916_click, res.getString(R.string.crop_916), TypeEditor.CROP_916))
            return rotates
        }

        /**
         * init all effect
         */
        fun initEffects(): ArrayList<EffectParentObject>? {
            var filter = ArrayList<EffectParentObject>()
            filter.add(EffectParentObject("effects/img_portrait.png", "Portrait", TypeEditor.PORTRAIT))
            filter.add(EffectParentObject("effects/img_food.png", "Food", TypeEditor.FOOD))
            filter.add(EffectParentObject("effects/img_bokeh.png", "Bokeh", TypeEditor.BROKEN))
            filter.add(EffectParentObject("effects/img_galaxy.png", "Galaxy", TypeEditor.GALAXY))
            filter.add(EffectParentObject("effects/img_light.png", "Light", TypeEditor.LIGHT))
            filter.add(EffectParentObject("effects/img_snow.png", "Snow", TypeEditor.SNOW))
            return filter
        }

        /**
         * init filter Protrait
         */
        fun initProtrait(): ArrayList<EffectChildObject>? {
            var filter = ArrayList<EffectChildObject>()
            filter.add(EffectChildObject("portrait/img_0_prv.png", "normal", "effects/0.acv"))
            filter.add(EffectChildObject("portrait/img_1_prv.png", "portrait 1", "portrait/portrait 1.acv"))
            filter.add(EffectChildObject("portrait/img_2_prv.png", "portrait 2", "portrait/portrait 2.acv"))
            filter.add(EffectChildObject("portrait/img_3_prv.png", "portrait 3", "portrait/portrait 3.acv"))
            filter.add(EffectChildObject("portrait/img_4_prv.png", "portrait 4", "portrait/portrait 4.acv"))
            filter.add(EffectChildObject("portrait/img_5_prv.png", "portrait 5", "portrait/portrait 5.acv"))
            filter.add(EffectChildObject("portrait/img_6_prv.png", "portrait 6", "portrait/portrait 6.acv"))
            filter.add(EffectChildObject("portrait/img_7_prv.png", "portrait 7", "portrait/portrait 7.acv"))
            filter.add(EffectChildObject("portrait/img_8_prv.png", "portrait 8", "portrait/portrait 8.acv"))
            filter.add(EffectChildObject("portrait/img_9_prv.png", "portrait 9", "portrait/portrait 9.acv"))
            filter.add(EffectChildObject("portrait/img_10_prv.png", "portrait 10", "portrait/portrait 10.acv"))
            return filter
        }

        /**
         * init filter Protrait
         */
        fun initFood(): ArrayList<EffectChildObject>? {
            var filter = ArrayList<EffectChildObject>()
            filter.add(EffectChildObject("food/img_0_prv.png", "normal", "effects/0.acv"))
            filter.add(EffectChildObject("food/img_1_prv.png", "food 1", "food/food 1.acv"))
            filter.add(EffectChildObject("food/img_2_prv.png", "food 2", "food/food 2.acv"))
            filter.add(EffectChildObject("food/img_3_prv.png", "food 3", "food/food 3.acv"))
            filter.add(EffectChildObject("food/img_4_prv.png", "food 4", "food/food 4.acv"))
            filter.add(EffectChildObject("food/img_5_prv.png", "food 5", "food/food 5.acv"))
            filter.add(EffectChildObject("food/img_6_prv.png", "food 6", "food/food 6.acv"))
            filter.add(EffectChildObject("food/img_7_prv.png", "food 7", "food/food 7.acv"))
            filter.add(EffectChildObject("food/img_8_prv.png", "food 8", "food/food 8.acv"))
            filter.add(EffectChildObject("food/img_9_prv.png", "food 9", "food/food 9.acv"))
            filter.add(EffectChildObject("food/img_10_prv.png", "food 10", "food/food 10.acv"))
            return filter
        }

        /**
         * init filter Bokeh
         */
        fun initBokeh(): ArrayList<EffectChildObject>? {
            var filter = ArrayList<EffectChildObject>()
            filter.add(EffectChildObject("bokeh/img_0_prv.png", "normal", "effects/0.acv"))
            filter.add(EffectChildObject("bokeh/img_1_prv.png", "bokeh 1", "bokeh/img_1.jpg"))
            filter.add(EffectChildObject("bokeh/img_2_prv.png", "bokeh 2", "bokeh/img_2.jpg"))
            filter.add(EffectChildObject("bokeh/img_3_prv.png", "bokeh 3", "bokeh/img_3.jpg"))
            filter.add(EffectChildObject("bokeh/img_4_prv.png", "bokeh 4", "bokeh/img_4.jpg"))
            filter.add(EffectChildObject("bokeh/img_5_prv.png", "bokeh 5", "bokeh/img_5.jpg"))
            filter.add(EffectChildObject("bokeh/img_6_prv.png", "bokeh 6", "bokeh/img_6.jpg"))
            filter.add(EffectChildObject("bokeh/img_7_prv.png", "bokeh 7", "bokeh/img_7.jpg"))
            filter.add(EffectChildObject("bokeh/img_8_prv.png", "bokeh 8", "bokeh/img_8.jpg"))
            filter.add(EffectChildObject("bokeh/img_9_prv.png", "bokeh 9", "bokeh/img_9.jpg"))
            filter.add(EffectChildObject("bokeh/img_10_prv.png", "bokeh 10", "bokeh/img_10.jpg"))
            return filter
        }

        /**
         * init filter Galaxy
         */
        fun initGalaxy(): ArrayList<EffectChildObject>? {
            var filter = ArrayList<EffectChildObject>()
            filter.add(EffectChildObject("galaxy/img_prv_0.png", "normal", "effects/0.acv"))
            filter.add(EffectChildObject("galaxy/img_1_prv.png", "galaxy 1", "galaxy/img_1.jpg"))
            filter.add(EffectChildObject("galaxy/img_2_prv.png", "galaxy 2", "galaxy/img_2.jpg"))
            filter.add(EffectChildObject("galaxy/img_3_prv.png", "galaxy 3", "galaxy/img_3.jpg"))
            filter.add(EffectChildObject("galaxy/img_4_prv.png", "galaxy 4", "galaxy/img_4.jpg"))
            filter.add(EffectChildObject("galaxy/img_5_prv.png", "galaxy 5", "galaxy/img_5.jpg"))
            filter.add(EffectChildObject("galaxy/img_6_prv.png", "galaxy 6", "galaxy/img_6.jpg"))
            filter.add(EffectChildObject("galaxy/img_7_prv.png", "galaxy 7", "galaxy/img_7.jpg"))
            filter.add(EffectChildObject("galaxy/img_8_prv.png", "galaxy 8", "galaxy/img_8.jpg"))
            filter.add(EffectChildObject("galaxy/img_9_prv.png", "galaxy 9", "galaxy/img_9.jpg"))
            filter.add(EffectChildObject("galaxy/img_10_prv.png", "galaxy 10", "galaxy/img_10.jpg"))
            return filter
        }

        /**
         * init filter Galaxy
         */
        fun initLight(): ArrayList<EffectChildObject>? {
            var filter = ArrayList<EffectChildObject>()
            filter.add(EffectChildObject("light/img_prv_0.png", "normal", "effects/0.acv"))
            filter.add(EffectChildObject("light/img_1_prv.png", "Light 1", "light/img_1.png"))
            filter.add(EffectChildObject("light/img_2_prv.png", "Light 2", "light/img_2.png"))
            filter.add(EffectChildObject("light/img_3_prv.png", "Light 3", "light/img_3.png"))
            filter.add(EffectChildObject("light/img_4_prv.png", "Light 4", "light/img_4.png"))
            filter.add(EffectChildObject("light/img_5_prv.png", "Light 5", "light/img_5.png"))
            filter.add(EffectChildObject("light/img_6_prv.png", "Light 6", "light/img_6.png"))
            filter.add(EffectChildObject("light/img_7_prv.png", "Light 7", "light/img_7.png"))
            filter.add(EffectChildObject("light/img_8_prv.png", "Light 8", "light/img_8.png"))
            filter.add(EffectChildObject("light/img_9_prv.png", "Light 9", "light/img_9.png"))
            filter.add(EffectChildObject("light/img_10_prv.png", "Light 10", "light/img_10.png"))
            return filter
        }

        /**
         * init filter Galaxy
         */
        fun initSnow(): ArrayList<EffectChildObject>? {
            var filter = ArrayList<EffectChildObject>()
            filter.add(EffectChildObject("snow/img_prv_0.png", "normal", "effects/0.acv"))
            filter.add(EffectChildObject("snow/img_1_prv.png", "Snow 1", "snow/img_1.png"))
            filter.add(EffectChildObject("snow/img_2_prv.png", "Snow 2", "snow/img_2.png"))
            filter.add(EffectChildObject("snow/img_3_prv.png", "Snow 3", "snow/img_3.png"))
            filter.add(EffectChildObject("snow/img_4_prv.png", "Snow 4", "snow/img_4.png"))
            filter.add(EffectChildObject("snow/img_5_prv.png", "Snow 5", "snow/img_5.png"))
            filter.add(EffectChildObject("snow/img_6_prv.png", "Snow 6", "snow/img_6.png"))
            filter.add(EffectChildObject("snow/img_7_prv.png", "Snow 7", "snow/img_7.png"))
            filter.add(EffectChildObject("snow/img_8_prv.png", "Snow 8", "snow/img_8.png"))
            filter.add(EffectChildObject("snow/img_9_prv.png", "Snow 9", "snow/img_9.png"))
            filter.add(EffectChildObject("snow/img_10_prv.png", "Snow 10", "snow/img_10.png"))
            return filter
        }

        /**
         * init list gliter into brush
         */
        fun initGliter(asset: AssetManager): ArrayList<GliterObject> {
            var arrGliters = java.util.ArrayList<GliterObject>()
            var list = asset.list("gliter")
            for (i in list) {
                arrGliters.add(GliterObject("gliter/${i}"))
            }
            arrGliters.get(0).status = true
            return arrGliters
        }

        /**
         * get list color
         */
        fun getListColor(context: Context): ArrayList<ColorObject> {
            var color = ArrayList<ColorObject>()
            val arrColor = context.resources.obtainTypedArray(R.array.listcolor)
            for (i in 0..arrColor.length() - 1) {
                color.add(ColorObject(arrColor.getColor(i, 0)))
            }
            color.get(0).isAnimationIn = false
            color.get(0).isAnimationOut = true
            return color
        }
    }
}