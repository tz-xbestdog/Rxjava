package com.io.rx.interceptor;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.myku.Load;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpKit3 {
    private static HttpKit3 mInstance;
    private static String USER_AGENT;
    private static String ACCESS_TOKEN;
    public static String HOST_ADDRESS;
    public static String DEVICE_TOKEN;
    private static Context context;
    private static OkHttpClient client;
    private static Retrofit retrofit;

    private HttpKit3(Context context) {
        HttpKit3.context = context;
    }

    public static void init(Context context, String host) {
        if (mInstance == null) mInstance = new HttpKit3(context);
        client = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggerInterceptor("TAG"))
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)

//                .addNetworkInterceptor(new CacheInterceptor())//也就这里不同
//                .cache(cache)
//                .retryOnConnectionFailure(true)//连接失败后是否重新连接
                .build();

        HOST_ADDRESS = host;
//        ACCESS_TOKEN = context.getResources().getString(R.string.accessToken);
        PackageManager packageManager = context.getPackageManager();
        String packageName = context.getPackageName(), version;
        try {
            PackageInfo info = packageManager.getPackageInfo(packageName, 0);
            version = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            version = "0.1";
        }
        Load.Companion.load();
//        USER_AGENT = String.format(context.getResources().getString(R.string.header), Build.VERSION.RELEASE, Build.MODEL, packageName, version);

    }


    public static Retrofit getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(HOST_ADDRESS)//baseURL提倡以“/”结尾
                    .client(client)//设置okhttp
                    .addConverterFactory(GsonConverterFactory.create()) //设置使用Gson解析(记得加入依赖)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // 支持RxJava
                    .build();
        }

        return retrofit;
    }
}
