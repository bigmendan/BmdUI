package com.bmd.bmd_ui.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.annotation.Nullable

/**
 *@author:       Bigmendan
 *@description:  ..
 *@create:       2022-01-17 17:05
 */
object ContextUtils {

    @Nullable
    fun getActivity(context: Context?): Activity? {
        if (context is Activity) {
            return context
        }
        if (context is ContextWrapper) {
            val baseContext = context.baseContext
            if (baseContext is Activity) {
                return baseContext
            }
        }
        return null
    }


}
