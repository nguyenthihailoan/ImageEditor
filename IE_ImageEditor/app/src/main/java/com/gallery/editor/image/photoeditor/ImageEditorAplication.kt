package com.gallery.editor.image.photoeditor

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import com.zer.android.newsdk.ZAndroidSDK

class ImageEditorAplication : Application() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        ZAndroidSDK.initApplication(this, applicationContext.packageName)
    }
}