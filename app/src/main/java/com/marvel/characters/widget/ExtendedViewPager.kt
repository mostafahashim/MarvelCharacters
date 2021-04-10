package com.marvel.characters.widget

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.View
import com.booking.rtlviewpager.RtlViewPager
import java.util.*

class ExtendedViewPager : RtlViewPager {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun canScroll(v: View, checkV: Boolean, dx: Int, x: Int, y: Int): Boolean {
        return if (v is TouchImageView) {
            //
            // canScrollHorizontally is not supported for Api < 14. To get around this issue,
            // ViewPager is extended and canScrollHorizontallyFroyo, a wrapper around
            // canScrollHorizontally supporting Api >= 8, is called.
            //
            v.canScrollHorizontallyFroyo(-dx)

        } else {
            super.canScroll(v, checkV, dx, x, y)
        }
    }

    internal var isAutoSwipe = false
    internal var timer: Timer? = Timer()
    internal var startAfter: Int = 0
    internal var period: Int = 0

    fun setAutoSwipe(isAutoSwipe: Boolean, startAfter: Int, period: Int) {
        this.isAutoSwipe = isAutoSwipe
        this.startAfter = startAfter
        this.period = period
        startTimer()
    }

    fun startTimer() {
        if (isAutoSwipe) {
            if (timer != null) {
                timer!!.cancel()
                timer = Timer()
            } else
                timer = Timer()
            timer!!.schedule(object : TimerTask() {

                override fun run() {
                    handler.post(update)
                }
            }, startAfter.toLong(), period.toLong())
        }
    }

    internal var handler = Handler()

    internal var update: Runnable = Runnable {
        if (currentItem == adapter!!.count - 1) {
            setCurrentItem(0, false)
            adapter!!.notifyDataSetChanged()
        } else {
            setCurrentItem(currentItem + 1, true)
            adapter!!.notifyDataSetChanged()
        }
    }
}