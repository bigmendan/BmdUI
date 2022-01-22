package com.bmd.bmd_ui.view.actionbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.bmd.bmd_ui.databinding.BmdActionBarSearchBinding

/**
 *@author:       Bigmendan
 *@des：
 *@create:
 */
class ActionSearchBar(context: Context, attrs: AttributeSet? = null) : BmdActionBar(context, attrs) {


    private lateinit var mBinding: BmdActionBarSearchBinding

    override fun inflateTitleBar(): View {

        mBinding = BmdActionBarSearchBinding.inflate(LayoutInflater.from(context))
        return mBinding.root
    }

}