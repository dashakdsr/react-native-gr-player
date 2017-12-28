import React from 'react'
import ReactNative from 'react-native'
import PropTypes from 'prop-types'

const {
  Component
} = React

const {
  StyleSheet,
  requireNativeComponent,
  View
} = ReactNative

export default class GiraffePlayer extends Component {
  constructor (props, context) {
    super(props, context)
    this.seek = this.seek.bind(this)
    this._assignRoot = this._assignRoot.bind(this)
    this._onError = this._onError.bind(this)
    this._onProgress = this._onProgress.bind(this)
    this._onEnded = this._onEnded.bind(this)
    this._onPlaying = this._onPlaying.bind(this)
    this._onStopped = this._onStopped.bind(this)
    this._onPaused = this._onPaused.bind(this)
    this._onBuffering = this._onBuffering.bind(this)
    this._onVolumeChanged = this._onVolumeChanged.bind(this)
  }

  setNativeProps (nativeProps) {
    this._root.setNativeProps(nativeProps)
  }

  seek (pos) {
    this.setNativeProps({ seek: pos })
  }

  _assignRoot (component) {
    this._root = component
  }

  _onBuffering (event) {
    if (this.props.onGRBuffering) {
      this.props.onGRBuffering(event.nativeEvent)
    }
  }

  _onError (event) {
    if (this.props.onGRError) {
      this.props.onGRError(event.nativeEvent)
    }
  }

  _onProgress (event) {
    if (this.props.onGRProgress) {
      this.props.onGRProgress(event.nativeEvent)
    }
  }

  _onEnded (event) {
    if (this.props.onGREnded) {
      this.props.onGREnded(event.nativeEvent)
    }
  }

  _onStopped (event) {
    this.setNativeProps({ paused: true })
    if (this.props.onGRStopped) {
      this.props.onGRStopped(event.nativeEvent)
    }
  }

  _onPaused (event) {
    if (this.props.onGRPaused) {
      this.props.onGRPaused(event.nativeEvent)
    }
  }

  _onPlaying (event) {
    if (this.props.onGRPlaying) {
      this.props.onGRPlaying(event.nativeEvent)
    }
  }

  _onVolumeChanged (event) {
    if (this.props.onGRVolumeChanged) {
      this.props.onGRVolumeChanged(event.nativeEvent)
    }
  }

  render () {
    const {
      source
    } = this.props
    source.initOptions = source.initOptions || []
    // repeat the input media
    const nativeProps = Object.assign({}, this.props)
    Object.assign(nativeProps, {
      style: [styles.base, nativeProps.style],
      source: source,
      onGRError: this._onError,
      onGRProgress: this._onProgress,
      onGREnded: this._onEnded,
      onGRPlaying: this._onPlaying,
      onGRPaused: this._onPaused,
      onGRStopped: this._onStopped,
      onGRuffering: this._onBuffering,
      onGRVolumeChanged: this._onVolumeChanged
    })

    return (
      <RCTGiraffePlayer ref={this._assignRoot} {...nativeProps} />
    )
  }
}

GiraffePlayer.propTypes = {
  /* Wrapper component */
  source: PropTypes.object,

  /* Native only */
  paused: PropTypes.bool,
  seek: PropTypes.number,
  resize: PropTypes.object,
  rate: PropTypes.number,
  volume: PropTypes.number,
  snapshotPath: PropTypes.string,

  onGRPaused: PropTypes.func,
  onGRStopped: PropTypes.func,
  onGRBuffering: PropTypes.func,
  onGRPlaying: PropTypes.func,
  onGREnded: PropTypes.func,
  onGRError: PropTypes.func,
  onGRProgress: PropTypes.func,
  onGRVolumeChanged: PropTypes.func,

  /* Required by react-native */
  scaleX: PropTypes.number,
  scaleY: PropTypes.number,
  translateX: PropTypes.number,
  translateY: PropTypes.number,
  rotation: PropTypes.number,
  ...View.propTypes
}

const styles = StyleSheet.create({
  base: {
    overflow: 'hidden'
  }
})
const RCTGiraffePlayer = requireNativeComponent('RCTGiraffePlayer', GiraffePlayer)
