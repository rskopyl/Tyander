package com.rskopyl.tyander.ui.chat.messaging

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.rskopyl.tyander.R
import java.lang.Integer.max

class MessageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attrs, defStyleAttr, defStyleRes) {

    private val messageView: View
        get() = checkNotNull(getChildAt(0))
    private val timeView: View
        get() = checkNotNull(getChildAt(1))

    private val gap: Int

    init {
        context.theme.obtainStyledAttributes(
            attrs, R.styleable.MessageView, defStyleAttr, defStyleRes
        ).run {
            gap = try {
                getDimensionPixelSize(R.styleable.MessageView_gap, 0)
            } finally {
                recycle()
            }
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        check(childCount == 2) {
            "MessageView supports only 2 children ($childCount found)"
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChildren(widthMeasureSpec, heightMeasureSpec)

        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)

        val rawContentWidth = messageView.measuredWidth + gap + timeView.measuredWidth
        val contentWidth = rawContentWidth.plus(paddingStart + paddingEnd)

        val areOnOneLine = contentWidth <= widthSpecSize ||
                widthMeasureSpec == MeasureSpec.UNSPECIFIED

        val measuredWidth = when (MeasureSpec.getMode(widthMeasureSpec)) {
            MeasureSpec.UNSPECIFIED -> contentWidth
            MeasureSpec.AT_MOST -> {
                if (areOnOneLine) {
                    contentWidth
                } else {
                    max(messageView.measuredWidth, timeView.measuredWidth)
                        .plus(paddingStart + paddingEnd)
                }.coerceAtMost(widthSpecSize)
            }
            MeasureSpec.EXACTLY -> widthMeasureSpec
            else -> throw IllegalArgumentException()
        }

        val rawContentHeight = if (areOnOneLine) {
            max(messageView.measuredHeight, timeView.measuredHeight)
        } else {
            messageView.measuredHeight + gap + timeView.measuredHeight
        }
        val contentHeight = rawContentHeight.plus(paddingTop + paddingBottom)

        val measuredHeight = when(MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.UNSPECIFIED -> contentHeight
            MeasureSpec.AT_MOST -> contentHeight.coerceAtMost(heightSpecSize)
            MeasureSpec.EXACTLY -> heightSpecSize
            else -> throw IllegalArgumentException()
        }

        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        messageView.layout(
            paddingStart,
            paddingTop,
            paddingStart + messageView.measuredWidth,
            paddingTop + messageView.measuredHeight
        )
        timeView.layout(
            (r - l) - (timeView.measuredWidth + paddingEnd),
            (b - t) - (timeView.measuredHeight + paddingBottom),
            (r - l) - paddingEnd,
            (b - t) - paddingBottom
        )
    }
}