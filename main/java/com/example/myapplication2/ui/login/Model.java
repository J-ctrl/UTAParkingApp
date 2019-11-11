package com.example.myapplication2.ui.login;

//The Model class is used to obtain the information for each parking lot
public class Model {
    private String title;
    private String desc;
    private int icon;


    // class constructor method
    Model(String title, String desc, int icon) {
        this.title = title;
        this.desc = desc;
        this.icon = icon;
    }


    //getters methods
    String getTitle() {
        return this.title;
    }

    String getDesc() {
        return this.desc;
    }

    int getIcon() {
        return this.icon;
    }


}
