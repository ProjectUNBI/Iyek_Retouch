package com.unbi.iyekretouch;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.google.gson.Gson;

import static com.unbi.iyekretouch.PublicStaticMethods.*;

public class myAccessibility extends AccessibilityService {

    private userSavePreferance userSaved;
    private shakeOptionsObj shakeOptionsObj;
    long currentmillis,previousmilli;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.d("TAG", "INtent");
        if (intent != null && intent.getExtras() != null) {
            //Got the intent
//            Log.d("TAG", "MYSTRTSERVICE");
            Bundle b = intent.getExtras();
            String GsonObject = b.getString("myObject");
            customIntent cusTum = getGsonToObject(GsonObject, customIntent.class);
            Integer extra = (Integer) cusTum.getMyflag();
            float shakevalue = (float) 0;
            if (cusTum != null) {
                ;
                shakevalue = cusTum.getShakevalue();
            }
            if (userSaved == null) {
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(USERSAVEPREFERANCE, Context.MODE_PRIVATE);
                if (sharedPreferences.contains("usersave")) {
                    final Gson gson = new Gson();
                    userSaved = gson.fromJson(sharedPreferences.getString("usersave", ""), userSavePreferance.class);
                } else {
                    userSaved = new userSavePreferance();
                }
                shakevalue = userSaved.getUsershakelevel();
            }
            /*
            For shake Protection
             */
            currentmillis=System.currentTimeMillis();
            ///////////////////

            switch (extra) {
                case MYSTARTSERVICE:
                    Log.d("TAG", "MYSTRTSERVICE");
                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(USERSAVEPREFERANCE, Context.MODE_PRIVATE);
                    if (sharedPreferences.contains("usersave")) {
                        final Gson gson = new Gson();
                        userSaved = gson.fromJson(sharedPreferences.getString("usersave", ""), userSavePreferance.class);
                    } else {
                        userSaved = new userSavePreferance();
                    }
                    shakeOptionsObj = new shakeOptionsObj(userSaved.getUsershakelevel());
                    //Read the User saved Object...
                    shakeOptionsObj.getShakeDetector().start(this);
                    //run the service
                    break;
                case MYSTOPSERVICE:
                    Log.d("TAG", "MYSTOPSERVICE");
                    stopforground();
                    userSaved.setIs_iyekOn(false, getApplicationContext());
                    //stop the service
                    break;
                case MYRESTARTSERVICE:
                    Log.d("TAG", "MYRESTART");
                    //TODO you need to save the User Object first
//                    SharedPreferences sharedPreferences2 = getApplicationContext().getSharedPreferences(USERSAVEPREFERANCE, Context.MODE_PRIVATE);
//                    if (sharedPreferences2.contains("usersave")) {
//                        final Gson gson = new Gson();
//                        userSaved=gson.fromJson(sharedPreferences2.getString("usersave", ""), userSavePreferance.class);
//                    }else {
//                        userSaved=new userSavePreferance();
//                    }
                    if (shakeOptionsObj != null && shakeOptionsObj.getShakeDetector() != null) {
                        shakeOptionsObj.getShakeDetector().stopShakeDetector(getApplicationContext());
                    }
                    userSaved = new userSavePreferance();
                    userSaved.setUsershakelevel(shakevalue, this);
                    shakeOptionsObj = new shakeOptionsObj(userSaved.getUsershakelevel());
                    shakeOptionsObj.getShakeDetector().start(this);
                    //Stop the service firse
                    //run cae1 again
                case MYSHAKEFROMBROADCAST:

                    if(currentmillis-previousmilli>1000){
                        previousmilli=currentmillis;//protecting this handeler method
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("TAG", "MYSHAKEFROMBROADCAST");

                            }
                        }, 1000);
                    }
                    currentmillis=System.currentTimeMillis();//assingning values to current milli




                    //shakebroadcast Receive from the Broadcast receiver
                    //so do iyek or not
//                    Log.d("Shakservice BAckround","Shake");
            }
        }
        return START_STICKY;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
    }

    @Override
    public void onInterrupt() {
    }

    /*
    OnGoing Notification
     */
    private void showNotification() {
        Intent notificationIntent = new Intent(this, myAccessibility.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, 0);
        int ONGOING_NOTIFICATION_ID = 001;
        Notification notification =
                new Notification.Builder(this)
                        .setSmallIcon(R.drawable.ic_iyek)
                        .setContentTitle("Kanglei Iyek")
                        .setContentText("Running.....")
                        .setContentIntent(pendingIntent)
                        .build();
        startForeground(ONGOING_NOTIFICATION_ID, notification);
    }

    private void stopforground() {
        stopForeground(true);
    }
}
