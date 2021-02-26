package com.isaac.m17.widget.dialog

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import com.isaac.m17.R

class ProgressDialog(context: Context?) : AlertDialog(context) {

    private var mLayoutParams: WindowManager.LayoutParams? = null

    init {
        initView(context)
    }

    private fun initView(context: Context?) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window!!.setBackgroundDrawableResource(R.color.transparent)
        this.window!!.setWindowAnimations(R.style.dialogWindowAnim)
        val window = this.window
        mLayoutParams = window!!.attributes
        if (mLayoutParams != null) {
            mLayoutParams!!.gravity = Gravity.CENTER
            mLayoutParams!!.alpha = 1f
        }
        window.attributes = mLayoutParams
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_progress)
    }
}