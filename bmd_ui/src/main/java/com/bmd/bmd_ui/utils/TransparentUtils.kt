package com.bmd.bmd_ui.utils

import android.annotation.TargetApi
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.NonNull

/**
 *@author:       Bigmendan
 *@description:
 *@create:       2022-01-17 17:37
 */
object TransparentUtils {
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun isTransparentStatusBarAbove21(@NonNull window: Window): Boolean {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        val ui = window.decorView.systemUiVisibility
        val flag = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        return flag == ui and flag
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    fun isTransparentStatusBarAbove19(@NonNull window: Window): Boolean {
        val flag = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        return flag == window.attributes.flags and flag
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun transparentStatusBarAbove21(@NonNull window: Window) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.TRANSPARENT
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    fun transparentStatusBarAbove19(@NonNull window: Window) {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun unTransparentStatusBarAbove21(@NonNull window: Window) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        var ui = window.decorView.systemUiVisibility
        ui = ui and (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE).inv()
        window.decorView.systemUiVisibility = ui
        window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    fun unTransparentStatusBarAbove19(@NonNull window: Window) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }
}