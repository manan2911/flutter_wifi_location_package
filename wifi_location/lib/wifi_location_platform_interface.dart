import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'wifi_location_method_channel.dart';

abstract class WifiLocationPlatform extends PlatformInterface {
  /// Constructs a WifiLocationPlatform.
  WifiLocationPlatform() : super(token: _token);

  static final Object _token = Object();

  static WifiLocationPlatform _instance = MethodChannelWifiLocation();

  /// The default instance of [WifiLocationPlatform] to use.
  ///
  /// Defaults to [MethodChannelWifiLocation].
  static WifiLocationPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [WifiLocationPlatform] when
  /// they register themselves.
  static set instance(WifiLocationPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
