package com.bmd.bmd_ui.base

import android.app.Application

/**
 *@author:       Bigmendan
 *@description:  保存全局application
 *@create:       2022-01-17 16:08
 */
object GlobalApp {
    var application: Application? = null

    fun get(): Application? {
        if (application == null) {
            try {
                application = Class.forName("android.app.ActivityThread")
                    .getMethod("currentApplication")
                    .invoke(null) as Application
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        return application
    }
}