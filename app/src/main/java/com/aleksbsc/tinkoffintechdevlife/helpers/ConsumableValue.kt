package com.aleksbsc.tinkoffintechdevlife.helpers

import androidx.annotation.UiThread

class ConsumableValue<T>(private val data: T) {
    private var consumed = false

    /**
     * Process this event, will only be called once
     */
    @UiThread
    fun handle(block: ConsumableValue<T>.(T) -> Unit) {
        val wasConsumed = consumed
        consumed = true
        if (!wasConsumed) {
            this.block(data)
        }
    }

    /**
     * Inside a handle lambda, you may call this if you discover that you cannot handle
     * the event right now. It will mark the event as available to be handled by another handler.
     */
    @UiThread
    fun ConsumableValue<T>.markUnhandled() {
        consumed = false
    }
}
