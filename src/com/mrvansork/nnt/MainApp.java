package com.mrvansork.nnt;

import com.mrvansork.nnt.model.Constants;
import com.mrvansork.nnt.model.Utilities;
import com.mrvansork.nnt.model.perceptron.Settings;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.opencv.core.Core;

import javax.rmi.CORBA.Util;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;

public class MainApp extends Application{

    private Stage primaryStage;
    private static HashMap<String, Parent> scenes = new HashMap<>();
    private static MainApp instance;

    @Override
    public void start(Stage primaryStage) throws Exception {
        instance = this;
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Neural Network Trainer");
        this.primaryStage.setResizable(false);
        try {
            this.primaryStage.getIcons().add(new Image(getClass().getResource("../../../res/img/icon.png").openStream()));
        } catch (IOException e) {
            System.err.println("\n----");
            e.printStackTrace();
            System.err.println("----\n");
        }

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                closeCheck();
            }
        });

        initOverviews();
    }

    private void initOverviews(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/MenuOverview.fxml"));
            scenes.put("main menu", loader.load());

            loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/TrainMenuOverview.fxml"));
            scenes.put("train menu", loader.load());

            loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/TrainOverview.fxml"));
            scenes.put("train", loader.load());

            loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/OpenMenuOverview.fxml"));
            scenes.put("open menu", loader.load());

            primaryStage.setScene(new Scene(scenes.get("main menu")));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void changeScene(String key){
        instance.primaryStage.getScene().setRoot(scenes.get(key));
    }

    static{System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}

    public static void closeCheck(){
        Utilities.saveObject(Constants.APPDATA+"\\settings", Settings.get());
    }

    public static void initialCheck() throws IOException {
        File profiles = new File(Constants.APPDATA+"\\profiles");
        if(!profiles.exists()){
            profiles.mkdirs();
            Files.setAttribute(profiles.getParentFile().toPath(), "dos:hidden", true);
        }

        File settings = new File(profiles.getParent()+"/settings");
        if(!settings.exists()){
            settings.createNewFile();
        }else{
            Settings.setSingleton((Settings) Utilities.loadObject(settings.getPath()));
        }

    }

    public static void main(String[] args) {
        try {
            initialCheck();
        } catch (IOException e) {
            e.printStackTrace();
        }
        launch(args);
    }

}
