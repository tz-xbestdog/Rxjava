package com.io.rx.util.util

import android.app.Activity
import com.google.gson.JsonParseException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeoutException

object ThrowableUtil {
    fun Throwable(activity: Activity, e: Throwable?, listener: HttpListener) {
        if (e is SocketException) {
            listener.event("网络异常")
        } else if (e is TimeoutException || e is SocketTimeoutException) {
            listener.event("请求超时")
        } else if (e is JsonParseException) {
            listener.event("数据解析失败")
        } else {
            listener.event("网络不稳定，请稍后再试")
        }
    }

    interface HttpListener {
        fun event(string: String)

    }
}