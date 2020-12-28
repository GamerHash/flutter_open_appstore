package flutter.moum.open_appstore;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;
import android.os.Build;
import androidx.annotation.NonNull;
import android.content.pm.PackageManager;
import android.content.pm.PackageInfo;

import java.util.List;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** OpenAppstorePlugin */
public class OpenAppstorePlugin implements FlutterPlugin {
  /** Plugin registration. */

  private MethodChannel channel;
  
  private static final String GooglePlayStorePackageNameOld = "com.google.market";
  private static final String GooglePlayStorePackageNameNew = "com.android.vending";

  public static void registerWith(Registrar registrar) {
    OpenAppstorePlugin plugin = new OpenAppstorePlugin();
    plugin.setMethodChannel(registrar.context(), registrar.messenger());
  }
  
  public static String getDeviceName() {
    String manufacturer = Build.MANUFACTURER;
    String model = Build.MODEL;
    if (model.startsWith(manufacturer)) {
        return model;
    }
    return manufacturer + " " + model;
  }
  
  public static void launchActivity(Context context, String uri) {
    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intent);
  }
  
  public static boolean googlePlayStoreInstalled(Context context) {
    PackageManager packageManager = context.getPackageManager();
    List<PackageInfo> packages = packageManager.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
    for (PackageInfo packageInfo : packages)
      if (packageInfo.packageName.equals(GooglePlayStorePackageNameOld) || packageInfo.packageName.equals(GooglePlayStorePackageNameNew))
        return true;
    return false;
  }

  private void setMethodChannel(final Context context, BinaryMessenger messenger) {
    channel = new MethodChannel(messenger, "flutter.moum.open_appstore");
    channel.setMethodCallHandler(new MethodCallHandler() {
      @Override
      public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        if (call.method.equals("getPlatformVersion")) {
          result.success("Android " + android.os.Build.VERSION.RELEASE);
        } else if (call.method.equals("openappstore")) {
          result.success("Android " + android.os.Build.VERSION.RELEASE);
          String android_id = call.argument("android_id");
          String huawei_id = call.argument("huawei_id");
          String manufacturer = android.os.Build.MANUFACTURER;
          //Toast.makeText(context, getDeviceName(), Toast.LENGTH_SHORT).show();
          if (manufacturer.equals("Amazon")) {
            launchActivity(context, "amzn://apps/android?p=" + android_id);
          } else if (manufacturer.equalsIgnoreCase("Huawei") && !googlePlayStoreInstalled(context)) {
            launchActivity(context, "appmarket://details?id=" + huawei_id);
          } else {
            try {
              launchActivity(context, "market://details?id=" + android_id);
            } catch (android.content.ActivityNotFoundException e) {
              launchActivity(context, "https://play.google.com/store/apps/details?id=" + android_id);
            }
          }
          result.success(null);
        } else {
          result.notImplemented();
        }
      }
    });
  }

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
    setMethodChannel(binding.getApplicationContext(), binding.getBinaryMessenger());
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }
}
