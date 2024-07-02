package com.example.wifi_location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Tasks
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class WifiLocationPlugin: FlutterPlugin, MethodChannel.MethodCallHandler, ActivityAware {
  private lateinit var channel: MethodChannel
  private lateinit var context: Context
  private var activityBinding: ActivityPluginBinding? = null
  private lateinit var fusedLocationClient: FusedLocationProviderClient

  override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "wifi_location")
    channel.setMethodCallHandler(this)
    context = flutterPluginBinding.applicationContext
    fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
  }

  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    channel.setMethodCallHandler(null)
  }

  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    activityBinding = binding
    checkAndRequestPermissions()
  }

  override fun onDetachedFromActivity() {
    activityBinding = null
  }

  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    activityBinding = binding
    checkAndRequestPermissions()
  }

  override fun onDetachedFromActivityForConfigChanges() {
    activityBinding = null
  }

  override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
    when (call.method) {
      "getWifiList" -> {
        if (arePermissionsGranted()) {
          result.success(getWifiNetworksInfo())
        } else {
          result.error("PERMISSION_DENIED", "Permissions not granted", null)
        }
      }
      "getUserLocation" -> {
        if (arePermissionsGranted()) {
          CoroutineScope(Dispatchers.IO).launch {
            try {
              val userLocation = getUserLocation()
              withContext(Dispatchers.Main) {
                result.success(userLocation)
              }
            } catch (e: Exception) {
              withContext(Dispatchers.Main) {
                result.error("LOCATION_ERROR", "Failed to get location: ${e.message}", null)
              }
            }
          }
        } else {
          result.error("PERMISSION_DENIED", "Permissions not granted", null)
        }
      }
      else -> result.notImplemented()
    }
  }

  private fun arePermissionsGranted(): Boolean {
    val permissions = listOf(
      Manifest.permission.ACCESS_FINE_LOCATION,
      Manifest.permission.ACCESS_WIFI_STATE
    )
    return permissions.all { ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED }
  }

  private fun checkAndRequestPermissions() {
    val permissions = mutableListOf(
      Manifest.permission.ACCESS_FINE_LOCATION,
      Manifest.permission.ACCESS_WIFI_STATE
    )
    val deniedPermissions = permissions.filter {
      ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
    }
    if (deniedPermissions.isNotEmpty() && activityBinding != null) {
      ActivityCompat.requestPermissions(activityBinding!!.activity, deniedPermissions.toTypedArray(), 101)
    }
  }

  private fun getWifiNetworksInfo(): String {
    val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
    wifiManager.startScan()
    val wifiScanResults = wifiManager.scanResults
    val wifiInfoBuilder = StringBuilder()
    for (scanResult in wifiScanResults) {
      wifiInfoBuilder.append("SSID: ").append(scanResult.SSID).append("\n")
      wifiInfoBuilder.append("BSSID: ").append(scanResult.BSSID).append("\n")
      wifiInfoBuilder.append("Signal Strength: ").append(scanResult.level).append("\n\n")
    }
    return wifiInfoBuilder.toString()
  }

  private suspend fun getUserLocation(): String = withContext(Dispatchers.IO) {
    var locationResult = "Location not available"
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
      try {
        val location = Tasks.await(fusedLocationClient.lastLocation, 30, TimeUnit.SECONDS)
        location?.let {
          locationResult = "Lat: ${location.latitude}, Lon: ${location.longitude}"
        }
      } catch (e: Exception) {
        locationResult = "Location fetch failed: ${e.message}"
      }
    } else {
      locationResult = "Location permission not granted"
    }
    locationResult
  }
}
