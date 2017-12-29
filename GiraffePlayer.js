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

export default class SGPlayer extends Component {
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
    if (this.props.onSGBuffering) {
      this.props.onSGBuffering(event.nativeEvent)
    }
  }

  _onError (event) {
    if (this.props.onSGError) {
      this.props.onSGError(event.nativeEvent)
    }
  }

  _onProgress (event) {
    if (this.props.onSGProgress) {
      this.props.onSGProgress(event.nativeEvent)
    }
  }

  _onEnded (event) {
    if (this.props.onSGEnded) {
      this.props.onSGEnded(event.nativeEvent)
    }
  }

  _onStopped (event) {
    this.setNativeProps({ paused: true })
    if (this.props.onSGStopped) {
      this.props.onSGStopped(event.nativeEvent)
    }
  }

  _onPaused (event) {
    if (this.props.onSGPaused) {
      this.props.onSGPaused(event.nativeEvent)
    }
  }

  _onPlaying (event) {
    if (this.props.onSGPlaying) {
      this.props.onSGPlaying(event.nativeEvent)
    }
  }

  _onVolumeChanged (event) {
    if (this.props.onSGVolumeChanged) {
      this.props.onSGVolumeChanged(event.nativeEvent)
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
      onSGError: this._onError,
      onSGProgress: this._onProgress,
      onSGEnded: this._onEnded,
      onSGPlaying: this._onPlaying,
      onSGPaused: this._onPaused,
      onSGStopped: this._onStopped,
      onSGBuffering: this._onBuffering,
      onSGVolumeChanged: this._onVolumeChanged
    })

    return (
      <RCTSGlayer ref={this._assignRoot} {...nativeProps} />
    )
  }
}

SGPlayer.propTypes = {
  /* Wrapper component */
  source: PropTypes.object,

  /* Native only */
  paused: PropTypes.bool,
  seek: PropTypes.number,
  resize: PropTypes.object,
  rate: PropTypes.number,
  volume: PropTypes.number,
  snapshotPath: PropTypes.string,

  onSGPaused: PropTypes.func,
  onSGStopped: PropTypes.func,
  onSGBuffering: PropTypes.func,
  onSGPlaying: PropTypes.func,
  onSGEnded: PropTypes.func,
  onSGError: PropTypes.func,
  onSGProgress: PropTypes.func,
  onSGVolumeChanged: PropTypes.func,

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
const RCTSGlayer = requireNativeComponent('RCTSGlayer', SGPlayer)
