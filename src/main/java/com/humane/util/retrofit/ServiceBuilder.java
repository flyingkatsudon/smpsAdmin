package com.humane.util.retrofit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.commons.codec.binary.Base64;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.concurrent.TimeUnit;

public enum ServiceBuilder {
    INSTANCE;
    private static Retrofit.Builder builder;

    static {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        // add default parameter
        httpClient.interceptors().add(chain -> {
            Request original = chain.request();

            String auth = "api:humane12!";
            byte[] encodeAuth = Base64.encodeBase64(auth.getBytes());

            // Request customization: add request headers
            Request.Builder requestBuilder = original.newBuilder()
                    .header("Authorization", "Basic " + new String(encodeAuth))
                    .header("packageName", "com.humane.smps2.app")
                    .method(original.method(), original.body());

            Request request = requestBuilder.build();
            return chain.proceed(request);
        });

        // db logging
        httpClient.interceptors().add(new HttpServiceLoggingInterceptor());

        // add timeout
        httpClient.connectTimeout(10, TimeUnit.SECONDS);
        httpClient.writeTimeout(10, TimeUnit.SECONDS);
        httpClient.readTimeout(30, TimeUnit.SECONDS);

        builder = new Retrofit.Builder()
                .client(httpClient.build())
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        ;
    }

    public <S> S createService(String url, Class<S> serviceClass) {
        return builder.baseUrl(url).build().create(serviceClass);
    }
}
