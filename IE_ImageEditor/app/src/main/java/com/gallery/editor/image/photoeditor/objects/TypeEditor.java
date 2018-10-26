package com.gallery.editor.image.photoeditor.objects;

import android.support.annotation.IntDef;

@IntDef({TypeEditor.ADJUST,
        TypeEditor.ROTATE, TypeEditor.ROTATE_RIGHT, TypeEditor.ROTATE_LEFT, TypeEditor.FLIP_HORIZONTAL, TypeEditor.FLIP_VERTICAL,
        TypeEditor.CROP, TypeEditor.CROP_FREE, TypeEditor.CROP_CIRCLE, TypeEditor.CROP_11, TypeEditor.CROP_34, TypeEditor.CROP_43, TypeEditor.CROP_169, TypeEditor.CROP_916,
        TypeEditor.EXPOSURE, TypeEditor.CONTRAST, TypeEditor.TEMPERATURE, TypeEditor.VINTAGE, TypeEditor.GRAIN, TypeEditor.SATURATION, TypeEditor.SHADOW, TypeEditor.COLORTINT,
TypeEditor.BRUSH,TypeEditor.DRAWLINE,TypeEditor.GLITER})
public @interface TypeEditor {
    int ADJUST = 1;

    int ROTATE = 11;
    int ROTATE_RIGHT = 111;
    int ROTATE_LEFT = 112;
    int FLIP_HORIZONTAL = 113;
    int FLIP_VERTICAL = 114;

    int CROP = 12;
    int CROP_FREE = 121;
    int CROP_CIRCLE = 122;
    int CROP_11 = 123;
    int CROP_34 = 124;
    int CROP_43 = 125;
    int CROP_169 = 126;
    int CROP_916 = 127;

    int EXPOSURE = 13;
    int CONTRAST = 14;
    int TEMPERATURE = 15;
    int VINTAGE = 16;
    int GRAIN = 17;
    int SATURATION = 18;
    int SHADOW = 19;
    int COLORTINT = 20;

    int EFFECT=2;
    int PORTRAIT=21;
    int FOOD=22;
    int GALAXY=23;
    int LIGHT=24;
    int BROKEN=25;
    int SNOW=26;

    int BRUSH=4;
    int DRAWLINE=41;
    int GLITER=42;
}
