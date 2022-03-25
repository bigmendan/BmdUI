package com.bmd.bmd_ui.etx

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible

/**
 *@author:       Bigmendan
 *@description:  view层kotlin的基础扩展
 *@create:       2022-01-17 16:07
 */
const val TAG = "Bmd_UI"

fun View.gone() {
    if (this.isVisible)
        this.visibility = View.GONE
}

fun View.visible() {
    if (!this.isVisible)
        this.visibility = View.VISIBLE
}

fun View.invisible() {
    if (this.isVisible)
        this.visibility = View.INVISIBLE
}


/**
 *@Description: 防止view短时间内点击多次
 *@Param:
 *@return:
 *@date:        2022/1/17
 */
inline fun <reified T : View> T.setOnClickListener2(
    crossinline clickAction: (view: View) -> Unit,
    toastMsg: String? = ""
) {

    val delayTime = 500L // 默认间隔500毫秒
    var lastTime = 0L
    this.setOnClickListener {
        val tempTime = System.currentTimeMillis()
        if (tempTime - lastTime > delayTime) {
            lastTime = tempTime
            clickAction.invoke(this)
        } else {
            // 点击过快，不能触发点击
            if (!toastMsg.isNullOrEmpty()) {
                Toast.makeText(this.context, toastMsg, Toast.LENGTH_SHORT).show()
            }
        }
    }
}

inline fun <reified T : View> T.setOnClickListener2(
    crossinline clickAction: (view: View) -> Unit
) {
    setOnClickListener2(clickAction, "")
}

inline fun <reified T : View> T.setOnClickListener2(
    listener: ViewClickListener2
) {
    listener.onClick2(this)
}


inline fun <reified T : View> T.click(crossinline block: (T) -> Unit) = setOnClickListener {
    block(it as T)
}






