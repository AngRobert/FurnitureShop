package com.AngRobert.ShopApplication;

import com.AngRobert.ShopApplication.controller.MenuController;
import com.AngRobert.ShopApplication.util.DBConnection;
import com.AngRobert.ShopApplication.util.SchemaLoader;

public class App {
    public void run() {
        DBConnection.init();
        SchemaLoader.run();        // creates tables if they don't exist
        new MenuController().start();
    }
}
