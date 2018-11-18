package com.unbi.iyekretouch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import static com.unbi.iyekretouch.PublicStaticMethods.CUSTOMWORDOBJ;
import static com.unbi.iyekretouch.PublicStaticMethods.CUSTUMWORDCHANGE;
import static com.unbi.iyekretouch.PublicStaticMethods.ObjectToGsonString;
import static com.unbi.iyekretouch.PublicStaticMethods.USERPREFCHANGE;
import static com.unbi.iyekretouch.PublicStaticMethods.USERSAVEPREFERANCE;

public class CustomWords implements Serializable {
    private String customword;

    private Map<String, String> CustomWordMap;

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
//                    ", Value = " + entry.getValue());//  :w+(w+:
            string=string+entry.getKey()+"("+entry.getValue()+":";
        }
        Log.d("THE CUSTUM W",string);
        this.setCustomword(string);


    }
    public  void saveMe(Context context) {
        processCustomWord();

        Map<String,String>mymap=new TreeMap<>(new AlphaNumericStringComparator());
        mymap.putAll(getCustomWordMap());
        setCustomWordMap(mymap);

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
    public Map<String, String> getCustomWordMap() {
        return CustomWordMap;
    }

    public void setCustomWordMap(Map<String, String> customWordMap) {
        CustomWordMap = customWordMap;
    }


}
