package com.unbi.iyekretouch;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xw.repo.BubbleSeekBar;

import info.hoang8f.widget.FButton;

import static com.unbi.iyekretouch.PublicStaticMethods.MYPACKAGE;
import static com.unbi.iyekretouch.PublicStaticMethods.MYSTARTSERVICE;
import static com.unbi.iyekretouch.PublicStaticMethods.MYSTOPSERVICE;
import static com.unbi.iyekretouch.PublicStaticMethods.ObjectToGsonString;

public class myExtraActivity extends AppCompatActivity {

    userSavePreferance userSaved;
    TextView wordcount, customwordEnable, iyekEngenable, isiyekFirst,PhonicEnable;
    EditText seperator;
    FButton IyekButton;
    BubbleSeekBar seekbar;
    boolean wenttoasscsibility;

    protected void IyekbuttonClick() {
        if (this.userSaved.isIs_iyekOn()) {
            this.userSaved.setIs_iyekOn(false, getApplication());
            doforIyekOff();
        } else {
            this.userSaved.setIs_iyekOn(true, getApplication());
            doforIyekOn();
        }
    }

    protected void doforIyekOn() {
        //Check if the Accessibility Permission is given to app
        //if not given Open the Accessibility settinng and return he code & set wenttoasscsibility=true and this.userSaved.setIs_iyekOn(false);
        //{if codes goes on the accessibility permisson is given}
        this.userSaved.setIs_iyekOn(true, getApplication());
        //show Notification//Start the service
        Context context = getApplicationContext();
//        Intent intent = new Intent(context, myAccessibility.class);
//        Bundle b = new Bundle();
//        //TODO HERE
//        b.putInt("extra", MYSTARTSERVICE);//Flag ""
//        intent.putExtras(b);
//        context.startService(intent);
        customIntent mycustumobj = new customIntent(MYSTARTSERVICE);
        Intent i = new Intent(context, myAccessibility.class);
        Bundle b = new Bundle();
        b.putString("myObject", ObjectToGsonString(mycustumobj));
        i.putExtras(b);
        context.startService(i);
        IyekButton.setTextColor(getResources().getColor(R.color.fbutton_color_green_seaColour));
    }

    protected void doforIyekOff() {
//        this.userSaved.setIs_iyekOn(false);
        this.userSaved.setIs_iyekOn(false, getApplication());
        //Hide Nothficcation// stop the service
        //Send Acceeibility service to disable it,,,i mean take down the Notificcation;
        Context context = getApplicationContext();
//        Intent intent = new Intent(context, myAccessibility.class);
//        Bundle b = new Bundle();
//        //TODO here
//        b.putInt("extra", MYSTOPSERVICE);//Flag ""
//        intent.putExtras(b);
//        context.startService(intent);
        customIntent mycustumobj = new customIntent(MYSTOPSERVICE);
        Intent i = new Intent(context, myAccessibility.class);
        Bundle b = new Bundle();
        b.putString("myObject", ObjectToGsonString(mycustumobj));
        i.putExtras(b);
        context.startService(i);
        IyekButton.setTextColor(getResources().getColor(R.color.fbutton_color_alizarinColour));
    }

    protected void comeOutafterAccessibilitOpen() {
        if (wenttoasscsibility) {
            doforIyekOn();
            wenttoasscsibility = false;
            if (!isAccessibilitySettingsOnok(this)) {
                Toast.makeText(this, "Sorry... we can't help you \nunless you give that permission...", Toast.LENGTH_LONG).show();
            }
        }
    }

    /*
    Check Accssibility service
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
            Log.e(TAG, "Error finding setting, default accessibility to not found: "
                    + e.getMessage());
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
                    Log.v(TAG, "-------------- > accessibilityService :: " + accessibilityService + " " + service);
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
    Open Accessibility Service
     */

    protected void OpenAccessibilityService() {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivity(intent);
        wenttoasscsibility = true;
    }
    /*
    Show Dialog Box
     */
    public void openDialog() {

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        // Set Custom Title
        TextView title = new TextView(this);
        // Title Properties
        title.setText("Phomic Separator");
        title.setPadding(10, 10, 10, 10);   // Set Position
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.BLACK);
        title.setTextSize(20);
        alertDialog.setCustomTitle(title);

        // Set Message
        final EditText msg = new EditText(this);
        // Message Properties
        msg.setText(userSaved.getPhonicSeperator());
        msg.setGravity(Gravity.CENTER_HORIZONTAL);
        msg.setTextColor(Color.BLACK);
        alertDialog.setView(msg);

        // Set Button
        // you can more buttons
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,"OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Perform Action on Button
                userSaved.setPhonicSeperator(msg.getText().toString(),getApplicationContext());
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Perform Action on Button
            }
        });

        new Dialog(getApplicationContext());
        alertDialog.show();

        // Set Properties for OK Button
        final Button okBT = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
        LinearLayout.LayoutParams neutralBtnLP = (LinearLayout.LayoutParams) okBT.getLayoutParams();
        neutralBtnLP.gravity = Gravity.FILL_HORIZONTAL;
        okBT.setPadding(50, 10, 10, 10);   // Set Position
        okBT.setTextColor(Color.BLUE);
        okBT.setLayoutParams(neutralBtnLP);

        final Button cancelBT = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        LinearLayout.LayoutParams negBtnLP = (LinearLayout.LayoutParams) okBT.getLayoutParams();
        negBtnLP.gravity = Gravity.FILL_HORIZONTAL;
        cancelBT.setTextColor(Color.RED);
        cancelBT.setLayoutParams(negBtnLP);
    }
}
