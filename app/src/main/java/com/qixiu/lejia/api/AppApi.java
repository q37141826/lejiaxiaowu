package com.qixiu.lejia.api;


import com.qixiu.lejia.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Long on 2017/2/19
 */
public final class AppApi {

    private final ApiService apiService;

    private AppApi() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS);
//                .retryOnConnectionFailure(true);
        if (BuildConfig.DEBUG) {
            //Log 拦截器，打印请求信息
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(
                    HttpLoggingInterceptor.Logger.DEFAULT);
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }
        OkHttpClient OK_HTTP_CLIENT = builder.build();

        Retrofit RETROFIT = new Retrofit.Builder()
                // text/plain
                .addConverterFactory(ScalarsConverterFactory.create())
                //gson
                .addConverterFactory(GsonConverterFactoryWrapper.create())
                //rxjava call adapter
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(OK_HTTP_CLIENT)
                .baseUrl(BuildConfig.BASE_URL)
                .build();

        apiService = RETROFIT.create(ApiService.class);
    }

    public static ApiService get() {
        return getInstance().apiService;
    }

    private static AppApi getInstance() {
        return SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static final AppApi instance = new AppApi();
    }

}
