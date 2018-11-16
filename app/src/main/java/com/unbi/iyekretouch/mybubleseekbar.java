
/*
Hello world, I am making this object because the change value of the seekbar give al ot of data
I want to chanvge some oh=f this code according to y rue , i want to delay 1 seconds to do after the data is change
or value is change

 */


package com.unbi.iyekretouch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.xw.repo.BubbleSeekBar;

import java.io.Serializable;

import static com.unbi.iyekretouch.PublicStaticMethods.MYRESTARTSERVICE;
import static com.unbi.iyekretouch.PublicStaticMethods.MYSTARTSERVICE;
import static com.unbi.iyekretouch.PublicStaticMethods.ObjectToGsonString;

public class mybubleseekbar extends BubbleSeekBar.OnProgressChangedListenerAdapter {

    boolean methodcontroller;
    long currentmillis;
    long previousmilli;
    int myprogress;
    Context context;
    customIntent mycustumobj;

    public mybubleseekbar(Context context){
        currentmillis=System.currentTimeMillis();
        previousmilli=System.currentTimeMillis();
        this.context=context;
    }


    ///Overide
    @Override
    public void onProgressChanged(BubbleSeekBar bubbleSeekBar, final int progress, float progressFloat, boolean fromUser) {

        myprogress=progress;
//        Log.d(String.valueOf(progress),"Lasttime change");

        mycustumobj=new customIntent(MYRESTARTSERVICE,progress);
        if(currentmillis-previousmilli>1000){
            previousmilli=currentmillis;//protecting this handeler method
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(context, myAccessibility.class);
                    Bundle b = new Bundle();
                    b.putString("myObject", ObjectToGsonString(mycustumobj));
                    i.putExtras(b);
                    context.startService(i);

                }
            }, 1000);
        }
        currentmillis=System.currentTimeMillis();//assingning values to current milli
    }

    @Override
    public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
    }

    @Override
    public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
    }
}


class customIntent implements Serializable{
    int myflag;

    public customIntent(int mystartservice, int progress) {
        setMyflag(mystartservice);
        setShakevalue(progress);
    }
    public customIntent(int mystartservice) {
        setMyflag(mystartservice);
    }


    public int getMyflag() {
        return myflag;
    }

    public void setMyflag(int myflag) {
        this.myflag = myflag;
    }

    public int getShakevalue() {
        return shakevalue;
    }

    public void setShakevalue(int shakevalue) {
        this.shakevalue = shakevalue;
    }

    int shakevalue;
}