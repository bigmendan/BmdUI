package com.bmd.bmd_ui.view.statusbar.compat

import android.app.Activity
import android.os.Build
import android.view.Window
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import com.bmd.bmd_ui.view.statusbar.utils.DarkModeUtils

/**
 *@author:       Bigmendan
 *@description:  Oppo设备状态栏处理
 *@create:       2022-01-17 17:31
 */
class OsCompatOppo : OsCompat {

    override fun isDarkIconMode(@NonNull fragment: Fragment): Boolean {
        val activity: Activity = fragment.getActivity() ?: return false
        return isDarkIconMode(activity)
    }

    override fun isDarkIconMode(@NonNull activity: Activity): Boolean {
        val window = activity.window ?: return false
        return isDarkIconMode(window)
    }

    override fun isDarkIconMode(@NonNull window: Window): Boolean {
        return OppoStatusBarUtils.isDarkIconMode(window)
    }

    override fun setDarkIconMode(@NonNull fragment: Fragment, darkIconMode: Boolean) {
        val activity: Activity = fragment.activity ?: return
        setDarkIconMode(activity, darkIconMode)
    }

    override fun setDarkIconMode(@NonNull activity: Activity, darkIconMode: Boolean) {
        val window = activity.window ?: return
        setDarkIconMode(window, darkIconMode)
    }

    override fun setDarkIconMode(@NonNull window: Window, darkIconMode: Boolean) {
        OppoStatusBarUtils.setDarkIconMode(window, darkIconMode)
    }

    private object OppoStatusBarUtils {
        private const val SYSTEM_UI_FLAG_OP_STATUS_BAR_TINT = 0x00000010
        fun isDarkIconMode(window: Window): Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return DarkModeUtils.isDarkIconMode(window)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val vis = window.decorView.systemUiVisibility
                return SYSTEM_UI_FLAG_OP_STATUS_BAR_TINT == SYSTEM_UI_FLAG_OP_STATUS_BAR_TINT and vis
            }
            return false
        }

        fun setDarkIconMode(window: Window, darkMode: Boolean) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                DarkModeUtils.setDarkIconMode(window, darkMode)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                var vis = window.decorView.systemUiVisibility
                vis = if (darkMode) {
                    vis or SYSTEM_UI_FLAG_OP_STATUS_BAR_TINT
                } else {
                    vis and SYSTEM_UI_FLAG_OP_STATUS_BAR_TINT.inv()
                }
                window.decorView.systemUiVisibility = vis
            }
        }
    }
}