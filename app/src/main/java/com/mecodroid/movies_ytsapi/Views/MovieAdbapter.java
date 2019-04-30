package com.mecodroid.movies_ytsapi.Views;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.mecodroid.movies_ytsapi.Model.Movies;
import com.mecodroid.movies_ytsapi.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;


public class MovieAdbapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Movies> moviesList = new ArrayList<Movies>();
    Context context;
    movieVH vh;
    LoadingViewHolder lh;
    private static final int VIEW_TYPE_EMPTY_LIST_PLACEHOLDER = 0;
    private static final int VIEW_TYPE_OBJECT_VIEW = 1;
    public MovieAdbapter(Context context) {
        this.context = context;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_OBJECT_VIEW) {
            View view = LayoutInflater.from(context).inflate(R.layout.movie_item_layout, parent, false);
            return new movieVH(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        switch(getItemViewType(position)) {
            case VIEW_TYPE_EMPTY_LIST_PLACEHOLDER:
                lh = (LoadingViewHolder) holder;
                lh.progressBar.setIndeterminate(true);
                break;
            case VIEW_TYPE_OBJECT_VIEW:
                vh = (movieVH) holder;

                Picasso.with(holder.itemView.getContext())
                        .load(moviesList.get(position).getMediumCoverImage())
                        .placeholder(R.drawable.placeholder)
                        .into(vh.pic);
                vh.name.setText(moviesList.get(position).getTitle());
                vh.year.setText("(" + moviesList.get(position).getYear() + ")");
                break;
        }

    }

    @Override
    public int getItemCount() {
        return (moviesList == null || moviesList.isEmpty()) ? 1 : moviesList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (moviesList == null) {
            return VIEW_TYPE_EMPTY_LIST_PLACEHOLDER;
        } else {
            return VIEW_TYPE_OBJECT_VIEW;
        }
    }

    // loading new pages
    public void addPage(List<Movies> newList) {
        int oldLastIndex = moviesList.size() - 1; // Current postion
        moviesList.addAll(newList);
        notifyItemRangeChanged(oldLastIndex, moviesList.size() - 1);
    }



    public class movieVH extends RecyclerView.ViewHolder {
        TextView name, year;
        ImageView pic;
        ProgressBar progress2;

        public movieVH(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.mov_name);
            year = (TextView) itemView.findViewById(R.id.mov_year);
            pic = (ImageView) itemView.findViewById(R.id.mov_pic);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent details = new Intent(context, MovieDetails.class);
                    details.putExtra("film", moviesList.get(getAdapterPosition()));
                    context.startActivity(details);
                }
            });
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        }
    }

}

