package com.lib.rinika.giraffeplayer;


import android.os.Handler;
import android.widget.FrameLayout;

import tcking.github.com.giraffeplayer2.DefaultPlayerListener;
import tcking.github.com.giraffeplayer2.GiraffePlayer;
import tcking.github.com.giraffeplayer2.Option;
import tcking.github.com.giraffeplayer2.PlayerManager;
import tcking.github.com.giraffeplayer2.VideoView;
import tcking.github.com.giraffeplayer2.PlayerListener;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import tv.danmaku.ijk.media.player.IMediaPlayer;


public class GiraffePlayerView extends FrameLayout implements LifecycleEventListener {

    private ThemedReactContext mThemedReactContext;
    private RCTEventEmitter mEventEmitter;
    private VideoView videoView;
    private Runnable mProgressUpdateRunnable = null;
    private Handler mProgressUpdateHandler = new Handler();

    private boolean mPaused = true;
    private boolean mCompleted = false;
    private String mSrcString;

    public static final String EVENT_PROP_DURATION = "duration";
    public static final String EVENT_PROP_CURRENT_TIME = "currentTime";
    public static final String EVENT_PROP_POSITION = "position";
    public static final String EVENT_PROP_END = "endReached";
    public static final String EVENT_PROP_SEEK_TIME = "seekTime";

    public static final String EVENT_PROP_ERROR = "error";
    public static final String EVENT_PROP_WHAT = "what";
    public static final String EVENT_PROP_EXTRA = "extra";

    public enum Events {
        EVENT_PROGRESS("onGRProgress"),
        EVENT_ENDED("onGREnded"),
        EVENT_STOPPED("onGRStopped"),
        EVENT_PLAYING("onGRPlaying"),
        EVENT_BUFFERING("onGRBuffering"),
        EVENT_PAUSED("onGRPaused"),
        EVENT_ERROR("onGRError"),
        EVENT_SEEK("onGRVideoSeek");

        private final String mName;

        Events(final String name) {
            mName = name;
        }

        @Override
        public String toString() {
            return mName;
        }
    }


    private PlayerListener playerListener = new DefaultPlayerListener(){//example of using playerListener

        @Override
        public void onPrepared(GiraffePlayer giraffePlayer) {
            mPaused = true;
            videoView.getPlayer().pause();
        }

        @Override
        public boolean onInfo(GiraffePlayer giraffePlayer, int what, int extra) {
            switch(what) {
                case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                    mEventEmitter.receiveEvent(getId(), Events.EVENT_BUFFERING.toString(), Arguments.createMap());
                    break;
            }
            return false;
        }

        @Override
        public void onCompletion(GiraffePlayer giraffePlayer) {
            WritableMap completion = Arguments.createMap();
            mPaused = true;
            mCompleted = true;
            completion.putBoolean(EVENT_PROP_END, true);
            mEventEmitter.receiveEvent(getId(), Events.EVENT_ENDED.toString(), completion);
        }

        @Override
        public void onSeekComplete(GiraffePlayer giraffePlayer) {
            WritableMap seek = Arguments.createMap();
            seek.putDouble(EVENT_PROP_CURRENT_TIME, videoView.getPlayer().getCurrentPosition());
            seek.putDouble(EVENT_PROP_DURATION, videoView.getPlayer().getDuration());
            mEventEmitter.receiveEvent(getId(), Events.EVENT_PROGRESS.toString(), seek);
        }

        @Override
        public boolean onError(GiraffePlayer giraffePlayer, int what, int extra) {
            WritableMap error = Arguments.createMap();
            error.putInt(EVENT_PROP_WHAT, what);
            error.putInt(EVENT_PROP_EXTRA, extra);
            WritableMap event = Arguments.createMap();
            event.putMap(EVENT_PROP_ERROR, error);
            mEventEmitter.receiveEvent(getId(), Events.EVENT_ERROR.toString(), event);
            return true;
        }

        @Override
        public void onPause(GiraffePlayer giraffePlayer) {
            mEventEmitter.receiveEvent(getId(), Events.EVENT_PAUSED.toString(), null);
        }

        @Override
        public void onPlaying(GiraffePlayer giraffePlayer) {
            WritableMap playing = Arguments.createMap();
            playing.putDouble(EVENT_PROP_DURATION, videoView.getPlayer().getDuration());
            mEventEmitter.receiveEvent(getId(), Events.EVENT_PLAYING.toString(), playing);
        }

    };

    public GiraffePlayerView(ThemedReactContext context) {
        super(context.getCurrentActivity());
        mThemedReactContext = context;
        mEventEmitter = mThemedReactContext.getJSModule(RCTEventEmitter.class);
        mThemedReactContext.addLifecycleEventListener(this);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.player, this);
        videoView = (VideoView) findViewById(R.id.video_view);
        PlayerManager.getInstance().getDefaultVideoInfo().addOption(Option.create(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "multiple_requests", 1L));
        videoView.setPlayerListener(playerListener);
    }

    private void setMedia(String filePath) {
        videoView.setVideoPath(filePath).setFingerprint(videoView.hashCode());
        videoView.getPlayer().initialize();
        checkProgress();
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

    private void checkProgress () {
        mProgressUpdateRunnable = new Runnable() {
            @Override
            public void run() {

                if (!mCompleted && !mPaused) {
                    WritableMap event = Arguments.createMap();
                    event.putDouble(EVENT_PROP_CURRENT_TIME, videoView.getPlayer().getCurrentPosition());
                    event.putDouble(EVENT_PROP_DURATION,  videoView.getPlayer().getDuration());
                    mEventEmitter.receiveEvent(getId(), Events.EVENT_PROGRESS.toString(), event);

                    // Check for update after an interval
                    mProgressUpdateHandler.postDelayed(mProgressUpdateRunnable,250);
                }
            }
        };
    }

    public void setFilePath(String filePath) {
        this.mSrcString = filePath;
        setMedia(mSrcString);
    }

    private void start() {
        mPaused = false;
        mCompleted = false;
        videoView.getPlayer().start();
        mProgressUpdateHandler.post(mProgressUpdateRunnable);
    }

    public void setVolume(float volume) {
        videoView.getPlayer().setVolume(volume, volume);
    }

    public void setPaused(boolean paused) {
        mPaused = paused;
        if (paused) {
            videoView.getPlayer().pause();
        } else {
            start();
        }
    }

    public void seek(float seek) {
        WritableMap event = Arguments.createMap();
        event.putDouble(EVENT_PROP_CURRENT_TIME, videoView.getPlayer().getCurrentPosition());
        event.putDouble(EVENT_PROP_SEEK_TIME, seek);
        mEventEmitter.receiveEvent(getId(), Events.EVENT_SEEK.toString(), event);
        int newPosition = (int) ((videoView.getPlayer().getDuration() * seek * 1.0));
        videoView.getPlayer().seekTo(newPosition);
    }

    public void onDropViewInstance() {
        videoView.getPlayer().release();
    }


}
