package com.bmd.bmd_ui.ktx

import android.app.Activity
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import com.bmd.bmd_ui.base.GlobalApp

/**
 *@author:       Bigmendan
 *@description:  view层kotlin的基础扩展
 *@create:       2022-01-17 16:07
 */

/**
 *@Description: 全局Toast弹出
 *@Param:
 *@return:
 *@date:        2022/1/17
 */
fun Toast.showToast(msg: String) {
    Toast.makeText(GlobalApp.application?.applicationContext, msg, Toast.LENGTH_SHORT).show()
}

/**
 *@Description: 防止view短时间内点击多次
 *@Param:
 *@return:
 *@date:        2022/1/17
 */
fun View.setOnClickListener2() {

}




