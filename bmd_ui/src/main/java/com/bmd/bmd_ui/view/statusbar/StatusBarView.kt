package com.bmd.bmd_ui.view.statusbar

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.bmd.bmd_ui.view.statusbar.compat.StatusBarCompat

/**
 *@author:       Bigmendan
 *@description:  android自定义状态栏
 *@create:       2022-01-17 16:55
 */
class StatusBarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height: Int = StatusBarCompat.getHeight(context)
        setMeasuredDimension(width, height)
    }

    fun isVisibility(): Boolean {
        return visibility == VISIBLE
    }

    fun setVisibility(visible: Boolean) {
        visibility = if (visible) VISIBLE else GONE
    }
}