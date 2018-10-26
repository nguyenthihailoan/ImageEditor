package com.gallery.editor.image.photoeditor.objects

import com.gallery.editor.image.photoeditor.utils.GPUImageFilterTool
import jp.co.cyberagent.android.gpuimage.GPUImageFilter

class AdjustObject(progress:Int,filter:GPUImageFilter,adjust:GPUImageFilterTool.FilterAdjuster) {
    var progress: Int
    var adjust: GPUImageFilterTool.FilterAdjuster
    var filter: GPUImageFilter

    init {
        this.progress = progress
        this.filter = filter
        this.adjust=adjust
    }
}