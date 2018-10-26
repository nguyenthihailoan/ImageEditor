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

public class RateAppDialog extends AlertDialog {
    private TextView tv_rate, tv_not_now;
    private OnClickRateApp onClick;

    public RateAppDialog(@NonNull Context context, OnClickRateApp onClick) {
        super(context);
        this.onClick = onClick;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.rate_dialog);
        tv_rate = findViewById(R.id.text_rate);
        tv_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                onClick.onRateClick();
            }
        });
        tv_not_now = findViewById(R.id.text_not_now);
        tv_not_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                onClick.onCancelClick();
            }
        });

    }

    public interface OnClickRateApp {
        void onRateClick();
        void onCancelClick();
    }
}
