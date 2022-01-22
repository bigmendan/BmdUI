package com.bmd.bmd_ui.view.actionbar

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.CallSuper
import androidx.annotation.ColorInt
import androidx.annotation.IdRes
import androidx.annotation.IntDef
import androidx.appcompat.app.AppCompatActivity
import com.bmd.bmd_ui.R
import com.bmd.bmd_ui.utils.ContextUtils
import com.bmd.bmd_ui.view.statusbar.StatusBarView
import com.bmd.bmd_ui.view.statusbar.compat.StatusBarCompat

/**
 *@author:       Bigmendan
 *@description:  高拓展性和定制性的ActionBar 整个ActionBar分为3层：
 *               ----BackgroundLayer 背景层：可自定义布局
 *               ----ActionBar 主体层：改层为垂直线性布局，包含下面三个部分：
 *                      |--------StatusBar：系统状态栏
 *                      |--------TitleBar：位于StatusBar和BottomLine之间，可自定义布局
 *                      |--------BottomLine：分割线
 *               ----ForegroundLayer 前景层：可自定义布局
 *
 *               三层结构已经布好，可以继承 BmdActionBar 自定义ActionBar
 *
 *@create:       2022-01-17 16:49
 *
 */
open class BmdActionBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val activity = ContextUtils.getActivity(context)

    @IntDef(
        StatusBarMode.UNCHANGED,
        StatusBarMode.LIGHT,
        StatusBarMode.DARK,
        StatusBarMode.AUTO,
        StatusBarMode.REAL_TIME
    )
    @Retention(AnnotationRetention.SOURCE)
    annotation class StatusBarMode {
        companion object {
            const val UNCHANGED = 0
            const val LIGHT = 1
            const val DARK = 2
            const val AUTO = 3
            const val REAL_TIME = 4
        }
    }

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(StatusBarVisible.AUTO, StatusBarVisible.VISIBLE, StatusBarVisible.GONE)
    annotation class StatusBarVisible {
        companion object {
            const val AUTO = 0
            const val VISIBLE = 1
            const val GONE = 2
        }
    }

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(Immersion.UNCHANGED, Immersion.ORDINARY, Immersion.IMMERSED)
    annotation class Immersion {
        companion object {
            const val UNCHANGED = 0
            const val ORDINARY = 1
            const val IMMERSED = 2
        }
    }

    @Immersion
    private var mImmersion: Int = Immersion.UNCHANGED

    @StatusBarVisible
    private var mStatusBarVisible: Int = StatusBarVisible.AUTO

    @StatusBarMode
    private var mStatusBarMode: Int = StatusBarMode.UNCHANGED

    @ColorInt
    private var mStatusBarColor = Color.TRANSPARENT
    private var mTitleBarHeight = -1
    protected var titleBarRes = 0
        private set

    @ColorInt
    private var mBottomLineColor = Color.TRANSPARENT
    private var mBottomLineResId = 0
    private var mBottomLineHeight = 0
    private var mBottomLineOutside = false
    private var mForegroundLayerLayoutRes = 0
    private var mBackgroundLayerLayoutRes = 0
    private var mBackgroundLayerImageRes = 0
    private var mClickToFinishViewId = 0
    private var mActivity: Activity? = null

    var actionBar: LinearLayout? = null
        private set
    private var mStatusBar: StatusBarView? = null

    var titleBar: FrameLayout? = null
        private set

    var bottomLine: View? = null
        private set

    var foregroundLayer: View? = null
        private set

    var backgroundLayer: View? = null
        private set

    private var views: SparseArray<View?>? = null

    init {
        hintSystemActionBar()
        initAttrs(attrs)
        initView()
        refresh()
    }

    @CallSuper
    protected fun initAttrs(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.BmdActionBar)
        mImmersion = typedArray.getInt(R.styleable.BmdActionBar_ab_immersion, mImmersion)

        mBackgroundLayerLayoutRes =
            typedArray.getResourceId(R.styleable.BmdActionBar_ab_backgroundLayerLayout, mBackgroundLayerLayoutRes)

        mBackgroundLayerImageRes =
            typedArray.getResourceId(R.styleable.BmdActionBar_ab_backgroundLayerImageRes, mBackgroundLayerLayoutRes)
        mStatusBarVisible = typedArray.getInt(R.styleable.BmdActionBar_ab_statusBarVisible, mStatusBarVisible)
        mStatusBarMode = typedArray.getInt(R.styleable.BmdActionBar_ab_statusBarMode, mStatusBarMode)

        mStatusBarColor = typedArray.getColor(R.styleable.BmdActionBar_ab_statusBarColor, mStatusBarColor)

        mTitleBarHeight =
            typedArray.getDimension(R.styleable.BmdActionBar_ab_titleBarHeight, mTitleBarHeight.toFloat()).toInt()
        titleBarRes = typedArray.getResourceId(R.styleable.BmdActionBar_ab_titleBarLayout, titleBarRes)

        mBottomLineHeight =
            typedArray.getDimension(R.styleable.BmdActionBar_ab_bottomLineHeight, mBottomLineHeight.toFloat())
                .toInt()

        mBottomLineColor = typedArray.getColor(R.styleable.BmdActionBar_ab_bottomLineColor, mBottomLineColor)
        mBottomLineResId = typedArray.getResourceId(R.styleable.BmdActionBar_ab_bottomLineResId, mBottomLineResId)
        mBottomLineOutside = typedArray.getBoolean(R.styleable.BmdActionBar_ab_bottomLineOutside, mBottomLineOutside)
        mForegroundLayerLayoutRes =
            typedArray.getResourceId(R.styleable.BmdActionBar_ab_foregroundLayerLayout, mForegroundLayerLayoutRes)
        mClickToFinishViewId =
            typedArray.getResourceId(R.styleable.BmdActionBar_ab_clickToFinish, mClickToFinishViewId)
        typedArray.recycle()
    }

    @CallSuper
    protected fun initView() {
        // 1 初始化BackgroundLayer
        if (mBackgroundLayerLayoutRes > 0) {
            backgroundLayer = inflate(context, mBackgroundLayerLayoutRes, null)
            addViewInLayout(backgroundLayer, childCount, makeLayerLayoutParamsMatch(), true)
        } else {
            if (mBackgroundLayerImageRes > 0) {
                val actionBarImageView = ImageView(context)
                backgroundLayer = actionBarImageView
                addViewInLayout(backgroundLayer, childCount, makeLayerLayoutParamsMatch(), true)
                actionBarImageView.setImageResource(mBackgroundLayerImageRes)
                actionBarImageView.scaleType = ImageView.ScaleType.FIT_XY
            }
        }

        // 2 初始ActionBarLayer
        actionBar = inflate(context, R.layout.bmd_action_bar, null) as LinearLayout
        addViewInLayout(actionBar, childCount, makeLayerLayoutParamsWrap(), true)

        // 2.1 初始StatusBar
        mStatusBar = actionBar!!.findViewById(R.id.bmd_action_status_bar)

        // 2.2 初始TitleBar
        titleBar = actionBar!!.findViewById(R.id.bmd_action_title_bar)
        titleBar!!.isClickable = true
        titleBar!!.isFocusable = true
        titleBar!!.isFocusableInTouchMode = true

        if (mTitleBarHeight >= 0) {
            titleBar!!.layoutParams.height = mTitleBarHeight
        }
        setTitleBarChild(inflateTitleBar())

        // 2.3 初始BottomLine
        bottomLine = actionBar!!.findViewById(R.id.bmd_action_bottom_line)
        bottomLine!!.layoutParams.height = mBottomLineHeight
        if (mBottomLineResId > 0) {
            bottomLine!!.setBackgroundResource(mBottomLineResId)
        } else {
            bottomLine!!.setBackgroundColor(mBottomLineColor)
        }
        if (mBottomLineOutside) {
            actionBar!!.clipChildren = false
            clipChildren = false
        }

        // 3 初始ForegroundLayer
        if (mForegroundLayerLayoutRes > 0) {
            foregroundLayer = inflate(context, mForegroundLayerLayoutRes, null)
            addViewInLayout(foregroundLayer, childCount, makeLayerLayoutParamsMatch(), true)
        }
        performClickToFinish()
    }

     open fun inflateTitleBar(): View? {
        return if (titleBarRes > 0) {
            LayoutInflater.from(context).inflate(titleBarRes, titleBar, false)
        } else null
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (mBottomLineOutside) {
            val parent = parent
            if (parent is ViewGroup) {
                parent.clipChildren = false
            }
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        for (i in childCount - 1 downTo 0) {
            val child = getChildAt(i)
            if (child === foregroundLayer) {
                continue
            }
            if (child === actionBar) {
                continue
            }
            if (child === backgroundLayer) {
                continue
            }
            removeView(child)
            titleBar!!.addView(child, 0)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        actionBar!!.measure(widthMeasureSpec, heightMeasureSpec)
        val width = actionBar!!.measuredWidth
        val height: Int = if (mBottomLineOutside) {
            mStatusBar!!.measuredHeight + titleBar!!.measuredHeight
        } else {
            actionBar!!.measuredHeight
        }
        val widthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY)
        val heightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        super.onMeasure(widthSpec, heightSpec)
    }

    val statusBar: StatusBarView?
        get() = mStatusBar

    private fun setTitleBarChild(titleBarChild: View?) {
        titleBar!!.removeAllViewsInLayout()
        if (titleBarChild != null) {
            var titleBarChildParams = titleBarChild.layoutParams
            if (titleBarChildParams == null) {
                titleBarChildParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            }
            if (mTitleBarHeight >= 0) {
                titleBarChildParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            }
            titleBar!!.addView(titleBarChild, titleBarChildParams)
        }
    }

    fun <V : View?> getView(@IdRes id: Int): V? {
        if (views == null) {
            views = SparseArray()
        }
        var view = views!![id]
        if (view == null) {
            view = findViewById(id)
            views!!.put(id, view)
        }
        return view as V?
    }

    fun setImmersion(@Immersion immersion: Int) {
        mImmersion = immersion
    }

    fun setStatusBarVisible(@StatusBarVisible statusBarVisible: Int) {
        mStatusBarVisible = statusBarVisible
    }

    fun setStatusBarMode(@StatusBarMode statusBarMode: Int) {
        mStatusBarMode = statusBarMode
    }

    fun setStatusBarColor(@ColorInt statusBarColor: Int) {
        mStatusBarColor = statusBarColor
    }

    fun refresh() {
        refreshImmersion()
        refreshStatusBarVisible()
        refreshStatusBarMode()
        refreshStatusBarColor()
    }

    fun refreshImmersion() {
        when (mImmersion) {
            Immersion.ORDINARY -> StatusBarCompat.unTransparent(
                context
            )
            Immersion.IMMERSED -> StatusBarCompat.transparent(context)
            Immersion.UNCHANGED -> {}
            else -> {}
        }
    }

    fun refreshStatusBarVisible() {
        when (mStatusBarVisible) {
            StatusBarVisible.AUTO -> mStatusBar!!.setVisibility(
                StatusBarCompat.isTransparent(
                    context
                )
            )
            StatusBarVisible.VISIBLE -> mStatusBar!!.setVisibility(true)
            StatusBarVisible.GONE -> mStatusBar!!.setVisibility(false)
            else -> {}
        }
    }

    fun refreshStatusBarMode() {
        StatusBarCompat.unregisterToAutoChangeIconMode(context)
        when (mStatusBarMode) {
            StatusBarMode.LIGHT -> StatusBarCompat.setIconMode(
                context, false
            )
            StatusBarMode.DARK -> StatusBarCompat.setIconMode(
                context,
                true
            )
            StatusBarMode.AUTO -> refreshStatusBarModeAuto()
            StatusBarMode.REAL_TIME -> StatusBarCompat.registerToAutoChangeIconMode(
                context
            )
            StatusBarMode.UNCHANGED -> {}
            else -> {}
        }
    }

    private fun refreshStatusBarModeAuto() {
        post { StatusBarCompat.setIconModeAuto(context) }
    }

    fun refreshStatusBarColor() {
        mStatusBar!!.setBackgroundColor(mStatusBarColor)
        if (StatusBarCompat.isTransparent(context) && mStatusBar!!.isVisibility()) {
            StatusBarCompat.setColor(context, Color.TRANSPARENT)
        } else {
            StatusBarCompat.setColor(context, mStatusBarColor)
        }
    }

    val isStatusBarIconDark: Boolean
        get() = StatusBarCompat.isIconDark(context)
    val isStatusBarBgLight: Boolean
        get() = StatusBarCompat.isBgLight(context)

    fun calculateStatusBarBgLuminance(): Double {
        return StatusBarCompat.calcBgLuminance(context)
    }

    fun finishActivity() {
        val activity = activity
        if (activity != null && !activity.isFinishing) {
            activity.finish()
        }
    }

    private fun performClickToFinish() {
        val clickToFinishView = getView<View>(mClickToFinishViewId) ?: return
        clickToFinishView.setOnClickListener { finishActivity() }
    }

    private fun makeLayerLayoutParamsWrap(): LayoutParams {
        return LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    }

    private fun makeLayerLayoutParamsMatch(): LayoutParams {
        return LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    }

    private fun hintSystemActionBar() {
        val activity = activity ?: return
        if (activity.actionBar != null) {
            activity.actionBar!!.hide()
        }
        if (activity is AppCompatActivity) {
            val compatActivity: AppCompatActivity = activity as AppCompatActivity
            if (compatActivity.supportActionBar != null) {
                compatActivity.supportActionBar!!.hide()
            }

            ContextUtils
        }
    }


}