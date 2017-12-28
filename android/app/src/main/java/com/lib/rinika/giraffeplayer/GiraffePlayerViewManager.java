package com.lib.rinika.giraffeplayer;


import android.support.annotation.Nullable;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.Map;

public class GiraffePlayerViewManager extends ViewGroupManager<GiraffePlayerView> {

    public static final String REACT_CLASS = "RCTGiraffePlayer";

    public static final String PROP_SOURCE = "source";
    public static final String PROP_SEEK = "seek";
    public static final String PROP_PAUSED = "paused";
    public static final String PROP_VOLUME = "volume";

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    protected GiraffePlayerView createViewInstance(ThemedReactContext reactContext) {
        return new GiraffePlayerView(reactContext);
    }

    @Nullable
    @Override
    public Map getExportedCustomDirectEventTypeConstants() {
        MapBuilder.Builder builder = MapBuilder.builder();
        for (GiraffePlayerView.Events event : GiraffePlayerView.Events.values()) {
            builder.put(event.toString(), MapBuilder.of("registrationName", event.toString()));
        }
        return builder.build();
    }

    @ReactProp(name = PROP_SOURCE)
    public void setPath(final GiraffePlayerView playerView, ReadableMap map) {
        String path = map.getString("uri");
        playerView.setFilePath(path);
    }

    @ReactProp(name = PROP_VOLUME)
    public void setVolume(final GiraffePlayerView playerView, float volume) {
        playerView.setVolume(volume);
    }

    @ReactProp(name = PROP_SEEK)
    public void setSeek(final GiraffePlayerView playerView, float seek) {
        playerView.seek(seek);
    }

    @ReactProp(name = PROP_PAUSED)
    public void setPaused(final GiraffePlayerView playerView, boolean paused) {
        playerView.setPaused(paused);
    }
}
