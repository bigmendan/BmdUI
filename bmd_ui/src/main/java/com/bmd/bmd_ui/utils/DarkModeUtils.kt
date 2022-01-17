package com.bmd.bmd_ui.utils

import android.os.Build
import android.view.View
import android.view.Window
import androidx.annotation.NonNull

/**
 *@author:       Bigmendan
 *@description:  状态栏颜色设置
 *@create:       2022-01-17 17:10
 */
object DarkModeUtils {
    fun isDarkIconMode(@NonNull window: Window): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val ui = window.decorView.systemUiVisibility
            val flag = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            return flag == flag and ui
        }
        return false
    }

    fun setDarkIconMode(@NonNull window: Window, darkIconMode: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val ui = window.decorView.systemUiVisibility

            if (darkIconMode) {
                window.decorView.systemUiVisibility = ui or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                window.decorView.systemUiVisibility = ui and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
        }
    }
}