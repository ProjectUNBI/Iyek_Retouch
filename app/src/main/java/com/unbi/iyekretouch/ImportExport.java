package com.unbi.iyekretouch;

public class ImportExport {
    CustomWords mycustomword;

    public ImportExport(CustomWords customWord){
        mycustomword=customWord;
    }

    private void export(){
        //Convert to GSON STRING
        //Save the String
    }

    private CustomWords importcus(){
        CustomWords custun=new CustomWords();
        //REad from share preferance
        //Readfrom file
        //convert to JSON
        //add all to CustomWord
        return  custun;
    }
}
