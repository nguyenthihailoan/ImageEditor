package com.gallery.editor.image.photoeditor.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.gallery.editor.image.photoeditor.R;

public class ProgressDialog extends AlertDialog {
    private TextView tv_content;

    public ProgressDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_progress);
        tv_content = findViewById(R.id.text_content);
        tv_content.setText(this.getContext().getResources().getString(R.string.loading));
    }

    public void setContent(String s) {
        tv_content.setText(s);
    }
}
