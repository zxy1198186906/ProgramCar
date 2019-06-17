package com.volvocars.broadCast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.volvocars.service.AdminService;

public class BootFinishReceiver extends BroadcastReceiver{

    private static final String TAG = "BootFinishReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
            Intent thisIntent = new Intent(context, AdminService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                context.startForegroundService(thisIntent);
            }else{
                context.startService(thisIntent);
            }
            Log.i(TAG, "Boot Broadcast receiver works");
            context.startService(thisIntent);
        }
    }
}
