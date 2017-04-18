package com.app.t24.utils;

import android.support.annotation.Nullable;

import com.app.t24.model.rest.CategoriesModel;
import com.app.t24.model.rest.NewsCategoriesModel;
import com.app.t24.model.rest.NewsContentModel;
import com.app.t24.model.rest.NewsModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by alican on 14.04.2017.
 */

public interface RestEndPoint {

    @Headers("Content-Type: application/json")
    @GET(Constants.API+Constants.API_VERSION+"/stories.json")
    Call<NewsModel> getNews(@Nullable @Query("paging") String param);

    @Headers("Content-Type: application/json")
    @GET(Constants.API+Constants.API_VERSION+"/stories.json")
    Call<NewsModel> getNews(@Nullable @Query("paging") String param, @Nullable @Query("category") String category);

    @Headers("Content-Type: application/json")
    @GET(Constants.API+Constants.API_VERSION+"/categories.json?type=story")
    Call<CategoriesModel> getCategories();

    @Headers("Content-Type: application/json")
    @GET(Constants.API+Constants.API_VERSION+"/stories.json")
    Call<NewsContentModel> getNewsContent(@Query("story") String param);

    @Headers("Content-Type: application/json")
    @GET(Constants.API+Constants.API_VERSION+"/stories.json")
    Call<NewsCategoriesModel> getNewsCategoryList(@Query("category") String category, @Query("paging") String page);
}
