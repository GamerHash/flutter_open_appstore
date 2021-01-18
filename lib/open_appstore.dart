import 'dart:async';
import 'dart:io';
import 'package:flutter/services.dart';

enum StoreType {
  GooglePlay,
  AppGallery,
  AppStore,
  Nothing
}

class OpenAppstore {
  static const MethodChannel _channel = const MethodChannel('flutter.moum.open_appstore');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
  
  static Future<StoreType> get storeType async {
    if (Platform.isAndroid) {
      final String type = await _channel.invokeMethod('getStoreType');
      if (type == "google") {
        return StoreType.GooglePlay;
      } else if (type == "huawei") {
        return StoreType.AppGallery;
      }
    } else if (Platform.isIOS) {
      return StoreType.AppStore;
    } else {
      return StoreType.Nothing;
    }
  }

  static void launch({String androidAppId, String huaweiAppId, String iOSAppId}) async {
    await _channel.invokeMethod('openappstore', {'android_id': androidAppId, 'huawei_id': huaweiAppId, 'ios_id':iOSAppId});
  }
}
