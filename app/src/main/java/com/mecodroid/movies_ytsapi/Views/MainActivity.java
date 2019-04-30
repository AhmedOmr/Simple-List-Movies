package com.mecodroid.movies_ytsapi.Views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mecodroid.movies_ytsapi.API.CheckInternet;
import com.mecodroid.movies_ytsapi.API.RetrofitClient;
import com.mecodroid.movies_ytsapi.API.YTS;
import com.mecodroid.movies_ytsapi.Begin.Beginning;
import com.mecodroid.movies_ytsapi.Model.ListResponse;
import com.mecodroid.movies_ytsapi.Model.Movies;
import com.mecodroid.movies_ytsapi.R;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    RecyclerView list;
    MovieAdbapter adapter;
    GridLayoutManager gridLayoutManager;
    CheckInternet checkInternet;
    Boolean connecting;
    HashMap<Integer, Boolean> pageStats = new HashMap<>();
    public int lastPageLoaded;
    Context context;
    List<Movies> moviesList1;
ProgressBar barpro1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkInternet = new CheckInternet(this);

        connecting = checkInternet.Is_Connecting();
        if (!connecting) {
            Toast.makeText(MainActivity.this, "please check Internet Connection", Toast.LENGTH_LONG).show();
            startActivity(new Intent(MainActivity.this, Beginning.class));
        } else {
            loadPage(1); // loading first page
            setuprecycler();
        }
    }

    private void setuprecycler() {
        barpro1 = findViewById(R.id.bar3);

        list = findViewById(R.id.rvlist);
        gridLayoutManager = new GridLayoutManager(this, 2);
        list.setLayoutManager(gridLayoutManager);

        adapter = new MovieAdbapter(this);
        list.setAdapter(adapter);
        list.addOnScrollListener(new PaginationScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                loadPage(current_page);

            }

        });

    }

    public void loadPage(final int pageNum) {
        if (pageStats.get(pageNum) == null) {

            Retrofit getinstance = RetrofitClient.getinstance();
            YTS yts = getinstance.create(YTS.class);
            yts.getListByPage(pageNum).enqueue(new Callback<ListResponse>() {

                @Override
                public void onResponse(Call<ListResponse> call, Response<ListResponse> response) {
                    if (response.isSuccessful()) {
                      barpro1.setVisibility(View.GONE);

                        moviesList1 = response.body().getData().getMovies();

                        adapter.addPage(moviesList1);


                        pageStats.put(pageNum, true);

                        lastPageLoaded = pageNum;

                    } else {
                        try {
                            Toast.makeText(MainActivity.this, "code:" + response.code() + " \n" + ",err:" + response.errorBody().string(), Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            Toast.makeText(MainActivity.this, "Massage:" + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        } finally {
                            pageStats.put(pageNum, false);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ListResponse> call, Throwable t) {
                    pageStats.put(pageNum, false);
                    Toast.makeText(MainActivity.this, "Error Massage:" + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    t.printStackTrace();

                }
            });
        }

    }


}

