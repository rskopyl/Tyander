package com.rskopyl.tyander.util

import androidx.lifecycle.*
import androidx.viewpager2.widget.ViewPager2
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import java.util.*

fun <T> Flow<T>.collectOnLifecycle(
    lifecycleOwner: LifecycleOwner,
    collector: FlowCollector<T>,
    minState: Lifecycle.State = Lifecycle.State.RESUMED,
) {
    lifecycleOwner.lifecycleScope.launch {
        lifecycleOwner.repeatOnLifecycle(minState) {
            collect(collector)
        }
    }
}

operator fun <T : ViewModel> ViewModelProvider.Factory.Companion.invoke(
    producer: () -> T
): ViewModelProvider.Factory {
    return object : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return producer() as T
        }
    }
}

fun ViewPager2.instantFakeDragBy(offsetPxFloat: Float): Boolean {
    beginFakeDrag()
    val executed = fakeDragBy(offsetPxFloat)
    endFakeDrag()
    return executed
}

fun ViewPager2.registerOnScrollStateChangeCallback(
    action: (Int) -> Unit
): ViewPager2.OnPageChangeCallback {
    return object : ViewPager2.OnPageChangeCallback() {

        override fun onPageScrollStateChanged(state: Int) = action(state)
    }.also(::registerOnPageChangeCallback)
}

fun ViewPager2.doOnNextIdle(action: () -> Unit) {
    object : ViewPager2.OnPageChangeCallback() {

        init {
            onPageScrollStateChanged(scrollState)
        }

        override fun onPageScrollStateChanged(state: Int) {
            if (state == ViewPager2.SCROLL_STATE_IDLE) {
                action()
                post {
                    unregisterOnPageChangeCallback(this)
                }
            }
        }
    }.also(::registerOnPageChangeCallback)
}

fun LocalDateTime.toJavaDate(): Date =
    Date(toInstant(TimeZone.UTC).toEpochMilliseconds())