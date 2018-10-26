package com.media.converter.photogif.videogif.gifmaker.screens.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gallery.editor.image.photoeditor.R
import com.gallery.editor.image.photoeditor.interfaces.ImageCallback
import com.gallery.editor.image.photoeditor.interfaces.OnClickImageListener
import com.gallery.editor.image.photoeditor.objects.ImageMedia
import com.gallery.editor.image.photoeditor.utils.SpaceItemDecorationGrid
import com.media.converter.photogif.videogif.gifmaker.adapters.ImagesAdapter
import java.util.*


class ImageFragment : Fragment(), OnClickImageListener {
    lateinit var callback: ImageCallback
    private lateinit var rc_image: RecyclerView
    private lateinit var imageAdapter: ImagesAdapter
    private lateinit var images: ArrayList<ImageMedia>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListImage(view)
    }

    /**
     * handle event click image in recyclerview  and send data to activity
     */
    override fun onClickImage(pos: Int) {
        callback.onClickImage(imageAdapter.getItemImage(pos))
    }

    /**
     * update list image in recycle
     */
    fun reloadListImages(images: ArrayList<ImageMedia>) {
        this.images = images
    }

    /**
     * set event click from activity to fragment
     */
    fun setImageCallback(callback: ImageCallback) {
        this.callback = callback
    }

    /**
     * init recycle
     */
    fun initListImage(v: View) {
        rc_image = v.findViewById(R.id.rc_album)
        rc_image.layoutManager = GridLayoutManager(activity, 3)
        var space = SpaceItemDecorationGrid(resources.getDimensionPixelSize(R.dimen.dp_12),
                resources.getDimensionPixelSize(R.dimen.dp_12))
        rc_image.addItemDecoration(space)
        imageAdapter = ImagesAdapter(v.context, this)
        imageAdapter.setListImage(images)
        rc_image.adapter = imageAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        callback.onUpdateNameAlbum()
    }
}
