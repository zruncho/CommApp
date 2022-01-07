package com.cryptophonecall.cv;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent serviceIntent = new Intent(context, myFirebaseMessagingService.class);
            context.startService(serviceIntent);

        }

    }