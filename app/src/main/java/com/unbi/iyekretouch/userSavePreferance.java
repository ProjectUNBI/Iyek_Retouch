package com.unbi.iyekretouch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

import java.io.Serializable;

import static com.unbi.iyekretouch.PublicStaticMethods.USERPREFCHANGE;
import static com.unbi.iyekretouch.PublicStaticMethods.ObjectToGsonString;
import static com.unbi.iyekretouch.PublicStaticMethods.USERSAVEPREFERANCE;

public class userSavePreferance implements Serializable {

    int multiplyer=16;
    private float usershakelevel = (float) 2.8;//preset value p\of shake
    private boolean is_iyekOn;
    private boolean is_customwordOn;
    private boolean is_IyekenglispaasteOn;
    private boolean iyek_first = true;
    private String userEngSeperator = "\n\n@@@@@@@@@@\n\n";
    private int MaxWord = 300;


    /*
    Getter and Setter
     */

    public boolean isIyek_first() {
        return iyek_first;
    }

    public void setIyek_first(boolean iyek_first, Context app) {
        this.iyek_first = iyek_first;
        saveMe(app);
    }

    public int getMaxWord() {
        if (MaxWord < 0) {
            MaxWord = 0;
        }
        return MaxWord;
    }

    public void setMaxWord(int maxWord, Context app) {
        MaxWord = maxWord;
        saveMe(app);
    }

    public float getUsershakelevel() {
//        //Log.d("MY SENSIBILIT",String.valueOf(usershakelevel+(float) 1));
        if((float)1.2>usershakelevel){
            return (float)1.2;
        }
        return usershakelevel+(float) 1;
    }


    public int getUserSeekbarshakelevel() {
        return (int)(usershakelevel*multiplyer);
    }

    public void setUsershakelevel(float usershakelevel, Context app) {
        float userfloat=((float)usershakelevel)/multiplyer;
        this.usershakelevel = userfloat;
        saveMe(app);
    }

    public boolean isIs_iyekOn() {
        return is_iyekOn;
    }

    public void setIs_iyekOn(boolean is_iyekOn, Context app) {
        this.is_iyekOn = is_iyekOn;
        saveMe(app);
    }

    public boolean isIs_customwordOn() {
        return is_customwordOn;
    }

    public void setIs_customwordOn(boolean is_customwordOn, Context app) {
        this.is_customwordOn = is_customwordOn;
        saveMe(app);
    }

    public boolean isIs_IyekenglispaasteOn() {
        return is_IyekenglispaasteOn;
    }

    public void setIs_IyekenglispaasteOn(boolean is_IyekenglispaasteOn, Context app) {
        this.is_IyekenglispaasteOn = is_IyekenglispaasteOn;
        saveMe(app);
    }

    public String getUserEngSeperator() {
        return userEngSeperator;
    }

    public void setUserEngSeperator(String userEngSeperator, Context app) {
        this.userEngSeperator = userEngSeperator;
        saveMe(app);
    }


    public  void saveMe(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USERSAVEPREFERANCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        final Gson gson = new Gson();
        String serializedObject = gson.toJson(this);
        sharedPreferencesEditor.putString("usersave", serializedObject);
        sharedPreferencesEditor.apply();

        //send to refreshObjectOd Assesibility service
        customIntent mycustumobj=new customIntent(USERPREFCHANGE,serializedObject);
        Intent i = new Intent(context, myAccessibility.class);
        Bundle b = new Bundle();
        b.putString("myObject", ObjectToGsonString(mycustumobj));
        i.putExtras(b);
        context.startService(i);
    }



}
