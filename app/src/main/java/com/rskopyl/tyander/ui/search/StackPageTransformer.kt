package com.rskopyl.tyander.ui.search

import android.view.View
import androidx.annotation.FloatRange
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs
import kotlin.math.max

class StackPageTransformer(
    private val stackAppearance: StackAppearance,
    private val dragAppearance: DragAppearance
) : ViewPager2.PageTransformer {

    private fun drag(page: View, position: Float) = page.run {
        alpha = max(1 - abs(position), 0f)
        rotation = (dragAppearance.rotationAngle * abs(position)).let {
            it * if (dragAppearance.direction == Direction.LEFT) 1 else -1
        }
        if (dragAppearance.direction == Direction.RIGHT) {
            translationX = 2 * width * (abs(position))
        }
    }

    override fun transformPage(page: View, position: Float) = page.run {
        if (position >= 0) {
            alpha = max(1 - position * stackAppearance.alphaStep, 0f)
            scaleX = max(1 - position * stackAppearance.scaleStep, 0f)
            scaleY = max(1 - position * stackAppearance.scaleStep, 0f)
            translationX = -width * position
            translationY = stackAppearance.translationYStep * position
            translationZ = -position
            rotation = 0f
        } else {
            drag(page, position)
        }
    }

    data class StackAppearance(
        val translationYStep: Float,
        @FloatRange(from = 0.0, to = 1.0)
        val scaleStep: Float = 0.1f,
        @FloatRange(from = 0.0, to = 1.0)
        val alphaStep: Float = 0.1f
    )

    data class DragAppearance(
        val direction: Direction,
        val rotationAngle: Float = 30f
    )

    enum class Direction { LEFT, RIGHT }
}