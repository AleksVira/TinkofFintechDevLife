package com.aleksbsc.tinkoffintechdevlife.extensions

import android.view.View
import java.lang.System.currentTimeMillis

fun View.setOnSingleClick(onClick: () -> Unit) {
    var lastClickTime = 0L
    setOnClickListener {
        if (currentTimeMillis() > lastClickTime + 500) onClick()
        lastClickTime = currentTimeMillis()
    }
}

