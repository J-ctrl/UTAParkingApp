package com.example.myapplication2.ui.login;

//The Model class is used to obtain the information for each parking lot
class Model {
    private final String title;
    private final String desc;
    private final int icon;

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
