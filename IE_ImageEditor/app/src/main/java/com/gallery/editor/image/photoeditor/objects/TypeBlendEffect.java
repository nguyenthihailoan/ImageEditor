package com.gallery.editor.image.photoeditor.objects;

import android.support.annotation.IntDef;

@IntDef({TypeBlendEffect.NORMAL, TypeBlendEffect.MULTIPLY, TypeBlendEffect.OVERLAY, TypeBlendEffect.SCREEN, TypeBlendEffect.DIVIDE})
public @interface TypeBlendEffect {
    int NORMAL = 231;
    int MULTIPLY = 232;
    int OVERLAY = 233;
    int SCREEN = 234;
    int DIVIDE = 235;
}
