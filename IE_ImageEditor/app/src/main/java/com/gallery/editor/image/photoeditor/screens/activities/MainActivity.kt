package com.gallery.editor.image.photoeditor.screens.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.AnimationDrawable
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import com.gallery.editor.image.photoeditor.R
import com.gallery.editor.image.photoeditor.dialogs.RateAppDialog
import com.gallery.editor.image.photoeditor.utils.Utils
import com.gallery.editor.image.photoeditor.utils.navigations.ActivityNavigator
import com.gallery.editor.image.photoeditor.utils.sdk.Ads
import com.zer.android.newsdk.ZAndroidSDK
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val REQUEST_CODE_PERMISSION: Int = 100
    private val PERMISSION = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private lateinit var rateDialog: RateAppDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION or WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION or WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)
//        handleLoopAnimatedBackground()
        var animation = background.getDrawable() as AnimationDrawable
        animation.setEnterFadeDuration(3000)
        animation.setExitFadeDuration(1200)
        animation.start()
        checkPermission()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            image_editor.id, rl_editor.id, text_editor.id -> {
                var intent = Intent(this, ImagesActivity::class.java)
                ActivityNavigator(this).startActivity(intent)
            }
            image_collection.id, rl_collection.id, text_collection.id -> {
                var intent = Intent(this, CollectionActivity::class.java)
                ActivityNavigator(this).startActivity(intent)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initView()
                ZAndroidSDK.onPermissionGranted(this)
            } else {
                finish()
            }
        }
    }

    override fun onBackPressed() {
        rateDialog.show()
    }

    /**
     * init view
     */
    fun initView() {
        ZAndroidSDK.init(this@MainActivity)
        Ads.f(baseContext)
        Utils.creatFolder()
        image_editor.setOnClickListener(this)
        text_editor.setOnClickListener(this)
        rl_editor.setOnClickListener(this)
        image_collection.setOnClickListener(this)
        rl_collection.setOnClickListener(this)
        text_collection.setOnClickListener(this)
        rateDialog = RateAppDialog(this, object : RateAppDialog.OnClickRateApp {
            override fun onRateClick() {
                Utils.rateApp(this@MainActivity)
            }

            override fun onCancelClick() {
                finish()
            }

        })
    }

    /**
     * check permission
     */
    fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Utils.checkPermission(this, PERMISSION) == PackageManager.PERMISSION_GRANTED) {
                initView()
                ZAndroidSDK.onPermissionGranted(this)
            } else {
                requestPermissions(PERMISSION, REQUEST_CODE_PERMISSION)
            }
        } else {
            initView()
            ZAndroidSDK.onPermissionGranted(this)
        }
    }
}
