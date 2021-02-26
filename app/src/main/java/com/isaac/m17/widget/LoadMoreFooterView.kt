package com.isaac.m17.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.aspsine.swipetoloadlayout.SwipeLoadMoreTrigger
import com.aspsine.swipetoloadlayout.SwipeTrigger


class LoadMoreFooterView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr), SwipeTrigger, SwipeLoadMoreTrigger {

    override fun onLoadMore() {
        text = "LOADING MORE"
    }

    override fun onPrepare() {
        text = ""
    }

    override fun onMove(
        yScrolled: Int,
        isComplete: Boolean,
        automatic: Boolean
    ) {
        if (!isComplete) {
            if (yScrolled <= -getHeight()) {
                text = "RELEASE TO LOAD MORE"
            } else {
                text = "SWIPE TO LOAD MORE"
            }
        } else {
            text = "LOAD MORE RETURNING"
        }
    }

    override fun onRelease() {
        text = "LOADING MORE"
    }

    override fun onComplete() {
        text = "COMPLETE"
    }

    override fun onReset() {
        text = ""
    }
}