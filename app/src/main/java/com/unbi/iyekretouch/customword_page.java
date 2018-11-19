package com.unbi.iyekretouch;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.clans.fab.FloatingActionButton;
import com.github.ksoichiro.android.observablescrollview.ObservableGridView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;

import static com.unbi.iyekretouch.PublicStaticMethods.CUSTOMWORDOBJ;
import static com.unbi.iyekretouch.PublicStaticMethods.USERSAVEPREFERANCE;

public class customword_page extends CustumwodPageBase implements ObservableScrollViewCallbacks {

    public static enum Action {
        LR, // Left to Right
        RL, // Right to Left
        TB, // Top to bottom
        BT, // Bottom to Top
        None // when no action was detected
    }


    private static final String logTag = "SwipeDetector";
    private static final int MIN_DISTANCE = 100;
    private float downX, downY, upX, upY;
    private Swipedetector.Action mSwipeDetected = Swipedetector.Action.None;
    private boolean conefromedittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customwordarray = new ArrayList<>();
        setContentView(R.layout.custumword);
        Engadd = (EditText) findViewById(R.id.engadd);
//        Engadd.setVisibility(View.GONE);
        Iyekadd = (EditText) findViewById(R.id.Iyekadd);
//        Iyekadd.setVisibility(View.GONE);
        addbutton = (FloatingActionButton) findViewById(R.id.addWord);
        sharebutton = (FloatingActionButton) findViewById(R.id.share);
        whatsappshare = (FloatingActionButton) findViewById(R.id.whatsappshare);
        whatsappshare.hide(false);
        toobar = (Toolbar) findViewById(R.id.toolbar);
        toobar.setVisibility(View.GONE);
        gridView = (ObservableGridView) findViewById(R.id.grid);
        gridView.setScrollViewCallbacks(this);
        whatsappshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharewhatsapp();
            }
        });
        Iyekadd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (!comefromredit) {
                        Iyekadd.setText("");
                    }
                    conefromedittext = true;
////                Iyekadd.requestFocus(
                }
            }
        });
        Engadd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (!comefromredit) {
                        Engadd.setText("");
                    }
                    conefromedittext = true;
////                    Engadd.requestFocus();
                }
            }
        });
        sharebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//TODO SHARE
                if (whatsappshare.isHidden()) {
                    sharebutton.hide(true);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            whatsappshare.show(true);
                            sharebutton.setImageResource(R.drawable.save);
                            sharebutton.show(true);
                        }
                    }, 500);
                    return;
                }
                doshareactivity();
            }
        });
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toobar.getVisibility() == View.VISIBLE) {
                    if (Engadd.getText().toString().equals("You word here....") || Engadd.getText().toString().equals("") || Engadd == null) {
                        return;
                    }
                    if (Iyekadd.getText().toString().equals("Your Iyek here...") || Iyekadd.getText().toString().equals("") || Engadd == null) {
                        return;
                    }
                    Log.d("BUGGGY BUG", Engadd.getText().toString());
                    Log.d("BUGGY BYG", Iyekadd.getText().toString());
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
                    doupdatesavecustomword();
                    Engadd.setText("");
                    Iyekadd.setText("");
                    return;
                }
                doaddwordactivity();
            }
        });
        addbutton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                doimportfromfile();
                return false;
            }
        });
        //Read if there is User Save Preferancr if no
        //Load the custom Word from Sharepreferance
        //Make the Object
        //Savetoo map
        //
//        for (int i = 0; i < 60; i++) {
//            customwordarray.add("key" + i);
//            customwordarray.add("value" + i);
//        }
        customwordarray = new ArrayList<>();
        getmycustumWord();
        thiscustomword = readcustomword();
        int counter = 1;
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, customwordarray);
        gridView.setAdapter(adapter);
        final Swipedetector swipeDetector = new Swipedetector();
        gridView.setOnTouchListener(swipeDetector);
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                Log.d("HELLO LONGCLICK", "YOLO");
                //TODO show the TEXT edit and test veiw
                doredittheword(position);
                //set the Text to the what is on it
                return true;
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("GRIDVIEW", String.valueOf(position) + "   " + String.valueOf(id) + "  " + String.valueOf(view) + "     " + String.valueOf(parent));
                Log.d("ADAPTER VIEW", parent.toString());
                if (swipeDetector.swipeDetected()) {
                    if (swipeDetector.getAction() == Swipedetector.Action.RL) {
                        dobviewdeletwithanim(parent,view,position);
                    } else if (swipeDetector.getAction() == Swipedetector.Action.LR) {
                        dobviewdeletwithanim(parent,view,position);
                    } else if (swipeDetector.getAction() == Swipedetector.Action.BT) {
                    } else if (swipeDetector.getAction() == Swipedetector.Action.TB) {
                    }
                }
            }
        });
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
                Log.d("TAG", "keypadHeight = " + keypadHeight);
                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                    // keyboard is opened
                    Log.d("KEYBOARD", "SHOW");
                    keyboardwasshown = true;
//                    keyboardopen();
                } else {
                    // keyboard is closed
                    keyboardclose();
                    keyboardwasshown = false;
                    Log.d("KEYBOARD", "HIGH");
                }
            }
        });
    }

    private void dobviewdeletwithanim(AdapterView<?> parent, final View view, final int position) {



        YoYo.with(Techniques.FadeOutDown)
                .duration(200)
                .repeat(0)
                .playOn(view);
        final View v = getPartnerview(parent, position);
        YoYo.with(Techniques.FadeOutDown)
                .duration(200)
                .repeat(0)
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        dodelete(position);
                        YoYo.with(Techniques.FadeInUp)
                                .duration(200)
                                .repeat(0)
                                .playOn(view);
                        YoYo.with(Techniques.FadeInUp)
                                .duration(200)
                                .repeat(0)
                                .playOn(v);

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                })
                .playOn(v);
//                        .playOn(gridView.findViewById(position));



    }

    private View getPartnerview(AdapterView<?> parent, int id) {
        if (!(id % 2 == 0)) {
            id--;
        } else {
            id++;
        }
        return parent.getChildAt(id);
    }

    private void keyboardclose() {//TODO HERE
//        getWindow().getDecorView().clearFocus();//clearing Focus
        if (keyboardwasshown) {
            conefromedittext = false;
            doafterkeyboardclose();
        }
    }
//
//    private void keyboardopen() {
//        seperator.setCursorVisible(true);
//    }

    private void dodelete(int position) {
        if (position % 2 == 0) {
            customwordarray.remove(position);
            customwordarray.remove(position);
        } else {
            customwordarray.remove(position);
            customwordarray.remove(position - 1);
        }
        adapter.notifyDataSetChanged();
        doupdatesavecustomword();
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        ActionBar ab = getSupportActionBar();
        if (ab == null) {
            return;
        }
        if (scrollState == ScrollState.UP) {
            if (ab.isShowing()) {
                ab.hide();
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (!ab.isShowing()) {
                ab.show();
            }
        }
    }

    private CustomWords readcustomword() {
        CustomWords custum = new CustomWords();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(USERSAVEPREFERANCE, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(CUSTOMWORDOBJ)) {
            final Gson gson = new Gson();
            custum = gson.fromJson(sharedPreferences.getString(CUSTOMWORDOBJ, ""), CustomWords.class);
        }
        return custum;
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int X = (int) event.getX();
        int Y = (int) event.getY();
        int eventaction = event.getAction();
        Log.d("ACTION", String.valueOf(eventaction));
        switch (eventaction) {
            case MotionEvent.ACTION_DOWN:
//                Toast.makeText(this, "ACTION_DOWN AT COORDS "+"X: "+X+" Y: "+Y, Toast.LENGTH_SHORT).show();
                hidevisible();
                userinterected();
                Log.d("MOVE", "Down");
                break;
            case MotionEvent.ACTION_MOVE:

//                Toast.makeText(this, "MOVE "+"X: "+X+" Y: "+Y, Toast.LENGTH_SHORT).show();
                userinterected();
                break;
            case MotionEvent.ACTION_UP:
                userinterected();
//                Toast.makeText(this, "ACTION_UP "+"X: "+X+" Y: "+Y, Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    private void userinterected() {
        //TODO USER INTERACT

        if (!whatsappshare.isHidden()) {
            whatsappshare.hide(true);
            sharebutton.hide(true);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    sharebutton.setImageResource(R.drawable.share2);
                    sharebutton.show(true);
                }
            }, 500);
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if(toobar.getVisibility()==View.VISIBLE){
            hidevisible();
        }else {
            finish();
        }
    }

}

