package com.zaclimon.acetv.ui.settings;

import android.os.AsyncTask;

import com.crashlytics.android.Crashlytics;
import com.zaclimon.acetv.BuildConfig;
import com.zaclimon.acetv.util.Constants;
import com.zaclimon.acetv.util.RichFeedUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * AsyncTask responsible for fetching the changelog if a new version is available.
 *
 * @author zaclimon
 * Creation date: 04/08/17
 */

public class AsyncDownloadChangelog extends AsyncTask<Void, Void, Integer> {

    // The tags are based on the ones used for the Google Play Store for easier updates.
    private static final String UPDATE_JSON_VERSION_CODE_TAG = "versionCode";
    private static final String UPDATE_JSON_VERSION_NAME_TAG = "versionName";
    private static final String UPDATE_JSON_CHANGELOG_TAG = "changelog";

    private static final int RESULT_SAME_VERSION = 0;
    private static final int RESULT_DIFFERENT_VERSION = 1;
    private static final int RESULT_ERROR = 2;

    private AppUpdateView mAppUpdateView;
    private String mVersionName;
    private String[] mChangelog;

    /**
     * Default constructor of the class
     * @param appUpdateView the {@link AppUpdateView} which will react based on it's callbacks.
     */
    public AsyncDownloadChangelog(AppUpdateView appUpdateView) {
        mAppUpdateView = appUpdateView;
    }

    @Override
    public Integer doInBackground(Void... params) {

        try (InputStream changelogStream = RichFeedUtil.getInputStream(Constants.ACE_CHANGELOG_URL);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(changelogStream))) {

            StringBuilder stringBuilder = new StringBuilder();
            JSONArray changelogs;
            String tempLine;

            while ((tempLine = bufferedReader.readLine()) != null) {
                stringBuilder.append(tempLine);
            }

            // The changelog elements are in a separate JSON array.
            changelogs = new JSONArray(stringBuilder.toString());

            for (int i = 0; i < changelogs.length(); i++) {
                JSONObject jsonObject = changelogs.getJSONObject(i);

                // If the changelog element is the right one
                if (jsonObject.getInt(UPDATE_JSON_VERSION_CODE_TAG) > BuildConfig.VERSION_CODE) {
                    JSONArray changelogElements = jsonObject.getJSONArray(UPDATE_JSON_CHANGELOG_TAG);
                    mVersionName = jsonObject.getString(UPDATE_JSON_VERSION_NAME_TAG);
                    mChangelog = new String[changelogElements.length()];

                    for (int j = 0; j < changelogElements.length(); j++) {
                        mChangelog[j] = (String) changelogElements.get(j);
                    }
                }
            }

            // Only one verification would suffice but this a double check shouldn't hurt.
            if (mVersionName != null && mChangelog != null) {
                return (RESULT_DIFFERENT_VERSION);
            }

        } catch (IOException | JSONException jsio) {
            Crashlytics.logException(jsio);
            return (RESULT_ERROR);
        }

        return (RESULT_SAME_VERSION);
    }

    @Override
    public void onPostExecute(Integer result) {

        switch (result) {
            case RESULT_DIFFERENT_VERSION:
                mAppUpdateView.onNewVersionDetected(mVersionName, mChangelog);
                break;
            case RESULT_SAME_VERSION:
                mAppUpdateView.onSameVersionDetected();
                break;
            default:
                mAppUpdateView.onFetchFailed();
        }
    }

}
