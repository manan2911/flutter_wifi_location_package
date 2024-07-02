import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'wifi_location_platform_interface.dart';

/// An implementation of [WifiLocationPlatform] that uses method channels.
class MethodChannelWifiLocation extends WifiLocationPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('wifi_location');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}
