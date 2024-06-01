
#ifdef RCT_NEW_ARCH_ENABLED
#import "RNKioskSpec.h"

@interface Kiosk : NSObject <NativeKioskSpec>
#else
#import <React/RCTBridgeModule.h>

@interface Kiosk : NSObject <RCTBridgeModule>
#endif

@end
