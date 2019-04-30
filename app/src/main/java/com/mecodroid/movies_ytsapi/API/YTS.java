package com.mecodroid.movies_ytsapi.API;

import com.mecodroid.movies_ytsapi.Model.ListResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface YTS {
    @GET("list_movies.json?limit=30")
    Call<ListResponse> getListByPage(@Query("page") int p);

    @GET("movie_details.json?with_images=true&with_cast=true")
    Call<ListResponse> getMovieDetiles(@Query("movie_id") int id);
}
