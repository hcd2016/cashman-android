package com.credit.xiaowei.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.credit.xiaowei.app.App;
import com.credit.xiaowei.config.Constant;
import com.credit.xiaowei.util.ServiceUtil;

public class BootBroadcastReceiver extends BroadcastReceiver {
    private Context mContext;
    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED) ||
                intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
            //在主线程实例化
            Handler handler = new Handler(Looper.getMainLooper());
            //after reboot the device,about 2 minutes later,upload the POI info to server
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(!ServiceUtil.isServiceRunning(mContext, Constant.POI_SERVICE)){
                        ServiceUtil.invokeTimerPOIService(App.getContext());
                    }
                }
            }, Constant.BROADCAST_ELAPSED_TIME_DELAY);
        }
    }
}

