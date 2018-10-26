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
import com.media.converter.photogif.videogif.gifmaker.adapters.AlbumsAdapter


class AlbumFragment : Fragment(), OnClickImageListener {
    private lateinit var callback: ImageCallback
    private lateinit var rc_image: RecyclerView
    private var albumAdapter: AlbumsAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_album, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListAlbum(view)
    }

    /**
     * handle event click album in recyclerview  and send data to activity
     */
    override fun onClickImage(pos: Int) {
        callback.onClickAlbum(albumAdapter!!.getItemAlbum(pos).idAlbum,
                albumAdapter!!.getItemAlbum(pos).nameAlbum)
    }

    fun reloadListAlbum(albums: ArrayList<ImageMedia>) {
        albumAdapter?.setListImage(albums)
    }

    fun setImageCallback(callback: ImageCallback) {
        this.callback = callback
    }

    /**
     * init recycle
     */
    fun initListAlbum(v: View) {
        rc_image = v.findViewById(R.id.rc_album)
        rc_image.layoutManager = GridLayoutManager(activity, 2)
        var space = SpaceItemDecorationGrid(resources.getDimensionPixelSize(R.dimen.dp_16),
                resources.getDimensionPixelSize(R.dimen.dp_12))
        rc_image.addItemDecoration(space)
        if (albumAdapter != null) {
            rc_image.adapter = albumAdapter
            albumAdapter?.notifyDataSetChanged()
        } else {
            albumAdapter = AlbumsAdapter(v.context, this)
            rc_image.adapter = albumAdapter
        }
    }
}
