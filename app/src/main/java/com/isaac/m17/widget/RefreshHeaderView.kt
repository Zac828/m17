package com.isaac.m17.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.aspsine.swipetoloadlayout.SwipeRefreshTrigger
import com.aspsine.swipetoloadlayout.SwipeTrigger

class RefreshHeaderView @kotlin.jvm.JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr), SwipeTrigger, SwipeRefreshTrigger {

    override fun onReset() {
        text = ""
    }

    override fun onComplete() {
        text = "COMPLETE"
    }

    override fun onRelease() {
    }

    override fun onMove(y: Int, isComplete: Boolean, automatic: Boolean) {
        text = if (!isComplete) {
            if (y >= height) {
                "RELEASE TO REFRESH"
            } else {
                "SWIPE TO REFRESH"
            }
        } else {
            "REFRESH RETURNING"
        }
    }

    override fun onPrepare() {
        text = ""
    }

    override fun onRefresh() {
        text = "REFRESHING"
    }
}