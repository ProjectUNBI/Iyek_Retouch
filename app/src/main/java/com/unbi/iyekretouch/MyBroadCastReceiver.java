package com.unbi.iyekretouch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import static com.unbi.iyekretouch.PublicStaticMethods.MYSHAKEFROMBROADCAST;
import static com.unbi.iyekretouch.PublicStaticMethods.MYSTARTSERVICE;
import static com.unbi.iyekretouch.PublicStaticMethods.ObjectToGsonString;

public class MyBroadCastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (null != intent && intent.getAction().equals("shake.detector")) {
            customIntent mycustumobj=new customIntent(MYSHAKEFROMBROADCAST);
            Intent i = new Intent(context, myAccessibility.class);
            Bundle b = new Bundle();
            b.putString("myObject", ObjectToGsonString(mycustumobj));
            i.putExtras(b);
            context.startService(i);
        }
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            customIntent mycustumobj=new customIntent(MYSTARTSERVICE);
            Intent i = new Intent(context, myAccessibility.class);
            Bundle b = new Bundle();
            b.putString("myObject", ObjectToGsonString(mycustumobj));
            i.putExtras(b);
            context.startService(i);
        }
    }
}
