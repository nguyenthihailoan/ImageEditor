package com.gallery.editor.image.photoeditor.screens.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.gallery.editor.image.photoeditor.R
import com.gallery.editor.image.photoeditor.interfaces.OnClickEditorListener
import com.gallery.editor.image.photoeditor.interfaces.OnListenerAdjustFragment
import com.gallery.editor.image.photoeditor.objects.TypeEditor
import com.gallery.editor.image.photoeditor.utils.IniterData
import com.gallery.editor.image.photoeditor.utils.SpaceItemDecorationHorizontal
import com.gallery.editor.image.photoeditor.widgets.simplecropview.CropImageView
import com.media.converter.photogif.videogif.gifmaker.adapters.EditorAdapter
import kotlinx.android.synthetic.main.fragment_adjust.*

class AdjustFragment : Fragment(), View.OnClickListener {
    private var onListener: OnListenerAdjustFragment? = null
    private lateinit var adjustsAdapter: EditorAdapter
    private var currentTypeParent: Int = TypeEditor.ADJUST
    private var currentTypeChild: Int = -1
    private var index = -1


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_adjust, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdjusts()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            cancel_adjust.id -> {
                if (currentTypeParent != TypeEditor.ADJUST) {
                    if (currentTypeParent == TypeEditor.CROP) {
                        onListener?.onCancelCrop()
                    } else {
                        onListener?.hidenGlView()
                    }
                    currentTypeParent = TypeEditor.ADJUST
                    reloadItemsIntoAdjust(currentTypeParent)
                } else {
                    onListener?.onremoveFragment()
                }
            }
            apply_adjust.id -> {
                if (currentTypeParent == -1 || currentTypeParent == TypeEditor.ADJUST) {
                    currentTypeParent = TypeEditor.ADJUST
                    reloadItemsIntoAdjust(currentTypeParent)
                } else {
                    actionApplyAdjust(currentTypeParent)
                    currentTypeChild = -1
                    currentTypeParent = TypeEditor.ADJUST
                    reloadItemsIntoAdjust(currentTypeParent)
                }
            }

        }
    }

    fun setOnListenerAdjust(onListener: OnListenerAdjustFragment) {
        this.onListener = onListener
    }

    /**
     * init recycler item adjusts
     */
    fun initAdjusts() {
        cancel_adjust.setOnClickListener(this)
        apply_adjust.setOnClickListener(this)
        recycler_adjusts.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recycler_adjusts.addItemDecoration(SpaceItemDecorationHorizontal(resources.getDimensionPixelSize(R.dimen.dp_30)))
        adjustsAdapter = EditorAdapter(context!!, object : OnClickEditorListener {
            override fun onClickEditor(pos: Int) {
                var type = adjustsAdapter.getItemEditor(pos).type
                if (type >= TypeEditor.ROTATE_RIGHT && type <= TypeEditor.FLIP_VERTICAL) {
                    onClickItemIntoRotate(type)
                    currentTypeChild = type
                } else if (type >= TypeEditor.CROP_FREE && type <= TypeEditor.CROP_916) {
                    currentTypeChild = type
                    onClickItemIntoCrop(currentTypeChild, pos)
                } else {
                    adjustsAdapter.changeStatus(index, pos)
                    index = pos
                    currentTypeParent = type
                    onListener?.onClickAdjust(currentTypeParent)
                    reloadItemsIntoAdjust(currentTypeParent)
                }
            }
        })
        recycler_adjusts.adapter = adjustsAdapter
        adjustsAdapter.setListEditor(IniterData.initAdjust(resources))
        adjustsAdapter.notifyDataSetChanged()
    }

    /**
     * apply adjust
     */
    fun actionApplyAdjust(@TypeEditor type: Int) {
        when (type) {
            TypeEditor.ROTATE -> {
                onListener?.onSaveRotate()
            }
            TypeEditor.CROP -> {
                onListener?.onSaveCrop()
            }
            else -> {
                onListener?.saveAdjust()
            }
        }
    }

    /**
     * change layoutmanager of recyclerview
     */
    fun changeLayoutMamagerRecycler(@TypeEditor type: Int) {
        var param = recycler_adjusts.layoutParams as LinearLayout.LayoutParams
        if (type == TypeEditor.ROTATE) {
            param.setMargins(resources.getDimensionPixelSize(R.dimen.dp_50), 0,
                    resources.getDimensionPixelSize(R.dimen.dp_50), 0)
        } else {
            param.setMargins(0, 0, 0, 0)
            recycler_adjusts.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    /**
     * onListener click rotate
     */
    fun onClickItemIntoRotate(@TypeEditor type: Int) {
        when (type) {
            TypeEditor.ROTATE_LEFT -> {
                onListener?.onRotateLeft()
            }
            TypeEditor.ROTATE_RIGHT -> {
                onListener?.onRotateRight()
            }
            TypeEditor.FLIP_HORIZONTAL -> {
                onListener?.onFlipHorizontal()
            }
            TypeEditor.FLIP_VERTICAL -> {
                onListener?.onFlipVertical()
            }
        }
    }

    /**
     * onListener click crop
     */
    fun onClickItemIntoCrop(@TypeEditor type: Int, pos: Int) {
        if (index == pos) return
        adjustsAdapter.changeStatus(index, pos)
        index = pos
        when (type) {
            TypeEditor.CROP_CIRCLE -> {
                onListener?.onChangeCropMode(CropImageView.CropMode.CIRCLE)
            }
            TypeEditor.CROP_FREE -> {
                onListener?.onChangeCropMode(CropImageView.CropMode.FREE)
            }
            TypeEditor.CROP_11 -> {
                onListener?.onChangeCropMode(CropImageView.CropMode.SQUARE)
            }
            TypeEditor.CROP_34 -> {
                onListener?.onChangeCropMode(CropImageView.CropMode.RATIO_3_4)
            }
            TypeEditor.CROP_43 -> {
                onListener?.onChangeCropMode(CropImageView.CropMode.RATIO_4_3)
            }
            TypeEditor.CROP_916 -> {
                onListener?.onChangeCropMode(CropImageView.CropMode.RATIO_9_16)
            }
            TypeEditor.CROP_169 -> {
                onListener?.onChangeCropMode(CropImageView.CropMode.RATIO_16_9)
            }
        }
    }

    /**
     *  reload list item into adjust when click item into adjust
     */
    fun reloadItemsIntoAdjust(@TypeEditor type: Int) {
        when (type) {
            TypeEditor.ADJUST -> {
                apply_adjust.visibility = View.INVISIBLE
                name_adjust.setText(getString(R.string.adjust))
                onListener?.hidenSeekbar()
                adjustsAdapter.changeShowText(true)
                changeLayoutMamagerRecycler(type)
                adjustsAdapter.setListEditor(IniterData.initAdjust(resources))
                adjustsAdapter.notifyDataSetChanged()
            }
            TypeEditor.CROP -> {
                apply_adjust.visibility = View.VISIBLE
                name_adjust.setText(getString(R.string.crop))
                adjustsAdapter.changeShowText(true)
                changeLayoutMamagerRecycler(type)
                adjustsAdapter.setListEditor(IniterData.initcrop(resources))
            }
            TypeEditor.ROTATE -> {
                apply_adjust.visibility = View.VISIBLE
                name_adjust.setText(getString(R.string.rotate))
                changeLayoutMamagerRecycler(type)
                adjustsAdapter.changeShowText(false)
                adjustsAdapter.setListEditor(IniterData.initrotate())

            }
            TypeEditor.EXPOSURE -> {
                apply_adjust.visibility = View.VISIBLE
                name_adjust.setText(getString(R.string.exp))
            }
            TypeEditor.CONTRAST -> {
                apply_adjust.visibility = View.VISIBLE
                name_adjust.setText(getString(R.string.contr))

            }
            TypeEditor.TEMPERATURE -> {
                apply_adjust.visibility = View.VISIBLE
                name_adjust.setText(getString(R.string.temp))
            }

            TypeEditor.VINTAGE -> {
                apply_adjust.visibility = View.VISIBLE
                name_adjust.setText(getString(R.string.vintage))
            }

            TypeEditor.GRAIN -> {
                apply_adjust.visibility = View.VISIBLE
                name_adjust.setText(getString(R.string.grain))
            }

            TypeEditor.SATURATION -> {
                apply_adjust.visibility = View.VISIBLE
                name_adjust.setText(getString(R.string.sat))
            }

            TypeEditor.SHADOW -> {
                apply_adjust.visibility = View.VISIBLE
                name_adjust.setText(getString(R.string.shaw))
            }

            TypeEditor.COLORTINT -> {
                apply_adjust.visibility = View.VISIBLE
                name_adjust.setText(getString(R.string.colortint))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onListener?.onDestroyView()
    }

}
