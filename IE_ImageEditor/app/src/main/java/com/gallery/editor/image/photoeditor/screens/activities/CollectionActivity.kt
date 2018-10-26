package com.gallery.editor.image.photoeditor.screens.activities

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.view.WindowManager
import android.widget.RelativeLayout
import com.gallery.editor.image.photoeditor.BuildConfig
import com.gallery.editor.image.photoeditor.R
import com.gallery.editor.image.photoeditor.dialogs.ImageDialog
import com.gallery.editor.image.photoeditor.interfaces.ImageCollectionCallback
import com.gallery.editor.image.photoeditor.interfaces.OnClickImageListener
import com.gallery.editor.image.photoeditor.objects.ImageMedia
import com.gallery.editor.image.photoeditor.utils.CollectionImageLoader
import com.gallery.editor.image.photoeditor.utils.Constants
import com.gallery.editor.image.photoeditor.utils.SpaceItemDecorationGrid
import com.gallery.editor.image.photoeditor.utils.navigations.ActivityNavigator
import com.gallery.editor.image.photoeditor.utils.sdk.Ads
import com.media.converter.photogif.videogif.gifmaker.adapters.ImagesAdapter
import kotlinx.android.synthetic.main.activity_collection.*
import java.io.File



class CollectionActivity : AppCompatActivity(), ImageCollectionCallback {


    private lateinit var imageAdapter: ImagesAdapter
    private lateinit var dialogMoreImage: ImageDialog
    private lateinit var imageCurrent: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION or WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION or WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_collection)
        initAds()
        initDialog()
        initListImage()
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

    override fun onLoadedImage(images: ArrayList<ImageMedia>) {
        imageAdapter.setListImage(images)

    }

    override fun onLoadedFailImage() {
    }

    /**
     * init ads
     */
    fun initAds() {
        val rlads = findViewById(R.id.rl_ads) as RelativeLayout
        Ads.b(this, rlads, object : Ads.OnAdsListener {
            override fun onError() {
                rlads.visibility = View.GONE
            }

            override fun onAdLoaded() {
                rlads.visibility = View.VISIBLE

            }

            override fun onAdOpened() {
                rlads.visibility = View.VISIBLE

            }
        })
    }

    /**
     * init dialog
     */
    fun initDialog() {
        dialogMoreImage = ImageDialog(this, object : ImageDialog.OnClickEvent {
            override fun onClickView() {
                viewImage()
            }

            override fun onClickEdit() {
                editImage()
            }

            override fun onClickShare() {
                shareImage()
            }

        })
    }

    /**
     * init recycle
     */
    fun initListImage() {
        image_back.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                ActivityNavigator(this@CollectionActivity).finishActivity()
            }
        })
        recycler_collection.layoutManager = GridLayoutManager(this, 3)
        var space = SpaceItemDecorationGrid(resources.getDimensionPixelSize(R.dimen.dp_12),
                resources.getDimensionPixelSize(R.dimen.dp_12))
        recycler_collection.addItemDecoration(space)
        imageAdapter = ImagesAdapter(baseContext, object : OnClickImageListener {
            override fun onClickImage(pos: Int) {
                imageCurrent = imageAdapter.getItemImage(pos).pathImage
                dialogMoreImage.show()
            }
        })
        recycler_collection.adapter = imageAdapter
        CollectionImageLoader(baseContext, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)

    }

    /**
     * init view image
     */
    fun viewImage() {
        val intent = Intent()
        val f = File(imageCurrent)
        intent.action = Intent.ACTION_VIEW
        val photoURI = FileProvider.getUriForFile(this,
                BuildConfig.APPLICATION_ID + ".fileprovider", File(f.getAbsolutePath()))
        intent.setDataAndType(photoURI, "image/*")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(intent)
    }

    /**
     * init view image
     */
    fun editImage() {
        var intent = Intent(this, ImageEditorActivity::class.java)
        intent.putExtra(Constants.EXTRA_IMAGE, imageCurrent)
        ActivityNavigator(this).startActivity(intent)
        finish()
    }

    /**
     * init view image
     */
    fun shareImage() {
        val f = File(imageCurrent)
        val uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", File(f.getAbsolutePath()))
        val share = Intent(Intent.ACTION_SEND)
        share.putExtra(Intent.EXTRA_STREAM, uri)
        share.type = "image/*"
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(Intent.createChooser(share, "Share image File"))
    }
}
