package com.acelerazg;

import com.acelerazg.view.Menu;

public class Main {
    public static void main(String[] args) {
        App app = new App();
        app.loadDataFromXml();
        Menu menu = new Menu(app);
        menu.run();
    }
}