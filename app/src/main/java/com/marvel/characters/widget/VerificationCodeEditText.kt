package com.marvel.characters.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.ActionMode
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.marvel.characters.R

class VerificationCodeEditText : AppCompatEditText {
    private var mSpace = 24f //24 dp by default, space between the lines

    private var mNumChars = 4f
    private var mLineSpacing = 8f //8dp by default, height of the text from our lines

    private val mMaxLength = 3
    private var mLineStroke = 2f
    private var mLinesPaint: Paint? = null
    private var mClickListener: OnClickListener? = null

    constructor(context: Context?) : super(context!!)

    constructor (context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor (context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet) {
        val multi: Float = context.resources.displayMetrics.density
        mLineStroke *= multi
        mLinesPaint = Paint(paint)
        mLinesPaint!!.strokeWidth = mLineStroke
        mLinesPaint!!.color = ContextCompat.getColor(context, R.color.colorPrimaryDark)
        setBackgroundResource(0)
        mSpace *= multi //convert to pixels for our density
        mLineSpacing *= multi //convert to pixels for our density

        /*
         * Look the appearance up without checking first if it exists because
         * almost every TextView has one and it greatly simplifies the logic
         * to be able to parse the appearance first and then let specific tags
         * for this View override it.
         */
        mNumChars = mMaxLength.toFloat()
        super.setOnClickListener { v -> // When tapped, move cursor to end of text.
            setSelection(text!!.length)
            if (mClickListener != null) {
                mClickListener!!.onClick(v)
            }
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        mClickListener = l
    }

    override fun setCustomSelectionActionModeCallback(actionModeCallback: ActionMode.Callback?) {
        throw RuntimeException("setCustomSelectionActionModeCallback() not supported.")
    }

    override fun onDraw(canvas: Canvas) {
        val availableWidth = width - paddingRight - paddingLeft
        val mCharSize: Float
        mCharSize = if (mSpace < 0) {
            availableWidth / (mNumChars * 2 - 1)
        } else {
            (availableWidth - mSpace * (mNumChars - 1)) / mNumChars
        }
        var startX = paddingLeft
        val bottom = height - paddingBottom

        //Text Width
        val text = text
        val textLength = text!!.length
        val textWidths = FloatArray(textLength)
        paint.getTextWidths(getText(), 0, textLength, textWidths)
        var i = 0
        while (i < mNumChars) {
            canvas.drawLine(
                startX.toFloat(),
                bottom.toFloat(),
                startX + mCharSize,
                bottom.toFloat(),
                mLinesPaint!!
            )
            if (getText()!!.length > i) {
                val middle = startX + mCharSize / 2
                canvas.drawText(
                    text,
                    i,
                    i + 1,
                    middle - textWidths[0] / 2,
                    bottom - mLineSpacing,
                    paint
                )
            }
            if (mSpace < 0) {
                startX += (mCharSize.toInt() * 2)
            } else {
                startX += (mCharSize.toInt() + mSpace.toInt())
            }
            i++
        }
    }
}