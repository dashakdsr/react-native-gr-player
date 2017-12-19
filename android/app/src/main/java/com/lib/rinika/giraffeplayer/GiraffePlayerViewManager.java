package com.lib.rinika.giraffeplayer;


import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;

public class GiraffePlayerViewManager extends ViewGroupManager<GiraffePlayerView> {

    public static final String REACT_CLASS = "GiraffePlayer";

    public static final String PROP_SOURCE = "source";
    public static final String PROP_SEEK = "seek";
    public static final String PROP_PAUSED = "paused";
    public static final String PROP_VOLUME = "volume";
    public static final String PROP_RESIZE = "resize";

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    protected GiraffePlayerView createViewInstance(ThemedReactContext reactContext) {
        return new GiraffePlayerView(reactContext);
    }

    @ReactProp(name = PROP_SOURCE)
    public void setPath(final GiraffePlayerView playerView, ReadableMap map) {
        String path = map.getString("uri");
        playerView.setFilePath(path);
    }

    @ReactProp(name = PROP_VOLUME)
    public void setVolume(final GiraffePlayerView playerView, float volume) {
        playerView.setVolume(300 * volume);
    }

    @ReactProp(name = PROP_PAUSED)
    public void setPaused(final GiraffePlayerView playerView, boolean paused) {
        playerView.setPaused(paused);
    }
}
