package com.bmd.bmd_ui.view.statusbar.compat

import android.app.Activity
import android.os.Build
import android.view.Window
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import com.bmd.bmd_ui.utils.DarkModeUtils

/**
 *@author:       Bigmendan
 *@description:
 *@create:       2022-01-17 17:28
 */

class OsCompatMiui : OsCompat {
    override fun isDarkIconMode(@NonNull fragment: Fragment): Boolean {
        val activity: Activity = fragment.getActivity() ?: return false
        return isDarkIconMode(activity)
    }

    override fun isDarkIconMode(@NonNull activity: Activity): Boolean {
        val window = activity.window ?: return false
        return isDarkIconMode(window)
    }

    override fun isDarkIconMode(window: Window): Boolean {
        return isDarkIconMode(window)
    }


    override fun setDarkIconMode(@NonNull fragment: Fragment, darkIconMode: Boolean) {
        val activity: Activity = fragment.getActivity() ?: return
        setDarkIconMode(activity, darkIconMode)
    }

    override fun setDarkIconMode(@NonNull activity: Activity, darkIconMode: Boolean) {
        val window = activity.window ?: return
        setDarkIconMode(window, darkIconMode)
    }


    override fun setDarkIconMode(@NonNull window: Window, darkIconMode: Boolean) {
        setDarkIconMode(window, darkIconMode)
    }

    private object MiuiStatusBarUtils {
        private fun isDarkIconMode(window: Window): Boolean {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                DarkModeUtils.isDarkIconMode(window)
            } else {
                isMiuiDarkIconMode(window)
            }
        }

        private fun isMiuiDarkIconMode(window: Window): Boolean {
            val clazz: Class<out Window> = window.javaClass
            try {
                val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
                val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
                val darkModeFlag = field.getInt(layoutParams)
                val extraFlagField = clazz.getMethod("getExtraFlags")
                val miuiFlags = extraFlagField.invoke(window) as Int
                return darkModeFlag == darkModeFlag and miuiFlags
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false
        }

        private fun setDarkIconMode(window: Window, darkIconMode: Boolean) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                DarkModeUtils.setDarkIconMode(window, darkIconMode)
            } else {
                setMiuiDarkIconMode(
                    window,
                    darkIconMode
                )
            }
        }

        private fun setMiuiDarkIconMode(window: Window, darkIconMode: Boolean) {
            val clazz: Class<out Window> = window.javaClass
            try {
                val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
                val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
                val darkModeFlag = field.getInt(layoutParams)
                val extraFlagField = clazz.getMethod(
                    "setExtraFlags",
                    Int::class.javaPrimitiveType,
                    Int::class.javaPrimitiveType
                )
                extraFlagField.invoke(window, if (darkIconMode) darkModeFlag else 0, darkModeFlag)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}