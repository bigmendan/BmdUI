package com.bmd.bmd_ui.view.statusbar.compat

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.View
import android.view.ViewTreeObserver
import android.view.Window
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import com.bmd.bmd_ui.utils.ContextUtils
import com.bmd.bmd_ui.utils.LuminanceUtils
import com.bmd.bmd_ui.utils.OsCompatHolder
import com.bmd.bmd_ui.utils.TransparentUtils

/**
 *@author:       Bigmendan
 *@description:  状态栏处理
 *@create:       2022-01-17 16:57
 */

object StatusBarCompat {
    fun getHeight(@NonNull context: Context): Int {
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        return try {
            context.resources.getDimensionPixelSize(resourceId)
        } catch (e: Exception) {
            0
        }
    }

    fun setColor(@NonNull fragment: Fragment, @ColorInt color: Int) {
        val activity: Activity = fragment.activity ?: return
        setColor(activity, color)
    }

    fun setColor(@NonNull context: Context?, @ColorInt color: Int) {
        val activity: Activity = ContextUtils.getActivity(context) ?: return
        setColor(activity, color)
    }

    fun setColor(@NonNull activity: Activity, @ColorInt color: Int) {
        val window = activity.window ?: return
        setColor(window, color)
    }

    fun setColor(@NonNull window: Window, @ColorInt color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = color
        }
    }

    fun isBgLight(@NonNull fragment: Fragment): Boolean {
        val activity: Activity = fragment.activity ?: return false
        return isBgLight(activity)
    }

    fun isBgLight(@NonNull context: Context?): Boolean {
        val activity: Activity = ContextUtils.getActivity(context) ?: return false
        return isBgLight(activity)
    }

    fun isBgLight(@NonNull activity: Activity): Boolean {
        val window = activity.window ?: return false
        return isBgLight(window)
    }

    fun isBgLight(@NonNull window: Window): Boolean {
        return LuminanceUtils.isLight(calcBgLuminance(window))
    }

    fun calcBgLuminance(@NonNull fragment: Fragment): Double {
        val activity: Activity = fragment.getActivity() ?: return 0.0
        return calcBgLuminance(activity)
    }

    fun calcBgLuminance(@NonNull context: Context?): Double {
        val activity: Activity = ContextUtils.getActivity(context) ?: return 0.0
        return calcBgLuminance(activity)
    }

    fun calcBgLuminance(@NonNull activity: Activity): Double {
        val window = activity.window ?: return 0.0
        return calcBgLuminance(window)
    }

    fun calcBgLuminance(@NonNull window: Window): Double {
        if (isTransparent(window)) {
            return LuminanceUtils.calcLuminanceByCapture(window)
        }
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            LuminanceUtils.calcLuminance(window.statusBarColor)
        } else 0.0
    }

    fun isIconDark(@NonNull fragment: Fragment): Boolean {
        return OsCompatHolder.get().isDarkIconMode(fragment)
    }

    fun isIconDark(@NonNull context: Context?): Boolean {
        val activity: Activity = ContextUtils.getActivity(context) ?: return false
        return OsCompatHolder.get().isDarkIconMode(activity)
    }

    fun isIconDark(@NonNull activity: Activity): Boolean {
        return OsCompatHolder.get().isDarkIconMode(activity)
    }

    fun isIconDark(@NonNull window: Window): Boolean {
        return OsCompatHolder.get().isDarkIconMode(window)
    }

    fun registerToAutoChangeIconMode(@NonNull fragment: Fragment) {
        val activity: Activity = fragment.activity ?: return
        registerToAutoChangeIconMode(activity)
    }

    fun registerToAutoChangeIconMode(@NonNull context: Context?) {
        val activity: Activity = ContextUtils.getActivity(context) ?: return
        registerToAutoChangeIconMode(activity)
    }

    fun registerToAutoChangeIconMode(@NonNull activity: Activity) {
        val window = activity.window ?: return
        registerToAutoChangeIconMode(window)
    }

    fun registerToAutoChangeIconMode(@NonNull window: Window) {
        val decorView = window.decorView
        val tag = decorView.tag
        if (tag is ViewTreeObserver.OnPreDrawListener) {
            decorView.viewTreeObserver.removeOnPreDrawListener(tag)
            decorView.tag = null
        }
        val onPreDrawListener = ViewTreeObserver.OnPreDrawListener {
            setIconModeAuto(window)
            true
        }
        decorView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {}
            override fun onViewDetachedFromWindow(v: View) {
                decorView.removeOnAttachStateChangeListener(this)
                decorView.viewTreeObserver.removeOnPreDrawListener(onPreDrawListener)
            }
        })
        decorView.viewTreeObserver.addOnPreDrawListener(onPreDrawListener)
        decorView.tag = onPreDrawListener
    }

    fun unregisterToAutoChangeIconMode(@NonNull fragment: Fragment) {
        val activity: Activity = fragment.getActivity() ?: return
        unregisterToAutoChangeIconMode(activity)
    }

    fun unregisterToAutoChangeIconMode(@NonNull context: Context?) {
        val activity: Activity = ContextUtils.getActivity(context) ?: return
        unregisterToAutoChangeIconMode(activity)
    }

    fun unregisterToAutoChangeIconMode(@NonNull activity: Activity) {
        val window = activity.window ?: return
        unregisterToAutoChangeIconMode(window)
    }

    fun unregisterToAutoChangeIconMode(@NonNull window: Window) {
        val decorView = window.decorView
        val tag = decorView.tag
        if (tag is ViewTreeObserver.OnPreDrawListener) {
            decorView.viewTreeObserver.removeOnPreDrawListener(tag)
            decorView.tag = null
        }
    }

    fun setIconModeAuto(@NonNull fragment: Fragment) {
        setIconMode(fragment, isBgLight(fragment))
    }

    fun setIconModeAuto(@NonNull context: Context?) {
        setIconMode(context, isBgLight(context))
    }

    fun setIconModeAuto(@NonNull activity: Activity?) {
        setIconMode(activity, isBgLight(activity))
    }

    fun setIconModeAuto(@NonNull window: Window) {
        setIconMode(window, isBgLight(window))
    }

    fun setIconMode(@NonNull fragment: Fragment, darkIconMode: Boolean) {
        OsCompatHolder.get().setDarkIconMode(fragment, darkIconMode)
    }

    fun setIconMode(@NonNull context: Context?, darkIconMode: Boolean) {
        val activity: Activity = ContextUtils.getActivity(context) ?: return
        OsCompatHolder.get().setDarkIconMode(activity, darkIconMode)
    }

    fun setIconMode(@NonNull activity: Activity, darkIconMode: Boolean) {
        OsCompatHolder.get().setDarkIconMode(activity, darkIconMode)
    }

    fun setIconMode(@NonNull window: Window, darkIconMode: Boolean) {
        OsCompatHolder.get().setDarkIconMode(window, darkIconMode)
    }

    fun setIconDark(@NonNull fragment: Fragment) {
        setIconMode(fragment, true)
    }

    fun setIconDark(@NonNull context: Context?) {
        setIconMode(context, true)
    }

    fun setIconDark(@NonNull activity: Activity?) {
        setIconMode(activity, true)
    }

    fun setIconDark(@NonNull window: Window) {
        setIconMode(window, true)
    }

    fun setIconLight(@NonNull fragment: Fragment) {
        setIconMode(fragment, false)
    }

    fun setIconLight(@NonNull context: Context?) {
        setIconMode(context, false)
    }

    fun setIconLight(@NonNull activity: Activity?) {
        setIconMode(activity, false)
    }

    fun setIconLight(@NonNull window: Window) {
        setIconMode(window, false)
    }

    fun isTransparent(@NonNull fragment: Fragment): Boolean {
        val activity: Activity = fragment.activity ?: return false
        return isTransparent(activity)
    }

    fun isTransparent(@NonNull context: Context?): Boolean {
        val activity: Activity = ContextUtils.getActivity(context) ?: return false
        return isTransparent(activity)
    }

    fun isTransparent(@NonNull activity: Activity): Boolean {
        return isTransparent(activity.window)
    }

    fun isTransparent(@NonNull window: Window): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return TransparentUtils.isTransparentStatusBarAbove21(window)
        }
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransparentUtils.isTransparentStatusBarAbove19(window)
        } else false
    }

    fun transparent(@NonNull fragment: Fragment) {
        val activity: Activity = fragment.getActivity() ?: return
        transparent(activity)
    }

    fun transparent(@NonNull context: Context?) {
        val activity: Activity = com.bmd.bmd_ui.utils.ContextUtils.getActivity(context) ?: return
        transparent(activity)
    }

    fun transparent(@NonNull activity: Activity) {
        transparent(activity.window)
    }

    fun transparent(@NonNull window: Window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TransparentUtils.transparentStatusBarAbove21(window)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransparentUtils.transparentStatusBarAbove19(window)
        }
    }

    fun unTransparent(@NonNull fragment: Fragment) {
        val activity: Activity = fragment.activity ?: return
        unTransparent(activity)
    }

    fun unTransparent(@NonNull context: Context?) {
        val activity: Activity = ContextUtils.getActivity(context) ?: return
        unTransparent(activity)
    }

    fun unTransparent(@NonNull activity: Activity) {
        unTransparent(activity.window)
    }

    fun unTransparent(@NonNull window: Window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TransparentUtils.unTransparentStatusBarAbove21(window)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransparentUtils.unTransparentStatusBarAbove19(window)
        }
    }
}