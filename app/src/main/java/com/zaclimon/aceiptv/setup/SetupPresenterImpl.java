package com.zaclimon.aceiptv.setup;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.media.tv.companionlibrary.XmlTvParser;
import com.google.android.media.tv.companionlibrary.model.Channel;
import com.google.android.media.tv.companionlibrary.model.Program;
import com.zaclimon.aceiptv.rich.RichFeedUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by isaac on 17-06-07.
 */

public class SetupPresenterImpl implements SetupPresenter {

    private SetupView setupView;
    private String m3uPlaylist;
    private String epgData;
    private static final String M3U_LINK = "";
    private static final String EPG_LINK = "";

    public SetupPresenterImpl(SetupView view) {
        setupView = view;
    }

    @Override
    public void validateInfo(String username, String password, Context context) {
        if (password != null && !password.isEmpty()) {
            InputStreamTest inputStreamTest = new InputStreamTest(context);
            inputStreamTest.execute();
        } else {
            setupView.onPasswordMissing();
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
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;

                    /*while ((line = bufferedReader.readLine()) != null) {
                        Log.d(getClass().getSimpleName(), line);
                    }*/
                }

                for (Channel channel : listing.getChannels()) {
                    Log.d(getClass().getSimpleName(), "Channel id: " + channel.getOriginalNetworkId());
                }

                for (Program program : listing.getAllPrograms()) {
                    Log.d(getClass().getSimpleName(), Long.toString(program.getChannelId()));
                }

            } catch (IOException io) {
                setupView.onConnectionFailed();
            }
            return (null);
        }

    }

}
