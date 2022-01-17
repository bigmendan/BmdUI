package com.bmd.bmd_ui.view.multistateview

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.IntDef
import androidx.annotation.LayoutRes
import androidx.annotation.Nullable
import com.bmd.bmd_ui.R

/**
 * View that contains 4 different states: Content, Error, Empty, and Loading.<br></br>
 * Each state has their own separate layout which can be shown/hidden by setting
 * the [MultiStateView.ViewState] accordingly
 * Every MultiStateView ***MUST*** contain a content view. The content view
 * is obtained from whatever is inside of the tags of the view via its XML declaration
 */
class MultiStateView : FrameLayout {


    @IntDef(
        ViewState.VIEW_STATE_UNKNOWN,
        ViewState.VIEW_STATE_CONTENT,
        ViewState.VIEW_STATE_ERROR,
        ViewState.VIEW_STATE_EMPTY,
        ViewState.VIEW_STATE_LOADING
    )
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class ViewState {
        companion object {
            const val VIEW_STATE_UNKNOWN = -1
            const val VIEW_STATE_CONTENT = 0
            const val VIEW_STATE_ERROR = 1
            const val VIEW_STATE_EMPTY = 2
            const val VIEW_STATE_LOADING = 3
        }
    }


    private lateinit var mInflater: LayoutInflater
    private var mContentView: View? = null
    private lateinit var mLoadingView: View
    private lateinit var mErrorView: View
    private lateinit var mEmptyView: View
    private var mAnimateViewChanges = false

    @Nullable
    private var mListener: StateListener? = null

    @ViewState
    private var mViewState = ViewState.VIEW_STATE_UNKNOWN

    @JvmOverloads
    constructor(context: Context?, attrs: AttributeSet? = null) : super(context!!, attrs) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context!!, attrs, defStyle) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        mInflater = LayoutInflater.from(context)
        val a = context.obtainStyledAttributes(attrs, R.styleable.MultiStateView)

        val loadingViewResId = a.getResourceId(R.styleable.MultiStateView_msv_loadingView, -1)

        if (loadingViewResId > -1) {
            mLoadingView = mInflater.inflate(loadingViewResId, this, false)
            addView(mLoadingView, mLoadingView.layoutParams)
        }

        val emptyViewResId = a.getResourceId(R.styleable.MultiStateView_msv_emptyView, -1)
        if (emptyViewResId > -1) {
            mEmptyView = mInflater.inflate(emptyViewResId, this, false)
            addView(mEmptyView, mEmptyView.getLayoutParams())
        }

        val errorViewResId = a.getResourceId(R.styleable.MultiStateView_msv_errorView, -1)
        if (errorViewResId > -1) {
            mErrorView = mInflater.inflate(errorViewResId, this, false)
            addView(mErrorView, mErrorView.getLayoutParams())
        }

        val viewState = a.getInt(R.styleable.MultiStateView_msv_viewState, ViewState.VIEW_STATE_CONTENT)
        mAnimateViewChanges = a.getBoolean(R.styleable.MultiStateView_msv_animateViewChanges, false)
        mViewState = when (viewState) {
            ViewState.VIEW_STATE_CONTENT -> ViewState.VIEW_STATE_CONTENT
            ViewState.VIEW_STATE_ERROR -> ViewState.VIEW_STATE_ERROR
            ViewState.VIEW_STATE_EMPTY -> ViewState.VIEW_STATE_EMPTY
            ViewState.VIEW_STATE_LOADING -> ViewState.VIEW_STATE_LOADING
            ViewState.VIEW_STATE_UNKNOWN -> ViewState.VIEW_STATE_UNKNOWN
            else -> ViewState.VIEW_STATE_UNKNOWN
        }
        a.recycle()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        requireNotNull(mContentView) { "Content view is not defined" }
        setView(ViewState.VIEW_STATE_UNKNOWN)
    }

    /* All of the addView methods have been overridden so that it can obtain the content view via XML
     It is NOT recommended to add views into MultiStateView via the addView methods, but rather use
     any of the setViewForState methods to set views for their given ViewState accordingly */
    override fun addView(child: View) {
        if (isValidContentView(child)) mContentView = child
        super.addView(child)
    }

    override fun addView(child: View, index: Int) {
        if (isValidContentView(child)) {
            mContentView = child
        }
        super.addView(child, index)
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        if (isValidContentView(child)) mContentView = child
        super.addView(child, index, params)
    }

    override fun addView(child: View, params: ViewGroup.LayoutParams) {
        if (isValidContentView(child)) mContentView = child
        super.addView(child, params)
    }

    override fun addView(child: View, width: Int, height: Int) {
        if (isValidContentView(child)) mContentView = child
        super.addView(child, width, height)
    }

    override fun addViewInLayout(child: View, index: Int, params: ViewGroup.LayoutParams): Boolean {
        if (isValidContentView(child)) mContentView = child
        return super.addViewInLayout(child, index, params)
    }

    override fun addViewInLayout(
        child: View,
        index: Int,
        params: ViewGroup.LayoutParams,
        preventRequestLayout: Boolean
    ): Boolean {
        if (isValidContentView(child)) mContentView = child
        return super.addViewInLayout(child, index, params, preventRequestLayout)
    }

    /**
     * Returns the [View] associated with the [com.kennyc.view.MultiStateView.ViewState]
     *
     * @param state The [com.kennyc.view.MultiStateView.ViewState] with to return the view for
     * @return The [View] associated with the [com.kennyc.view.MultiStateView.ViewState], null if no view is present
     */
    @Nullable
    fun getView(@ViewState state: Int): View? {
        return when (state) {
            ViewState.VIEW_STATE_LOADING -> mLoadingView
            ViewState.VIEW_STATE_CONTENT -> mContentView
            ViewState.VIEW_STATE_EMPTY -> mEmptyView
            ViewState.VIEW_STATE_ERROR -> mErrorView
            else -> null
        }
    }
    /**
     * Returns the current [com.kennyc.view.MultiStateView.ViewState]
     *
     * @return
     */
    /**
     * Sets the current [com.kennyc.view.MultiStateView.ViewState]
     *
     * @param state The [com.kennyc.view.MultiStateView.ViewState] to set [MultiStateView] to
     */
    @get:ViewState
    var viewState: Int
        get() = mViewState
        set(state) {
            if (state != mViewState) {
                val previous = mViewState
                mViewState = state
                setView(previous)
                if (mListener != null) mListener!!.onStateChanged(mViewState)
            }
        }

    /**
     * Shows the [View] based on the [com.kennyc.view.MultiStateView.ViewState]
     */
    private fun setView(@ViewState previousState: Int) {
        when (mViewState) {
            ViewState.VIEW_STATE_LOADING -> {
                if (mLoadingView == null) {
                    throw NullPointerException("Loading View")
                }
                if (mContentView != null) mContentView!!.visibility = GONE
                if (mErrorView != null) mErrorView!!.visibility = GONE
                if (mEmptyView != null) mEmptyView!!.visibility = GONE
                if (mAnimateViewChanges) {
                    animateLayoutChange(getView(previousState))
                } else {
                    mLoadingView!!.visibility = VISIBLE
                }
            }
            ViewState.VIEW_STATE_EMPTY -> {
                if (mEmptyView == null) {
                    throw NullPointerException("Empty View")
                }
                if (mLoadingView != null) mLoadingView!!.visibility = GONE
                if (mErrorView != null) mErrorView!!.visibility = GONE
                if (mContentView != null) mContentView!!.visibility = GONE
                if (mAnimateViewChanges) {
                    animateLayoutChange(getView(previousState))
                } else {
                    mEmptyView!!.visibility = VISIBLE
                }
            }
            ViewState.VIEW_STATE_ERROR -> {
                if (mErrorView == null) {
                    throw NullPointerException("Error View")
                }
                if (mLoadingView != null) mLoadingView!!.visibility = GONE
                if (mContentView != null) mContentView!!.visibility = GONE
                if (mEmptyView != null) mEmptyView!!.visibility = GONE
                if (mAnimateViewChanges) {
                    animateLayoutChange(getView(previousState))
                } else {
                    mErrorView!!.visibility = VISIBLE
                }
            }
            ViewState.VIEW_STATE_CONTENT -> {
                if (mContentView == null) {
                    // Should never happen, the view should throw an exception if no content view is present upon creation
                    throw NullPointerException("Content View")
                }
                if (mLoadingView != null) mLoadingView!!.visibility = GONE
                if (mErrorView != null) mErrorView!!.visibility = GONE
                if (mEmptyView != null) mEmptyView!!.visibility = GONE
                if (mAnimateViewChanges) {
                    animateLayoutChange(getView(previousState))
                } else {
                    mContentView!!.visibility = VISIBLE
                }
            }
            else -> {
                if (mContentView == null) {
                    throw NullPointerException("Content View")
                }
                if (mLoadingView != null) mLoadingView!!.visibility = GONE
                if (mErrorView != null) mErrorView!!.visibility = GONE
                if (mEmptyView != null) mEmptyView!!.visibility = GONE
                if (mAnimateViewChanges) {
                    animateLayoutChange(getView(previousState))
                } else {
                    mContentView!!.visibility = VISIBLE
                }
            }
        }
    }

    /**
     * Checks if the given [View] is valid for the Content View
     *
     * @param view The [View] to check
     * @return
     */
    private fun isValidContentView(view: View): Boolean {
        return if (mContentView != null && mContentView !== view) {
            false
        } else view !== mLoadingView && view !== mErrorView && view !== mEmptyView
    }

    /**
     * Sets the view for the given view state
     *
     * @param view          The [View] to use
     * @param state         The [com.kennyc.view.MultiStateView.ViewState]to set
     * @param switchToState If the [com.kennyc.view.MultiStateView.ViewState] should be switched to
     */
    fun setViewForState(view: View?, @ViewState state: Int, switchToState: Boolean) {
        when (state) {
            ViewState.VIEW_STATE_LOADING -> {
                removeView(mLoadingView)
                mLoadingView = view!!
                addView(mLoadingView)
            }
            ViewState.VIEW_STATE_EMPTY -> {
                removeView(mEmptyView)
                mEmptyView = view!!
                addView(mEmptyView)
            }
            ViewState.VIEW_STATE_ERROR -> {
                removeView(mErrorView)
                mErrorView = view!!
                addView(mErrorView)
            }
            ViewState.VIEW_STATE_CONTENT -> {
                if (mContentView != null) removeView(mContentView)
                mContentView = view
                addView(mContentView!!)
            }
        }
        setView(ViewState.VIEW_STATE_UNKNOWN)
        if (switchToState) viewState = state
    }

    /**
     * Sets the [View] for the given [com.kennyc.view.MultiStateView.ViewState]
     *
     * @param view  The [View] to use
     * @param state The [com.kennyc.view.MultiStateView.ViewState] to set
     */
    fun setViewForState(view: View?, @ViewState state: Int) {
        setViewForState(view, state, false)
    }

    /**
     * Sets the [View] for the given [com.kennyc.view.MultiStateView.ViewState]
     *
     * @param layoutRes     Layout resource id
     * @param state         The [com.kennyc.view.MultiStateView.ViewState] to set
     * @param switchToState If the [com.kennyc.view.MultiStateView.ViewState] should be switched to
     */
    fun setViewForState(@LayoutRes layoutRes: Int, @ViewState state: Int, switchToState: Boolean) {
        val view = mInflater.inflate(layoutRes, this, false)
        setViewForState(view, state, switchToState)
    }

    /**
     * Sets the [View] for the given [com.kennyc.view.MultiStateView.ViewState]
     *
     * @param layoutRes Layout resource id
     * @param state     The [View] state to set
     */
    fun setViewForState(@LayoutRes layoutRes: Int, @ViewState state: Int) {
        setViewForState(layoutRes, state, false)
    }

    /**
     * Sets whether an animate will occur when changing between [ViewState]
     *
     * @param animate
     */
    fun setAnimateLayoutChanges(animate: Boolean) {
        mAnimateViewChanges = animate
    }

    /**
     * Sets the [StateListener] for the view
     *
     * @param listener The [StateListener] that will receive callbacks
     */
    fun setStateListener(listener: StateListener?) {
        mListener = listener
    }

    /**
     * Animates the layout changes between [ViewState]
     *
     * @param previousView The view that it was currently on
     */
    private fun animateLayoutChange(@Nullable previousView: View?) {
        if (previousView == null) {
            getView(mViewState)!!.visibility = VISIBLE
            return
        }
        previousView.visibility = VISIBLE
        val anim = ObjectAnimator.ofFloat(previousView, "alpha", 1.0f, 0.0f).setDuration(250L)
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                previousView.visibility = GONE
                getView(mViewState)!!.visibility = VISIBLE
                ObjectAnimator.ofFloat(getView(mViewState), "alpha", 0.0f, 1.0f).setDuration(250L).start()
            }
        })
        anim.start()
    }

    interface StateListener {
        /**
         * Callback for when the [ViewState] has changed
         *
         * @param viewState The [ViewState] that was switched to
         */
        fun onStateChanged(@ViewState viewState: Int)
    }


//    companion object {
//        const val VIEW_STATE_UNKNOWN = -1
//        const val VIEW_STATE_CONTENT = 0
//        const val VIEW_STATE_ERROR = 1
//        const val VIEW_STATE_EMPTY = 2
//        const val VIEW_STATE_LOADING = 3
//    }
}