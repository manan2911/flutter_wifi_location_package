import 'package:flutter/material.dart';
import 'package:wifi_location/wifi_location.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return const MaterialApp(
      home: MyHomePage(),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key});

  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  String _wifiList = 'Unknown';
  String _location = 'Unknown';

  @override
  void initState() {
    super.initState();
    _getWifiList();
    _getLocation();
  }

  Future<void> _getWifiList() async {
    String wifiList;
    try {
      wifiList = await WifiLocation.getWifiList();
    } catch (e) {
      wifiList = 'Failed to get Wi-Fi list: $e';
    }

    if (!mounted) return;

    setState(() {
      _wifiList = wifiList;
    });
  }

  Future<void> _getLocation() async {
    String location;
    try {
      location = await WifiLocation.getUserLocation();
    } catch (e) {
      location = 'Failed to get location: $e';
    }

    if (!mounted) return;

    setState(() {
      _location = location;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Wifi Location Example'),
      ),
      body: SingleChildScrollView(
        // Make the content scrollable
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text('Wi-Fi List:\n$_wifiList\n'),
            Text('Location:\n$_location\n'),
          ],
        ),
      ),
    );
  }
}
