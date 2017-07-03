package com.zaclimon.aceiptv.ui.playback;

import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v17.leanback.app.VideoFragment;
import android.support.v17.leanback.app.VideoFragmentGlueHost;
import android.support.v17.leanback.media.PlaybackGlue;
import android.support.v17.leanback.media.PlaybackTransportControlGlue;

import com.zaclimon.aceiptv.ui.catchup.CatchupTvFragment;

/**
 * Fragment responsible for playing an {@link com.zaclimon.aceiptv.data.AvContent}
 *
 * @author zaclimon
 * Creation date: 02/07/17
 */

public class PlaybackFragment extends VideoFragment {

    PlaybackTransportControlGlue<ExoPlayerAdapter> mPlayerGlue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        String url = arguments.getString(CatchupTvFragment.AV_CONTENT_LINK_BUNDLE);

        ExoPlayerAdapter exoPlayerAdapter = new ExoPlayerAdapter(getActivity());
        exoPlayerAdapter.setAudioStreamType(AudioManager.USE_DEFAULT_STREAM_TYPE);
        mPlayerGlue = new AceVideoMediaPlayerGlue<>(getActivity(), exoPlayerAdapter);
        mPlayerGlue.setHost(new VideoFragmentGlueHost(this));
        mPlayerGlue.setTitle(arguments.getString(CatchupTvFragment.AV_CONTENT_TITLE_BUNDLE));
        mPlayerGlue.setSubtitle(arguments.getString(CatchupTvFragment.AV_CONTENT_GROUP_BUNDLE));
        mPlayerGlue.getPlayerAdapter().setDataSource(Uri.parse(url));

        if (mPlayerGlue.isPrepared()) {
            mPlayerGlue.play();
        } else {
            mPlayerGlue.addPlayerCallback(new PlaybackGlue.PlayerCallback() {
                @Override
                public void onPreparedStateChanged(PlaybackGlue glue) {
                    super.onPreparedStateChanged(glue);
                    if (glue.isPrepared()) {
                        glue.removePlayerCallback(this);
                        glue.play();
                    }
                }
            });
        }
        setBackgroundType(BG_LIGHT);
    }

    @Override
    public void onPause() {
        if (mPlayerGlue != null) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N || Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && !getActivity().isInPictureInPictureMode()) {
                mPlayerGlue.pause();
            }
        }
        super.onPause();

    }

}
