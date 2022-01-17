package com.bmd.bmd_ui.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.Window
import androidx.core.graphics.ColorUtils
import com.bmd.bmd_ui.view.statusbar.compat.StatusBarCompat

/**
 *@author:       Bigmendan
 *@description:
 *@create:       2022-01-17 17:16
 */
object LuminanceUtils {
    fun isLight(luminance: Double): Boolean {
        return luminance >= 0.382
    }

    fun calcLuminance(color: Int): Double {
        return ColorUtils.calculateLuminance(color)
    }

    fun calcLuminanceByCapture(window: Window): Double {
        val bitmap = captureStatusBar(window)
        val luminance = calcBitmapLuminance(bitmap)
        bitmap.recycle()
        return luminance
    }

    private fun captureStatusBar(window: Window): Bitmap {
        val decor = window.decorView
        val w = decor.measuredWidth
        val h: Int = StatusBarCompat.getHeight(window.context)
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565)
        val canvas = Canvas(bitmap)
        decor.draw(canvas)
        return bitmap
    }

    private fun calcBitmapLuminance(bitmap: Bitmap?): Double {
        if (bitmap == null) return 0.0
        if (bitmap.isRecycled) return 0.0
        val w = bitmap.width
        val h = bitmap.height
        if (w == 0 || h == 0) return 0.0
        val y_m = h / 2
        val x_m = w / 2
        var light = 0
        var dark = 0
        for (x in 0 until w) {
            val f = x.toFloat() / w.toFloat()
            val y_t2b = (h * f).toInt()
            val y_b2t = h - y_t2b - 1
            if (isLight(bitmap.getPixel(x, y_t2b))) ++light else ++dark
            if (isLight(bitmap.getPixel(x, y_b2t))) ++light else ++dark
            if (isLight(bitmap.getPixel(x, y_m))) ++light else ++dark
            if (x == x_m) for (y in 0 until h) if (isLight(bitmap.getPixel(x, y))) ++light else ++dark
        }
        return if (dark > light) 0.0 else 1.0
    }

    private fun isLight(color: Int): Boolean {
        return isLight(calcLuminance(color))
    }
}