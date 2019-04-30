package com.mecodroid.movies_ytsapi.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitClient {
    private static final String API_URL = "https://yts.ag/api/v2/";

    public  static Retrofit getinstance(){
        return new Retrofit.Builder().baseUrl(API_URL).addConverterFactory(GsonConverterFactory.create()).build();
    }
    }

