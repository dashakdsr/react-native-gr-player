package com.lib.rinika.giraffeplayer;


import android.net.Uri;
import android.widget.FrameLayout;

import tcking.github.com.giraffeplayer2.GiraffePlayer;
import tcking.github.com.giraffeplayer2.VideoView;
import tcking.github.com.giraffeplayer2.PlayerListener;
import tv.danmaku.ijk.media.player.IjkTimedText;

import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.events.RCTEventEmitter;


public class GiraffePlayerView extends FrameLayout implements LifecycleEventListener, PlayerListener {

    private ThemedReactContext mThemedReactContext;
    private RCTEventEmitter mEventEmitter;
    private VideoView videoView;
    private boolean pausedState;

    private String mSrcString;


    public GiraffePlayerView(ThemedReactContext context) {
        super(context);
        mThemedReactContext = context;
        mEventEmitter = mThemedReactContext.getJSModule(RCTEventEmitter.class);
        mThemedReactContext.addLifecycleEventListener(this);
        inflate(getContext(), R.layout.player, this);
        videoView = (VideoView) findViewById(R.id.video_view);
    }

    private void setMedia(String filePath) {
        videoView.setVideoPath(filePath);
    }


    @Override
    public void onHostResume() {

    }

    @Override
    public void onHostPause() {

    }

    @Override
    public void onHostDestroy() {

    }

    public void setFilePath(String filePath) {
        this.mSrcString = filePath;
        setMedia(mSrcString);
    }

    public void setVolume(float volume) {
        videoView.getPlayer().setVolume(volume, volume);
    }

    public void setPaused(boolean paused) {
        pausedState = paused;
        if (paused) {
            if (videoView.getPlayer().isPlaying()) {
                videoView.getPlayer().pause();
            }
        } else {
            if (!videoView.getPlayer().isPlaying()) {
                videoView.getPlayer().start();
            }
        }
    }

    @Override
    public void onPrepared(GiraffePlayer giraffePlayer) {

    }

    @Override
    public void onBufferingUpdate(GiraffePlayer giraffePlayer, int percent) {

    }

    @Override
    public boolean onInfo(GiraffePlayer giraffePlayer, int what, int extra) {
        return false;
    }

    @Override
    public void onCompletion(GiraffePlayer giraffePlayer) {

    }

    @Override
    public void onSeekComplete(GiraffePlayer giraffePlayer) {

    }

    @Override
    public boolean onError(GiraffePlayer giraffePlayer, int what, int extra) {
        return false;
    }

    @Override
    public void onPause(GiraffePlayer giraffePlayer) {

    }

    @Override
    public void onRelease(GiraffePlayer giraffePlayer) {

    }

    @Override
    public void onStart(GiraffePlayer giraffePlayer) {

    }

    @Override
    public void onTargetStateChange(int oldState, int newState) {

    }

    @Override
    public void onCurrentStateChange(int oldState, int newState) {

    }

    @Override
    public void onDisplayModelChange(int oldModel, int newModel) {

    }

    @Override
    public void onPreparing(GiraffePlayer giraffePlayer) {

    }

    @Override
    public void onTimedText(GiraffePlayer giraffePlayer, IjkTimedText text) {

    }
}
