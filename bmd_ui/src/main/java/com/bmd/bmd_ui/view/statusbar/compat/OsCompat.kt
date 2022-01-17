package com.bmd.bmd_ui.view.statusbar.compat

import android.app.Activity
import android.view.Window
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment

/**
 *@author:       Bigmendan
 *@description:
 *@create:       2022-01-17 17:21
 */
interface OsCompat {
    fun isDarkIconMode(@NonNull fragment: Fragment): Boolean
    fun isDarkIconMode(@NonNull activity: Activity): Boolean
    fun isDarkIconMode(@NonNull window: Window): Boolean
    fun setDarkIconMode(@NonNull fragment: Fragment, darkIconMode: Boolean)
    fun setDarkIconMode(@NonNull activity: Activity, darkIconMode: Boolean)
    fun setDarkIconMode(@NonNull window: Window, darkIconMode: Boolean)
}