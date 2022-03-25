package com.bmd.bmd_ui.etx

import android.os.SystemClock
import android.view.View

/**
 *@author:       Bigmendan
 *@description:  防止View 重复点击
 *@create:       2022-02-28 10:36
 */
interface ViewClickListener2 : View.OnClickListener {

    companion object {
        private val delayTime = 500L
        private var lastTime = 0L
    }

    override fun onClick(v: View?) {
        val tempTime = SystemClock.elapsedRealtime()
        if (tempTime - lastTime > delayTime) {
            lastTime = tempTime
            onClick2(v)
        }
    }

    fun onClick2(v: View?)
}