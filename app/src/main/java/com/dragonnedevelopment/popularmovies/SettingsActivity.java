package com.dragonnedevelopment.popularmovies;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

/**
 * PopularMovies Created by Muir on 13/03/2018.
 */

public class SettingsActivity extends AppCompatActivity {

    public static final String LOG_TAG = SettingsActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public static class MoviePreferenceFragment extends PreferenceFragment
            implements Preference.OnPreferenceChangeListener {

        SharedPreferences preferences;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.movie_settings);
            preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

            Preference sortByPreferences = findPreference(getString(R.string.settings_sort_by_key));
            bindPreferenceSummaryToValue(sortByPreferences);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {

            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;

                int preferenceIndex = listPreference.findIndexOfValue(stringValue);

                if (preferenceIndex >= 0) {
                    CharSequence[] labels = listPreference.getEntries();
                    preference.setSummary(labels[preferenceIndex]);
                }
            } else preference.setSummary(stringValue);
            return true;
        }

        private void bindPreferenceSummaryToValue(Preference sortByPreferences) {

            sortByPreferences.setOnPreferenceChangeListener(this);
            preferences = PreferenceManager
                    .getDefaultSharedPreferences(sortByPreferences.getContext());
            String preferenceString = preferences.getString(sortByPreferences.getKey(), "");
            onPreferenceChange(sortByPreferences, preferenceString);
        }
    }
}
