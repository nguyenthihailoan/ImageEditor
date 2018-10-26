package com.gallery.editor.image.photoeditor.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.gallery.editor.image.photoeditor.R;

public class SaveImageDialog extends AlertDialog {
    private TextView txtBack, txtTitle, txtContent;
    private OnClickEvent onClick;

    public SaveImageDialog(@NonNull Context context, OnClickEvent onClick) {
        super(context);
        this.onClick = onClick;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(true);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_warning);
        txtTitle = findViewById(R.id.text_title);
        txtTitle.setText(getContext().getString(R.string.save_image));
        txtContent = findViewById(R.id.content);
        txtContent.setText(getContext().getString(R.string.content_save_image));
        txtBack = findViewById(R.id.text_back);
        txtBack.setText(getContext().getString(R.string.save));
        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                onClick.onClickBack();
            }
        });
    }

    public interface OnClickEvent {
        void onClickBack();
    }
}
