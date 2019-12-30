package com.io.rx.interceptor;

import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * Created by Administrator on 2016/12/12.
 */

public class HttpLoggerInterceptor implements Interceptor {
    public static final String TAG = "OkHttpUtils";
    private String tag;
    private boolean showResponse;

    public HttpLoggerInterceptor(String tag, boolean showResponse) {
        if (TextUtils.isEmpty(tag)) {
            tag = TAG;
        }
        this.showResponse = showResponse;
        this.tag = tag;
    }

    public HttpLoggerInterceptor(String tag) {
        this(tag, true);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        logForRequest(request);
        Response originalResponse = chain.proceed(request);
        return logForResponse(originalResponse);
    }

    public Response logForResponse(Response response) {
        try {
            //===>response log
            if(response==null) return response;
            LoggerKit.PrinterHelper h = LoggerKit.with(Log.WARN, tag, "response'log");
            Response.Builder builder = response.newBuilder();
//                    .removeHeader("Pragma")
//                    .removeHeader("Cache-Control")
//                    //cache for 30 days
//                    .header("Cache-Control", "max-age=" + 3600 * 24 * 30);
            Response clone = builder.build();
            h.add("%s\t\t\t: %s", "Url", clone.request().url().toString());
            h.add("%s\t\t: %d", "Code", clone.code());
            h.add("%s\t: %s", "Protocol", clone.protocol().toString());
            h.add("%s\t: %s", "Headers", clone.headers().toString());
            if (!TextUtils.isEmpty(clone.message())) {
                h.add("%s\t: %s", "message", clone.message());
            }
            if (showResponse) {
                ResponseBody body = clone.body();
                if (body != null) {
                    MediaType mediaType = body.contentType();
                    h.add("Response body");
                    if (mediaType != null) {
                        h.add("\t%s\t: %s", "Content type", mediaType.toString());
                        if (isText(mediaType)) {
                            String resp = body.string();
                            h.add("\t%s\t: %s", "Content", resp);
                            h.add(LoggerKit.json(new String(resp.toString())));
//                            h.add(LoggerKit.json(new String(Base64.decode(resp.getBytes(), Base64.DEFAULT))), new Object[0]);
                            h.print();
                            body = ResponseBody.create(mediaType, resp);
                            return response.newBuilder().body(body).build();
                        } else {
                            h.add("\t%s\t: %s", "Content", "maybe [file part] , too large too print , ignored!");
                        }
                    }
                }
            }
            h.print();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    private void logForRequest(Request request) {
        try {
            String url = request.url().toString();
            Headers headers = request.headers();
            LoggerKit.PrinterHelper h = LoggerKit.with(Log.WARN, tag, "request'log");
            h.add("%s\t: %s", "Method", request.method());
            h.add("%s\t\t: %s", "Url", url);
            if (headers != null && headers.size() > 0) {
                h.add("%s\t: %s", "Headers", headers.toString());
            }
            RequestBody requestBody = request.body();
            if (requestBody != null) {
                MediaType mediaType = requestBody.contentType();
                h.add("Request body");
                if (mediaType != null) {
                    h.add("\t%s\t: %s", "Content type", mediaType.toString());
                    if (isText(mediaType)) {
                        h.add("\t%s\t: %s", "Content", bodyToString(request));
                    } else {
                        h.add("\t%s\t: %s", "Content", "maybe [file part] , too large too print , ignored!");
                    }
                }
            }
            h.print();
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    private boolean isText(MediaType mediaType) {
        if (mediaType.type() != null && mediaType.type().equals("text")) {
            return true;
        }
        if (mediaType.subtype() != null) {
            if (mediaType.subtype().equals("json") ||
                    mediaType.subtype().equals("xml") ||
                    mediaType.subtype().equals("html") ||
                    mediaType.subtype().equals("webviewhtml") ||
                    mediaType.subtype().equals("x-www-form-urlencoded")
                    )
                return true;
        }
        return false;
    }

    private String bodyToString(final Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "something error when show requestBody.";
        }
    }
}
