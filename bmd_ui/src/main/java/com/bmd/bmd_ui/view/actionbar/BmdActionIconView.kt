package com.bmd.bmd_ui.view.actionbar

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

/**
 *@author:       Bigmendan
 *@description:   ActionBar 上的图标
 *@create:       2022-01-17 18:47
 */
class BmdActionIconView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatImageView(context, attrs) {


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        setMeasuredDimension(heightSize, heightSize)
    }


}