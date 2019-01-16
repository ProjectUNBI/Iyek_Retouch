package com.unbi.iyekretouch;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.clans.fab.FloatingActionButton;
import com.github.ksoichiro.android.observablescrollview.ObservableGridView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;

import static com.unbi.iyekretouch.PublicStaticMethods.CUSTOMWORDOBJ;
import static com.unbi.iyekretouch.PublicStaticMethods.USERSAVEPREFERANCE;

public class CustumwodPageBase extends AppCompatActivity {

    protected CustomWords thiscustomword = new CustomWords();
    protected ArrayList<String> customwordarray;
    protected EditText Engadd, Iyekadd;
    protected FloatingActionButton addbutton, sharebutton,whatsappshare;
    protected ObservableGridView gridView;
    protected ArrayAdapter adapter;
    protected boolean wasLongclick;
    protected Toolbar toobar;
    protected boolean keyboardwasshown;
    protected boolean comefromredit;
    protected int thiscosition;

    protected void doimportfromfile() {
        //TODO IMPORT FILE
    }

    protected void doaddwordactivity() {
        //show text view
        showvisible();
    }

    protected void sharewhatsapp(){
        ImportExport myexpoet=new ImportExport();
        myexpoet.whatsappshare(this);
    }
    protected void doshareactivity() {
        //TODO Share activity


        //////////////////

        ImportExport myexport=new ImportExport();
        myexport.export(getApplicationContext());


    }



    protected void doupdatesavecustomword() {
        //convert arraylist to object arralyiost tomave
        //save to the sharepreferance
        //check if it is updat ein the assecibility service
        thiscustomword.getCustomWordMap().clear();//cleare the previous value
        for (int i=customwordarray.size();i>0;i-=2){
            thiscustomword.addCustomWorsd(customwordarray.get(i-2),customwordarray.get(i-1));
        }
        thiscustomword.saveMe(getApplicationContext());
    }

    protected void doredittheword(int position) {
        showvisible();
        if (position % 2 != 0) {
            position--;
        }
        Engadd.setText(customwordarray.get(position));
        Iyekadd.setText(customwordarray.get(position + 1));
        customwordarray.remove(position);
        customwordarray.remove(position);
        adapter.notifyDataSetChanged();
        comefromredit = true;
        thiscosition = position;
    }

    protected void doafterkeyboardclose() {
        if (Engadd.getText().toString().equals("") || Engadd == null) {
            return;
        }
        if (Iyekadd.getText().toString().equals("") || Engadd == null) {
            return;
        }
        //Log.d("BUGGGY BUG",Engadd.getText().toString());
        //Log.d("BUGGY BYG",Iyekadd.getText().toString());
        if (comefromredit) {
            comefromredit = false;
            if (thiscosition % 2 != 0) {
                thiscosition--;
            }
            ArrayList<String> array = new ArrayList<>();
            array.add(Engadd.getText().toString());
            array.add(Iyekadd.getText().toString());
            customwordarray.addAll(thiscosition, array);
        } else {
            customwordarray.add(0, Engadd.getText().toString());
            customwordarray.add(1, Iyekadd.getText().toString());
        }
        adapter.notifyDataSetChanged();
//        hidevisible();
        doupdatesavecustomword();
    }

    private void showvisible() {
        YoYo.with(Techniques.BounceInDown)
                .duration(700)
                .repeat(0)
                .playOn(toobar);
        toobar.setVisibility(View.VISIBLE);
    }

    protected void hidevisible() {
        if(!(toobar.getVisibility()==View.VISIBLE)){
            return;
        }
        YoYo.with(Techniques.FadeOutRight)
                .duration(300)
                .repeat(0)
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        toobar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                })
                .playOn(toobar);
//        toobar.setVisibility(View.GONE);
    }

    protected void getmycustumWord() {

        CustomWords custum = new CustomWords();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(USERSAVEPREFERANCE, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(CUSTOMWORDOBJ)) {
            final Gson gson = new Gson();
            custum = gson.fromJson(sharedPreferences.getString(CUSTOMWORDOBJ, ""), CustomWords.class);
        }
        for (Map.Entry<String,String> entry : custum.getCustomWordMap().entrySet()){
//            System.out.println("Key = " + entry.getKey() +
//                    ", Value = " + entry.getValue());
//            string=string+entry.getKey()+entry.getValue();
            customwordarray.add(entry.getKey());
            customwordarray.add(entry.getValue());
        }
    }


}
