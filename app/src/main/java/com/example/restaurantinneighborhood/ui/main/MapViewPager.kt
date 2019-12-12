package com.example.restaurantinneighborhood.ui.main

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager


class MapViewPager(
    context: Context?,
    attrs: AttributeSet?
) :
    ViewPager(context!!, attrs) {
    private var enable = true
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return false
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return false
    }

    fun setPagingEnabled(enabled: Boolean) {
        this.enable = enabled
    }

    override fun scrollTo(x: Int, y: Int) {
        if (enable) {
            super.scrollTo(x, y)
        }
    }

}