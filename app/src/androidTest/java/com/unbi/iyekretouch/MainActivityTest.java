package com.unbi.iyekretouch;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertNotNull;

public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule=new ActivityTestRule<>(MainActivity.class);
    private MainActivity mainActivity=null;

    @Before
    public void setUp() throws Exception {
        mainActivity=mainActivityActivityTestRule.getActivity();
    }

    @Test
    public void testLaunch(){
        View view=mainActivity.findViewById(R.id.seperator);
        assertNotNull(view);
    }

    @After
    public void tearDown() throws Exception {
        mainActivity=null;
    }
}