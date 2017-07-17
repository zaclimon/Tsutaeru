package com.zaclimon.acetv.service;

import android.content.Context;
import android.media.tv.TvInputManager;
import android.media.tv.TvTrackInfo;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.BehindLiveWindowException;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.UnrecognizedInputFormatException;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.media.tv.companionlibrary.BaseTvInputService;
import com.google.android.media.tv.companionlibrary.TvPlayer;
import com.google.android.media.tv.companionlibrary.model.Channel;
import com.google.android.media.tv.companionlibrary.model.Program;
import com.google.android.media.tv.companionlibrary.model.RecordedProgram;
import com.zaclimon.acetv.BuildConfig;
import com.zaclimon.acetv.R;
import com.zaclimon.acetv.player.AcePlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom {@link BaseTvInputService} used for Ace TV.
 *
 * @author zaclimon
 * Creation date: 11/06/17
 */

public class AceTvInputService extends BaseTvInputService {

    @Override
    public final Session onCreateSession(String inputId) {
        AceSession aceSession = new AceSession(this, inputId);
        return (super.sessionCreated(aceSession));
    }

    /**
     * Custom {@link com.google.android.media.tv.companionlibrary.BaseTvInputService.Session} which
     * handles playback when a {@link Channel} is tuned.
     * <p>
     * It also implements an {@link com.google.android.exoplayer2.ExoPlayer.EventListener} in which
     * it can adapt better to callbacks from a {@link AcePlayer}
     */
    private class AceSession extends Session implements ExoPlayer.EventListener {

        private AcePlayer mAcePlayer;
        private Context mContext;

        /**
         * Base constructor
         *
         * @param context context which will be used for session.
         * @param inputId the input id of the application
         */
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
            mAcePlayer.addListener(this);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                notifyTimeShiftStatusChanged(TvInputManager.TIME_SHIFT_STATUS_AVAILABLE);
            }

            if (BuildConfig.DEBUG) {
                Log.d(getClass().getSimpleName(), "Video Url: " + channel.getInternalProviderData().getVideoUrl());
                Log.d(getClass().getSimpleName(), "Video format: " + channel.getVideoFormat());
            }

            // Notify when the video is available so the channel surface can be shown to the screen.
            mAcePlayer.play();
        }

        @Override
        public void onSetCaptionEnabled(boolean enabled) {
        }

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

        /**
         * Method that stops and releases a given player's resources.
         */
        public void releasePlayer() {
            if (mAcePlayer != null) {
                mAcePlayer.setSurface(null);
                mAcePlayer.stop();
                mAcePlayer.release();
                mAcePlayer = null;
            }
        }

        /**
         * Gets a list of given tracks for a given channel, whether it is video or audio.
         * <p>
         * This way, it is possible for a user to see in the Live Channels application the video
         * resolution and the audio layout.
         *
         * @return the track list usable by the Live Channels application
         */
        private List<TvTrackInfo> getAllTracks() {
            List<TvTrackInfo> tracks = new ArrayList<>();
            tracks.add(getTrack(TvTrackInfo.TYPE_VIDEO));
            tracks.add(getTrack(TvTrackInfo.TYPE_AUDIO));
            return (tracks);
        }

        /**
         * Returns a given track based on the player's video or audio format.
         *
         * @param trackType the type as defined by {@link TvTrackInfo#TYPE_VIDEO} or {@link TvTrackInfo#TYPE_AUDIO}
         * @return the related {@link TvTrackInfo} for a given player track.
         */
        private TvTrackInfo getTrack(int trackType) {

             /*
              Note that we should allow for multiple tracks. However, since this is a TV stream, it
              most likely consists of one video, one audio and one subtitle track.

              We're skipping the subtitle track since it gets mostly delayed and might not offer
              the best experience. It might get added in the future.
              */

            Format format = null;
            TvTrackInfo.Builder builder = new TvTrackInfo.Builder(trackType, Integer.toString(0));

            if (trackType == TvTrackInfo.TYPE_VIDEO) {
                format = mAcePlayer.getVideoFormat();
            } else if (trackType == TvTrackInfo.TYPE_AUDIO) {
                format = mAcePlayer.getAudioFormat();
            }

            if (format != null) {
                if (trackType == TvTrackInfo.TYPE_VIDEO) {
                    if (format.width != Format.NO_VALUE) {
                        builder.setVideoWidth(format.width);
                    }

                    if (format.height != Format.NO_VALUE) {
                        builder.setVideoHeight(format.height);
                    }
                } else {
                    builder.setAudioChannelCount(format.channelCount);
                    builder.setAudioSampleRate(format.sampleRate);
                }
            }
            return (builder.build());
        }

        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest) {

        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

        }

        @Override
        public void onLoadingChanged(boolean isLoading) {

        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

            if (playWhenReady && playbackState == ExoPlayer.STATE_READY) {
                notifyTracksChanged(getAllTracks());
                notifyTrackSelected(TvTrackInfo.TYPE_VIDEO, Integer.toString(0));
                notifyTrackSelected(TvTrackInfo.TYPE_AUDIO, Integer.toString(0));
                notifyVideoAvailable();
            }

            if (BuildConfig.DEBUG) {
                Log.d(getClass().getSimpleName(), "Player state changed to " + playbackState + ", PWR: " + playWhenReady);
            }
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
            if (error.getCause() instanceof BehindLiveWindowException) {
                Toast.makeText(mContext, R.string.stream_failure_retry, Toast.LENGTH_SHORT).show();
                mAcePlayer.restart(mContext);
                mAcePlayer.play();
            } else if (error.getCause() instanceof UnrecognizedInputFormatException) {
                // Channel cannot be played in case of an error in parsing the ".m3u8" file.
                Toast.makeText(mContext, mContext.getString(R.string.channel_stream_failure), Toast.LENGTH_SHORT).show();
            } else if (error.getCause() instanceof HttpDataSource.InvalidResponseCodeException) {

                 /*
                  We might get errors different like 403 which indicate permission denied due to multiple
                  connections at the same time or 502 meaning bad gateway.

                  Restart the loading after 5 seconds.
                  */

                SystemClock.sleep(5000);
                Toast.makeText(mContext, R.string.stream_failure_retry, Toast.LENGTH_SHORT).show();
                mAcePlayer.restart(mContext);
                mAcePlayer.play();
            } else if (error.getCause() instanceof HttpDataSource.HttpDataSourceException) {
                // Timeout, nothing we can do really...
                Toast.makeText(mContext, R.string.channel_stream_failure, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onPositionDiscontinuity() {

        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

        }

    }
}
