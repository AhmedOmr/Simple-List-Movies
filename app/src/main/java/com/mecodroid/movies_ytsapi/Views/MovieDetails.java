package com.mecodroid.movies_ytsapi.Views;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mecodroid.movies_ytsapi.API.RetrofitClient;
import com.mecodroid.movies_ytsapi.API.YTS;
import com.mecodroid.movies_ytsapi.Model.ListResponse;
import com.mecodroid.movies_ytsapi.Model.Movies;
import com.mecodroid.movies_ytsapi.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MovieDetails extends Activity {
    RelativeLayout bg;
    TextView title, imbdRate, gnere, downloaded, castname, aduRate, syno, likes, availa;
    ImageView pic, picwatch;
    Movies film;
    Movies movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        bg = (RelativeLayout) findViewById(R.id.movieBg);
        title = (TextView) findViewById(R.id.movieName);
        gnere = (TextView) findViewById(R.id.movieGenre);
        downloaded = (TextView) findViewById(R.id.auid2);
        imbdRate = (TextView) findViewById(R.id.imdb2);
        syno = (TextView) findViewById(R.id.movieSyno);
        likes = (TextView) findViewById(R.id.Rotten2);
        castname = (TextView) findViewById(R.id.movieCast2);
        pic = (ImageView) findViewById(R.id.movePic);
        picwatch = (ImageView) findViewById(R.id.gotowatch);

        getData();

    }

    public String getLineGener() {
        StringBuilder sb = new StringBuilder();
        String line = "";

        int size = film.getGenres().size();
        for (int j = 0; j < size; j++) {
            if (j != size - 1) {
                line = film.getGenres().get(j) + " / ";
            } else {
                line = film.getGenres().get(j) + ".";
            }
            sb.append(line);
        }
        return sb.toString();
    }

    public String getCastnames() {
        StringBuilder sn = new StringBuilder();
        String lin = "";
        if (movie.getCast() !=  null ) {
            int size = movie.getCast().size();
            for (int o = 0; o < size; o++) {
                if (o != size - 1) {
                    lin = movie.getCast().get(o).getName() + " - ";
                } else {
                    lin = movie.getCast().get(o).getName() + ".";
                }


                sn.append(lin);
            }
        } else {
            sn.append("...........");
        }
        return sn.toString();

    }

    public void showfilm(final Movies movie) {

        Picasso.with(this)
                .load(movie.getBackgroundImage())
                .into(new Target() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        bg.setBackground(new BitmapDrawable(getResources(), bitmap));
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
        title.setText(movie.getTitle() + " ( " + movie.getYear() + " )");

        gnere.setText(getLineGener());

        downloaded.setText(String.valueOf(movie.getDownloadCount()));

        likes.setText(String.valueOf(movie.getLikeCount()));

        imbdRate.setText(String.valueOf(movie.getRating()));

        castname.setText(getCastnames());

        syno.setText(movie.getDescriptionFull());

        Picasso.with(this)
                .load(movie.getMediumCoverImage())
                .into(pic);
        picwatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent overview = new Intent(Intent.ACTION_VIEW);
                overview.setData(Uri.parse(movie.getUrl()));
                if (overview.resolveActivity(getPackageManager()) != null) {
                    startActivity(overview);
                } else {
                    Toast.makeText(MovieDetails.this, "something went wrong", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });


    }

    public void getData() {
        Intent moviesdata = getIntent();
        if (moviesdata != null && moviesdata.hasExtra("film")) {
            film = moviesdata.getExtras().getParcelable("film");
            Retrofit getinstance = RetrofitClient.getinstance();
            YTS yts = getinstance.create(YTS.class);
            yts.getMovieDetiles(film.getId()).enqueue(new Callback<ListResponse>() {
                @Override
                public void onResponse(Call<ListResponse> call, Response<ListResponse> response) {
                    if (response.isSuccessful()) {
                        movie = response.body().getData().getMovie();
                        showfilm(movie);
                    } else {
                        try {
                            Toast.makeText(MovieDetails.this, "code:" + response.code() + ", Error Massage: " + response.errorBody().string(), Toast.LENGTH_LONG).show();
                        } catch (IOException e) {

                            Toast.makeText(MovieDetails.this, "Error Massage: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ListResponse> call, Throwable t) {
                    Toast.makeText(MovieDetails.this, "Error Massage: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    t.printStackTrace();
                }
            });
        } else {
            Toast.makeText(MovieDetails.this, "Error Massage: Something went wrong ", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}