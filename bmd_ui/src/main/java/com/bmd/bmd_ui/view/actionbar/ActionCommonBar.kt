package com.bmd.bmd_ui.view.actionbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.bmd.bmd_ui.R
import com.bmd.bmd_ui.databinding.BmdActionBarCommonBinding

/**
 *@author:       Bigmendan
 *@description:  通用ActionBar 标题栏
 *@create:       2022-01-18 17:33
 */
class ActionCommonBar(context: Context, attrs: AttributeSet? = null) : BmdActionBar(context, attrs) {

    // titleBar布局
    private var mTitleBarBinding: BmdActionBarCommonBinding? = null


    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.ActionBarCommon)

        // 测试 dev


        ta.recycle()
    }

    /**
     *@Description:   find titlebar's views
     *@Param:
     *@return:
     *@date:        2022/1/18
     */
    override fun inflateTitleBar(): View? {

        mTitleBarBinding = BmdActionBarCommonBinding.inflate(LayoutInflater.from(context))

        mTitleBarBinding?.let {


        }


        return null
    }


}