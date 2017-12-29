#import "React/RCTView.h"

@class SGPlayer;

@interface RCTSGPlayer : UIView

//- (instancetype)initWithPlayer:(VLCMediaPlayer*)player;

@property (nonatomic) BOOL paused;
@property (nonatomic) float volume;

@property (nonatomic, copy) RCTDirectEventBlock onSGPaused;
@property (nonatomic, copy) RCTDirectEventBlock onSGStopped;
@property (nonatomic, copy) RCTDirectEventBlock onSGBuffering;
@property (nonatomic, copy) RCTDirectEventBlock onSGPlaying;
@property (nonatomic, copy) RCTDirectEventBlock onSGEnded;
@property (nonatomic, copy) RCTDirectEventBlock onSGError;
@property (nonatomic, copy) RCTDirectEventBlock onSGProgress;
@property (nonatomic, copy) RCTDirectEventBlock onSGVolumeChanged;

@end
