package com.bmd.bmd_ui.view.drawabletextview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import androidx.annotation.IntDef
import androidx.appcompat.widget.AppCompatTextView
import com.bmd.bmd_ui.R

/**
 *@author:       Bigmendan
 *@description:  实现 TextView 添加图片并可配置大小
 *               唉 有materialButton  还弄你干个甚 既生瑜何生亮
 *@create:       2022-02-23 13:53
 */
class DrawableTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs) {

    private val TAG = "DrawableTextView"

    @IntDef(
        DrawableLocation.TOP,
        DrawableLocation.BOTTOM,
        DrawableLocation.LEFT,
        DrawableLocation.RIGHT
    )

    @Retention(AnnotationRetention.SOURCE)
    annotation class DrawableLocation {
        companion object {
            const val TOP = 0
            const val LEFT = 1
            const val RIGHT = 2
            const val BOTTOM = 3
        }
    }

    @DrawableLocation
    private var mDrawableLocation: Int = DrawableLocation.RIGHT

    var mDrawableRes = -1
        set

    private var mDrawableWidth: Int = 0
    private var mDrawableHeight: Int = 0

    private var mDrawableMarginLeft: Int = 10
    private var mDrawableMarginRight: Int = 10
    private var mDrawableMarginTop: Int = 10
    private var mDrawableMarginBottom: Int = 10


    init {
        initAttrs(attrs)
        initView()
    }

    private fun initAttrs(attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.DrawableTextView)

        mDrawableRes = ta.getResourceId(R.styleable.DrawableTextView_drawable_res, mDrawableRes)

        mDrawableWidth = ta.getDimension(R.styleable.DrawableTextView_drawable_width, mDrawableWidth.toFloat()).toInt()
        mDrawableHeight =
            ta.getDimension(R.styleable.DrawableTextView_drawable_height, mDrawableHeight.toFloat()).toInt()

        mDrawableMarginLeft =
            ta.getDimensionPixelSize(R.styleable.DrawableTextView_drawable_marginLeft, mDrawableMarginLeft)
        mDrawableMarginRight =
            ta.getDimensionPixelSize(R.styleable.DrawableTextView_drawable_marginRight, mDrawableMarginRight)
        mDrawableMarginTop =
            ta.getDimensionPixelSize(R.styleable.DrawableTextView_drawable_marginTop, mDrawableMarginTop)
        mDrawableMarginBottom =
            ta.getDimensionPixelSize(R.styleable.DrawableTextView_drawable_marginBottom, mDrawableMarginBottom)

        mDrawableLocation = ta.getInt(R.styleable.DrawableTextView_drawableLocation, mDrawableLocation)



        ta.recycle()

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initView() {
        val drawable: Drawable
        if (mDrawableRes > 0) {
            drawable = context.resources.getDrawable(mDrawableRes, null)
            if (mDrawableWidth != 0 && mDrawableHeight != 0) {

                // 动态设置图片的尺寸
                drawable.setBounds(0, 0, mDrawableWidth, mDrawableHeight)

            } else {

                // 如果没有设置宽高, 设置默认值啊得
                drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight)
            }


            when (mDrawableLocation) {
                DrawableLocation.TOP -> {
                    compoundDrawablePadding = mDrawableMarginTop
                    setCompoundDrawables(null, drawable, null, null)
                }
                DrawableLocation.LEFT -> {
                    compoundDrawablePadding = mDrawableMarginLeft
                    Log.e(TAG, "initView: $mDrawableMarginLeft")
                    setCompoundDrawables(drawable, null, null, null)
                }
                DrawableLocation.RIGHT -> {
                    compoundDrawablePadding = mDrawableMarginRight
                    setCompoundDrawables(null, null, drawable, null)
                }
                DrawableLocation.BOTTOM -> {
                    compoundDrawablePadding = mDrawableMarginBottom
                    setCompoundDrawables(null, null, null, drawable)
                }
            }

        }
    }


}