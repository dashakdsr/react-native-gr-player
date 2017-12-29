#import "RCTSGPlayerManager.h"
#import "RCTSGPlayer.h"

@implementation RCTSGPlayerManager

RCT_EXPORT_MODULE();

- (UIView *)view {
    return [[RCTSGPlayer alloc] init];
}

RCT_EXPORT_VIEW_PROPERTY(source, NSDictionary);
RCT_EXPORT_VIEW_PROPERTY(paused, BOOL);
RCT_EXPORT_VIEW_PROPERTY(seek, float);
RCT_EXPORT_VIEW_PROPERTY(volume, float);
RCT_EXPORT_VIEW_PROPERTY(snapshotPath, NSString);
RCT_EXPORT_VIEW_PROPERTY(onSGPaused, RCTDirectEventBlock);
RCT_EXPORT_VIEW_PROPERTY(onSGBuffering, RCTDirectEventBlock);
RCT_EXPORT_VIEW_PROPERTY(onSGPlaying, RCTDirectEventBlock);
RCT_EXPORT_VIEW_PROPERTY(onSGEnded, RCTDirectEventBlock);
RCT_EXPORT_VIEW_PROPERTY(onSGError, RCTDirectEventBlock);
RCT_EXPORT_VIEW_PROPERTY(onSGProgress, RCTDirectEventBlock);
RCT_EXPORT_VIEW_PROPERTY(onSGVolumeChanged, RCTDirectEventBlock);

@end
