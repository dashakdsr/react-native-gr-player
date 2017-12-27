package com.lib.rinika.giraffeplayer;


import android.net.Uri;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import tcking.github.com.giraffeplayer2.DefaultPlayerListener;
import tcking.github.com.giraffeplayer2.GiraffePlayer;
import tcking.github.com.giraffeplayer2.Option;
import tcking.github.com.giraffeplayer2.PlayerManager;
import tcking.github.com.giraffeplayer2.VideoInfo;
import tcking.github.com.giraffeplayer2.VideoView;
import tcking.github.com.giraffeplayer2.PlayerListener;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.IjkTimedText;

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
    private boolean pausedState;
    private GiraffePlayer player;
    private PlayerListener playerListener = new DefaultPlayerListener(){//example of using playerListener
        WritableMap eventMap = Arguments.createMap();
        @Override
        public void onPreparing(GiraffePlayer giraffePlayer) {
            Toast.makeText(getContext(), "start playing:"+giraffePlayer.getVideoInfo().getUri(),Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBufferingUpdate(GiraffePlayer giraffePlayer, int percent) {
            super.onBufferingUpdate(giraffePlayer, percent);
        }

        @Override
        public boolean onInfo(GiraffePlayer giraffePlayer, int what, int extra) {
            switch(what) {
                case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                    mEventEmitter.receiveEvent(getId(), Events.EVENT_BUFFERING.toString(), eventMap);
                    break;
            }
            return false;
        }

        @Override
        public void onCompletion(GiraffePlayer giraffePlayer) {
//            Log.v("comlpletion", String.valueOf(eventMap));
            Toast.makeText(getContext(), "play completion:"+giraffePlayer.getVideoInfo().getUri(),Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSeekComplete(GiraffePlayer giraffePlayer) {
            eventMap.putDouble(EVENT_PROP_CURRENT_TIME, videoView.getPlayer().getCurrentPosition());
            eventMap.putDouble(EVENT_PROP_DURATION, videoView.getPlayer().getDuration());
            mEventEmitter.receiveEvent(getId(), Events.EVENT_PROGRESS.toString(), eventMap);
        }

        @Override
        public boolean onError(GiraffePlayer giraffePlayer, int what, int extra) {
            mEventEmitter.receiveEvent(getId(), Events.EVENT_ERROR.toString(), null);
            return super.onError(giraffePlayer, what, extra);
        }

        @Override
        public void onPause(GiraffePlayer giraffePlayer) {
            mEventEmitter.receiveEvent(getId(), Events.EVENT_PAUSED.toString(), null);
        }

    };

    private String mSrcString;

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

    public static final String EVENT_PROP_DURATION = "duration";
    public static final String EVENT_PROP_CURRENT_TIME = "currentTime";
    public static final String EVENT_PROP_POSITION = "position";
    public static final String EVENT_PROP_END = "endReached";
    public static final String EVENT_PROP_SEEK_TIME = "seekTime";

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
//        Uri pathUri = Uri.parse(filePath);
//        VideoInfo videoInfo = videoView.getVideoInfo()
//                .setFullScreenAnimation(false)
//                .setTitle("test video")
//                .setShowTopBar(true)
//                .setUri(pathUri);
//        videoView.getPlayer().aspectRatio(VideoInfo.AR_MATCH_PARENT);
//                videoView.getPlayer().setDisplayModel(GiraffePlayer.DISPLAY_NORMAL);
        videoView.setVideoPath(filePath).setFingerprint(videoView.hashCode());
//        player = videoView.getPlayer();
//        player = videoView.getPlayer();
        videoView.getPlayer().initialize();
//        player.setDisplayModel(GiraffePlayer.DISPLAY_NORMAL);
    }

    public void onPlaying() {
        WritableMap eventMap = Arguments.createMap();
        eventMap.putDouble(EVENT_PROP_DURATION, videoView.getPlayer().getDuration());
        mEventEmitter.receiveEvent(getId(), Events.EVENT_PLAYING.toString(), eventMap);
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
            } else {
                videoView.getPlayer().pause();
            }
        } else {
            if (!videoView.getPlayer().isPlaying()) {
                videoView.getPlayer().start();
            } else {
                videoView.getPlayer().pause();
            }
        }
    }

    public void seek(int seek) {
        WritableMap event = Arguments.createMap();
        event.putDouble(EVENT_PROP_CURRENT_TIME, videoView.getPlayer().getCurrentPosition());
        event.putDouble(EVENT_PROP_SEEK_TIME, seek);
        mEventEmitter.receiveEvent(getId(), Events.EVENT_SEEK.toString(), event);
        int newPosition = (int) ((videoView.getPlayer().getDuration() * seek * 1.0) / 1000);
        videoView.getPlayer().seekTo(newPosition);
    }

    public void onDropViewInstance() {
        videoView.getPlayer().release();
    }


}
