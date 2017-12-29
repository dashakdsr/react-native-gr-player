# react-native-gr-player

A `GiraffePlayer2` and `SGPlayer` component for react-native

Based on [GiraffePlayer2](https://github.com/tcking/GiraffePlayer2) from [tcking](https://github.com/tcking) and [SGPlayer](https://github.com/libobjc/SGPlayer) from [libobjc](https://github.com/libobjc)

### Support Android and iOS


### Add it to your project

Run `npm i -S https://github.com/dashakdsr/react-native-gr-player.git`

#### Android

- run `react-native link react-native-gr-player`

- in settings.gradle:


`include ':giraffeplayer2'`  
`project(':giraffeplayer2').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-gr-player/android/giraffeplayer2')`  
`include ':react-native-gr-player'`  
`project(':react-native-gr-player').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-gr-player/android/app')`

- in build.grable:

`compile project(':react-native-gr-player')`

- in MainApplication:

`import com.lib.rinika.giraffeplayer.GiraffePlayerPackage`

##### and

`new GiraffePlayerPackage()`

#### iOS

- Add to `./node_modules/react-native-gr-player/ios` folder - Frameworks

Link to [Frameworks](https://drive.google.com/open?id=1ExSPtcKUJMWm0qxGbvZ2W2Pe49wZe-a)

- Add to `Link binary with libraries`:

![Image of libraries]
(http://prntscr.com/httw95)

- Add to `Build Settings/Framework Search Paths` path to your Frameworks.


## Usage

```
<SGPlayer
    ref='sgplayer'
    paused={this.state.paused}
    volume={this.state.volume}
    style={styles.giraffePlayer}
    source={{uri: this.props.uri}}
    onSGProgress={this.onProgress.bind(this)}
    onSGEnded={this.onEnded.bind(this)}
    onSGPlaying={this.onPlaying.bind(this)}
    onSGBuffering={this.onBuffering.bind(this)}
    onSGPaused={this.onPaused.bind(this)}
 />

```

## Static Methods

`seek(msc)`

```
this.refs['sgplayer'].seek(value);
```

### for iOS

`snapshot(path)`

```
this.refs['sgplayer'].snapshot(path);
```
