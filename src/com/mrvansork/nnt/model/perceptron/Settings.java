package com.mrvansork.nnt.model.perceptron;

import java.io.Serializable;

public class Settings implements Serializable{

    private static Settings singleton = new Settings();
    public static void setSingleton(Settings s) { singleton = s; }
    public static Settings get() { return singleton; }

    public String LAST_IMPORT_PATH = "C:\\";
    public String LAST_OPEN_DATA_PATH = "C:\\";
    public String LAST_OPEN_PROFILE_PATH = "C:\\";
    public String LAST_SAVE_PATH = "C:\\";

}
