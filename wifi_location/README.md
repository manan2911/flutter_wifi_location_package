
# wifi_location

A Flutter plugin to fetch available Wi-Fi SSIDs and the user's location, with built-in permission handling for both Android and iOS.

## Features

- **Fetch a list of available Wi-Fi SSIDs**: Quickly get a list of all available Wi-Fi networks nearby.
- **Get the current user's location**: Retrieve the user's current latitude and longitude.
- **Built-in permission handling**: Automatically handle permissions for location and Wi-Fi state.

## Getting Started

To use this plugin, add `wifi_location` as a dependency in your `pubspec.yaml` file:

```yaml
dependencies:
  wifi_location: ^0.0.1
```

## Usage

### Import the Package

```dart
import 'package:wifi_location/wifi_location.dart';
```

### Fetch Wi-Fi List

```dart
void getWifiList() async {
  try {
    List<String> wifiList = await WifiLocation.getWifiList();
    print(wifiList);
  } catch (e) {
    print("Failed to get Wi-Fi list: $e");
  }
}
```

### Get User Location

```dart
void getUserLocation() async {
  try {
    LocationData location = await WifiLocation.getUserLocation();
    print("Latitude: ${location.latitude}, Longitude: ${location.longitude}");
  } catch (e) {
    print("Failed to get location: $e");
  }
}
```

## Example

Check out the `example` directory for a complete example on how to use the `wifi_location` plugin.

## Contributing

Contributions are welcome! Please read `CONTRIBUTING.md` for details on our code of conduct, and the process for submitting pull requests.

## License

This project is licensed under the MIT License - see the `LICENSE.md` file for details.