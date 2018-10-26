package com.gallery.editor.image.photoeditor.screens.activities

import android.content.Intent
import android.graphics.*
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.SeekBar
import android.widget.Toast
import com.gallery.editor.image.photoeditor.R
import com.gallery.editor.image.photoeditor.dialogs.ProgressDialog
import com.gallery.editor.image.photoeditor.dialogs.SaveImageDialog
import com.gallery.editor.image.photoeditor.dialogs.WarningDialog
import com.gallery.editor.image.photoeditor.interfaces.*
import com.gallery.editor.image.photoeditor.objects.AdjustObject
import com.gallery.editor.image.photoeditor.objects.TypeBlendEffect
import com.gallery.editor.image.photoeditor.objects.TypeEditor
import com.gallery.editor.image.photoeditor.screens.fragments.AdjustFragment
import com.gallery.editor.image.photoeditor.screens.fragments.BrushFragment
import com.gallery.editor.image.photoeditor.screens.fragments.EffectFragment
import com.gallery.editor.image.photoeditor.screens.fragments.TimeStampFragment
import com.gallery.editor.image.photoeditor.utils.*
import com.gallery.editor.image.photoeditor.utils.navigations.ActivityNavigator
import com.gallery.editor.image.photoeditor.utils.navigations.FragmentNavigator
import com.gallery.editor.image.photoeditor.utils.sdk.Ads
import com.gallery.editor.image.photoeditor.widgets.imageview.BrushView
import com.gallery.editor.image.photoeditor.widgets.imageview.GliterView
import com.gallery.editor.image.photoeditor.widgets.simplecropview.CropImageView
import com.gallery.editor.image.photoeditor.widgets.simplecropview.callback.CropCallback
import jp.co.cyberagent.android.gpuimage.*
import kotlinx.android.synthetic.main.activity_image_editor.*
import java.util.*


class ImageEditorActivity : AppCompatActivity(), View.OnClickListener, BitmapActionListener {

    val TAG = "ImageEditorActivity"
    val VALUE_DEFAULT = 50
    private lateinit var progressDialog: ProgressDialog
    private lateinit var pathImage: String
    private var bitmap: Bitmap? = null
    private var adjustFragment: AdjustFragment? = null
    private var mFilter: GPUImageFilter? = null
    private var mFilterAdjuster: GPUImageFilterTool.FilterAdjuster? = null
    private var mFilterTool: GPUImageFilterTool? = null
    private var modeCrop = CropImageView.CropMode.FREE
    private lateinit var gpuImage: GPUImage
    private var isCrop = false
    private var bitmapTem: Bitmap? = null
    private var blend: Bitmap? = null
    private var effectFragment: EffectFragment? = null
    private var typeBlend = TypeBlendEffect.SCREEN
    private var timestampFragment: TimeStampFragment? = null
    private var typeFont: String? = null
    private var brushFragment: BrushFragment? = null
    private lateinit var paramDrawLine: FrameLayout.LayoutParams
    private var isPopTop: Boolean = false
    private var arrViewBrush = Stack<View>()
    private var arrViewBrushTemp = Stack<View>()
    //    private var gliterView: GliterView? = null
    private var gliterPath = "gliter/img_1.png"

    private lateinit var question: WarningDialog
    private lateinit var saveDialog: SaveImageDialog
    private var isQuestion = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
                or WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
                        or WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_image_editor)
        pathImage = intent.getStringExtra(Constants.EXTRA_IMAGE)
//        pathImage = Environment.getExternalStorageDirectory().absolutePath + "/image1.jpg"
        bitmap = BitmapImage.rotateBitmap(BitmapFactoryL.getBitmapFromLocal(pathImage),
                BitmapImage.getDirectionImage(pathImage))
//        Glide.with(baseContext).load(pathImage).into(image_editor)
        image_editor.setImageBitmap(bitmap)
        image_editor.setBackgroundColor(Color.TRANSPARENT)
        image_editor.isEnabled = true
        image_editor.setInitialFrameScale(0.75f);
        hidenBorderCropImageView()
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

    override fun onBackPressed() {
        Ads.f(baseContext)
        if (isQuestion && !checkFragmentShow()) {
            question.show()
        } else {
            super.onBackPressed()
        }
    }

    override fun saveBitmap(path: String) {
        progressDialog.dismiss()
//        val uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", File(pathImage))
        Utils.updatefile(contentResolver, path)
//        sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))
        var intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        ActivityNavigator(this).startActivityWithTransactionPrevious(intent)
    }

    override fun saveErrorBitmap() {
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            image_back.id -> {
                if (isQuestion) {
                    question.show()
                } else {
                    Ads.f(baseContext)
                    ActivityNavigator(this).finishActivity()
                }
            }
            image_check.id -> {
                if (isQuestion) {
                    progressDialog.show()
                    SaveBitmapToLocal(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, bitmap)
                } else {
                    saveDialog.show()
                }
                Ads.f(baseContext)
            }
            image_redo.id -> {
                handleRedo()
            }
            image_undo.id -> {
                handleUndo()
            }
        }

    }

    /**
     * check has other fragmeng show
     */
    fun checkFragmentShow(): Boolean {
        if (adjustFragment != null && adjustFragment!!.isVisible) return true
        if (effectFragment != null && effectFragment!!.isVisible) return true
        if (brushFragment != null && brushFragment!!.isVisible) return true
        if (timestampFragment != null && timestampFragment!!.isVisible) return true
        return false
    }

    /**
     * init dialog
     */
    fun initDialog() {
        progressDialog = ProgressDialog(this)
        saveDialog = SaveImageDialog(this, object : SaveImageDialog.OnClickEvent {
            override fun onClickBack() {
                progressDialog.show()
                SaveBitmapToLocal(this@ImageEditorActivity).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, bitmap)
            }

        })
        question = WarningDialog(this, object : WarningDialog.OnClickEvent {
            override fun onClickBack() {
                ActivityNavigator(this@ImageEditorActivity).finishActivity()
            }
        })
    }

    /**
     * init toolbar
     */
    fun initToolbar() {
        image_back.setOnClickListener(this)
        image_check.setOnClickListener(this)
        image_redo.setOnClickListener(this)
        image_undo.setOnClickListener(this)
    }

    /**
     * init View
     */
    fun initView() {
        initItemEditor()
        initToolbar()
        initDialog()
        initItemEditor()
        initSeekbar()
        paramDrawLine = brush_draw.layoutParams as FrameLayout.LayoutParams
    }

    /**
     * init seekbar for adjust
     */
    fun initSeekbar() {
        gpuImage = GPUImage(baseContext)
        gpuImage.setGLSurfaceView(image_gl)
        gpuImage.setScaleType(GPUImage.ScaleType.CENTER_INSIDE)
        gpuImage.setImage(bitmap)
        mFilterTool = GPUImageFilterTool()
        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    if (brushFragment != null && brushFragment!!.isVisible
                            && frame_draw_line.visibility == View.VISIBLE) {
                        var size = seekBar?.progress
                        if (size == 0) {
                            size = 1
                        }
                        (arrViewBrush.peek() as BrushView).setSize(size!!.toFloat())
                        return
                    }
                    if (mFilterAdjuster?.adjuster != null) {
                        mFilterAdjuster?.adjust(progress)
                    } else if (blend != null) {
                        changeTypeBlend(adjustOpacity(blend!!, progress), typeBlend)
                    }
                    gpuImage.requestRender()
                }

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (effectFragment != null && effectFragment!!.isVisible && blend == null) {
                    Toast.makeText(baseContext, getString(R.string.select_effect), Toast.LENGTH_SHORT).show()
                    return
                }
                if (brushFragment != null && brushFragment!!.isVisible
                        && frame_draw_gliter.visibility == View.VISIBLE) {
                    var size = seekBar?.progress
                    if (size == 0) {
                        size = 10
                    }
                    (arrViewBrush.peek() as GliterView).setSize(size!!.toFloat())
                    var bitmap = BitmapFactoryL.getBitmapFromAsset(baseContext, gliterPath!!, size!!)
                    (arrViewBrush.peek() as GliterView).setBitmapGliter(bitmap!!)
                }
            }

        })
    }

    /**
     * init item editor
     */
    fun initItemEditor() {
        image_adjust.setOnClickListener(on_click)
        image_effect.setOnClickListener(on_click)
        image_brush.setOnClickListener(on_click)
        image_timestamp.setOnClickListener(on_click)
        text_adjust.setOnClickListener(on_click)
        text_effect.setOnClickListener(on_click)
        text_brush.setOnClickListener(on_click)
        text_time.setOnClickListener(on_click)
    }

    /**
     * on listener event click item editor
     */
    private val on_click = object : View.OnClickListener {
        override fun onClick(v: View?) {
            when (v?.id) {
                image_adjust.id, text_adjust.id -> {
                    showAdjustFragment()
                }
                image_effect.id, text_effect.id -> {
                    showEffectFragment()
                }
                image_brush.id, text_brush.id -> {
                    showBrushFramgment()
                }
                image_timestamp.id, text_time.id -> {
                    showTimeStampFramgent()
                }
            }
            item_editor.visibility = View.INVISIBLE
        }
    }

    /**
     * handle show fragment adjust
     */
    fun showAdjustFragment() {
        if (adjustFragment == null) {
            adjustFragment = AdjustFragment()
            adjustFragment?.setOnListenerAdjust(onListenerAdjust)
        }
        seekbar.max = 100
        FragmentNavigator(supportFragmentManager).fragmentTransaction(fragment.id, adjustFragment!!)
    }

    /**
     * isshow gpuimage and seekbar
     */
    fun isShowSeekbar(@TypeEditor type: Int): Boolean {
        when (type) {
            TypeEditor.COLORTINT, TypeEditor.CONTRAST,
            TypeEditor.SATURATION, TypeEditor.TEMPERATURE,
            TypeEditor.EXPOSURE, TypeEditor.SHADOW,
            TypeEditor.GRAIN, TypeEditor.VINTAGE -> {
                return true
            }
            else -> {
                return false
            }
        }
    }

    /**
     * rotate bitmap
     */
    fun Bitmap.rotate(degrees: Float, fX: Float, fY: Float): Bitmap? {
        val matrix = Matrix().apply {
            postRotate(degrees)
            postScale(fX, fY, width / 2f, height / 2f);
        }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }

    /**
     * show border cropimageview
     */
    fun showBoderCropImageView() {
        image_editor.isEnabled = true
        image_editor.showBorderCropView()
    }

    /**
     * hiden border cropimageview
     */
    fun hidenBorderCropImageView() {
        image_editor.isEnabled = false
        image_editor.hidenBorderCropView()
    }

    /**
     * change opacity bitmap
     */
    private fun adjustOpacity(blend: Bitmap, opacity: Int): Bitmap {
        val newBitmap = Bitmap.createBitmap(blend.getWidth(), blend.getHeight(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(newBitmap)
        val alphaPaint = Paint()
        alphaPaint.alpha = opacity
        canvas.drawBitmap(blend, 0f, 0f, alphaPaint)
        return newBitmap
    }

    /**
     * handle show fragment effect
     */
    fun showEffectFragment() {
        if (effectFragment == null) {
            effectFragment = EffectFragment()
            effectFragment?.setOnListener(onListenerEffect)
        }
        seekbar.max = 255
        seekbar.progress = 255
        image_gl.visibility = View.VISIBLE
        onListenerEffect.removeEffect()
        gpuImage.deleteImage()
        gpuImage.setImage(bitmap)
        FragmentNavigator(supportFragmentManager).fragmentTransaction(fragment.id, effectFragment!!)
    }

    /**
     * change filter
     */
    private fun switchFilterTo(@TypeEditor type: Int) {
        var adjustObject: AdjustObject? = null
        if (mFilterTool!!.checkAdjustExist(type)) {
            adjustObject = mFilterTool?.getFilter(type)
        } else {
            adjustObject = mFilterTool?.addFilter(type)
            gpuImage.setFilter(mFilterTool?.filterGroup)
        }
        mFilter = adjustObject?.filter
        mFilterAdjuster = adjustObject?.adjust
        seekbar.progress = adjustObject!!.progress

    }

    /**
     * add effect
     */
    fun showSeekBarBlend(@TypeEditor type: Int) {
        when (type) {
            TypeEditor.PORTRAIT, TypeEditor.FOOD -> {
                layout_seekbar.visibility = View.GONE
            }
            TypeEditor.BROKEN, TypeEditor.LIGHT, TypeEditor.GALAXY, TypeEditor.SNOW -> {
                layout_seekbar.visibility = View.VISIBLE
                size.visibility = View.GONE
            }
        }
    }

    /**
     * add effect
     */
    fun addEffect(effect: String, @TypeEditor type: Int) {
        when (type) {
            TypeEditor.PORTRAIT, TypeEditor.FOOD -> {
                mFilter = GPUImageFilterTool.createCurveFilter(baseContext, effect)
                mFilterAdjuster = GPUImageFilterTool.FilterAdjuster(mFilter!!)
                gpuImage.setFilter(mFilter)
            }
            TypeEditor.BROKEN, TypeEditor.LIGHT, TypeEditor.GALAXY, TypeEditor.SNOW -> {
                blend = Bitmap.createScaledBitmap(BitmapFactoryL.getBitmapFromAsset(baseContext, effect),
                        bitmap!!.width, bitmap!!.height, true)
                changeTypeBlend(adjustOpacity(blend!!, seekbar.progress), typeBlend)
                mFilterAdjuster = GPUImageFilterTool.FilterAdjuster(mFilter!!)
            }
        }
    }

    /**
     * change type blend for effect: overlay, screen,divide
     */
    fun changeTypeBlend(blendEffect: Bitmap, @TypeBlendEffect typeBlend: Int) {
        when (typeBlend) {
            TypeBlendEffect.NORMAL -> {
                mFilter = GPUImageFilterTool.createGlFilterNormal(blendEffect!!)
                gpuImage.setFilter(mFilter)
            }
            TypeBlendEffect.MULTIPLY -> {
                mFilter = GPUImageFilterTool.createGlFilterMultiply(blendEffect!!)
                gpuImage.setFilter(mFilter)
            }
            TypeBlendEffect.OVERLAY -> {
                mFilter = GPUImageFilterTool.createGlFilterOverlay(blendEffect!!)
                gpuImage.setFilter(mFilter)
            }
            TypeBlendEffect.SCREEN -> {
                mFilter = GPUImageFilterTool.createGlFilterScreen(blendEffect!!)
                gpuImage.setFilter(mFilter)
            }
            TypeBlendEffect.DIVIDE -> {
                mFilter = GPUImageFilterTool.createGlFilterDivide(blendEffect!!)
                gpuImage.setFilter(mFilter)
            }
        }
    }

    /**
     * get bitmapFrom GPUImage Blend
     */
    fun getBitmapGPUImageBlend(): Bitmap? {
        when (typeBlend) {
            TypeBlendEffect.NORMAL -> {
                return (mFilter as GPUImageNormalBlendFilter).bitmap
            }
            TypeBlendEffect.MULTIPLY -> {
                return (mFilter as GPUImageMultiplyBlendFilter).bitmap
            }
            TypeBlendEffect.OVERLAY -> {
                return (mFilter as GPUImageOverlayBlendFilter).bitmap
            }
            TypeBlendEffect.SCREEN -> {
                return (mFilter as GPUImageScreenBlendFilter).bitmap
            }
            TypeBlendEffect.DIVIDE -> {
                return (mFilter as GPUImageDivideBlendFilter).bitmap
            }
            else -> {
                return null
            }
        }
    }

    /**
     * handle show fragment timestamp
     */
    fun showTimeStampFramgent() {
        if (timestampFragment == null) {
            timestampFragment = TimeStampFragment()
            timestampFragment?.setOnListenerTimeStamp(onListenerTimeStamp)
        }
        caculatorFrameDraw(frame_time)
        frame_time.visibility = View.VISIBLE
        var typeFace = Typeface.createFromAsset(assets, timestampFragment?.FONT1)
        text_timestamp.typeface = typeFace
        text_timestamp.setText(Html.fromHtml(TimeStampIE.convertTimeToHtml()))
        FragmentNavigator(supportFragmentManager).fragmentTransaction(fragment.id, timestampFragment!!)
    }


    fun showBrushFramgment() {
        if (brushFragment == null) {
            brushFragment = BrushFragment()
            brushFragment?.setOnListenerBrush(onListenerBrush)
        }
        var view = removeFramDrawLine(true)
        caculatorFrameDraw(frame_draw_line)
        caculatorFrameDraw(frame_draw_gliter)
        rl_edit.visibility = View.VISIBLE
        seekbar.max = 100
        seekbar.progress = 20
        frame_draw_line.visibility = View.VISIBLE
        view?.setColor(Color.WHITE)
        view?.setSize(seekbar.progress.toFloat())
        layout_seekbar.visibility = View.VISIBLE
        size.visibility = View.VISIBLE
        arrViewBrush.push(view)
        FragmentNavigator(supportFragmentManager).fragmentTransaction(fragment.id, brushFragment!!)
    }

    /**
     * caculator framlayout to draw text, brush
     */
    fun caculatorFrameDraw(frameDraw: FrameLayout) {
        var widthS = Utils.getWidthScreen(baseContext)
        var heightS = Utils.getHeightScreen(baseContext)
        heightS = heightS - resources.getDimensionPixelSize(R.dimen.dp_80) - resources.getDimensionPixelSize(R.dimen.dp_240)
        var widthB = bitmap?.width
        var heightB = bitmap?.height
        var params = frameDraw.layoutParams as FrameLayout.LayoutParams
        if (widthB!! >= heightB!!) {
            params.width = widthS
            var scale = heightB / widthB.toFloat()
            var h = scale * widthS
            params.height = h.toInt()
        } else {
            params.height = image_editor.height
            var scale = widthB / heightB.toFloat()
            var w = scale * params.height
            params.width = w.toInt()
        }
        frameDraw.layoutParams = params
    }

    /**
     * handle undo and redo brush
     */
    fun handleUndo() {
        if (!arrViewBrush.isEmpty()) {
            if (isPopTop) {
                isPopTop = false
                if (frame_draw_line.visibility == View.VISIBLE) {
                    frame_draw_line.removeView(arrViewBrush.pop())
                } else if (frame_draw_gliter.visibility == View.VISIBLE) {
                    frame_draw_gliter.removeView(arrViewBrush.pop())
                }
            }
            arrViewBrushTemp.push(arrViewBrush.pop())
            if (frame_draw_line.visibility == View.VISIBLE) {
                frame_draw_line.removeView(arrViewBrushTemp.peek())
            } else if (frame_draw_gliter.visibility == View.VISIBLE) {
                frame_draw_gliter.removeView(arrViewBrushTemp.peek())
            }
        }
        if (arrViewBrush.isEmpty()) {
            if (frame_draw_line.visibility == View.VISIBLE) {
                var view = createViewDrawLine()
                arrViewBrush.push(view)
                frame_draw_line.addView(view)
            } else if (frame_draw_gliter.visibility == View.VISIBLE) {
                var view = createViewDrawGliter()
                arrViewBrush.push(view)
                frame_draw_gliter.addView(view)
            }
        }
    }

    fun handleRedo() {
        if (!arrViewBrushTemp.isEmpty()) {
            arrViewBrush.push(arrViewBrushTemp.pop())
            if (frame_draw_line.visibility == View.VISIBLE) {
                frame_draw_line.addView(arrViewBrush.peek())
            } else if (frame_draw_gliter.visibility == View.VISIBLE) {
                frame_draw_gliter.addView(arrViewBrush.peek())
            }
        }
    }

    /**
     * on Listener draw brush
     */
    private val onListenerDraw = object : OnListenerStopDraw {
        override fun onStopDrawLineBrush() {
            isPopTop = true
            var view = createViewDrawLine()
            arrViewBrush.push(view)
            frame_draw_line.addView(view)
        }
    }

    /**
     * on Listener draw brush
     */
    private val onListenerDrawGliter = object : OnListenerStopDraw {
        override fun onStopDrawLineBrush() {
            isPopTop = true
            var view = createViewDrawGliter()
            arrViewBrush.push(view)
            frame_draw_gliter.addView(view)
        }
    }

    /**
     * creat View Draw line
     */
    fun createViewDrawLine(): BrushView {
        var view = BrushView(baseContext)
        view.layoutParams = paramDrawLine
        if (!arrViewBrush.isEmpty()) {
            arrViewBrush.peek().isEnabled = false
        }
        view.setOnListener(onListenerDraw)
        view.setSize(seekbar.progress.toFloat())
        view.setColor(brushFragment!!.getCurrentColor())
        return view
    }

    /**
     *creat View Draw gliter
     */
    fun createViewDrawGliter(): GliterView {
        var view = GliterView(baseContext)
        view.layoutParams = paramDrawLine
        if (!arrViewBrush.isEmpty()) {
            arrViewBrush.peek().isEnabled = false
        }
        view.setSize(seekbar.progress.toFloat())
        view.setOnListener(onListenerDrawGliter)
        view.setBitmapGliter(BitmapFactoryL.getBitmapFromAsset(baseContext,
                gliterPath,
                seekbar.progress)!!)
        return view
    }

    /**
     * remove all view draw line
     */
    fun removeFramDrawLine(isCreatNew: Boolean): BrushView? {
        while (!arrViewBrush.isEmpty()) {
            if (frame_draw_line.visibility == View.VISIBLE) {
                frame_draw_line.removeView(arrViewBrush.pop())
            } else if (frame_draw_gliter.visibility == View.VISIBLE) {
                frame_draw_gliter.removeView(arrViewBrush.pop())
            }
        }
        while (!arrViewBrushTemp.isEmpty()) {
            arrViewBrushTemp.pop()
        }
        if (isCreatNew) {
            var view = createViewDrawLine()
            frame_draw_line.addView(view)
            return view
        }
        return null
    }

    /**
     * on listener event from brush fragment
     */
    private val onListenerBrush = object : OnListenerBrushFragment {

        override fun changeGliter(gliter: String) {
            gliterPath = gliter
            var bitmap = BitmapFactoryL.getBitmapFromAsset(baseContext, gliter, seekbar.progress)
            (arrViewBrush.peek() as GliterView).setBitmapGliter(bitmap!!)
        }

        override fun onSaveWithDrawLine() {
            isQuestion = true
            if (frame_draw_line.visibility == View.VISIBLE) {
                bitmap = BitmapImage.drawBitmapWithText(bitmap!!, frame_draw_line)
            } else if (frame_draw_gliter.visibility == View.VISIBLE) {
                bitmap = BitmapImage.drawBitmapWithText(bitmap!!, frame_draw_gliter)
            }
            image_editor.imageBitmap = bitmap
            onremoveFragment()
        }

        override fun changeStatusBrush(type: Int) {
            if (type == TypeEditor.DRAWLINE) {
                removeFramDrawLine(false)
                var view = createViewDrawLine()
                arrViewBrush.push(view)
                frame_draw_line.addView(view)
                frame_draw_line.visibility = View.VISIBLE
                frame_draw_gliter.visibility = View.GONE
            } else {
                removeFramDrawLine(false)
                frame_draw_line.visibility = View.GONE
                frame_draw_gliter.visibility = View.VISIBLE
                var gliterView = createViewDrawGliter()
                frame_draw_gliter.addView(gliterView)
                var bitmap = BitmapFactoryL.getBitmapFromAsset(baseContext, gliterPath, seekbar.progress)
                gliterView?.setBitmapGliter(bitmap!!)
                arrViewBrush.push(gliterView)
            }
        }

        override fun changeColorBrush(color: Int) {
            (arrViewBrush.peek() as BrushView).setColor(color)
        }

        override fun hidenViewDraw() {
            frame_draw_gliter.visibility = View.GONE
            frame_draw_line.visibility = View.GONE
            rl_edit.visibility = View.GONE
            layout_seekbar.visibility = View.GONE
            item_editor.visibility = View.VISIBLE
        }

        override fun onDestroyView() {
            isPopTop = false
            removeFramDrawLine(false)
            hidenViewDraw()
        }

        override fun onremoveFragment() {
            removeFramDrawLine(false)
            hidenViewDraw()
            isPopTop = false
            FragmentNavigator(supportFragmentManager).removeFragmentTransaction(brushFragment!!)
        }
    }

    /**
     * on listener event from timestamp fragment
     */
    private val onListenerTimeStamp = object : OnListenerTimestampFragment {
        override fun saveTextWithBitmap(color: Int, typeface: Typeface) {
            isQuestion = true
            bitmap = BitmapImage.drawBitmapWithText(bitmap!!, frame_time)
            frame_time.visibility = View.GONE
            image_editor.imageBitmap = bitmap
            onremoveFragment()
        }

        override fun onClickColor(color: Int) {
            text_timestamp.setTextColor(color)
        }

        override fun onClickFont(font: String) {
            var typeface = Typeface.createFromAsset(assets, font)
            text_timestamp.setTypeface(typeface)
        }

        override fun onDestroyView() {
            item_editor.visibility = View.VISIBLE
            frame_time.visibility = View.GONE
        }

        override fun onremoveFragment() {
            frame_time.visibility = View.GONE
            FragmentNavigator(supportFragmentManager).removeFragmentTransaction(timestampFragment!!)
        }
    }


    /**
     * on listener event from effect fragment
     */
    private val onListenerEffect = object : OnListenerEffectFragment {
        override fun saveImageWithBlende() {
            isQuestion = true
            bitmapTem = gpuImage.getBitmapWithFilterApplied(bitmap)
            bitmap = bitmapTem!!
            image_editor.imageBitmap = bitmap
            onremoveFragment()
        }

        override fun removeEffect() {
            mFilter = GPUImageFilterTool.createCurveFilter(baseContext, "effects/0.acv")
            blend?.recycle()
            blend = null
            gpuImage.setFilter(mFilter)
        }

        override fun onClickTypeEffect(type: Int) {
            showSeekBarBlend(type)
        }

        override fun onClickEffect(effect: String, type: Int) {
            addEffect(effect, type)
        }

        override fun onClickEffectType(typeBlend: Int) {
            if (blend == null) {
                Toast.makeText(baseContext, getString(R.string.select_effect), Toast.LENGTH_SHORT).show()
            } else {
                changeTypeBlend(getBitmapGPUImageBlend()!!, typeBlend)
            }
            this@ImageEditorActivity.typeBlend = typeBlend
        }

        override fun onDestroyView() {
            item_editor.visibility = View.VISIBLE
            image_gl.visibility = View.GONE
            layout_seekbar.visibility = View.GONE
        }

        override fun onremoveFragment() {
            image_gl.visibility = View.GONE
            layout_seekbar.visibility = View.GONE
            FragmentNavigator(supportFragmentManager).removeFragmentTransaction(effectFragment!!)
        }

    }

    /**
     * on listener event from adjust fragment
     */
    private val onListenerAdjust = object : OnListenerAdjustFragment {
        override fun saveAdjust() {
            isQuestion = true
            bitmapTem = gpuImage.getBitmapWithFilterApplied(bitmap)
            bitmap = bitmapTem!!
            image_editor.imageBitmap = bitmap
            onremoveFragment()
        }

        override fun hidenGlView() {
            image_gl.visibility = View.GONE
            layout_seekbar.visibility = View.GONE
        }

        override fun hidenSeekbar() {
            layout_seekbar.visibility = View.GONE
        }

        override fun onCancelCrop() {
            hidenBorderCropImageView()
        }

        override fun onSaveCrop() {
            isQuestion = true
            image_editor.cropAsync(object : CropCallback {
                override fun onSuccess(cropped: Bitmap?) {
                    hidenBorderCropImageView()
                    bitmap = Bitmap.createBitmap(cropped!!)
                    image_editor.imageBitmap = bitmap
                    cropped.recycle()
                    isCrop = true
                }

                override fun onError(e: Throwable?) {
                    Toast.makeText(baseContext, getString(R.string.crop_error), Toast.LENGTH_SHORT).show()
                }

            })
        }

        override fun onChangeCropMode(mode: CropImageView.CropMode) {
            image_editor.setCropMode(mode)
            modeCrop = mode
        }

        override fun onSaveRotate() {
            isQuestion = true
            Log.d(TAG, image_editor.rotation.toString())
            bitmap = bitmap?.rotate(image_editor.rotation, image_editor.scaleX, image_editor.scaleY)!!
            if (bitmap != null) {
                image_editor.scaleX = 1f
                image_editor.scaleY = 1f
                image_editor.rotation = 0f
                image_editor.setImageBitmap(bitmap)
            }
        }

        override fun onRotateLeft() {
            image_editor.rotation = image_editor.rotation + 90f
        }

        override fun onRotateRight() {
            image_editor.rotation = image_editor.rotation - 90f
        }

        override fun onFlipVertical() {
            image_editor.scaleY = -1f * image_editor.scaleY
        }

        override fun onFlipHorizontal() {
            image_editor.scaleX = -1f * image_editor.scaleX
        }

        override fun onremoveFragment() {
            hidenGlView()
            FragmentNavigator(supportFragmentManager).removeFragmentTransaction(adjustFragment!!)
        }

        override fun onClickAdjust(type: Int) {
            if (isShowSeekbar(type)) {
                if (image_editor.isShowBorder) {
                    hidenBorderCropImageView()
                }
                if (image_gl.visibility == View.GONE) {
                    image_gl.visibility = View.VISIBLE
                    layout_seekbar.visibility = View.VISIBLE
                    size.visibility = View.VISIBLE
                    mFilterTool?.initFilterGroup(true)
                    gpuImage.setFilter(mFilterTool?.filterGroup)
                    gpuImage.deleteImage()
                    gpuImage.setImage(bitmap)
                }
                switchFilterTo(type)
            } else {
                image_gl.visibility = View.GONE
                layout_seekbar.visibility = View.GONE
            }
            when (type) {
                TypeEditor.CROP -> {
                    if (!image_editor.isShowBorder) {
                        showBoderCropImageView()
                    }
                }
                TypeEditor.ROTATE -> {
                    if (image_editor.isShowBorder) {
                        hidenBorderCropImageView()
                    }
                }
            }
        }

        override fun onDestroyView() {
            item_editor.visibility = View.VISIBLE
        }

    }
}
