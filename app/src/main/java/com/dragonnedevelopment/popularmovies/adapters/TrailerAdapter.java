package com.dragonnedevelopment.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dragonnedevelopment.popularmovies.R;
import com.dragonnedevelopment.popularmovies.models.Film;
import com.dragonnedevelopment.popularmovies.models.FilmTrailer;
import com.dragonnedevelopment.popularmovies.models.FilmTrailerResponse;
import com.dragonnedevelopment.popularmovies.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v7.widget.RecyclerView.ViewHolder;

/**
 * PopularMovies Created by Muir on 29/03/2018.
 *
 * {@link TrailerAdapter} creates a list of film items to a {@link android.support.v7.widget.RecyclerView}
 */

public class TrailerAdapter extends RecyclerView.Adapter<ViewHolder> {

    private  static final String LOG_TAG = TrailerAdapter.class.getSimpleName();

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private FilmTrailerResponse filmTrailerResponse;
    private Film film;
    private List<FilmTrailer> filmTrailerList;
    private TrailerAdapter.TrailerAdapterOnClickHandler clickHandler;
    private Context context;

    /**
     * Called when a new ViewHolder gets created in the event of a RecyclerView being laid out.
     * This creates enough ViewHolders to fill up the screen and allow scrolling.

     * @return ViewHolder which holds the View for each list item
     */
    @Nullable
    @Override
    public ViewHolder onCreateViewHolder(@Nullable ViewGroup viewGroup, int viewType) {
        if (viewGroup != null) {
            context = viewGroup.getContext();
        }

        if (viewType == TYPE_ITEM) {

            View view = LayoutInflater.from(context).inflate(R.layout.trailer_list_item, viewGroup, false);
            return new TrailerAdapter.ItemViewHolder(view);

        }else if (viewType == TYPE_HEADER) {

            View view = LayoutInflater.from(context).inflate(R.layout.list_header, viewGroup, false);
            return new TrailerAdapter.HeaderViewHolder(view);

        }else {

            return null;

        }

    }

    /**
     * called by the RecyclerView to display the data at the specified position
     */

    @Override
    public void onBindViewHolder(@Nullable ViewHolder holder, int position) {

        if (holder instanceof HeaderViewHolder) {

            HeaderViewHolder viewHolder = (HeaderViewHolder) holder;
            viewHolder.listHeader.setText(context.getString(R.string.label_header_trailer, film.getTitle()));

        } else if (holder instanceof ItemViewHolder) {

            if (position < getItemCount()) {

                ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                FilmTrailer filmTrailer = filmTrailerResponse.getFilmTrailerList().get(position - 1);

                if (!Utils.isEmptyString(filmTrailer.getTrailerKey())){

                    Picasso.with(context)
                            .load(filmTrailer.getVideoThumbnailImage(filmTrailer))
                            .placeholder(R.drawable.ic_logo)
                            .error(R.drawable.ic_no_image)
                            .into(itemViewHolder.imageViewTrailerThumbnail);

                }

                itemViewHolder.textViewTrailerName.setText(filmTrailer.getTrailerName().trim());

            }
        }

    }

    /**
     * @return the number of items in the fetched list
     */
    @Override
    public int getItemCount() {

        return (filmTrailerResponse == null) ? 0 : filmTrailerResponse.getFilmTrailerList().size() + 1;

    }

    /**
     * Method used to refresh the list once the Adapter is already created, to avoid creating a new
     * Adapter.
     * @param filmTrailerResponse the new film set to be displayed
     */
    public void setTrailerData (FilmTrailerResponse filmTrailerResponse) {

        this.filmTrailerResponse = filmTrailerResponse;
        this.filmTrailerList = filmTrailerResponse.getFilmTrailerList();

        notifyDataSetChanged();
    }

    // interface which receives onClick messages
    public interface TrailerAdapterOnClickHandler {

        void onClick(FilmTrailer filmTrailer);
    }

    /**
     * Initialise the dataset of the Adapter that contains the data to populate views to be used by
     * the RecyclerView.
     */

    public TrailerAdapter (FilmTrailerResponse filmTrailerResponse, Film film, TrailerAdapterOnClickHandler clickHandler) {
        this.filmTrailerResponse = filmTrailerResponse;
        this.film = film;
        this.clickHandler = clickHandler;
    }

    /**
     * Custom ViewHolder class for the list items
     */
    public class ItemViewHolder extends ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_trailer_thumbnail)
        ImageView imageViewTrailerThumbnail;

        @BindView(R.id.tv_trailer_name)
        TextView textViewTrailerName;

        private ItemViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            Utils.setCustomTypeFace(context, textViewTrailerName);
            itemView.setOnClickListener(this);

        }

        /**
         * this gets called when the child view is clicked
         * @param view the child view
         */
        @Override
        public void onClick(View view) {

            int adapterPosition = getAdapterPosition();
            FilmTrailer filmTrailer = filmTrailerList.get(adapterPosition - 1);
            clickHandler.onClick(filmTrailer);

        }

    }

    /**
     * custom Viewholder class for the list header
     */
    public class HeaderViewHolder extends ViewHolder {

        @BindView(R.id.tv_header)
        TextView listHeader;

        private HeaderViewHolder(View headerView) {
            super(headerView);
            ButterKnife.bind(this, headerView);
        }
    }

}
