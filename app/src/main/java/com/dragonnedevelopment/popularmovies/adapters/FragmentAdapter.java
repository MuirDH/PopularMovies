package com.dragonnedevelopment.popularmovies.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.dragonnedevelopment.popularmovies.R;
import com.dragonnedevelopment.popularmovies.fragments.FilmDetailsFragment;
import com.dragonnedevelopment.popularmovies.fragments.FilmReviewFragment;
import com.dragonnedevelopment.popularmovies.fragments.FilmTrailerFragment;

/**
 * PopularMovies Created by Muir on 27/03/2018.
 * <p>
 * provides the appropriate {@link Fragment} for a view pager
 */
public class FragmentAdapter extends FragmentPagerAdapter {

    private static final int PAGE_COUNT = 3;
    private static final String[] TAB_TITLES = new String[]{"Details", "Trailers", "Reviews"};
    private Context context;

    private static final int[] TAB_ICON_RESID = {
            R.drawable.ic_camera_roll_white,
            R.drawable.ic_play_arrow_white,
            R.drawable.ic_message_white
    };

    /**
     * Default constructor
     */
    public FragmentAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;

        switch (position) {
            case 0:
                fragment = new FilmDetailsFragment();
                break;
            case 1:
                fragment = new FilmTrailerFragment();
                break;
            case 2:
                fragment = new FilmReviewFragment();
                break;
            default:
                fragment = new FilmDetailsFragment();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {

        return PAGE_COUNT;

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        // return image resources and titles for tab icons
        Drawable image = ContextCompat.getDrawable(context, TAB_ICON_RESID[position]);

        if (image != null) {
            image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        }

        SpannableString spannableString = new SpannableString("  " + TAB_TITLES[position]);
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);

        spannableString.setSpan(imageSpan, 0, 1, Spannable.SPAN_INTERMEDIATE);

        return spannableString;
    }
}
