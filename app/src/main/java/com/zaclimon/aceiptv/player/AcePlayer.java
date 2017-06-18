package com.zaclimon.aceiptv.player;

import android.content.Context;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.Surface;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.BehindLiveWindowException;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.media.tv.companionlibrary.TvPlayer;
import com.zaclimon.aceiptv.BuildConfig;
import com.zaclimon.aceiptv.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by isaac on 17-06-11.
 */

public class AcePlayer implements TvPlayer {

    private SimpleExoPlayer player;
    private List<TvPlayer.Callback> callbacks;

    public AcePlayer(Context context, String url) {
        callbacks = new ArrayList<>();
        init(context, url);
    }

    private void init(Context context, String url) {

        Uri mediaUrl = Uri.parse(url);
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context, Util.getUserAgent(context, context.getString(R.string.app_name)));
        final MediaSource mediaSource = new HlsMediaSource(mediaUrl, dataSourceFactory, new Handler(), null);
        player.prepare(mediaSource);

        player.addListener(new ExoPlayer.EventListener() {
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
                for (Callback tvCallback : callbacks) {
                    if (playWhenReady && playbackState == ExoPlayer.STATE_ENDED) {
                        tvCallback.onCompleted();
                    } else if (playWhenReady && playbackState == ExoPlayer.STATE_READY) {
                        tvCallback.onStarted();
                    }
                }

                if (BuildConfig.DEBUG) {
                    Log.d(AcePlayer.this.getClass().getSimpleName(), "Player state changed to " + playbackState + ", PWR: " + playWhenReady);
                }
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                if (error.getCause() instanceof BehindLiveWindowException) {
                    player.prepare(mediaSource);
                    play();
                }
            }

            @Override
            public void onPositionDiscontinuity() {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }
        });

    }

    @Override
    public void play() {
        Log.d(getClass().getSimpleName(), "play() called!");
        player.setPlayWhenReady(true);
    }

    @Override
    public void pause() {
        player.setPlayWhenReady(false);
    }

    @Override
    public void setVolume(float volume) {
        player.setVolume(volume);
    }

    @Override
    public void registerCallback(TvPlayer.Callback callback) {
        callbacks.add(callback);
    }

    @Override
    public void unregisterCallback(TvPlayer.Callback callback) {
        callbacks.remove(callback);
    }

    @Override
    public void seekTo(long positionMs) {
        player.seekTo(positionMs);
    }

    @Override
    public long getCurrentPosition() {
        return (player.getCurrentPosition());
    }

    @Override
    public void setSurface(Surface surface) {
        player.setVideoSurface(surface);
    }

    @Override
    public long getDuration() {
        return (player.getDuration());
    }

    @Override
    public void setPlaybackParams(PlaybackParams params) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PlaybackParameters playbackParameters = new PlaybackParameters(params.getSpeed(), params.getPitch());
            player.setPlaybackParameters(playbackParameters);
        }
    }

    public void stop() {
        player.stop();
    }

    public void release() {
        player.release();
    }

}
