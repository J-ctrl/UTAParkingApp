package com.example.mapselection;

//The Model class is used to obtain the information for each parking lot
public class Model {
    String title;
    String desc;
    int icon;

    // class constructor method
    public Model(String title, String desc, int icon) {
        this.title = title;
        this.desc = desc;
        this.icon = icon;
    }

    //getters methods
    public String getTitle() {
        return this.title;
    }

    public String getDesc() {
        return this.desc;
    }

    public int getIcon() {
        return this.icon;
    }


}
