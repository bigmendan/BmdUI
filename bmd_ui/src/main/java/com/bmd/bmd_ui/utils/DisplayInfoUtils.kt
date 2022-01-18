package com.bmd.bmd_ui.utils

import com.bmd.bmd_ui.base.GlobalApp.application
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Point
import android.util.DisplayMetrics
import com.bmd.bmd_ui.utils.DisplayInfoUtils
import com.bmd.bmd_ui.base.GlobalApp
import android.view.WindowManager
import android.view.Display
import java.lang.Exception

/**
 * 显示相关帮助类
 * 1.获取屏幕宽度
 * 2.获取屏幕高度
 * 3.获取屏幕密度相关
 * 4.获取状态栏高度
 * 5.dp/px/sp相互转换
 */
@SuppressLint("StaticFieldLeak")
class DisplayInfoUtils private constructor() {
    private val mContext: Context?
    private val mDisplayMetrics: DisplayMetrics

    companion object {
        val instance = DisplayInfoUtils()
    }

    init {
        mContext = application
        mDisplayMetrics = mContext!!.resources.displayMetrics
    }

    /**
     * 获取屏幕宽度像素
     *
     * @return px
     */
    val widthPixels: Int
        get() = mDisplayMetrics.widthPixels

    /**
     * 获取屏幕高度像素
     *
     * @return px
     */
    val heightPixels: Int
        get() = mDisplayMetrics.heightPixels

    /**
     * 获取屏幕像素密度(每英寸多少像素)
     *
     * @return dpi
     */
    val densityDpi: Int
        get() = mDisplayMetrics.densityDpi

    /**
     * 获取屏幕密度(像素密度/160)
     *
     * @return float
     */
    val density = mDisplayMetrics.density

    /**
     * 字体缩放比例（一般和屏幕密度相等）
     *
     * @return float
     */
    val scaledDensity = mDisplayMetrics.scaledDensity

    /**
     * X方向的像素密度
     *
     * @return dpi
     */
    val xdpi = mDisplayMetrics.xdpi

    /**
     * Y方向的像素密度
     *
     * @return dpi
     */
    val ydpi = mDisplayMetrics.ydpi

    /**
     * 获取状态栏高度像素
     *
     * @return px
     */
    val statusBarHeight: Int
        get() {
            val resourceId = mContext!!.resources.getIdentifier("status_bar_height", "dimen", "android")
            return if (resourceId > 0) {
                mContext.resources.getDimensionPixelSize(resourceId)
            } else 0
        }

    /**
     * 检测是否有虚拟导航栏
     */
    fun hasNavigationBar(): Boolean {
        var hasNavigationBar = false
        val rs = mContext!!.resources
        val id = rs.getIdentifier("config_showNavigationBar", "bool", "android")
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id)
        }
        try {
            val systemPropertiesClass = Class.forName("android.os.SystemProperties")
            val m = systemPropertiesClass.getMethod("get", String::class.java)
            val navBarOverride = m.invoke(systemPropertiesClass, "qemu.hw.mainkeys") as String
            if ("1" == navBarOverride) {
                hasNavigationBar = false
            } else if ("0" == navBarOverride) {
                hasNavigationBar = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return hasNavigationBar
    }

    /**
     * 获取导航栏高度
     */
    val navigationBarHeight: Int
        get() {
            val rid = mContext!!.resources.getIdentifier("config_showNavigationBar", "bool", "android")
            if (rid != 0) {
                val resourceId = mContext.resources.getIdentifier("navigation_bar_height", "dimen", "android")
                if (resourceId > 0) {
                    return mContext.resources.getDimensionPixelSize(resourceId)
                }
            }
            return 0
        }
    val appUsableScreenSize: Point
        get() {
            val windowManager: WindowManager
            windowManager = mContext!!.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val size = Point(0, 0)
            if (windowManager != null) {
                val display = windowManager.defaultDisplay
                display.getSize(size)
            }
            return size
        }

    /**
     * dp: dip，Density-independent pixel(设备独立像素), 是安卓开发用的长度单位，1dp表示在屏幕像素点密度为160ppi时1px长度
     * px: pixel，像素，电子屏幕上组成一幅图画或照片的最基本单元
     * dp转px
     *
     * @param dp dp
     * @return px
     */
    fun dp2px(dp: Float): Float {
        return dp * density
    }

    /**
     * px: pixel，像素，电子屏幕上组成一幅图画或照片的最基本单元
     * dp: dip，Density-independent pixel(设备独立像素), 是安卓开发用的长度单位，1dp表示在屏幕像素点密度为160ppi时1px长度
     * px转dp
     *
     * @param px px
     * @return dp
     */
    fun px2dp(px: Float): Float {
        return px / density
    }

    /**
     * sp: scale-independent pixel，放大的像素，安卓开发用的字体大小单位
     * px: pixel，像素，电子屏幕上组成一幅图画或照片的最基本单元
     * sp转px
     *
     * @param sp sp
     * @return px
     */
    fun sp2px(sp: Float): Float {
        return sp * scaledDensity
    }

    /**
     * px: pixel，像素，电子屏幕上组成一幅图画或照片的最基本单元
     * sp: scale-independent pixel，放大的像素，安卓开发用的字体大小单位
     * px转sp
     *
     * @param px px
     * @return sp
     */
    fun px2sp(px: Float): Float {
        return px / scaledDensity
    }

    /**
     * dp: dip，Density-independent pixel(设备独立像素), 是安卓开发用的长度单位，1dp表示在屏幕像素点密度为160ppi时1px长度
     * sp: scale-independent pixel，放大的像素，安卓开发用的字体大小单位
     * dp转sp
     *
     * @param dp dp
     * @return sp
     */
    fun dp2sp(dp: Float): Float {
        return dp * density / scaledDensity
    }

    /**
     * sp: scale-independent pixel，放大的像素，安卓开发用的字体大小单位
     * dp: dip，Density-independent pixel(设备独立像素), 是安卓开发用的长度单位，1dp表示在屏幕像素点密度为160ppi时1px长度
     * sp转dp
     *
     * @param sp sp
     * @return dp
     */
    fun sp2dp(sp: Float): Float {
        return sp * scaledDensity / density
    }


}