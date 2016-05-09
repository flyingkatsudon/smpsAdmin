package com.humane.admin.smps.config;

import com.humane.admin.smps.api.RestApi;
import com.humane.util.retrofit.HttpServiceLoggingInterceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.codec.Base64;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.util.concurrent.TimeUnit;

@Configuration
public class ApiConfig {

    @Value("${humane.api.url:http://localhost:9000}") String serverUrl;

    @Bean
    public OkHttpClient okHttpClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        // add default parameter
        httpClient.interceptors().add(chain -> {
            Request original = chain.request();

            String auth = "api:humane12!";
            byte[] encodeAuth = Base64.encode(auth.getBytes());

            // Request customization: add request headers
            Request.Builder requestBuilder = original.newBuilder()
                    .header("Authorization", "Basic " + new String(encodeAuth))
                    .method(original.method(), original.body());

            Request request = requestBuilder.build();
            return chain.proceed(request);
        });

        // add logging
        // HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        // loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        // httpClient.interceptors().add(loggingInterceptor);

        // logger logging
        httpClient.interceptors().add(new HttpServiceLoggingInterceptor());

        // add timeout
        httpClient.connectTimeout(10, TimeUnit.SECONDS);
        httpClient.writeTimeout(10, TimeUnit.SECONDS);
        httpClient.readTimeout(30, TimeUnit.SECONDS);

        return httpClient.build();
    }

    @Bean
    public Retrofit retrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(serverUrl)
                .client(client)
                .build();
    }

    @Bean
    public RestApi restApi(Retrofit retrofit) {
        return retrofit.create(RestApi.class);
    }
}
