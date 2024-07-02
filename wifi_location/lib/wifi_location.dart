import 'dart:async';
import 'package:flutter/services.dart';
import 'package:wifi_location/wifi_location_platform_interface.dart';

class WifiLocation {
  Future<String?> getPlatformVersion() {
    return WifiLocationPlatform.instance.getPlatformVersion();
  }

  static const MethodChannel _channel = MethodChannel('wifi_location');

  static Future<String> getWifiList() async {
    try {
      final String wifiList = await _channel.invokeMethod('getWifiList');
      return wifiList;
    } catch (e) {
      throw Exception('Failed to get Wi-Fi list: $e');
    }
  }

  static Future<String> getUserLocation() async {
    try {
      final String location = await _channel.invokeMethod('getUserLocation');
      return location;
    } catch (e) {
      throw Exception('Failed to get location: $e');
    }
  }
}
