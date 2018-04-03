package com.dragonnedevelopment.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dragonnedevelopment.popularmovies.R;
import com.dragonnedevelopment.popularmovies.models.Film;
import com.dragonnedevelopment.popularmovies.models.FilmReview;
import com.dragonnedevelopment.popularmovies.models.FilmReviewResponse;
import com.dragonnedevelopment.popularmovies.utils.Utils;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * PopularMovies Created by Muir on 29/03/2018.
 * <p>
 * {@link ReviewAdapter} creates a list of film items to a {@link RecyclerView}
 */

public class ReviewAdapter extends RecyclerView.Adapter<ViewHolder> {

    private static final String LOG_TAG = ReviewAdapter.class.getSimpleName();

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private FilmReviewResponse filmReviewResponse;
    private Film film;
    private List<FilmReview> filmReviewList;
    private Context context;

    // Initialise the data set of the Adapter which contains the data to populate views to be used by the RecyclerView
    public ReviewAdapter(FilmReviewResponse filmReviewResponse, Film film) {
        this.filmReviewResponse = filmReviewResponse;
        this.film = film;
    }

    // Custom Viewholder class for the list items
    public class ItemViewHolder extends ViewHolder {

        @BindView(R.id.expand_view)
        ExpandableTextView expandableTextView;

        @BindView(R.id.expandable_text)
        TextView textViewReview;

        @BindView(R.id.tv_review_author)
        TextView textViewReviewAuthor;

        private ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            Utils.setCustomTypeFace(context, textViewReview);
        }
    }

    // Custom Viewholder class for the list header
    public class HeaderViewHolder extends ViewHolder {

        @BindView(R.id.tv_header)
        TextView listHeader;

        private HeaderViewHolder(View headerView) {
            super(headerView);
            ButterKnife.bind(this, headerView);
        }
    }

    /**
     * return the view type to be inflated in the adapter
     *
     * @param position position in the adapter
     * @return header type (if the position is 0), or item type in all other cases
     */
    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_HEADER : TYPE_ITEM;
    }

    /**
     * called when a new ViewHolder gets created in the event of a RecyclerView being laid out.
     * This creates enough Viewholders to fill up the screen and allow scrolling.
     *
     * @return the Viewholder that holds the view for each list item.
     */

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();

        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.review_list_item, parent, false);
            return new ItemViewHolder(view);
        } else if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(context).inflate(R.layout.list_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            return null;
        }
    }

    // Called by RecyclerView to display the data at the specified position
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            headerViewHolder.listHeader.setText(context.getString(R.string.label_header_review, film.getTitle()));
        } else if (holder instanceof ItemViewHolder) {
            if (position < getItemCount()) {
                ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                FilmReview filmReview = filmReviewResponse.getFilmReviewList().get(position - 1);
                itemViewHolder.textViewReviewAuthor.setText(context.getString(R.string.label_review_by,
                        filmReview.getReviewAuthor().trim()));
                itemViewHolder.expandableTextView.setText(filmReview.getReviewContent().replace("_", "").trim());
            }
        }

    }

    // returns number of the items in the fetched list
    @Override
    public int getItemCount() {
        return (filmReviewResponse == null) ? 0 : filmReviewResponse.getFilmReviewList().size() + 1;
    }

    /**
     * Used to refresh the list once the Adapter is already created, to avoid creating a new one.
     *
     * @param filmReviewResponse the new film set to be displayed
     */
    public void setReviewData(FilmReviewResponse filmReviewResponse) {
        this.filmReviewResponse = filmReviewResponse;
        filmReviewList = filmReviewResponse.getFilmReviewList();
        notifyDataSetChanged();
    }
}
