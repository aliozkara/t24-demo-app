package com.app.t24.utils;

import com.app.t24.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by alican on 14.04.2017.
 */

public class RestClient {

    private static OkHttpClient httpClient = new OkHttpClient.Builder()
            .readTimeout(Constants.readTimeOut, TimeUnit.SECONDS)
            .connectTimeout(Constants.connectTimeOut, TimeUnit.SECONDS)
            .writeTimeout(Constants.writeimeOut, TimeUnit.SECONDS)
            .build();


    // for decoding html tags
    private static Gson gson = new GsonBuilder()
            .disableHtmlEscaping()
            .create();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(BuildConfig.API)
                    .addConverterFactory(GsonConverterFactory.create(gson));


    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = builder.client(httpClient).build();
        return retrofit.create(serviceClass);
    }

}
