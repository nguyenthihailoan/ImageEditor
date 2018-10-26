package com.gallery.editor.image.photoeditor.utils.navigations

import android.app.Activity
import android.content.Intent
import android.support.annotation.IntDef
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.gallery.editor.image.photoeditor.R

class ActivityNavigator {
    lateinit var mActivity: Activity

    constructor(mActivity: Activity) {
        this.mActivity = mActivity
    }

    fun startActivity(intent: Intent) {
        mActivity.startActivity(intent);
        setAnimationTransaction(START)
    }

    fun startActivityWithTransactionPrevious(intent: Intent) {
        mActivity.startActivity(intent);
        finishActivity()
    }

    fun startActivityResult(intent: Intent, requestCode: Int) {
        mActivity.startActivityForResult(intent, requestCode);
        setAnimationTransaction(START)
    }

    fun finishActivity() {
        mActivity.finish();
        setAnimationTransaction(FINISH);
    }

    fun finishActivityResult(intent: Intent, requestCode: Int) {
        mActivity.setResult(requestCode, intent)
        finishActivity()
    }

    private fun setAnimationTransaction(@AnimationTransaction aniamtion: Int) {
        when (aniamtion) {
            START -> mActivity.overridePendingTransition(R.anim.anim_right, R.anim.anim_left)
            FINISH -> mActivity.overridePendingTransition(R.anim.enter_anim_rtl, R.anim.exit_anim_rtl)
        }
    }


    @IntDef(NONE.toInt(), START.toInt(), FINISH.toInt())
    annotation class AnimationTransaction

    companion object {
        const val NONE: Int = 0
        const val START: Int = 1
        const val FINISH: Int = 2
    }
}