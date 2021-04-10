package com.marvel.characters.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout

class EqualWidthHeightLinearLayout : LinearLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val h = this.measuredHeight
        val w = this.measuredWidth
        val r = Math.max(w, h)

        setMeasuredDimension(r, r)

    }
}