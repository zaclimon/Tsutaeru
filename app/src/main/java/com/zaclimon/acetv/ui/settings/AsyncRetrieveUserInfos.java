package com.zaclimon.acetv.ui.settings;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.zaclimon.acetv.ui.settings.userinfo.UserInfoView;
import com.zaclimon.acetv.util.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;

/**
 * Async class that will retrieve a given user's information.
 *
 * @author zaclimon
 * Creation date: 03/07/17
 */

public class AsyncRetrieveUserInfos extends AsyncTask<Void, Void, Boolean> {

    private static final String USER_INFO_JSON_OBJECT = "user_info";
    private static final String STATUS_JSON_OBJECT = "status";
    private static final String EXPIRATION_DATE_JSON_OBJECT = "exp_date";
    private static final String TRIAL_ACCOUNT_JSON_OBJECT = "is_trial";
    private static final String MAX_CONNECTION_JSON_OBJECT = "max_connections";

    private UserInfoView mUserInfoView;
    private String mStatus;
    private Date mExpirationDate;
    private boolean mTrial;
    private int mMaxConnections;

    /**
     * Default constructor of the class
     *
     * @param userInfoView the view that will receive the concerned callbacks.
     */
    public AsyncRetrieveUserInfos(UserInfoView userInfoView) {
        mUserInfoView = userInfoView;
    }

    @Override
    public Boolean doInBackground(Void... params) {

        try {
            // The ace endpoint returns a Json file. Let's parse it...
            InputStream inputStream = NetworkUtils.getNetworkInputStream(mUserInfoView.getUserInfoApiEndpoint());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            int character;

            while ((character = bufferedReader.read()) != -1) {
                stringBuilder.append((char) character);
            }

            String jsonString = stringBuilder.toString();

            if (!TextUtils.isEmpty(jsonString)) {
                JSONObject baseJsonObject = new JSONObject(jsonString);
                JSONObject realJsonObject = baseJsonObject.getJSONObject(USER_INFO_JSON_OBJECT);
                Calendar calendar = Calendar.getInstance();

                mStatus = realJsonObject.getString(STATUS_JSON_OBJECT);

                /*
                 The returned date is in unix time, which is in seconds since the January 1st 1970.

                 The time zone as set in the original Ace TV application was set according to
                 GMT +2 (Central european/CET as defined now) let's set it according to the user's
                 actual time zone.
                 */

                long expirationDateSeconds = realJsonObject.getLong(EXPIRATION_DATE_JSON_OBJECT);
                calendar.setTimeInMillis(expirationDateSeconds * 1000);
                mExpirationDate = calendar.getTime();

                int trial = realJsonObject.getInt(TRIAL_ACCOUNT_JSON_OBJECT);

                if (trial == 1) {
                    mTrial = true;
                }

                mMaxConnections = realJsonObject.getInt(MAX_CONNECTION_JSON_OBJECT);
                return (true);
            }
        } catch (IOException | JSONException jio) {
            // Nothing we can do really...
        }
        return (false);
    }

    @Override
    public void onPostExecute(Boolean result) {

        if (result) {
            mUserInfoView.onConnectionSuccess(mStatus, mExpirationDate, mTrial, mMaxConnections);
        } else {
            mUserInfoView.onConnectionFailed();
        }

    }

}
