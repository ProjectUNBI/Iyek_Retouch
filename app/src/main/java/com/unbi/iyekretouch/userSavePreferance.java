package com.unbi.iyekretouch;

public class userSavePreferance {

    private int usershakelevel;
    private boolean is_iyekOn;
    private boolean is_customwordOn;
    private boolean is_IyekenglispaasteOn;
    private String userEngSeperator="\n@@@@@@@@@@\n";


    /*
    Getter and Setter
     */
    public int getUsershakelevel() {
        return usershakelevel;
    }

    public void setUsershakelevel(int usershakelevel) {
        this.usershakelevel = usershakelevel;
    }

    public boolean isIs_iyekOn() {
        return is_iyekOn;
    }

    public void setIs_iyekOn(boolean is_iyekOn) {
        this.is_iyekOn = is_iyekOn;
    }

    public boolean isIs_customwordOn() {
        return is_customwordOn;
    }

    public void setIs_customwordOn(boolean is_customwordOn) {
        this.is_customwordOn = is_customwordOn;
    }

    public boolean isIs_IyekenglispaasteOn() {
        return is_IyekenglispaasteOn;
    }

    public void setIs_IyekenglispaasteOn(boolean is_IyekenglispaasteOn) {
        this.is_IyekenglispaasteOn = is_IyekenglispaasteOn;
    }

    public String getUserEngSeperator() {
        return userEngSeperator;
    }

    public void setUserEngSeperator(String userEngSeperator) {
        this.userEngSeperator = userEngSeperator;
    }


}
