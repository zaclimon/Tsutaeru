package com.zaclimon.aceiptv.service;

import android.content.Context;
import android.media.tv.TvInputManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.Surface;

import com.google.android.media.tv.companionlibrary.BaseTvInputService;
import com.google.android.media.tv.companionlibrary.TvPlayer;
import com.google.android.media.tv.companionlibrary.model.Channel;
import com.google.android.media.tv.companionlibrary.model.InternalProviderData;
import com.google.android.media.tv.companionlibrary.model.Program;
import com.google.android.media.tv.companionlibrary.model.RecordedProgram;
import com.zaclimon.aceiptv.BuildConfig;
import com.zaclimon.aceiptv.player.AcePlayer;

/**
 * Created by isaac on 17-06-11.
 */

public class AceInputTvService extends BaseTvInputService {

    @Override
    public final Session onCreateSession(String inputId) {
        AceSession aceSession = new AceSession(this, inputId);
        return (super.sessionCreated(aceSession));
    }

    private class AceSession extends Session {

        private AcePlayer mAcePlayer;
        private Context mContext;

        public AceSession(Context context, String inputId) {
            super(context, inputId);
            mContext = context;
        }

        @Override
        public boolean onPlayProgram(Program program, long startPosMs) {
            return (true);
        }

        @Override
        public void onPlayChannel(Channel channel) {
            mAcePlayer = new AcePlayer(mContext, channel.getInternalProviderData().getVideoUrl());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                notifyTimeShiftStatusChanged(TvInputManager.TIME_SHIFT_STATUS_AVAILABLE);
            }

            if (BuildConfig.DEBUG) {
                Log.d(getClass().getSimpleName(), "Video Url: " + channel.getInternalProviderData().getVideoUrl());
                Log.d(getClass().getSimpleName(), "Video format: " + channel.getVideoFormat());
            }

            // Notify when the video is available so the channel surface can be shown to the screen.
            mAcePlayer.play();
            notifyVideoAvailable();
        }

        @Override
        public void onSetCaptionEnabled(boolean enabled) {}

        @Override
        public boolean onPlayRecordedProgram(RecordedProgram recordedProgram) {
            return (false);
        }

        @Override
        public TvPlayer getTvPlayer() {
            return (mAcePlayer);
        }

        @Override
        public boolean onTune(Uri channelUri) {

            Log.d(getClass().getSimpleName(), "Tune to " + channelUri.toString());

            // Notify the video to be unavailable in order to not show artifacts when changing channels.
            notifyVideoUnavailable(TvInputManager.VIDEO_UNAVAILABLE_REASON_TUNING);
            releasePlayer();
            return super.onTune(channelUri);
        }

        @Override
        public void onRelease() {
            super.onRelease();
            releasePlayer();
        }

        public void releasePlayer() {
            if (mAcePlayer != null) {
                mAcePlayer.setSurface(null);
                mAcePlayer.stop();
                mAcePlayer.release();
                mAcePlayer = null;
            }
        }
    }
}
