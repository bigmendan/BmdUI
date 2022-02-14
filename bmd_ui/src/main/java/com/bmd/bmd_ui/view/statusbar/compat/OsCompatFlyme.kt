package com.bmd.bmd_ui.view.statusbar.compat

import android.app.Activity
import android.os.Build
import android.view.Window
import android.view.WindowManager
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import com.bmd.bmd_ui.view.statusbar.utils.DarkModeUtils
import java.lang.reflect.Method


/**
 *@author:       Bigmendan
 *@description:  魅族手机状态栏适配
 *@create:       2022-01-17 17:26
 */

/**
 * 描述：
 *
 * @author Cuizhen
 * @date 2019/3/1
 */
class OsCompatFlyme : OsCompat {

    override fun isDarkIconMode(@NonNull fragment: Fragment): Boolean {
        val activity: Activity = fragment.activity ?: return false
        return FlymeStatusBarUtils.isStatusBarDarkIcon(activity)
    }

    override fun isDarkIconMode(@NonNull activity: Activity): Boolean {
        return FlymeStatusBarUtils.isStatusBarDarkIcon(activity)
    }

    override fun isDarkIconMode(@NonNull window: Window): Boolean {
        return FlymeStatusBarUtils.isStatusBarDarkIcon(window)
    }

    override fun setDarkIconMode(@NonNull fragment: Fragment, darkIconMode: Boolean) {
        val activity: Activity = fragment.getActivity() ?: return
        FlymeStatusBarUtils.setStatusBarDarkIcon(activity, darkIconMode)
    }

    override fun setDarkIconMode(@NonNull activity: Activity, darkIconMode: Boolean) {
        FlymeStatusBarUtils.setStatusBarDarkIcon(activity, darkIconMode)
    }

    override fun setDarkIconMode(@NonNull window: Window, darkIconMode: Boolean) {
        FlymeStatusBarUtils.setStatusBarDarkIcon(window, darkIconMode)
    }

    private object FlymeStatusBarUtils {
        fun isStatusBarDarkIcon(activity: Activity): Boolean {
            return isStatusBarDarkIcon(activity.window)
        }

        fun setStatusBarDarkIcon(activity: Activity, dark: Boolean) {
            if (MethodHolder.mSetStatusBarDarkIcon != null) {
                try {
                    MethodHolder.mSetStatusBarDarkIcon!!.invoke(activity, dark)
                } catch (e: Exception) {
                    setStatusBarDarkIcon(activity.window, dark)
                }
            } else {
                setStatusBarDarkIcon(activity.window, dark)
            }
        }

        fun isStatusBarDarkIcon(window: Window): Boolean {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                DarkModeUtils.isDarkIconMode(window)
            } else {
                isMeizuStatusBarDarkIcon(window.attributes)
            }
        }

        fun setStatusBarDarkIcon(window: Window, dark: Boolean) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                DarkModeUtils.setDarkIconMode(window, dark)
            } else {
                setMeizuStatusBarDarkIcon(window.attributes, dark)
            }
        }

        private fun isMeizuStatusBarDarkIcon(winParams: WindowManager.LayoutParams): Boolean {
            try {
                val f = winParams.javaClass.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
                f.isAccessible = true
                val darkFlag = f.getInt(winParams)
                val f2 = winParams.javaClass.getDeclaredField("meizuFlags")
                f2.isAccessible = true
                val meizuFlags = f2.getInt(winParams)
                return darkFlag == darkFlag and meizuFlags
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            } catch (e: Throwable) {
                e.printStackTrace()
            }
            return false
        }

        private fun setMeizuStatusBarDarkIcon(winParams: WindowManager.LayoutParams, dark: Boolean) {
            try {
                val f = winParams.javaClass.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
                f.isAccessible = true
                val bits = f.getInt(winParams)
                val f2 = winParams.javaClass.getDeclaredField("meizuFlags")
                f2.isAccessible = true
                var meizuFlags = f2.getInt(winParams)
                val oldFlags = meizuFlags
                meizuFlags = if (dark) {
                    meizuFlags or bits
                } else {
                    meizuFlags and bits.inv()
                }
                if (oldFlags != meizuFlags) {
                    f2.setInt(winParams, meizuFlags)
                }
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }

        private object MethodHolder {
            var mSetStatusBarDarkIcon: Method? = null

            init {
                try {
                    mSetStatusBarDarkIcon =
                        Activity::class.java.getMethod("setStatusBarDarkIcon", Boolean::class.javaPrimitiveType)
                } catch (e: NoSuchMethodException) {
                    e.printStackTrace()
                }
            }
        }
    }
}