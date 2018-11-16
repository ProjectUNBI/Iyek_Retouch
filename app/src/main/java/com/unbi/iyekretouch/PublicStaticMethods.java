package com.unbi.iyekretouch;

import com.google.gson.Gson;

public class PublicStaticMethods {

    public static final int MYSTOPSERVICE=2;
    public static final int MYSTARTSERVICE=1;
    public static final int MYRESTARTSERVICE=3;
    public static final int MYSHAKEFROMBROADCAST=4;
    public static final String USERSAVEPREFERANCE="USERSAVEPREFERANCE";
    public static final String CUSTOMWORD="CUSTOMWORD";

    public static <T> String ObjectToGsonString(T obj){
        final Gson gson = new Gson();
        return gson.toJson(obj);
    }
    public static <T> T getGsonToObject(String gsonString,Class<T> classType){
        final Gson gson = new Gson();
        return gson.fromJson(gsonString, classType);
    }

}
