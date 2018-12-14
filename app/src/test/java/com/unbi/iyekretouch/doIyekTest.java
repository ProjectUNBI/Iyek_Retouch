package com.unbi.iyekretouch;

import android.content.Context;

import org.junit.Test;

import static org.junit.Assert.*;

public class doIyekTest {

    @Test
    public void convertnow() {
        userSavePreferance userpref=new userSavePreferance();
        Context context =null;
        String preIyek="k";
        doIyek doiyek=new doIyek();
        doiyek.convertnow("hh",context,preIyek,userpref);
        String output=doiyek.getConverted();
        System.out.print(output);
        String expect="ꯀ";
        assertEquals(expect,output);

    }
}