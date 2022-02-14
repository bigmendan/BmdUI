package com.bmd.bmd_ui.view.statusbar.compat

import android.app.Activity
import android.view.Window
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import com.bmd.bmd_ui.view.statusbar.utils.DarkModeUtils

/**
 *@author:       Bigmendan
 *@description:
 *@create:       2022-01-17 17:22
 */

class OsCompatDef : OsCompat {

    override fun isDarkIconMode(@NonNull fragment: Fragment): Boolean {
        val activity = fragment.activity ?: return false
        return isDarkIconMode(activity)
    }

    override fun isDarkIconMode(@NonNull activity: Activity): Boolean {
        val window = activity.window ?: return false
        return isDarkIconMode(window)
    }

    override fun isDarkIconMode(@NonNull window: Window): Boolean {
        return DarkModeUtils.isDarkIconMode(window)
    }

    override fun setDarkIconMode(@NonNull fragment: Fragment, darkIconMode: Boolean) {
        val activity = fragment.activity ?: return
        setDarkIconMode(activity, darkIconMode)
    }

    override fun setDarkIconMode(@NonNull activity: Activity, darkIconMode: Boolean) {
        val window = activity.window ?: return
        setDarkIconMode(window, darkIconMode)
    }

    override fun setDarkIconMode(@NonNull window: Window, darkIconMode: Boolean) {
        DarkModeUtils.setDarkIconMode(window, darkIconMode)
    }

}