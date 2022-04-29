package com.bmd.bmd_ui.view.actionbar

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import com.bmd.bmd_ui.R
import com.bmd.bmd_ui.databinding.BmdActionBarCommonBinding
import com.bmd.bmd_ui.etx.setOnClickListener2

/**
 *@author:       Bigmendan
 *@description:  通用ActionBar 标题栏
 *@create:       2022-01-18 17:33
 */
class ActionCommonBar(context: Context, attrs: AttributeSet? = null) : BmdActionBar(context, attrs) {

    // titleBar布局
    private var mTitleBarBinding: BmdActionBarCommonBinding? = null


    // ------------------------基础默认属性

    private var leftIconRes = 0
    private var leftIconColor = 0
    private var leftIconMarginLeft = 0
    private var leftIconPaddingLeft = 0
    private var leftIconPaddingRight = 0
    private var leftIconPaddingTop = 0
    private var leftIconPaddingBottom = 0
    private var leftIconMarginRight = 0
    private var leftIconMarginTop = 0
    private var leftIconMarginBottom = 0
    private var leftIconClickToFinish = false


    private var leftText = ""
    private var leftTextColor = 0
    private var leftTextSize = 0F
    private var leftTextPaddingLeft = 0
    private var leftTextPaddingRight = 0
    private var leftTextPaddingTop = 0
    private var leftTextPaddingBottom = 0
    private var leftTextClickToDo = false


    private var centerText = ""
    private var centerTextColor = 0
    private var centerTextSize = 0F


    private var rightText = ""
    private var rightTextColor = 0
    private var rightTextSize = 0F
    private var rightTextPaddingLeft = 0
    private var rightTextPaddingRight = 0
    private var rightTextPaddingTop = 0
    private var rightTextPaddingBottom = 0
    private var rightTextClickToDo = false


    private var rightIconRes = 0
    private var rightIconPaddingLeft = 0
    private var rightIconPaddingRight = 0
    private var rightIconPaddingTop = 0
    private var rightIconPaddingBottom = 0
    private var rightIconMarginLeft = 0
    private var rightIconMarginRight = 0
    private var rightIconMarginTop = 0
    private var rightIconMarginBottom = 0
    private var rightIconColor = 0


    //--------------------------- 点击事件;
    private var actionClickListener: BmdActionClickListener? = null


    // 一定要复写这个方法 做自定义属性配置;
    override fun initAttrs(attrs: AttributeSet?) {
        super.initAttrs(attrs)

        val ta = context.obtainStyledAttributes(attrs, R.styleable.ActionBarCommon)
        val res = context.resources

        //各个属性默认值;
        val leftIconPaddingDef = res.getDimension(R.dimen.action_bar_title_padding_left_def)
        val leftIconColorDef = ContextCompat.getColor(context, R.color.action_bar_left_icon_color_def)

        val leftTextColorDef = ContextCompat.getColor(context, R.color.action_bar_left_text_color_def)
        val leftTextSizeDef = res.getDimension(R.dimen.action_bar_left_text_size_def)
        val leftTextPaddingDef = res.getDimension(R.dimen.action_bar_left_text_padding_def)
        val leftTextPaddingStartDef = res.getDimension(R.dimen.action_bar_left_text_padding_left_def)
        val leftTextPaddingEndDef = res.getDimension(R.dimen.action_bar_left_text_padding_right_def)
        val leftTextPaddingTopDef = res.getDimension(R.dimen.action_bar_left_text_padding_top_def)
        val leftTextPaddingBottomDef = res.getDimension(R.dimen.action_bar_left_text_padding_bottom_def)

        val centerTextSizeDef = res.getDimension(R.dimen.action_bar_title_center_text_size_def)
        val centerTextColorDef = ContextCompat.getColor(context, R.color.action_bar_center_text_color_def)


        val rightTextSizeDef = res.getDimension(R.dimen.action_bar_right_text_size_def)
        val rightTextPaddingLeftDef = res.getDimension(R.dimen.action_bar_right_text_padding_left_def)
        val rightTextPaddingRightDef = res.getDimension(R.dimen.action_bar_right_text_padding_right_def)
        val rightTextPaddingTopDef = res.getDimension(R.dimen.action_bar_right_text_padding_top_def)
        val rightTextPaddingBottomDef = res.getDimension(R.dimen.action_bar_right_text_padding_bottom_def)
        val rightTextColorDef = ContextCompat.getColor(context, R.color.action_bar_right_text_color_def)

        val rightIconPaddingDef = res.getDimension(R.dimen.action_bar_right_icon_padding_def)
        val rightIconColorDef = ContextCompat.getColor(context, R.color.action_bar_right_icon_color_def)

        // leftIcon
        leftIconPaddingLeft =
            ta.getDimension(R.styleable.ActionBarCommon_abc_leftIconPaddingLeft, leftIconPaddingDef).toInt()
        leftIconPaddingRight =
            ta.getDimension(R.styleable.ActionBarCommon_abc_leftIconPaddingRight, leftIconPaddingDef).toInt()
        leftIconPaddingTop =
            ta.getDimension(R.styleable.ActionBarCommon_abc_leftIconPaddingTop, leftIconPaddingDef).toInt()
        leftIconPaddingBottom =
            ta.getDimension(R.styleable.ActionBarCommon_abc_leftIconPaddingBottom, leftIconPaddingDef).toInt()

        leftIconColor = ta.getColor(R.styleable.ActionBarCommon_abc_leftIconColor, leftIconColorDef)
        leftIconMarginLeft = ta.getDimension(R.styleable.ActionBarCommon_abc_leftIconMarginLeft, 0F).toInt()
        leftIconMarginRight = ta.getDimension(R.styleable.ActionBarCommon_abc_leftIconMarginRight, 0F).toInt()
        leftIconMarginTop = ta.getDimension(R.styleable.ActionBarCommon_abc_leftIconMarginTop, 0F).toInt()
        leftIconMarginBottom = ta.getDimension(R.styleable.ActionBarCommon_abc_leftIconMarginBottom, 0F).toInt()
        leftIconRes = ta.getResourceId(R.styleable.ActionBarCommon_abc_leftIconRes, 0)
        leftIconClickToFinish = ta.getBoolean(R.styleable.ActionBarCommon_abc_leftIconClickToFinish, false)


        // leftText
        leftText = ta.getString(R.styleable.ActionBarCommon_abc_leftText) ?: ""
        leftTextColor = ta.getColor(R.styleable.ActionBarCommon_abc_leftTextColor, leftTextColorDef)
        leftTextSize = ta.getDimension(R.styleable.ActionBarCommon_abc_leftTextSize, leftTextSizeDef)
        leftTextPaddingLeft =
            ta.getDimension(R.styleable.ActionBarCommon_abc_leftTextPaddingLeft, leftTextPaddingStartDef).toInt()
        leftTextPaddingRight =
            ta.getDimension(R.styleable.ActionBarCommon_abc_leftTextPaddingRight, leftTextPaddingEndDef).toInt()
        leftTextPaddingTop =
            ta.getDimension(R.styleable.ActionBarCommon_abc_leftTextPaddingTop, leftTextPaddingTopDef).toInt()
        leftTextPaddingBottom =
            ta.getDimension(R.styleable.ActionBarCommon_abc_leftTextPaddingBottom, leftTextPaddingBottomDef).toInt()

        leftTextClickToDo = ta.getBoolean(R.styleable.ActionBarCommon_abc_leftTextClickToDo, false)


        // centerText
        centerText = ta.getString(R.styleable.ActionBarCommon_abc_CenterText) ?: ""
        centerTextSize = ta.getDimension(R.styleable.ActionBarCommon_abc_CenterTextSize, centerTextSizeDef)
        centerTextColor = ta.getColor(R.styleable.ActionBarCommon_abc_CenterTextColor, centerTextColorDef)


        //rightText
        rightText = ta.getString(R.styleable.ActionBarCommon_abc_rightText) ?: ""
        rightTextSize = ta.getDimension(R.styleable.ActionBarCommon_abc_rightTextSize, rightTextSizeDef)
        rightTextPaddingLeft =
            ta.getDimension(R.styleable.ActionBarCommon_abc_rightTextPaddingLeft, rightTextPaddingLeftDef).toInt()
        rightTextPaddingRight =
            ta.getDimension(R.styleable.ActionBarCommon_abc_rightTextPaddingRight, rightTextPaddingRightDef).toInt()
        rightTextPaddingTop =
            ta.getDimension(R.styleable.ActionBarCommon_abc_rightTextPaddingTop, rightTextPaddingTopDef).toInt()
        rightTextPaddingBottom =
            ta.getDimension(R.styleable.ActionBarCommon_abc_rightTextPaddingBottom, rightTextPaddingBottomDef).toInt()
        rightTextColor = ta.getColor(R.styleable.ActionBarCommon_abc_rightTextColor, rightTextColorDef)

        //rightIcon
        rightIconRes = ta.getResourceId(R.styleable.ActionBarCommon_abc_rightIconRes, 0)
        rightIconColor = ta.getColor(R.styleable.ActionBarCommon_abc_rightIconColor, rightIconColorDef)

        rightIconPaddingLeft =
            ta.getDimension(R.styleable.ActionBarCommon_abc_rightIconPaddingLeft, rightIconPaddingDef).toInt()
        rightIconPaddingRight =
            ta.getDimension(R.styleable.ActionBarCommon_abc_rightIconPaddingRight, rightIconPaddingDef).toInt()
        rightIconPaddingTop =
            ta.getDimension(R.styleable.ActionBarCommon_abc_rightIconPaddingTop, rightIconPaddingDef).toInt()
        rightIconPaddingBottom =
            ta.getDimension(R.styleable.ActionBarCommon_abc_rightIconPaddingBottom, rightIconPaddingDef).toInt()
        rightIconMarginLeft = ta.getDimension(R.styleable.ActionBarCommon_abc_rightIconMarginLeft, 0F).toInt()
        rightIconMarginRight = ta.getDimension(R.styleable.ActionBarCommon_abc_rightIconMarginRight, 0F).toInt()
        rightIconMarginTop = ta.getDimension(R.styleable.ActionBarCommon_abc_rightIconMarginTop, 0F).toInt()
        rightIconMarginBottom = ta.getDimension(R.styleable.ActionBarCommon_abc_rightIconMarginBottom, 0F).toInt()
        //...


        ta.recycle()
    }

    /**
     *@Description:   find titleBar's views
     *@date:        2022/1/18
     */
    override fun inflateTitleBar(): View? {

        mTitleBarBinding = BmdActionBarCommonBinding.inflate(LayoutInflater.from(context))


        mTitleBarBinding?.let {

            //  左边Icon - 左边距
            it.actionBarCommonIconLeft.apply {
                //  marginLeft
                val lps: LinearLayoutCompat.LayoutParams = layoutParams as LinearLayoutCompat.LayoutParams
                lps.leftMargin = leftIconMarginLeft
                lps.rightMargin = leftIconMarginRight
                lps.topMargin = leftIconMarginTop
                lps.bottomMargin = leftIconMarginBottom
                this.layoutParams = lps

                if (leftIconRes > 0) {
                    visibility = View.VISIBLE
                    setPadding(leftIconPaddingLeft, leftIconPaddingTop, leftIconPaddingRight, leftIconPaddingBottom)
                    setImageResource(leftIconRes)
                    setColorFilter(leftIconColor)

                    //是否点击返回
                    if (leftIconClickToFinish) {
                        setOnClickListener2 {
                            finishActivity()
                        }
                    }

                } else {
                    visibility = View.GONE
                }

            }

            // 左侧文字;
            it.actionBarCommonLeftText.let { t ->
                if (leftText.isNotEmpty()) {
                    t.visibility = View.VISIBLE
                    t.text = leftText
                    t.setPadding(leftTextPaddingLeft, leftTextPaddingTop, leftTextPaddingRight, leftTextPaddingBottom)
                    t.setTextColor(leftTextColor)
                    t.setTextSize(TypedValue.COMPLEX_UNIT_PX, leftTextSize)

                } else {
                    t.visibility = View.GONE
                }
            }

            // 中间标题
            it.actionBarCommonCenterText.apply {
                visibility = View.VISIBLE
                text = centerText
                setTextColor(centerTextColor)
                setTextSize(TypedValue.COMPLEX_UNIT_PX, centerTextSize)

            }

            //右侧文字
            it.actionBarCommonRightText.let { r ->

                if (rightText.isNotEmpty()) {
                    r.apply {
                        visibility = View.VISIBLE
                        text = rightText
                        setPadding(
                            rightTextPaddingLeft,
                            rightTextPaddingRight,
                            rightTextPaddingTop,
                            rightTextPaddingBottom
                        )
                        setTextColor(rightTextColor)
                        setTextSize(TypedValue.COMPLEX_UNIT_PX, rightTextSize)
                    }

                } else {
                    r.visibility = View.GONE
                }

            }


            //右侧图标
            it.actionBarCommonRightIcon.apply {
                //  marginRight
                val lps: LinearLayoutCompat.LayoutParams = layoutParams as LinearLayoutCompat.LayoutParams
                lps.leftMargin = rightIconMarginLeft
                lps.rightMargin = rightIconMarginRight
                lps.topMargin = rightIconMarginTop
                lps.bottomMargin = rightIconMarginBottom

                this.layoutParams = lps

                if (rightIconRes > 0) {
                    visibility = View.VISIBLE
                    setPadding(rightIconPaddingLeft, rightIconPaddingRight, rightIconPaddingTop, rightIconPaddingBottom)
                    setImageResource(rightIconRes)
//                    setColorFilter(rightIconColor)

                } else {
                    visibility = View.GONE
                }
            }

        }


        return mTitleBarBinding?.root
    }


    /**
     *@Description: 左侧点击事件  leftIcon  + LeftText
     *@date:        2022/2/21
     */
    fun setLeftClickToDo(listener: BmdActionClickListener) {
        mTitleBarBinding?.let {
            it.actionBarCommonLeft.setOnClickListener2 { v ->
                listener.onclick(v)
            }
        }
    }

    // 左侧文字 + style
    fun setLeftText(leftStr: String, targetStyle: Int = -1) {
        mTitleBarBinding?.let {
            it.actionBarCommonLeftText.apply {

                visibility = View.VISIBLE
                text = leftStr
                //颜色
                setTextColor(leftTextColor)


                if (targetStyle > 0) {
                    setTextAppearance(targetStyle)
                }
            }
        }

    }

    fun setCenterText(centerStr: String, targetGravity: Int = Gravity.LEFT, targetStyle: Int = -1) {
        mTitleBarBinding?.let {
            it.actionBarCommonCenterText.apply {
                text = centerStr
                gravity = targetGravity
                if (targetStyle > 0) {
                    setTextAppearance(targetStyle)
                }
            }
        }
    }

    fun setRightText(rightStr: String, targetStyle: Int = -1) {
        mTitleBarBinding?.let {
            it.actionBarCommonRightText.apply {
                visibility = View.VISIBLE
                text = rightStr

                if (targetStyle > 0) {
                    setTextAppearance(targetStyle)
                }
            }
        }
    }

    fun setRightIconRes(res: Int) {
        mTitleBarBinding?.let {
            it.actionBarCommonRightIcon.apply {
                if (res > 0) {

                    visibility = View.VISIBLE
                    setPadding(rightIconPaddingLeft, rightIconPaddingTop, rightIconPaddingRight, rightIconPaddingBottom)
                    setImageResource(res)


                } else {
                    visibility = View.GONE
                }
            }
        }

    }

    fun setRightTextClickToDo(listener: BmdActionClickListener) {
        mTitleBarBinding?.let {
            it.actionBarCommonRightText.setOnClickListener2 { v ->
                listener.onclick(v)
            }
        }
    }

    fun setRightIconClickToDo(listener: BmdActionClickListener) {
        mTitleBarBinding?.let {
            it.actionBarCommonRightIcon.setOnClickListener2 { v ->
                listener.onclick(v)
            }
        }
    }

}
