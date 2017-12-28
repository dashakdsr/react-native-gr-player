# react-native-gr-player

A `<GiraffePlayer2>` component for react-native

Based on [GiraffePlayer2](https://github.com/tcking/GiraffePlayer2) from [tcking](https://github.com/tcking)

### Add it to your project

Run `npm i -S https://github.com/dashakdsr/react-native-gr-player.git`

#### Android

- run `react-native link react-native-gr-player`

- in settings.gradle add:

  `include ':giraffeplayer2'`
  `project(':giraffeplayer2').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-gr-player/android/giraffeplayer2')`


## Usage

```
<GiraffePlayer
    ref='giraffePlayer'
    paused={this.state.paused}
    volume={this.state.volume}
    style={styles.giraffePlayer}
    source={{uri: this.props.uri}}
    onGRProgress={this.onProgress.bind(this)}
    onGREnded={this.onEnded.bind(this)}
    onGRPlaying={this.onPlaying.bind(this)}
    onGRBuffering={this.onBuffering.bind(this)}
    onGRPaused={this.onPaused.bind(this)}
 />

```

## Static Methods

`seek(msc)`

```
this.refs['giraffePlayer'].seek(value);
```
