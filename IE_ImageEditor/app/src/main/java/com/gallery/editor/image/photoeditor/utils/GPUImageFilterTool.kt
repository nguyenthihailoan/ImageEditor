package com.gallery.editor.image.photoeditor.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.PointF
import com.gallery.editor.image.photoeditor.objects.AdjustObject
import com.gallery.editor.image.photoeditor.objects.TypeEditor
import jp.co.cyberagent.android.gpuimage.*
import java.io.IOException
import java.io.InputStream

class GPUImageFilterTool {

    companion object {
        fun createCurveFilter(context: Context, path: String): GPUImageFilter? {
            val toneCurveFilter = GPUImageToneCurveFilter()
            var inputStream: InputStream? = null
            try {
                inputStream = context.assets.open(path)
                toneCurveFilter.setFromCurveFileInputStream(inputStream)
                return toneCurveFilter
            } catch (e: IOException) {
                e.printStackTrace()
                return null
            }
        }

        fun createGlFilterNormal(bitmap: Bitmap): GPUImageFilter? {
            val normal = GPUImageNormalBlendFilter()
            normal.bitmap = bitmap
            return normal
        }

        fun createGlFilterOverlay(bitmap: Bitmap): GPUImageFilter? {
            val overlay = GPUImageOverlayBlendFilter()
            overlay.bitmap = bitmap
            return overlay
        }

        fun createGlFilterDivide(bitmap: Bitmap): GPUImageFilter? {
            val divide = GPUImageDivideBlendFilter()
            divide.bitmap = bitmap
            return divide
        }

        fun createGlFilterMultiply(bitmap: Bitmap): GPUImageFilter? {
            val multiply = GPUImageMultiplyBlendFilter()
            multiply.bitmap = bitmap
            return multiply
        }

        fun createGlFilterScreen(bitmap: Bitmap): GPUImageFilter? {
            val screen = GPUImageScreenBlendFilter()
            screen.bitmap = bitmap
            return screen
        }
    }

    private var filters: ArrayList<GPUImageFilter>? = null
    var filterGroup: GPUImageFilterGroup? = null
    private var mapFilter: HashMap<Int, AdjustObject>? = null

    init {
        filters = ArrayList<GPUImageFilter>()
        filterGroup = GPUImageFilterGroup(filters)
        mapFilter = HashMap<Int, AdjustObject>()
        initFilterGroup(true)
    }

    fun getFilter(@TypeEditor type: Int): AdjustObject? {
        return mapFilter?.get(type)
    }

    fun checkAdjustExist(@TypeEditor type: Int): Boolean {
        return mapFilter?.contains(type)!!
    }

    fun addFilter(@TypeEditor type: Int): AdjustObject? {
        if (type == TypeEditor.SHADOW || type == TypeEditor.VINTAGE) {
            mapFilter?.put(type, creatFilter(type, 0))
        } else {
            mapFilter?.put(type, creatFilter(type, 50))
        }
        return mapFilter?.get(type)
    }

    fun initFilterGroup(boolean: Boolean) {
        mapFilter?.clear()
        filters?.clear()
        if (boolean) {
            addFilter(TypeEditor.EXPOSURE)
        }
        filterGroup = GPUImageFilterGroup(filters)
    }

    fun creatFilter(@TypeEditor type: Int, progress: Int): AdjustObject {
        var filter = createFilterForAdjust(type)
        var adjuster = FilterAdjuster(filter!!)
        if (type == TypeEditor.EXPOSURE) {
            adjuster.adjust(progress)
        }
        filterGroup?.addFilter(filter)
        return AdjustObject(progress, filter, adjuster)
    }

    fun createFilterForAdjust(@TypeEditor type: Int): GPUImageFilter? {
        when (type) {
            TypeEditor.CONTRAST -> {
                return GPUImageContrastFilter(1.0f)
            }
            TypeEditor.SATURATION -> {
                return GPUImageSaturationFilter(1.0f)
            }
            TypeEditor.EXPOSURE -> {
                return GPUImageExposureFilter(1.0f)
            }
            TypeEditor.TEMPERATURE -> {
                return GPUImageWhiteBalanceFilter()
            }
            TypeEditor.VINTAGE -> {
                val centerPoint = PointF()
                centerPoint.x = 0.5f
                centerPoint.y = 0.5f
                return GPUImageVignetteFilter(centerPoint, floatArrayOf(0.0f, 0.0f, 0.0f), 0.3f, 1f)

            }
            TypeEditor.GRAIN -> {
                return GPUImageSharpenFilter()
            }
            TypeEditor.SHADOW -> {
                return GPUImageHighlightShadowFilter(0.0f, 1.0f)
            }
            TypeEditor.COLORTINT -> {
                return GPUImageRGBFilter(1.0f, 1.0f, 1.0f)
            }
        }
        return null
    }



    class FilterAdjuster(filter: GPUImageFilter) {
        var adjuster: Adjuster<out GPUImageFilter>?

        init {
            if (filter is GPUImageSharpenFilter) {
                adjuster = SharpnessAdjuster().filter(filter)
            } else if (filter is GPUImageSepiaFilter) {
                adjuster = SepiaAdjuster().filter(filter)
            } else if (filter is GPUImageContrastFilter) {
                adjuster = ContrastAdjuster().filter(filter)
            } else if (filter is GPUImageGammaFilter) {
                adjuster = GammaAdjuster().filter(filter)
            } else if (filter is GPUImageBrightnessFilter) {
                adjuster = BrightnessAdjuster().filter(filter)
            } else if (filter is GPUImageSobelEdgeDetection) {
                adjuster = SobelAdjuster().filter(filter)
            } else if (filter is GPUImageEmbossFilter) {
                adjuster = EmbossAdjuster().filter(filter)
            } else if (filter is GPUImage3x3TextureSamplingFilter) {
                adjuster = GPU3x3TextureAdjuster().filter(filter)
            } else if (filter is GPUImageHueFilter) {
                adjuster = HueAdjuster().filter(filter)
            } else if (filter is GPUImagePosterizeFilter) {
                adjuster = PosterizeAdjuster().filter(filter)
            } else if (filter is GPUImagePixelationFilter) {
                adjuster = PixelationAdjuster().filter(filter)
            } else if (filter is GPUImageSaturationFilter) {
                adjuster = SaturationAdjuster().filter(filter)
            } else if (filter is GPUImageExposureFilter) {
                adjuster = ExposureAdjuster().filter(filter)
            } else if (filter is GPUImageHighlightShadowFilter) {
                adjuster = HighlightShadowAdjuster().filter(filter)
            } else if (filter is GPUImageMonochromeFilter) {
                adjuster = MonochromeAdjuster().filter(filter)
            } else if (filter is GPUImageOpacityFilter) {
                adjuster = OpacityAdjuster().filter(filter)
            } else if (filter is GPUImageRGBFilter) {
                adjuster = RGBAdjuster().filter(filter)
            } else if (filter is GPUImageWhiteBalanceFilter) {
                adjuster = WhiteBalanceAdjuster().filter(filter)
            } else if (filter is GPUImageVignetteFilter) {
                adjuster = VignetteAdjuster().filter(filter)
            } else if (filter is GPUImageDissolveBlendFilter) {
                adjuster = DissolveBlendAdjuster().filter(filter)
            } else if (filter is GPUImageGaussianBlurFilter) {
                adjuster = GaussianBlurAdjuster().filter(filter)
            } else if (filter is GPUImageCrosshatchFilter) {
                adjuster = CrosshatchBlurAdjuster().filter(filter)
            } else if (filter is GPUImageBulgeDistortionFilter) {
                adjuster = BulgeDistortionAdjuster().filter(filter)
            } else if (filter is GPUImageGlassSphereFilter) {
                adjuster = GlassSphereAdjuster().filter(filter)
            } else if (filter is GPUImageHazeFilter) {
                adjuster = HazeAdjuster().filter(filter)
            } else if (filter is GPUImageSphereRefractionFilter) {
                adjuster = SphereRefractionAdjuster().filter(filter)
            } else if (filter is GPUImageSwirlFilter) {
                adjuster = SwirlAdjuster().filter(filter)
            } else if (filter is GPUImageColorBalanceFilter) {
                adjuster = ColorBalanceAdjuster().filter(filter)
            } else if (filter is GPUImageLevelsFilter) {
                adjuster = LevelsMinMidAdjuster().filter(filter)
            } else if (filter is GPUImageBilateralFilter) {
                adjuster = BilateralAdjuster().filter(filter)
            } else {
                adjuster = null
            }
        }

        fun canAdjust(): Boolean {
            return adjuster != null
        }

        fun adjust(percentage: Int) {
            if (adjuster != null) {
                adjuster!!.adjust(percentage)
            }
        }

        abstract inner class Adjuster<T : GPUImageFilter> {
            var filter: T? = null
                private set

            fun filter(filter: GPUImageFilter): Adjuster<T> {
                this.filter = filter as T
                return this
            }

            abstract fun adjust(percentage: Int)

            protected fun range(percentage: Int, start: Float, end: Float): Float {
                return (end - start) * percentage / 100.0f + start
            }

            protected fun range(percentage: Int, start: Int, end: Int): Int {
                return (end - start) * percentage / 100 + start
            }
        }

        private inner class SharpnessAdjuster : Adjuster<GPUImageSharpenFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.setSharpness(range(percentage, -3.0f, 3.0f))
            }
        }

        private inner class PixelationAdjuster : Adjuster<GPUImagePixelationFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.setPixel(range(percentage, 1.0f, 100.0f))
            }
        }

        private inner class HueAdjuster : Adjuster<GPUImageHueFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.setHue(range(percentage, 0.0f, 360.0f))
            }
        }

        private inner class ContrastAdjuster : Adjuster<GPUImageContrastFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.setContrast(range(percentage, 0.0f, 2.0f))
            }
        }

        private inner class GammaAdjuster : Adjuster<GPUImageGammaFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.setGamma(range(percentage, 0.0f, 3.0f))
            }
        }

        private inner class BrightnessAdjuster : Adjuster<GPUImageBrightnessFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.setBrightness(range(percentage, -1.0f, 1.0f))
            }
        }

        private inner class SepiaAdjuster : Adjuster<GPUImageSepiaFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.setIntensity(range(percentage, 0.0f, 2.0f))
            }
        }

        private inner class SobelAdjuster : Adjuster<GPUImageSobelEdgeDetection>() {
            override fun adjust(percentage: Int) {
                filter!!.setLineSize(range(percentage, 0.0f, 5.0f))
            }
        }

        private inner class EmbossAdjuster : Adjuster<GPUImageEmbossFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.intensity = range(percentage, 0.0f, 4.0f)
            }
        }

        private inner class PosterizeAdjuster : Adjuster<GPUImagePosterizeFilter>() {
            override fun adjust(percentage: Int) {
                // In theorie to 256, but only first 50 are interesting
                filter!!.setColorLevels(range(percentage, 1, 50))
            }
        }

        private inner class GPU3x3TextureAdjuster : Adjuster<GPUImage3x3TextureSamplingFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.setLineSize(range(percentage, 0.0f, 5.0f))
            }
        }

        private inner class SaturationAdjuster : Adjuster<GPUImageSaturationFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.setSaturation(range(percentage, 0.0f, 2.0f))
            }
        }

        private inner class ExposureAdjuster : Adjuster<GPUImageExposureFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.setExposure(range(percentage, -1.0f, 1.0f))
            }
        }

        private inner class HighlightShadowAdjuster : Adjuster<GPUImageHighlightShadowFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.setShadows(range(percentage, 0.0f, 1.0f))
//                filter!!.setHighlights(range(percentage, 0.0f, 1.0f))
            }
        }

        private inner class MonochromeAdjuster : Adjuster<GPUImageMonochromeFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.setIntensity(range(percentage, 0.0f, 1.0f))
                //getFilter().setColor(new float[]{0.6f, 0.45f, 0.3f, 1.0f});
            }
        }

        private inner class OpacityAdjuster : Adjuster<GPUImageOpacityFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.setOpacity(range(percentage, 0.0f, 1.0f))
            }
        }

        private inner class RGBAdjuster : Adjuster<GPUImageRGBFilter>() {
            override fun adjust(percentage: Int) {
                if (percentage > 50) {
                    filter!!.setGreen(range((100 - percentage) * 2, 0.7f, 1.0f));
                    filter!!.setRed(1.0f)
                } else {
                    filter!!.setRed(range(percentage * 2, 0.7f, 1.0f))
                    filter!!.setGreen(1.0f)
                }
            }
        }

        private inner class WhiteBalanceAdjuster : Adjuster<GPUImageWhiteBalanceFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.setTemperature(range(percentage, 2000.0f, 8000.0f))
                //getFilter().setTint(range(percentage, -100.0f, 100.0f));
            }
        }

        private inner class VignetteAdjuster : Adjuster<GPUImageVignetteFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.setVignetteStart(range(percentage, 0.0f, 1.0f))
            }
        }

        private inner class DissolveBlendAdjuster : Adjuster<GPUImageDissolveBlendFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.setMix(range(percentage, 0.0f, 1.0f))
            }
        }

        private inner class GaussianBlurAdjuster : Adjuster<GPUImageGaussianBlurFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.setBlurSize(range(percentage, 0.0f, 1.0f))
            }
        }

        private inner class CrosshatchBlurAdjuster : Adjuster<GPUImageCrosshatchFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.setCrossHatchSpacing(range(percentage, 0.0f, 0.06f))
                filter!!.setLineWidth(range(percentage, 0.0f, 0.006f))
            }
        }

        private inner class BulgeDistortionAdjuster : Adjuster<GPUImageBulgeDistortionFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.setRadius(range(percentage, 0.0f, 1.0f))
                filter!!.setScale(range(percentage, -1.0f, 1.0f))
            }
        }

        private inner class GlassSphereAdjuster : Adjuster<GPUImageGlassSphereFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.setRadius(range(percentage, 0.0f, 1.0f))
            }
        }

        private inner class HazeAdjuster : Adjuster<GPUImageHazeFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.setDistance(range(percentage, -0.3f, 0.3f))
                filter!!.setSlope(range(percentage, -0.3f, 0.3f))
            }
        }

        private inner class SphereRefractionAdjuster : Adjuster<GPUImageSphereRefractionFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.setRadius(range(percentage, 0.0f, 1.0f))
            }
        }

        private inner class SwirlAdjuster : Adjuster<GPUImageSwirlFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.setAngle(range(percentage, 0.0f, 2.0f))
            }
        }

        private inner class ColorBalanceAdjuster : Adjuster<GPUImageColorBalanceFilter>() {

            override fun adjust(percentage: Int) {
                filter!!.setMidtones(floatArrayOf(range(percentage, 0.0f, 1.0f), range(percentage / 2, 0.0f, 1.0f), range(percentage / 3, 0.0f, 1.0f)))
            }
        }

        private inner class LevelsMinMidAdjuster : Adjuster<GPUImageLevelsFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.setMin(0.0f, range(percentage, 0.0f, 1.0f), 1.0f)
            }
        }

        private inner class BilateralAdjuster : Adjuster<GPUImageBilateralFilter>() {
            override fun adjust(percentage: Int) {
                filter!!.setDistanceNormalizationFactor(range(percentage, 0.0f, 15.0f))
            }
        }
    }
}