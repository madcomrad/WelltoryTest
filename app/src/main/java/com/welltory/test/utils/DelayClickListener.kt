package com.welltory.test.utils

import android.view.View

class DelayClickListener(
    private val delay: Long = DEFAULT_DELAY,
    private val handler: (View) -> Unit
) : View.OnClickListener {

    private val unlock = Runnable { isLocked = false }
    private var isLocked = false

    override fun onClick(v: View) {
        if (isLocked) {
            return
        }

        isLocked = true

        handler.invoke(v)

        v.postDelayed(unlock, delay)
    }

    companion object {
        const val DEFAULT_DELAY = 250L
    }
}

fun View.setDelayClickListener(delay: Long = DelayClickListener.DEFAULT_DELAY, block: (View) -> Unit) {
    setOnClickListener(DelayClickListener(delay, block))
}
