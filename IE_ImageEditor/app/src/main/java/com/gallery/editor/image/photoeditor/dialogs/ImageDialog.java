package com.gallery.editor.image.photoeditor.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.gallery.editor.image.photoeditor.R;

public class ImageDialog extends AlertDialog implements View.OnClickListener {
    private ImageView imageView, imageEdit, imageShare;
    private OnClickEvent onclick;

    public ImageDialog(@NonNull Context context, OnClickEvent onclick) {
        super(context);
        this.onclick = onclick;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(true);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_more_image);
        initView();
    }

    /**
     * init view
     */
    public void initView() {
        imageView = findViewById(R.id.image_view);
        imageView.setOnClickListener(this);
        imageEdit = findViewById(R.id.image_edit);
        imageEdit.setOnClickListener(this);
        imageShare = findViewById(R.id.image_share);
        imageShare.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_view:
                dismiss();
                onclick.onClickView();
                break;
            case R.id.image_edit:
                dismiss();
                onclick.onClickEdit();
                break;
            case R.id.image_share:
                dismiss();
                onclick.onClickShare();
                break;

        }
    }

    public interface OnClickEvent {
        void onClickView();

        void onClickEdit();

        void onClickShare();
    }
}
