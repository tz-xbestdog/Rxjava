package com.myku.util.util

import android.view.View

object ViewUtil {
    fun setViewsOnClickListenter(listener: View.OnClickListener, vararg views: View) {
        views.forEach {
            it.setOnClickListener(listener)
        }
    }


}