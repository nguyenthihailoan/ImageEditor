package com.gallery.editor.image.photoeditor.screens.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import android.widget.RelativeLayout
import android.widget.Toast
import com.gallery.editor.image.photoeditor.BuildConfig
import com.gallery.editor.image.photoeditor.R
import com.gallery.editor.image.photoeditor.dialogs.ProgressDialog
import com.gallery.editor.image.photoeditor.interfaces.ImageCallback
import com.gallery.editor.image.photoeditor.objects.ImageMedia
import com.gallery.editor.image.photoeditor.utils.AlbumImageLoader
import com.gallery.editor.image.photoeditor.utils.Constants
import com.gallery.editor.image.photoeditor.utils.Utils
import com.gallery.editor.image.photoeditor.utils.navigations.ActivityNavigator
import com.gallery.editor.image.photoeditor.utils.navigations.FragmentNavigator
import com.gallery.editor.image.photoeditor.utils.sdk.Ads
import com.media.converter.photogif.videogif.gifmaker.screens.fragments.AlbumFragment
import com.media.converter.photogif.videogif.gifmaker.screens.fragments.ImageFragment
import kotlinx.android.synthetic.main.activity_images.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ImagesActivity : AppCompatActivity(), View.OnClickListener {
    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_CAMERA = 2
    val RESULT_OK = -1
    private val REQUEST_CODE_PERMISSION: Int = 100
    private val PERMISSION = arrayOf(Manifest.permission.CAMERA)
    private lateinit var progressDialog: ProgressDialog
    private var imageFragment: ImageFragment? = null
    private var albumFragment: AlbumFragment? = null
    private var mapImages: HashMap<String, ArrayList<ImageMedia>>? = null
    private var uriCapture: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION or WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION or WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_images)
        initView()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when (requestCode) {
            REQUEST_IMAGE_CAPTURE -> {
                if (resultCode == RESULT_OK) {
//                    Constants.bitmap = data?.extras?.get("data") as Bitmap
//            progressDialog.show()
//            SaveBitmapToLocal(saveBitmapAction).execute(bitmap)
                    var intent = Intent(this@ImagesActivity, ImageEditorActivity::class.java)
                    intent.putExtra(Constants.EXTRA_IMAGE, mCurrentPhotoPath)
                    ActivityNavigator(this@ImagesActivity).startActivity(intent)
                } else {
                    Toast.makeText(baseContext, getString(R.string.selecte_other_image), Toast.LENGTH_SHORT).show()
                }
            }
            REQUEST_CAMERA -> {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
            }
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent()
            } else {

            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            image_back.id -> {
                if (imageFragment?.isVisible!!) {
                    FragmentNavigator(supportFragmentManager).removeFragmentTransaction(imageFragment!!)
                } else {
                    Ads.f(baseContext)
                    ActivityNavigator(this).finishActivity()
                }
            }
            image_camera.id -> {
                checkPermission()
            }
        }

    }

    /**
     * check permission
     */
    fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Utils.checkPermission(this, PERMISSION) == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent()
            } else {
                requestPermissions(PERMISSION, REQUEST_CODE_PERMISSION)
            }
        } else {
            dispatchTakePictureIntent()
        }
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
        progressDialog = ProgressDialog(this)
    }

    /**
     * init toolbar
     */
    fun initToolbar() {
        text_title.setText(getString(R.string.albums))
        image_back.setOnClickListener(this)
        image_camera.setOnClickListener(this)
    }

    /**
     * init view
     */
    fun initView() {
        initAds()
        initDialog()
        initToolbar()
        initFragment()
    }

    /**
     * init fragment list album&image
     */
    fun initFragment() {
        imageFragment = ImageFragment()
        imageFragment?.setImageCallback(onImageCallback)
        albumFragment = AlbumFragment()
        albumFragment?.setImageCallback(onImageCallback)
        FragmentNavigator(supportFragmentManager).fragmentTransactionNoBackStack(gragment.id, albumFragment!!)
        progressDialog.show()
        AlbumImageLoader(baseContext, onImageCallback).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
    }

    /**
     * init List album
     */
    fun getAlbums(): ArrayList<ImageMedia>? {
        if (mapImages != null) {
            var albums = ArrayList<ImageMedia>()
            for (key in mapImages!!.keys) {
                var size = mapImages?.get(key)!!.size
                if (size > 0) {
                    albums.add(mapImages?.get(key)!!.get(0))
                }
            }
            return albums
        }
        return null
    }

    lateinit var mCurrentPhotoPath: String

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        ).apply {
            mCurrentPhotoPath = absolutePath
        }
    }

    val REQUEST_TAKE_PHOTO = 1

    /**
     * capture image from camera using intent
     */
    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                    val dir = Constants.TEMP
                    val newdir = File(dir)
                    newdir.mkdirs()
                    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                    val file = "$dir${timeStamp}.jpg"
                    val newfile = File(file)
                    try {
                        newfile.createNewFile()
                    } catch (e: IOException) {
                    }
                    val outputFileUri = Uri.fromFile(newfile)
                    mCurrentPhotoPath = outputFileUri.path
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri)
                } else {
                    val photoFile: File? = try {
                        createImageFile()
                    } catch (ex: IOException) {
                        null
                    }
                    // Continue only if the File was successfully created
                    photoFile?.also {
                        val photoURI: Uri = FileProvider.getUriForFile(
                                this,
                                "${BuildConfig.APPLICATION_ID}.fileprovider",
                                it
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    }
                }
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
            }
        }
    }

    /**
     * on listener from fragment update activity
     */
    val onImageCallback = object : ImageCallback {
        override fun onClickAlbum(idAlbum: String, nameAlbum: String) {
            imageFragment?.reloadListImages(mapImages?.get(idAlbum)!!)
            FragmentNavigator(supportFragmentManager).fragmentTransaction(gragment.id, imageFragment!!)
            text_title.setText(nameAlbum)
        }

        override fun onClickImage(image: ImageMedia) {
            var intent = Intent(this@ImagesActivity, ImageEditorActivity::class.java)
            intent.putExtra(Constants.EXTRA_IMAGE, image.pathImage)
            ActivityNavigator(this@ImagesActivity).startActivity(intent)

        }

        override fun onUpdateNameAlbum() {
            text_title.setText(getString(R.string.albums))

        }

        override fun onLoadedImage(albums: HashMap<String, ArrayList<ImageMedia>>) {
            progressDialog.dismiss()
            mapImages = albums
            albumFragment?.reloadListAlbum(getAlbums()!!)
        }

        override fun onLoadedFailImage() {
            progressDialog.dismiss()
        }

    }
}
