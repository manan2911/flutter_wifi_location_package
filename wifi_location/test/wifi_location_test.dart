import 'package:flutter_test/flutter_test.dart';
import 'package:wifi_location/wifi_location.dart';
import 'package:wifi_location/wifi_location_platform_interface.dart';
import 'package:wifi_location/wifi_location_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockWifiLocationPlatform
    with MockPlatformInterfaceMixin
    implements WifiLocationPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final WifiLocationPlatform initialPlatform = WifiLocationPlatform.instance;

  test('$MethodChannelWifiLocation is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelWifiLocation>());
  });

  test('getPlatformVersion', () async {
    WifiLocation wifiLocationPlugin = WifiLocation();
    MockWifiLocationPlatform fakePlatform = MockWifiLocationPlatform();
    WifiLocationPlatform.instance = fakePlatform;

    expect(await wifiLocationPlugin.getPlatformVersion(), '42');
  });
}
