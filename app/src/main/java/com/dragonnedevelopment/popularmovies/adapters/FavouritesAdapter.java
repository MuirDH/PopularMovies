package com.dragonnedevelopment.popularmovies.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dragonnedevelopment.popularmovies.R;
import com.dragonnedevelopment.popularmovies.utils.BuildConfig;
import com.dragonnedevelopment.popularmovies.utils.Utils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dragonnedevelopment.popularmovies.data.FilmContract.FilmsEntry;

/**
 * PopularMovies Created by Muir on 29/03/2018.
 * <p>
 * {@link FavouritesAdapter} an adapter which creates a list of film items to a {@link RecyclerView}
 */
public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.ItemViewHolder> {

    private static final String LOG_TAG = FavouritesAdapter.class.getSimpleName();

    private Cursor mCursor;
    private Context context;

    public FavouritesAdapter(Context context) {
        this.context = context;
    }

    /**
     * Custom ViewHolder class for the list items
     */
    class ItemViewHolder extends RecyclerView.ViewHolder {

        // Bind the image view for a single film in the user's favourite list
        @BindView(R.id.iv_favourite_film)
        ImageView imageViewFavouriteFilm;

        // Bing the text view for a film in the user's favourite list
        @BindView(R.id.text_favourite_film_name)
        TextView textViewFavouriteFilm;

        private ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            Utils.setCustomTypeFace(context, textViewFavouriteFilm);

        }

    }

    /**
     * this method is called when a new Viewholder gets created in the event of RecyclerView being
     * laid out. This creates enough ViewHolders to fill up the screen and allow scrolling
     */
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.favourites_list_item, parent, false);
        return new ItemViewHolder(view);

    }

    /**
     * this method is called by the RecyclerView to display the film data at the specified position
     *
     * @param holder   the ItemViewHolder
     * @param position the place specified by the RecyclerView
     */
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        String filmPosterUrl;

        // Get indices for the _id, film id, film title, and film poster image columns in the Db.
        int idIndex = mCursor.getColumnIndex(FilmsEntry._ID);
        int filmIdIndex = mCursor.getColumnIndex(FilmsEntry.COLUMN_FILM_ID);
        int filmTitleIndex = mCursor.getColumnIndex(FilmsEntry.COLUMN_FILM_TITLE);
        int filmPosterIndex = mCursor.getColumnIndex(FilmsEntry.COLUMN_POSTER_PATH);

        // fetch the correct location in the cursor
        mCursor.moveToPosition(position);

        // fetch data
        final int rowId = mCursor.getInt(idIndex);
        final int filmId = mCursor.getInt(filmIdIndex);
        String filmTitle = mCursor.getString(filmTitleIndex);
        String filmPoster = mCursor.getString(filmPosterIndex);

        // set values
        holder.itemView.setTag(rowId);
        holder.textViewFavouriteFilm.setText(filmTitle);

        if (!Utils.isEmptyString(filmPoster)) {
            filmPosterUrl = BuildConfig.MOVIE_POSTER_BASE_URL + filmPoster;
            Picasso.with(context)
                    .load(filmPosterUrl)
                    .placeholder(R.drawable.ic_logo)
                    .error(R.drawable.ic_no_image)
                    .into(holder.imageViewFavouriteFilm);
        }

    }

    /**
     * Swap the old Cursor with a newly updated Cursor when the data changes and a re-query occurs.
     *
     * @param cursor the cursor we are updating
     * @return tempCursor
     */
    public Cursor swapCursor(Cursor cursor) {

        // check if this cursor is the same as the previous cursor
        if (mCursor == cursor) {
            return null;
        }

        // store the old cursor value in a temp variable
        Cursor tempCursor = mCursor;
        // assign new cursor value
        mCursor = cursor;

        // check if the cursor is valid, then update it
        if (cursor != null) {
            this.notifyDataSetChanged();
        }

        return tempCursor;

    }

    /**
     * returns the number of items in the fetched list
     *
     * @return the number of list items.
     */
    @Override
    public int getItemCount() {

        return (mCursor == null) ? 0 : mCursor.getCount();

    }


}
