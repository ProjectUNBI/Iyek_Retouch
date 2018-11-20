package com.unbi.iyekretouch;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.xw.repo.BubbleSeekBar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import info.hoang8f.widget.FButton;

import static com.unbi.iyekretouch.PublicStaticMethods.MYPACKAGE;
import static com.unbi.iyekretouch.PublicStaticMethods.MYSTARTSERVICE;
import static com.unbi.iyekretouch.PublicStaticMethods.USERSAVEPREFERANCE;

public class MainActivity extends MainActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        TODO delet this code
         */
//        Intent intent = new Intent(getApplicationContext(), myAccessibility.class);
//        Bundle b = new Bundle();
//        b.putInt("extra", MYSTARTSERVICE);//Flag ""
//        intent.putExtras(b);
//        getApplicationContext().startService(intent);
        ////////////////////
        //menu
        userSaved = new userSavePreferance();
        //Reading From Share Preference
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(USERSAVEPREFERANCE, Context.MODE_PRIVATE);
        if (sharedPreferences.contains("usersave")) {
            final Gson gson = new Gson();
            userSaved = gson.fromJson(sharedPreferences.getString("usersave", ""), userSavePreferance.class);
        }
        //Readed
        wordcount = (TextView) findViewById(R.id.wordnumber);
        customwordEnable = (TextView) findViewById(R.id.customEnbaleTextveiw);
        iyekEngenable = (TextView) findViewById(R.id.isIyekIngPating);
        isiyekFirst = (TextView) findViewById(R.id.isIyekisFirst);
        seekbar = (BubbleSeekBar) findViewById(R.id.seekbar);
        IyekButton = (FButton) findViewById(R.id.IyekButton);
        seperator = (EditText) findViewById(R.id.seperator);
        //Custom Word enable Button Set
        if (userSaved.isIs_customwordOn()) {
            customwordEnable.setText("Enabled");
            customwordEnable.setBackgroundColor(getResources().getColor(R.color.fbutton_color_nephritisCoulor));//fbutton_color_concreteColour,fbutton_color_nephritisCoulo
        } else {
            customwordEnable.setText("Disabled");
            customwordEnable.setBackgroundColor(getResources().getColor(R.color.fbutton_color_concreteColour));
        }
        //Is iyek-Eng pasting enable set
        if (userSaved.isIs_IyekenglispaasteOn()) {
            iyekEngenable.setText("Enabled");
            iyekEngenable.setBackgroundColor(getResources().getColor(R.color.fbutton_color_nephritisCoulor));//fbutton_color_concreteColour,fbutton_color_nephritisCoulo
        } else {
            iyekEngenable.setText("Disabled");
            iyekEngenable.setBackgroundColor(getResources().getColor(R.color.fbutton_color_concreteColour));
        }
        //is iyek preeed in Iyek eng Pasting
        if (userSaved.isIyek_first()) {
            isiyekFirst.setText("Iyek+Eng");
        } else {
            isiyekFirst.setText("Eng-Iyek");
        }
        /*
        Doim=ng for Iyek Button
         */
        if (this.userSaved.isIs_iyekOn()) {
            doforIyekOn();
        } else {
            doforIyekOff();
        }
        /*
        Done fore Iyek Button
         */
        seperator.setText(userSaved.getUserEngSeperator());
        seekbar.setProgress((float) userSaved.getUserSeekbarshakelevel());
        wordcount.setText(String.valueOf(userSaved.getMaxWord()));
        /*
         Set Seekba
         */
        seekbar.setOnProgressChangedListener(new mybubleseekbar(getApplicationContext()));
        //We Set SEEKBAR
        /*

        Keyboard checker
         */
        final View thisview = this.findViewById(android.R.id.content).getRootView();
        thisview.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                thisview.getWindowVisibleDisplayFrame(r);
                int screenHeight = thisview.getRootView().getHeight();
                // r.bottom is the position above soft keypad or device button.
                // if keypad is shown, the r.bottom is smaller than that before.
                int keypadHeight = screenHeight - r.bottom;
//                //Log.d("TAG", "keypadHeight = " + keypadHeight);
                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                    // keyboard is opened
                    keyboardopen();
                } else {
                    // keyboard is closed
                    keyboardclose();
                }
            }
        });
        /*
        Finished Keyboard checker
         */
        myintent = new Intent(this, myAccessibility.class);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateUI(intent);
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter(MYPACKAGE));

         /*
        INTENT RECEIVE MANAGEMENT
         */
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
//        //Log.d("hi", intent.toString());
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            handleSendText(intent); // Handle text being sent
        }
        if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            handleSendTextMultipe(intent); // Handle text being sent
        }
        //VIEW"
        if (Intent.ACTION_VIEW.equals(action) && type != null) {
            viewhelper(intent); // Handle text being sent
        }

//        if(!batteryoptimize()){
//            showoptimiseui();
//        }
    }



    /*
    Updtae You UI HERE
     */
    private void updateUI(Intent intent) {
        userSaved.setIs_iyekOn(false, getApplicationContext());
        IyekButton.setTextColor(getResources().getColor(R.color.fbutton_color_alizarinColour));
        //This code is for the accessibility sewrvice not given
        //So Open The Accessibility
        if (buttonIyekClick) {
            buttonIyekClick = false;
            wenttoasscsibility = true;
            Toast.makeText(this, "Ooops..Please give the Accesibility permission...", Toast.LENGTH_LONG).show();
            OpenAccessibilityService();
        }
    }

    ///////////////////////
    @Override
    public void onResume() {
        super.onResume();
        startService(myintent);
        registerReceiver(broadcastReceiver, new IntentFilter(MYPACKAGE));
        comeOutafterAccessibilitOpen();
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
        stopService(myintent);
    }

    /*
    Method calling from is there Keyboard checker
     */
    private void keyboardclose() {
        seperator.setCursorVisible(false);//set cuser invisible
        getWindow().getDecorView().clearFocus();//clearing Focus
        String seper = seperator.getText().toString();
        //stor the text in sepertor
        userSaved.setUserEngSeperator(seper, getApplicationContext());//TODO you might get some Bug here
        seperator.setText(String.valueOf(userSaved.getUserEngSeperator()));
    }

    private void keyboardopen() {
        seperator.setCursorVisible(true);
    }

    /*
    Methods on clicking Iyek Eng Pasting
     */
    public void IyekEngPasteEnable(View view) {
        if (userSaved.isIs_IyekenglispaasteOn()) {
            userSaved.setIs_IyekenglispaasteOn(false, getApplicationContext());
        } else {
            userSaved.setIs_IyekenglispaasteOn(true, getApplicationContext());
        }
        if (userSaved.isIs_IyekenglispaasteOn()) {
            iyekEngenable.setText("Enabled");
            iyekEngenable.setBackgroundColor(getResources().getColor(R.color.fbutton_color_nephritisCoulor));//fbutton_color_concreteColour,fbutton_color_nephritisCoulo
        } else {
            iyekEngenable.setText("Disabled");
            iyekEngenable.setBackgroundColor(getResources().getColor(R.color.fbutton_color_concreteColour));
        }
    }
    /*
    Method on Clicking Iyek+Eng
     */

    public void IyekFirst(View view) {
        if (userSaved.isIyek_first()) {
            userSaved.setIyek_first(false, getApplicationContext());
        } else {
            userSaved.setIyek_first(true, getApplicationContext());
        }
        if (userSaved.isIyek_first()) {
            isiyekFirst.setText("Iyek+Eng");
        } else {
            isiyekFirst.setText("Eng+Iyek");
        }
    }

    public void layout2OnClick(View view) {
    }

    /*
    Fore Custom Word
     */
    public void customWordEnable(View view) {
        if (userSaved.isIs_customwordOn()) {
            userSaved.setIs_customwordOn(false, getApplicationContext());
        } else {
            userSaved.setIs_customwordOn(true, getApplicationContext());
        }
        if (userSaved.isIs_customwordOn()) {
            customwordEnable.setText("Enabled");
            customwordEnable.setBackgroundColor(getResources().getColor(R.color.fbutton_color_nephritisCoulor));//fbutton_color_concreteColour,fbutton_color_nephritisCoulo
        } else {
            customwordEnable.setText("Disabled");
            customwordEnable.setBackgroundColor(getResources().getColor(R.color.fbutton_color_concreteColour));
        }
    }

    public void CustomWordPage(View view) {
        Intent intent = new Intent(getBaseContext(), customword_page.class);
        startActivity(intent);
    }

    public void layoutOneOnClick(View view) {
    }

    //menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public void MinusButton(View view) {
        this.userSaved.setMaxWord(userSaved.getMaxWord() - 50, getApplicationContext());
        this.wordcount.setText(String.valueOf(this.userSaved.getMaxWord()));
    }

    public void PlusButton(View view) {
        this.userSaved.setMaxWord(userSaved.getMaxWord() + 50, getApplicationContext());
        this.wordcount.setText(String.valueOf(this.userSaved.getMaxWord()));
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.unbiabout: {
//                //Log.d("log", "select unbi about");
                Intent intent = new Intent(getBaseContext(), about.class);
                startActivity(intent);
                break;
            }
        }
        //respond to menu item selection
        return true;
    }

    public void ClickOnIyek(View view) {
        buttonIyekClick = true;
        IyekbuttonClick();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
