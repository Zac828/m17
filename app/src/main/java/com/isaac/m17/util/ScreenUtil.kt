package com.isaac.m17.util

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

object ScreenUtil {

    fun getScreenWidth(context: Context) : Int {
        val wm = context
            .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(outMetrics)
        return outMetrics.widthPixels
    }
}