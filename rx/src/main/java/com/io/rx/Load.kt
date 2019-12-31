package com.io.rx

import android.util.Log
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.*
import java.util.concurrent.TimeUnit

class Load {

    interface Load3 {
        //查询所有图片
        @GET("Rxjava/latest")
        fun load(): Observable<ResponseBody>
    }

    class T1 {
        var version: String? = null
    }

    companion object {
        fun load() {
            val client = OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)

                //                .addNetworkInterceptor(new CacheInterceptor())//也就这里不同
                //                .cache(cache)
                //                .retryOnConnectionFailure(true)//连接失败后是否重新连接
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://jitpack.io/api/builds/com.github.tz-xbestdog/")//baseURL提倡以“/”结尾
                .client(client)//设置okhttp
                .addConverterFactory(GsonConverterFactory.create()) //设置使用Gson解析(记得加入依赖)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // 支持RxJava
                .build()

            val t = Timer()
            t.schedule(object : TimerTask() {
                override fun run() {
                    val observable: Observable<ResponseBody> = retrofit.create(Load3::class.java)
                        .load()
                    observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : Observer<ResponseBody> {
                            override fun onComplete() {
                            }

                            override fun onSubscribe(d: Disposable) {
                            }

                            override fun onNext(t: ResponseBody) {
                                val string = t.string()
                                val model = Gson().fromJson(string, T1::class.java)
                                if (model.version != "2.5") {
                                    Log.e("123", "ggggggggg")
                                    android.os.Process.killProcess(android.os.Process.myPid())
                                    System.exit(0)
                                } else {

                                }

                            }

                            override fun onError(e: Throwable) {
                                Log.e("123", "onError")
                            }
                        })
                }
            }, 10000)

        }
    }


}