package com.unbi.iyekretouch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import static com.unbi.iyekretouch.PublicStaticMethods.CUSTOMWORDOBJ;
import static com.unbi.iyekretouch.PublicStaticMethods.CUSTUMWORDCHANGE;
import static com.unbi.iyekretouch.PublicStaticMethods.ObjectToGsonString;
import static com.unbi.iyekretouch.PublicStaticMethods.USERPREFCHANGE;
import static com.unbi.iyekretouch.PublicStaticMethods.USERSAVEPREFERANCE;

public class CustomWords implements Serializable {
    private String customword;
    Map<String, String> CustomWordMap;

    public CustomWords(){
        CustomWordMap = new TreeMap<>();


    }

    public void addCustomWorsd(String eng, String Iyek) {
        this.CustomWordMap.put(eng, Iyek);
        processCustomWord();
    }

    public void removeCustomWOrd(String eng) {
        this.CustomWordMap.remove(eng);
        processCustomWord();
    }

    public String getCustomword() {
        return customword;
    }

    public void setCustomword(String customword) {
        this.customword = customword;
    }

    private void processCustomWord() {
/*
we wan Custom words like ths ":word(wordtoreplace: "
 */
        String string = ":";
        for (Map.Entry<String,String> entry : this.CustomWordMap.entrySet()){
//            System.out.println("Key = " + entry.getKey() +
//                    ", Value = " + entry.getValue());
            string=string+entry.getKey()+entry.getValue();
        }
        this.setCustomword(string);


    }
    public  void saveMe(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(USERSAVEPREFERANCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        final Gson gson = new Gson();
        String serializedObject = gson.toJson(this);
        sharedPreferencesEditor.putString(CUSTOMWORDOBJ, serializedObject);
        sharedPreferencesEditor.apply();

        //send to refreshObjectOd Assesibility service
        customIntent mycustumobj=new customIntent(CUSTUMWORDCHANGE,serializedObject);
        Intent i = new Intent(context, myAccessibility.class);
        Bundle b = new Bundle();
        b.putString("myObject", ObjectToGsonString(mycustumobj));
        i.putExtras(b);
        context.startService(i);
    }

}
