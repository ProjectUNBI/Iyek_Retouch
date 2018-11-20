package com.unbi.iyekretouch;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.EditText;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayDeque;
import java.util.Date;
import java.util.Deque;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;
import static android.support.constraint.Constraints.TAG;
import static com.unbi.iyekretouch.PublicStaticMethods.*;

public class myAccessibility extends AccessibilityService {

    private userSavePreferance userSaved;
    private shakeOptionsObj shakeOptionsObj;
    private long currentmillis, previousmilli;
    private final Handler handler = new Handler();
    private Intent intentToMainAct = new Intent(MYPACKAGE);
    private String userPreviousClippboard;
    private CustomWords thiscustuword;
    private String custumstring;
    doIyek doiyek = new doIyek();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        //Log.d("TAG", "INtent");
        if (intent != null && intent.getExtras() != null) {
            //Got the intent
//            //Log.d("TAG", "MYSTRTSERVICE");
            Bundle b = intent.getExtras();
            String GsonObject = b.getString("myObject");
            customIntent cusTum = getGsonToObject(GsonObject, customIntent.class);
            Integer extra = (Integer) cusTum.getMyflag();
            float shakevalue = (float) 0;
            if (cusTum != null) {
                shakevalue = cusTum.getShakevalue();
            }
            if (cusTum == null) {
                if (userSaved == null) {
                    makeNonNullUsersave();
                }
                shakevalue = userSaved.getUsershakelevel();
            }
            /*
            For shake Protection
             */
            ///////////////////
            switch (extra) {
                case MYSTARTSERVICE:
                    //Log.d("TAG", "MYSTRTSERVICE");
                    startshakeservice();//run the service
                    //check the user accessibility
                    //do stop service...but run the shake service
                    break;
                case MYSTOPSERVICE:
                    //Log.d("TAG", "MYSTOPSERVICE");
                    stopshakeservice();
                    break;
                case MYRESTARTSERVICE:
//                    //Log.d("TAG", "MYRESTART");
//                    if (shakeOptionsObj != null && shakeOptionsObj.getShakeDetector() != null) {
//                        shakeOptionsObj.getShakeDetector().stopShakeDetector(getApplicationContext());//stoping the previous one
//                    }
//                    if (userSaved == null) {
//                        makeNonNullUsersave();
//                    }
//                    userSaved.setUsershakelevel(shakevalue, this);
//                    shakeOptionsObj = new shakeOptionsObj(userSaved.getUsershakelevel());
//                    shakeOptionsObj.getShakeDetector().start(this);
                    //Stop the service firse
                    //run cae1 again
                case MYSHAKEFROMBROADCAST:
//                    //Log.d("TAG", "PreFiltre");
                    currentmillis = System.currentTimeMillis();
                    if (currentmillis - previousmilli > 500) {
                        previousmilli = currentmillis;//protecting this handeler method
                        if (userSaved == null) {
                            makeNonNullUsersave();
                        }
                        if (userSaved.isIs_iyekOn()) {
                            //Log.d("TAG", "MYSHAKEFROMBROADCAST");
                            try {//TODO I dont want to loose the accessibility service
                                doIyekTransliteration();
                            } catch (Exception e) {
                            }
                        }
                    }
                    currentmillis = System.currentTimeMillis();//assingning values to current milli
                    //shakebroadcast Receive from the Broadcast receiver
                    //so do iyek or not
//                    //Log.d("Shakservice BAckround","Shake");
                    break;
                case USERPREFCHANGE:
                    if (cusTum != null) {
                        userSaved = getGsonToObject(cusTum.getMyString(), userSavePreferance.class);
                    }
                    if (shakeOptionsObj != null && shakeOptionsObj.getShakeDetector() != null) {
                        shakeOptionsObj.getShakeDetector().stopShakeDetector(this);
                    }
                    //Log.d("FromAccessibilitye", String.valueOf(userSaved.getUsershakelevel()));
                    shakeOptionsObj = new shakeOptionsObj(userSaved.getUsershakelevel());
                    shakeOptionsObj.getShakeDetector().start(this);
                    break;
                case CUSTUMWORDCHANGE:
                    if (cusTum != null) {
                        //TODO READCUSTOM WORD FROM AANY START from background i mean when the accesibility service is connected
                        thiscustuword = getGsonToObject(cusTum.getMyString(), CustomWords.class);
                        custumstring = thiscustuword.getCustomword();
                    }
                    break;
            }
        }
        return START_STICKY;
    }

    private void makeNonNullUsersave() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(USERSAVEPREFERANCE, Context.MODE_PRIVATE);
        if (sharedPreferences.contains("usersave")) {
            final Gson gson = new Gson();
            userSaved = gson.fromJson(sharedPreferences.getString("usersave", ""), userSavePreferance.class);
        } else {
            userSaved = new userSavePreferance();
        }
    }

    private void stopshakeservice() {
        stopforground();
        if (userSaved == null) {
            makeNonNullUsersave();
        }
        userSaved.setIs_iyekOn(false, getApplicationContext());
        //stop the service
    }

    private void readcustomword() {
        CustomWords custum = new CustomWords();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(USERSAVEPREFERANCE, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(CUSTOMWORDOBJ)) {
            final Gson gson = new Gson();
            custum = gson.fromJson(sharedPreferences.getString(CUSTOMWORDOBJ, ""), CustomWords.class);
        }
        thiscustuword = custum;
        custumstring = thiscustuword.getCustomword();
    }

    private void startshakeservice() {
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
        if (isAccessibilitySettingsOnok(getApplicationContext()))
        //if the user accessibility is notgiven
        {
            showNotification();
        } else {
            stopforground();
            userSaved.setIs_iyekOn(false, getApplicationContext());
            //Log.d("Hndeler", "1");
            //Send Intent to update The UI
            DisplayLoggingInfo();
        }
    }

    /*
    Metho to send to update UI Functions
     */

    private void DisplayLoggingInfo() {
        //Log.d(TAG, "entered DisplayLoggingInfo");
        intentToMainAct.putExtra("time", new Date().toLocaleString());
        intentToMainAct.putExtra("counter", String.valueOf(22222));
        sendBroadcast(intentToMainAct);
    }
////////////////////////////////////////////////////////

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
    }

    @Override
    public void onInterrupt() {
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
//        //Log.d("dfghnrgh", "onServiceConnected");
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.flags = AccessibilityServiceInfo.DEFAULT |
                AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS |
                AccessibilityServiceInfo.FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY |
                AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS;
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
        setServiceInfo(info);
        if (shakeOptionsObj != null && shakeOptionsObj.getShakeDetector() != null) {
            shakeOptionsObj.getShakeDetector().stopShakeDetector(getApplicationContext());
        }
        if (userSaved == null) {
            makeNonNullUsersave();
        }
        if (userSaved.isIs_iyekOn()) {
            startshakeservice();
        }
        readcustomword();
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
                        .setContentTitle("Iyek")
                        .setContentText("Running.....")
                        .setContentIntent(pendingIntent)
                        .build();
        startForeground(ONGOING_NOTIFICATION_ID, notification);
    }

    private void stopforground() {
        stopForeground(true);
    }

    /*
    Accessibility service Checker
     */
    public boolean isAccessibilitySettingsOnok(Context mContext) {
        int accessibilityEnabled = 0;
        String TAG = "chekacess";
        final String service = getPackageName() + "/" + myAccessibility.class.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    mContext.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
//            Log.v(TAG, "accessibilityEnabled = " + accessibilityEnabled);
        } catch (Settings.SettingNotFoundException e) {
//            Log.e(TAG, "Error finding setting, default accessibility to not found: "
//                    + e.getMessage());
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');
        if (accessibilityEnabled == 1) {
//            Log.v(TAG, "***ACCESSIBILITY IS ENABLED*** -----------------");
            String settingValue = Settings.Secure.getString(
                    mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();
//                    Log.v(TAG, "-------------- > accessibilityService :: " + accessibilityService + " " + service);
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        Log.v(TAG, "We've found the correct setting - accessibility is switched on!");
                        return true;
                    }
                }
            }
        } else {
            Log.v(TAG, "***ACCESSIBILITY IS DISABLED***");
        }
        return false;
    }
    /////////////////////////////////////////////

    /*
    Doing Iyek Transliteration
     */
    private void doIyekTransliteration() {
        //First Cut theword from text veiw
        //ifthe text is all meitei mayek
        // dont continue &&if previous word is not null paste previous word
        //else
        //Cut the word a
        // nd Do Iyek
        //paste the Word
        //Store tothe previous Word
        AccessibilityNodeInfo curActivity = findScrollableNode(getRootInActiveWindow());
        if (curActivity == null) {
            return;
        }
        Bundle arguments = new Bundle();
//        //Log.d("INSIDE ACCESSIBILIT1", String.valueOf(curActivity.getMaxTextLength()));
//        //Log.d("INSIDE ACCESSIBILIT2", String.valueOf(curActivity.getText().length()));
//        //Log.d("INSIDE ACCESSIBILIT3", String.valueOf(curActivity.getTextSelectionEnd()));
//        //Log.d("INSIDE ACCESSIBILIT4", String.valueOf(curActivity.getTextSelectionStart()));
        try {
            arguments.putInt(AccessibilityNodeInfo.ACTION_ARGUMENT_SELECTION_START_INT, 0);
            arguments.putInt(AccessibilityNodeInfo.ACTION_ARGUMENT_SELECTION_END_INT, curActivity.getText().length());
//            Log.d("THE LENTH VALUE", String.valueOf(curActivity.getTextSelectionEnd()) + "   " + curActivity.getText());
            if (curActivity.getTextSelectionEnd() < 1) {
                return;
            }
//            Log.d("LENGTH" + curActivity.getTextSelectionEnd(), curActivity.getText().toString());
            curActivity.performAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SET_SELECTION.getId(), arguments);
        } catch (Exception e) {
            return;
        }
        //TODO read clipboard data
        try {
            userPreviousClippboard = readclipboard();
            String toconvert = curActivity.getText().toString();
            doiyek.convertnow(custumstring, getApplicationContext(), toconvert, userSaved);
//            Log.d("LENGTHConverted", doiyek.getConverted());
            setprevclip(doiyek.getConverted());
            if (toconvert.length() < 1) {
                setprevclip(this.userPreviousClippboard);
                return;
            }
            patchit(toconvert, curActivity, this.userPreviousClippboard, userSaved, doiyek.getConverted());
            waituntillchange(curActivity);
        } catch (Exception e) {
        }
    }

    private void patchit(final String toconvert, final AccessibilityNodeInfo curActivity, final String stringforclipboard, final userSavePreferance user, final String converted) {
        Runnable runnable = new Runnable() {
            int counter = 0;

            @Override
            public void run() {
//                Log.e("HANDLER", "run: Outside Runnable");
                if (!(toconvert.length() > 0 && toconvert.length() < user.getMaxWord())) {
//                    Log.e("HANDLER", "run: Runnable");
                    handler.removeCallbacks(this);
                } else {
                    if (converted.equals(readclipboard())) {
                        curActivity.performAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_PASTE.getId());
                        setprevclip(stringforclipboard);
//                        Toast.makeText(myAccessibility.this, "Cannot able to paste...", Toast.LENGTH_SHORT).show();

                        handler.removeCallbacks(this);
                    }
                    if (counter > 100) {
//                        Toast.makeText(myAccessibility.this, "Cannot able to paste...", Toast.LENGTH_SHORT).show();
                        setprevclip(stringforclipboard);
                        handler.removeCallbacks(this);
                    }
                    counter++;
                    handler.postDelayed(this, 20);
                }
            }
        };
    }

    private void waituntillchange(final AccessibilityNodeInfo curActivity) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                curActivity.performAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_PASTE.getId());
            }
        }, 100);
    }

    private void setprevclip(String string) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipdata = ClipData.newPlainText("label", string);
        clipboard.setPrimaryClip(clipdata);
    }

    private String readclipboard() {
        try {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            if ((clipboard.hasPrimaryClip()) && (clipboard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))) {
                ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
                if (item == null || item.getText() == null) {
                    return "";
                }
                return item.getText().toString();
            }
            return null;
        } catch (Exception e) {
        }
        return "";
    }

    private AccessibilityNodeInfo findScrollableNode(AccessibilityNodeInfo root) {
        if (root == null) {
            return null;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Deque<AccessibilityNodeInfo> deque = new ArrayDeque<>();
            deque.add(root);
            while (!deque.isEmpty()) {
                AccessibilityNodeInfo node = deque.removeFirst();
                if (node.getActionList().contains(AccessibilityNodeInfo.AccessibilityAction.ACTION_CLEAR_FOCUS)) {
//                //Log.d("shakemsg", String.valueOf(node));
                    ////Log.d("msg",String.valueOf(node.getTextSelectionEnd().toString().toLowerCase().contains("text")));
                    return node;
                }
                for (int i = 0; i < node.getChildCount(); i++) {
                    deque.addLast(node.getChild(i));
                }
            }
        }
        return null;
    }
}
