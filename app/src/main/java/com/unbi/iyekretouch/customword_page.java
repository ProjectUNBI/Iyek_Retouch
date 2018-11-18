package com.unbi.iyekretouch;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
        toobar = (Toolbar) findViewById(R.id.toolbar);
        toobar.setVisibility(View.GONE);
        gridView = (ObservableGridView) findViewById(R.id.grid);
        gridView.setScrollViewCallbacks(this);
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
                doshareactivity();
            }
        });
        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        customwordarray=new ArrayList<>();
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
                if (swipeDetector.swipeDetected()) {
                    if (swipeDetector.getAction() == Swipedetector.Action.RL) {
                        dodelete(position);
                    } else if (swipeDetector.getAction() == Swipedetector.Action.LR) {
                        dodelete(position);
                    } else if (swipeDetector.getAction() == Swipedetector.Action.BT) {
                    } else if (swipeDetector.getAction() == Swipedetector.Action.TB) {
                    }
                }
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        break;
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
    public void onBackPressed() {
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
        toobar.setVisibility(View.GONE);
    }
}

