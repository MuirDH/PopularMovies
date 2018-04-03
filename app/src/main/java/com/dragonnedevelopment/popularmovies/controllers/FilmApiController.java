package com.dragonnedevelopment.popularmovies.controllers;

import android.content.Context;

import com.dragonnedevelopment.popularmovies.exceptions.NoConnectivityException;
import com.dragonnedevelopment.popularmovies.utils.BuildConfig;
import com.dragonnedevelopment.popularmovies.utils.Utils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * PopularMovies Created by Muir on 13/03/2018.
 * this class is used to issue network requests to TMDb API, using the base URL provided
 */

public class FilmApiController {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(Context context) throws NoConnectivityException {

        // check if device has a connection, else throw an exception error and exit early
        if (!Utils.hasConnectivity(context)) {
            throw new NoConnectivityException();
        }

        if (retrofit == null) {
            // Create OkhttpClient.Builder object
            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            // Create HttpLoggingInterceptor object and set logging level
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            //BASIC prints request methods and response code
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

            // Couple OkHttpClient.Builder object with HttpLoggingInterceptor object, & set
            // connection timeout duration
            builder.addInterceptor(httpLoggingInterceptor);
            builder.connectTimeout(BuildConfig.DURATION_CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);

            // Create Retrofit object and attack okHttp client
            retrofit = new Retrofit.Builder().baseUrl(BuildConfig.TMDB_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(builder.build())
                    .build();
        }
        return retrofit;
    }


}
