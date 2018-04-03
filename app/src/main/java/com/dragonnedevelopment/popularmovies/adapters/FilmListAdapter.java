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
import com.dragonnedevelopment.popularmovies.models.FilmResponse;
import com.dragonnedevelopment.popularmovies.utils.BuildConfig;
import com.dragonnedevelopment.popularmovies.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * PopularMovies Created by Muir on 13/03/2018.
 * FilmListAdapter creates a list of weather forecasts to a
 * {@link android.support.v7.widget.RecyclerView}
 */

public class FilmListAdapter extends RecyclerView.Adapter<FilmListAdapter.MovieListAdapterViewHolder> {

    private FilmResponse filmResponse;
    private final MovieListAdapterOnClickHandler clickHandler;
    private List<Film> filmList;
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
    public FilmListAdapter(MovieListAdapterOnClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class MovieListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_movie_poster) ImageView imageViewPoster;

        private MovieListAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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
            Film film = filmResponse.getFilmList().get(adapterPosition);
            clickHandler.onClick(film);
        }

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
    public void onBindViewHolder(MovieListAdapterViewHolder holder, int position) {

        String poster = "";
        String posterUrl = "";

        if (position < getItemCount()) {
            Film film = filmResponse.getFilmList().get(position);
            poster = film.getPosterPath();
            if (!Utils.isEmptyString(poster)) {
                posterUrl = BuildConfig.MOVIE_POSTER_BASE_URL + poster;
                Picasso.with(context)
                        .load(posterUrl)
                        .placeholder(R.drawable.backdrop_image_placeholder)
                        .error(R.drawable.ic_no_image)
                        .into(holder.imageViewPoster);
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
        return (filmResponse == null) ? 0 : filmResponse.getFilmList().size();
    }

    /**
     * method used to refresh the movie list once the FilmListAdapter is already created, to avoid
     * creating more than one.
     *
     * @param filmResponse the new movie set to be displayed
     */
    public void setMovieData(FilmResponse filmResponse) {
        this.filmResponse = filmResponse;
        filmList = filmResponse.getFilmList();
        notifyDataSetChanged();
    }

}
