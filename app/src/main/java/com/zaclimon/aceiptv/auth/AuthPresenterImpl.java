package com.zaclimon.aceiptv.auth;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import com.google.android.media.tv.companionlibrary.XmlTvParser;
import com.google.android.media.tv.companionlibrary.model.Channel;
import com.zaclimon.aceiptv.R;
import com.zaclimon.aceiptv.util.AceChannelUtil;
import com.zaclimon.aceiptv.util.RichFeedUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by isaac on 17-06-07.
 */

public class AuthPresenterImpl implements AuthPresenter {

    private AuthView mAuthView;
    private String m3uPlaylist;
    private String epgData;
    private static final String M3U_LINK = "";
    private static final String EPG_LINK = "";

    public AuthPresenterImpl(AuthView view) {
        mAuthView = view;
    }

    @Override
    public void validateInfo(String username, String password, Context context) {

        InputStreamTest inputStreamTest = new InputStreamTest(context);
        inputStreamTest.execute();

        if (password != null && !password.isEmpty()) {
            // WIP...
        } else {
            mAuthView.onPasswordMissing();
        }
    }

    private class InputStreamTest extends AsyncTask<Void, Void, Void> {

        private Context testContext;

        public InputStreamTest(Context context) {
            testContext = context;
        }

        protected Void doInBackground(Void... params) {
            try {
                InputStream inputStream = RichFeedUtil.getInputStream(testContext, Uri.parse(M3U_LINK));
                XmlTvParser.TvListing listing = RichFeedUtil.getRichTvListings(testContext, EPG_LINK);

                if (inputStream != null) {
                    List<Channel> realChannelList = AceChannelUtil.getChannelList(inputStream, listing.getChannels());

                }

            } catch (IOException io) {
                mAuthView.onConnectionFailed(testContext.getString(R.string.wrong_credentials));
            }
            return (null);
        }

    }

}
