package com.dragonnedevelopment.popularmovies.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dragonnedevelopment.popularmovies.R;

import java.util.Locale;

/**
 * PopularMovies Created by Muir on 13/03/2018.
 * A class containing common methods
 */

public class Utils {

    public static final String LOG_TAG = Utils.class.getSimpleName();


    private void Utils() {
        // private constructor
    }

    /**
     * check if a string is empty
     *
     * @param stringToCheck the string we are checking
     * @return true (if the string is empty) else return false
     */
    public static boolean isEmptyString(String stringToCheck) {
        return stringToCheck == null || stringToCheck.trim().length() == 0;
    }

    public static boolean hasConnectivity(Context context) {
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    /**
     * method to construct a Toast message
     *
     * @param context the current context (where the user is in the app)
     * @param toast   the Toast
     * @param message the message to be shown
     * @return toast
     */
    @SuppressLint("ShowToast")
    public static Toast showToastMessage(Context context, Toast toast, String message) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        return toast;
    }

    /**
     * sets custom typeface to UI elements
     *
     * @param context where the user is in the app
     * @param view    the textview in question
     */
    public static void setCustomTypeFace(Context context, View view) {
        Typeface typefaceRegular = Typeface.createFromAsset(context.getAssets(), "fonts/roboto_condensed_r.ttf");
        Typeface typefaceThin = Typeface.createFromAsset(context.getAssets(), "fonts/roboto_condensed_l.ttf");
        Typeface typefaceRegularNoto = Typeface.createFromAsset(context.getAssets(), "fonts/notosans_r.ttf");

        TextView textView = (TextView) view;

        // get the tag on the view
        String viewTag = view.getTag().toString();

        // Cast view to appropriate view element based on tag received and set typefaces
        if (viewTag.equals(context.getString(R.string.regular_roboto_cond)))
            textView.setTypeface(typefaceRegular);
        else if (viewTag.equals(context.getString(R.string.light_roboto_cond)))
            textView.setTypeface(typefaceThin);
        else if (viewTag.equals(context.getString(R.string.regular_noto)))
            textView.setTypeface(typefaceRegularNoto);
    }

    /**
     * method to convert ISO language codes to language names (e.g. en -> English, es -> Español
     *
     * @param languageCode ISO language code
     * @return language name
     */
    public static String getLanguageName(String languageCode) {
        String language;

        Locale locale = new Locale(languageCode);
        language = locale.getDisplayLanguage(locale);
        language = language.substring(0, 1).toUpperCase() + language.substring(1);

        return language;
    }
}
