package com.marvel.characters.widget

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager

class NoScrollLinearLayoutManager(context: Context, spanCount: Int, orientation: Int, reverseLayout: Boolean) : GridLayoutManager(context, spanCount, orientation, reverseLayout) {
    private var scrollable = true


    fun enableScrolling() {
        scrollable = true
    }

    fun disableScrolling() {
        scrollable = false
    }

    override fun canScrollVertically() =
            super.canScrollVertically() && scrollable


    override fun canScrollHorizontally() =
            super.canScrollVertically()

                    && scrollable
}