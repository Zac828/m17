package com.isaac.m17.util

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

object KeyBoardUtil {
    fun closeKeyboard(mEditText: EditText, mContext: Context) {
        val imm = mContext
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mEditText.windowToken, 0)
    }
}