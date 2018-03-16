package com.dragonnedevelopment.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dragonnedevelopment.popularmovies.R;
import com.dragonnedevelopment.popularmovies.models.Film;
import com.dragonnedevelopment.popularmovies.models.MovieResponse;
import com.dragonnedevelopment.popularmovies.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.dragonnedevelopment.popularmovies.utils.BuildConfig.BACKDROP_POSTER_BASE_URL;

/**
 * PopularMovies Created by Muir on 13/03/2018.
 * MovieListAdapter creates a list of weather forecasts to a
 * {@link android.support.v7.widget.RecyclerView}
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieListAdapterViewHolder> {

    private MovieResponse movieResponse;
    private final MovieListAdapterOnClickHandler clickHandler;
    private Context context;

    /*
     * Interface to receive onClick messages
     */
    public interface MovieListAdapterOnClickHandler {
        void onClick(Film film);
    }

    /*
     * OnClick handler for the adapter which handles when a single item is clicked
     * @param clickHandler
     */
    public MovieListAdapter(MovieListAdapterOnClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    /**
     * called when a new ViewHolder gets created in the event of the RecyclerView being laid out.
     * This creates enough ViewHolders to fill up the screen and allow scrolling.
     *
     * @param parent   the viewgroup comprised of posters
     * @param viewType images
     * @return a new MovieListAdapterViewHolder which holds the View for each list item
     */
    @NonNull
    @Override
    public MovieListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        int listItemLayoutId = R.layout.movie_list_item;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = layoutInflater.inflate(listItemLayoutId, parent, shouldAttachToParentImmediately);
        return new MovieListAdapterViewHolder(view);
    }

    /**
     * method used by recyclerview to display the movie poster image
     *
     * @param holder   the viewholder where the image will be displayed
     * @param position the position in the list
     */
    @Override
    public void onBindViewHolder(@NonNull MovieListAdapterViewHolder holder, int position) {

        String poster;
        String posterUrl;

        if (position < getItemCount()) {
            Film film = movieResponse.getFilmList().get(position);
            poster = film.getPosterPath();
            if (!Utils.isEmptyString(poster)) {
                posterUrl = BACKDROP_POSTER_BASE_URL + poster;
                Picasso.with(context).load(posterUrl).into(holder.imageViewPoster);
            }
        }

    }

    /**
     * Returns number of items in the movie list
     *
     * @return number of movie items
     */
    @Override
    public int getItemCount() {
        return (movieResponse == null) ? 0 : movieResponse.getFilmList().size();
    }

    /**
     * method used to refresh the movie list once the MovieListAdapter is already created, to avoid
     * creating more than one.
     *
     * @param movieResponse the new movie set to be displayed
     */
    public void setMovieData(MovieResponse movieResponse) {
        this.movieResponse = movieResponse;
        List<Film> filmList = movieResponse.getFilmList();
        notifyDataSetChanged();
    }

    public class MovieListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView imageViewPoster;

        MovieListAdapterViewHolder(View itemView) {
            super(itemView);
            imageViewPoster = itemView.findViewById(R.id.iv_movie_poster);
            itemView.setOnClickListener(this);
        }

        /**
         * called when the child view is clicked
         *
         * @param view the child view
         */

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Film film = movieResponse.getFilmList().get(adapterPosition);
            clickHandler.onClick(film);
        }

    }
}
