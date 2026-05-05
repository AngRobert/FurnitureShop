package com.AngRobert.Zpotifai;

import com.AngRobert.Zpotifai.controller.MenuController;
import com.AngRobert.Zpotifai.util.DBConnection;
import com.AngRobert.Zpotifai.util.SchemaLoader;

public class App {
    public void run() {
        DBConnection.init();
        SchemaLoader.run();        // creates tables if they don't exist
        new MenuController().start();
    }
}
