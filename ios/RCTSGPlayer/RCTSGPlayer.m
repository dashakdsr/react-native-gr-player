#import "React/RCTConvert.h"
#import "RCTSGPlayer.h"
#import "React/RCTBridgeModule.h"
#import "React/RCTEventDispatcher.h"
#import "UIView+React.h"
#import <MediaPlayer/MediaPlayer.h>
#import <AVFoundation/AVFoundation.h>
#import <SGPlayer/SGPlayer.h>
#import <SGPlayer/SGPlayerAction.h>

static NSString *const statusKeyPath = @"status";
static NSString *const playbackLikelyToKeepUpKeyPath = @"playbackLikelyToKeepUp";
static NSString *const playbackBufferEmptyKeyPath = @"playbackBufferEmpty";
static NSString *const readyForDisplayKeyPath = @"readyForDisplay";
static NSString *const playbackRate = @"rate";

@interface MPVolumeView()

@property (nonatomic, readonly) UISlider *volumeSlider;

@end

@implementation MPVolumeView (private_volume)

- (UISlider*)volumeSlider {
    for(id view in self.subviews) {
        if ([view isKindOfClass:[UISlider class]]) {
            UISlider *slider = (UISlider*)view;
            slider.continuous = NO;
            slider.value = AVAudioSession.sharedInstance.outputVolume;
            return slider;
        }
    }
    return nil;
}

@end


@interface RCTSGPlayer()

@property (nonatomic) UISlider *volumeSlider;
@property (nonatomic, strong) SGPlayer *player;

@end


@implementation RCTSGPlayer

@synthesize volume = _volume;


- (id)init {
  if (self = [super init]) {
      _volume = -1.0;
      self.volumeSlider = [[[MPVolumeView alloc] init] volumeSlider];
      self.player = [SGPlayer player];
      self.player.decoder = [SGPlayerDecoder decoderByFFmpeg];
      [self addSubview:self.player.view];
      [self.player registerPlayerNotificationTarget:self
                                        stateAction:@selector(stateAction:)
                                     progressAction:@selector(progressAction:)
                                     playableAction:@selector(playableAction:)];

      [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(applicationWillResignActive:)
                                                 name:UIApplicationWillResignActiveNotification
                                               object:nil];

      [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(applicationWillEnterForeground:)
                                                 name:UIApplicationWillEnterForegroundNotification
                                               object:nil];

      [[NSNotificationCenter defaultCenter] addObserver:self
                                               selector:@selector(volumeChanged:)
                                                   name:@"AVSystemController_SystemVolumeDidChangeNotification"
                                                 object:nil];

  }
  return self;
}


- (void)applicationWillResignActive:(NSNotification *)notification {
    if (!_paused) {
        [self setPaused:_paused];
    }
}


- (void)applicationWillEnterForeground:(NSNotification *)notification {
    if(!_paused) {
        [self setPaused:NO];
    }
}


- (void)setBounds:(CGRect)bounds {
    [super setBounds:bounds];
    self.player.view.frame = bounds;
}


- (void)setPaused:(BOOL)paused {
    if (self.player) {
        if (paused) {
            [self.player pause];
        } else {
            [self.player play];
        }
        _paused = paused;
    }
}


- (void)setVolume:(float)volume {
    if ((_volume != volume)) {
        _volume = volume;
        self.volumeSlider.value = volume;
    }
}


- (float)volume {
    return self.volumeSlider.value; //self.player.volume;
}


- (void)setSource:(NSDictionary *)source {
    if(self.player.state == SGPlayerStatePlaying) {
        [self.player pause];
    }

    NSString *uri = [source objectForKey:@"uri"];
    BOOL autoplay = [RCTConvert BOOL:[source objectForKey:@"autoplay"]];
    NSURL *url = [NSURL URLWithString:uri];

    //init player && play
    [self.player replaceVideoWithURL:url];
    [self setPaused:!autoplay];
}


- (void)stateAction:(NSNotification *)notification {
    SGState * state = [SGState stateFromUserInfo:notification.userInfo];

    NSString * text;
    switch (state.current) {
        case SGPlayerStateNone:
            text = @"None";
            break;
        case SGPlayerStateBuffering:
            if (self.onSGBuffering) {
                self.onSGBuffering(@{ @"target": self.reactTag });
            }
            break;
        case SGPlayerStateReadyToPlay:

            break;
        case SGPlayerStatePlaying:
            if (self.onSGPlaying) {
                self.onSGPlaying(@{ @"target": self.reactTag,
                                  @"seekable": [NSNumber numberWithBool:self.player.seekEnable],
                                  @"duration":[NSNumber numberWithInt:self.player.duration] });
            }
            break;
        case SGPlayerStateSuspend:
            if (self.onSGPaused) {
                self.onSGPaused(@{ @"target": self.reactTag });
            }
            break;
        case SGPlayerStateFinished:
            if (self.onSGEnded) {
                self.onSGEnded(@{ @"target": self.reactTag });
            }
            break;
        case SGPlayerStateFailed:
            if (self.onSGError) {
                self.onSGError(@{ @"target": self.reactTag });
            }
            [self _release];
            break;
    }
}


- (void)progressAction:(NSNotification *)notification {
    SGProgress *progress = [SGProgress progressFromUserInfo:notification.userInfo];
    [self updateVideoProgress:progress];
}


- (void)playableAction:(NSNotification *)notification {
    SGPlayable * playable = [SGPlayable playableFromUserInfo:notification.userInfo];
    NSLog(@"playable time : %f", playable.current);
}


- (void)volumeChanged:(NSNotification *)notification {
    float volume = [[[notification userInfo] objectForKey:@"AVSystemController_AudioVolumeNotificationParameter"] floatValue];
    if (_volume != volume) {
        _volume = volume;
        if (self.onSGVolumeChanged) {
            self.onSGVolumeChanged(@{@"volume": [NSNumber numberWithFloat: volume]});
        }
    }
}


- (void)updateVideoProgress:(SGProgress*)progress {
    int currentTime   = progress.current;
    int remainingTime = progress.total - progress.current;
    int duration      = progress.total;
    CGFloat position  = progress.current / progress.total;

    if( currentTime >= 0 && currentTime < duration) {
        if (self.onSGProgress) {
            self.onSGProgress(@{ @"target": self.reactTag,
                               @"currentTime": [NSNumber numberWithInt:currentTime],
                               @"remainingTime": [NSNumber numberWithInt:remainingTime],
                               @"duration":[NSNumber numberWithInt:duration],
                               @"position":[NSNumber numberWithFloat:position] });
        }
    }
}


- (void)setSeek:(float)pos {
    if([self.player seekEnable]) {
        if(pos >= 0 && pos <= 1.0) {
            [self.player seekToTime:self.player.duration * pos];
        }
    }
}


- (void)setSnapshotPath:(NSString*)path {
    if(self.player) {
        UIImage *image = self.player.snapshot;
        NSData *imageData = UIImageJPEGRepresentation(image, 1.0);
        [imageData writeToFile:path atomically:NO];
    }
}


- (void)_release {
    [self.player pause];
    self.player = nil;
}


#pragma mark - Lifecycle
- (void)removeFromSuperview {
    [self.player removePlayerNotificationTarget:self];
    [self _release];
    [[NSNotificationCenter defaultCenter] removeObserver:self];
    [super removeFromSuperview];
}

@end
