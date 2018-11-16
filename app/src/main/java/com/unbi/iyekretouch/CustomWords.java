package com.unbi.iyekretouch;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

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

}
