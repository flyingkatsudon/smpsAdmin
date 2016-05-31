package com.humane.util.retrofit;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSource;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class HttpServiceLoggingInterceptor implements Interceptor {
    private final HttpLoggingInterceptor httpLoggingInterceptor;

    public HttpServiceLoggingInterceptor() {
        httpLoggingInterceptor = new HttpLoggingInterceptor();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        log.debug("--> {} {}", request.method(), request.url());
        long startNs = System.nanoTime();
        Response response = httpLoggingInterceptor.intercept(chain);

        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        BufferedSource source = response.body().source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();
        log.debug("<-- {} {} {} {}bytes ({}ms)", response.code(), response.message(), request.url(), buffer.size(), tookMs);
        return response;
    }

    public void setLevel(HttpLoggingInterceptor.Level level) {
        httpLoggingInterceptor.setLevel(level);
    }
}