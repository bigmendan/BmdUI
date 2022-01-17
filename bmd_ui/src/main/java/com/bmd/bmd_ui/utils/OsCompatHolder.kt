package com.bmd.bmd_ui.utils

import com.bmd.bmd_ui.view.statusbar.compat.*

/**
 *@author:       Bigmendan
 *@description:
 *@create:       2022-01-17 17:20
 */
object OsCompatHolder {

    private var sOsCompat: OsCompat? = null

    fun get(): OsCompat {
        if (sOsCompat == null) {
            sOsCompat = if (OsUtils.isMiui) {
                OsCompatMiui()
            } else if (OsUtils.isFlyme) {
                OsCompatFlyme()
            } else if (OsUtils.isOppo) {
                OsCompatOppo()
            } else {
                OsCompatDef()
            }
        }
        return sOsCompat!!
    }
}