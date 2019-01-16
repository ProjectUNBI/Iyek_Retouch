package com.unbi.iyekretouch;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

public class doIyek {

    private String prepreiyek;
    private String preIyek;
    private String Converted;
    private String EnglishIyek;
    private String IyekEnglish;
    private boolean IyekEng;
    private boolean IyekFirst;
    private String Seperator;
    private boolean isCustomword;
    private int maximumcharacter;
    private String whatpaste = "";
    private String previousPreiyek = "";
    private boolean datamustclear,isphonicEnable;
    private String phonicSeperator;



    private void setpreferance(String preiyek, userSavePreferance userpreferance) {
        this.setPreIyek(preiyek);
        this.setIyekEng(userpreferance.isIs_IyekenglispaasteOn());
        this.setIyekFirst(userpreferance.isIyek_first());
        this.setSeperator(userpreferance.getUserEngSeperator());
        this.setCustomword(userpreferance.isIs_customwordOn());
        this.setMaximumcharacter(userpreferance.getMaxWord());
        this.setIsphonicEnable(userpreferance.isPhonic());
        this.setPhonicSeperator(userpreferance.getPhonicSeperator());
    }

    public void convertnow(String costumwordo, final Context context, String preiyek, userSavePreferance userpreferance) {
        setpreferance(preiyek, userpreferance);
//        try {
//            //Log.d("getWhatpaste()", getWhatpaste());
//            //Log.d("getPreviousPreiyek()", getPreviousPreiyek());
//            //Log.d("getPreIyek()", getPreIyek());
//        } catch (Exception e) {
//        }
        String arg = getPreIyek();
        String temp = null;
        if (this.isIyekEng()&getWhatpaste()!=null) {
            temp = getWhatpaste().replace("\n", " ").replace("\r", " ");
        }
        if (getWhatpaste() != null && getPreviousPreiyek() != null
                &&
                (getPreIyek().equals(getWhatpaste()) ||
                        (temp != null && getPreIyek().equals(temp))
                )
                ) {
            this.setConverted(getPreviousPreiyek());
            datamustclear = true;
            return;
        }
        setPreviousPreiyek(arg);
        if (arg.length() > 0 && arg.length() < this.getMaximumcharacter()) {
            /*
            PhonicSeperator
             */
            if(isIsphonicEnable()){
                arg = arg.replaceAll(getPhonicSeperator(), "ￅ");
            }
            //End of Phonic Seperator
            arg = arg.replaceAll("<", "︻");
            arg = arg.replaceAll(">", "︼");
            arg = arg.replaceAll("ã", "︶");
            arg = arg.replaceAll(":", "﹅");
            arg = arg.replaceAll("∆", "︴");
            if (isCustomword()) {
                //adding escape like
                arg = arg + "*####*:::::::" + costumwordo;
                arg = arg.replaceAll("(?s)\\b(\\w+)\\b(?=.*:\\1\\((\\w+)\\b)", "$2"); // :w+(w+:                                   //here we are doing this replacement of Custom Words......etc and all exept the following shown in next line comment
                String[] seperate0 = arg.split("\\*####\\*");                                                                         //here the *####*Dictio........... is remove
                arg = seperate0[0];
            }
            arg=arg.replaceAll("C","c");
            arg = arg + "*####*Dictionary:c=k:z=j:ch=ç:sh=s:oo=u:kh=õ:ng=ñ:th=θ:ph=f:jh=ɫ:gh=ö:bh=v:dh=ð:ee=i:aa=â:ei=ê:ou=ō:ae=e:ai=å:oi=ø:ui=û:ao=œ:L-꯭ꯂã:W-꯭ꯋã:R-꯭ꯔã:Y-꯭ꯌã:ar=ꯑ꯭ꯔ:er=ꯑꯦ꯭ꯔ:ir=ꯏ꯭ꯔ:or=ꯑꯣ꯭ꯔ:ur=ꯎ꯭ꯔ:aaa=ꯑꯥ:E-ꯑꯦ:I-ꯏ:O-ꯑꯣ:U-ꯎ:A-ꯑ:r-꯭ꯔã:";//here setting things for doing conditional search and replacement
            arg = arg.replaceAll("(?s)(\\w+)(?=.*:\\1=(.)\\b)", "$2");                                                 //here leter like "ch","dh","bh" are converted to single letter like ç,ð,v and so on
            arg = arg.replaceAll("(?s)([^aeiouAEIOU])\\B([WLRY])(?=.*:\\2\\-(\\w+)\\b:)", "ã$1$3");                    //here we converting word like "kWa" to "ãk-꯭ꯋã","kRa" to"ãk-꯭ꯔã" and so on["ã" is used to protect k from converting to kok lonsum and "l" to lai lonsum etc ...................
            arg = arg.replaceAll("([aeiouAEIOU])r", "$1ꯔ");                                                            //here we are converting word like "kar"to "kaꯔ","mer"to "meꯔ","lør"[previously it was loir before step2] to"løꯔ" and so on.......
            arg = arg.replaceAll("(?s)([AEIOU])(?=.*:\\1\\-(\\w+)\\b:)", "$2");                                        //here we are converting E-ꯑꯦ,I-ꯏ,O-ꯑꯣ,U-ꯎ,A-ꯑ
            arg = arg.toLowerCase();                                                                                                  // now we converted all word to lowercase. Upto here the input word,for example "KYaamgei hairiba maphamsi" to "ãk-꯭ꯌâmgê håꯔiba mafamsi"
            arg = arg.replaceAll("(?s)\\b([aeiou]r)\\B([^aeiou\\s][^aeiou])(?=.*:\\1=(\\w+)\\b)", "$3>$2");            //word like"markwa" to ????????????{{{i cannot rremember properly about this line}}}???????????
            arg = arg.replaceAll("(?s)([^âêōåøûœãaeiou\\W])r(?=.*:r\\-(\\w+)\\b)", "<$1$2");                           //here search and replace of "krip" to "<k-꯭ꯔãip"
            String[] seperate = arg.split("\\*####\\*");                                                                         //here the *####*Dictio........... is remove
            arg = seperate[0];                                                                                                        // upto here input word "KYaamgei hairiba maphamsi" to "ãk-꯭ꯌâmgã håꯔiba mafamsi" [no change because the above two line is for handlng "r" in different words]
            arg = arg.replaceAll("(\\w[aeiouâêōåøûœ])", "<$1");                                                        //now the word is like"ãk-꯭<ꯌâm<gã <hå<ꯔi<ba <ma<fam<si>"
            arg = arg.replaceAll("([aeiouâêōåøûœ])", "$1>");                                                           //now the word is like"ãk-꯭<ꯌâ>m<gã> <hå><ꯔi><ba> <ma<fa>m<si>"
            arg = arg + "*####*Dictionary:<k=ꯀ:<s=ꯁ:<l=ꯂ:<m=ꯃ:<p=ꯄ:<n=ꯅ:<ç=ꯆ:<t=ꯇ:<õ=ꯈ:<ñ=ꯉ:<θ=ꯊ:<w=ꯋ:<y=ꯌ:<h=ꯍ:<ŋ=ꯐ:<g=ꯒ:<ɫ=ꯓ:<r=ꯔ:<b=ꯕ:<j=ꯖ:<d=ꯗ:<ö=ꯘ:<ð=ꯙ:<v=ꯚ:œ>=ꯥꯎ:a>=ã:e>=ꯦ:â>=ꯥ:ê>=ꯩ:i>=ꯤ:o>=ꯣ:u>=ꯨ:ō>=ꯧ:å>=ꯥꯢ:ø>=ꯣꯢ:û>=ꯨꯢ:>c=ꯛ:>k=ꯛ:>l=ꯜ:>m=ꯝ:>p=ꯞ:>n=ꯟ:>t=ꯠ:>ñ=ꯪ:>x=ꯛꯁ:k=ꯀ:s=ꯁ:l=ꯂ:m=ꯃ:p=ꯄ:n=ꯅ:ç=ꯆ:t=ꯇ:õ=ꯈ:ñ=ꯉ:θ=ꯊ:w=ꯋ:y=ꯌ:h=ꯍ:u=ꯎ:i=ꯏ:f=ꯐ:a=ꯑ:g=ꯒ:ɫ=ꯓ:r=ꯔ:b=ꯕ:x=ꯖ:j=ꯖ:d=ꯗ:ö=ꯘ:ð=ꯙ:v=ꯚ:0=꯰:1=꯱:2=꯲:3=꯳:4=꯴:5=꯵:6=꯶:7=꯷:8=꯸:9=꯹:â=ꯑꯥ:e=ꯑꯦ:o=ꯑꯣ:ê=ꯑꯩ:ō=ꯑꯧ:å=ꯑꯏ:ø=ꯑꯣꯢ:û=ꯎꯢ:œ=ꯑꯥꯎ:.=꯫∆:?=꫱:,=꫰:";
            arg = arg.replaceAll("(?s)(<.)(?=.*:\\1=(\\w+)\\b)", "<$2");                                               //the word is like""ãk-꯭<ꯌâ>m<ꯒã> <ꯍå><ꯔi><ꯕa> <ꯃa<ꯐa>m<ꯁi>"
            arg = arg.replaceAll("(?s)(<.)(.>)(?=.*:\\2=(..)\\b)", "$1$3>");                                           //the word is like"ãk-꯭<ꯌꯥ>m<ꯒꯩ> <ꯍ=ꯥꯢ><ꯔꯤ><ꯕã> <ꯃã<ꯐã>m<ꯁꯤ>"
            arg = arg.replaceAll("(?s)(>.)(?=.*:\\1=(\\w+)\\b)", ">$2");                                               //the word is like""ãk-꯭<ꯌꯥ>ꯝ<ꯒꯩ> <ꯍ=ꯥꯢ><ꯔꯤ><ꯕã> <ꯃã<ꯐã>ꯝ<ꯁꯤ>"
            arg = arg.replaceAll("(?s)(.)(?=.*:\\1=(.*?):)", "<$2>");                                                  //the word is like""ãꯀ-꯭<ꯌꯥ>ꯝ<ꯒꯩ> <ꯍ=ꯥꯢ><ꯔꯤ><ꯕã> <ꯃã<ꯐã>ꯝ<ꯁꯤ>"
            String[] seperate1 = arg.split("\\*####\\*");                                                                        //again the sentence is splited like we done before
            arg = seperate1[0];
            arg = arg.replaceAll("∆><꯫", ".");                                                                         // this step is for diferentiating "." in sentence and "." in like"424.343"
            arg = arg.replaceAll("<|>|ã|:|∆", "");                                                                     //the word is like"ꯀ-꯭ꯌꯥꯝꯒꯩ ꯍ=ꯥꯢꯔꯤꯕ ꯃꯐꯝꯁꯤ"
            arg = arg.replaceAll("([꯰꯱꯲꯳꯴꯵꯶꯷꯸꯹])꯫([꯰꯱꯲꯳꯴꯵꯶꯷꯸꯹])", "$1.$2");                                         // here is number replacement
            arg = arg.replaceAll("([ꯣꯤꯥꯦꯧꯨꯩ])ꯪ", "$1ꯡ");                                                                     //here we are doing not to make nungtap and itap,ounap etc in same time and if happen so, we replace nungtap by ngou lonsum
            //arg=arg.replaceAll("꫰","");
            // the finaly word become "ꯀ-꯭ꯌꯥꯝꯒꯩ ꯍ=ꯥꯢꯔꯤꯕ ꯃꯐꯝꯁꯤ"
            //deescaping
            arg = arg.replaceAll("︻", "<");
            arg = arg.replaceAll("︼", ">");
            arg = arg.replaceAll("︶", "ã");
            arg = arg.replaceAll("﹅", ":");
            arg = arg.replaceAll("︴", "∆");
            arg = arg.replaceAll("ￅ", "");
            if (isIyekEng()) {
                if (isIyekFirst()) {
                    this.setConverted(arg + getSeperator() + getPreIyek());
                } else {
                    this.setConverted(getPreIyek() + getSeperator() + arg);
                }
            } else {
                this.setConverted(arg);
            }
        }//end if of the word lengh checker
        else {
            this.setConverted(arg);
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "Please increase the charecter limit in Iyek app", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }







    /*
    Getter and Setter
     */

    public boolean isCustomword() {
        return isCustomword;
    }

    public void setCustomword(boolean customword) {
        isCustomword = customword;
    }

    public String getSeperator() {
        return Seperator;
    }

    public void setSeperator(String seperator) {
        Seperator = seperator;
    }

    public boolean isIyekEng() {
        return IyekEng;
    }

    public void setIyekEng(boolean iyekEng) {
        IyekEng = iyekEng;
    }

    public boolean isIyekFirst() {
        return IyekFirst;
    }

    public void setIyekFirst(boolean iyekFirst) {
        IyekFirst = iyekFirst;
    }

    public String getPreIyek() {
        return preIyek;
    }

    public void setPreIyek(String preIyek) {
        this.preIyek = preIyek;
    }

    public String getConverted() {
        if (datamustclear) {
            datamustclear = false;
            setWhatpaste(null);
        }
        return Converted;
    }

    public void setConverted(String converted) {
        setWhatpaste(converted);
        Converted = converted;
    }

    public String getEnglishIyek() {
        return EnglishIyek;
    }

    public void setEnglishIyek(String englishIyek) {
        EnglishIyek = englishIyek;
    }

    public String getIyekEnglish() {
        return IyekEnglish;
    }

    public void setIyekEnglish(String iyekEnglish) {
        IyekEnglish = iyekEnglish;
    }

    public int getMaximumcharacter() {
        return maximumcharacter;
    }

    public void setMaximumcharacter(int maximumcharacter) {
        this.maximumcharacter = maximumcharacter;
    }

    public String getPreviousPreiyek() {
        return previousPreiyek;
    }

    public void setPreviousPreiyek(String previousPreiyek) {
        this.previousPreiyek = previousPreiyek;
    }

    public String getWhatpaste() {
        return whatpaste;
    }

    public void setWhatpaste(String whatpaste) {
        this.whatpaste = whatpaste;
    }

    public boolean isIsphonicEnable() {
        return isphonicEnable;
    }

    public void setIsphonicEnable(boolean isphonicEnable) {
        this.isphonicEnable = isphonicEnable;
    }

    public String getPhonicSeperator() {
        return phonicSeperator;
    }

    public void setPhonicSeperator(String phonicSeperator) {
        this.phonicSeperator = phonicSeperator;
    }
}
