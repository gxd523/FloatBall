package com.demo.floatball;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showFloatBall(View view) {
        if (!isServiceExisted()) {
            Log.d("gxd", "MainActivity.showFloatBall-->");
            Intent intent = new Intent(this, FloatService.class);
            startService(intent);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermission(View view) {
        try {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            intent.setData(Uri.parse("package:" + getPackageName()));

            PackageManager packageManager = getPackageManager();
            List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if (resolveInfoList.size() > 0) {// 判断跳转意图是否有效
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Toast.makeText(this, "无法跳转到权限设置页面", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "无法跳转到权限设置页面", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isServiceExisted() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            List<ActivityManager.RunningServiceInfo> runningServiceInfoList = activityManager.getRunningServices(50);
            for (ActivityManager.RunningServiceInfo runningServiceInfo : runningServiceInfoList) {
                if ("com.demo.floatball:float".equals(runningServiceInfo.process) && FloatService.class.getName().equals(runningServiceInfo.service.getClassName())) {
                    return true;
                }
            }
        }
        return false;
    }
}
