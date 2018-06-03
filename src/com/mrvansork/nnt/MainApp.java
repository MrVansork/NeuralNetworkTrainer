package com.mrvansork.nnt;

import com.mrvansork.nnt.model.Constants;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.opencv.core.Core;

import java.io.File;
import java.io.IOException;
import java.net.*;
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

            loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/TestImageOverview.fxml"));
            scenes.put("test", loader.load());

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

    public static void getHTML() throws Exception {
        URL url = new URL("https://opendata.aemet.es/opendata/api/observacion/convencional/datos/estacion/8416Y/?api_key=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZHJpYW4uYmxlc2EubW9yZW5vQGdtYWlsLmNvbSIsImp0aSI6IjRmMTc1Y2JlLTc3ODUtNGZmYy1iYzM3LTNiYjVhZjMwNmJlZSIsImlzcyI6IkFFTUVUIiwiaWF0IjoxNTI3ODQ1MTYwLCJ1c2VySWQiOiI0ZjE3NWNiZS03Nzg1LTRmZmMtYmMzNy0zYmI1YWYzMDZiZWUiLCJyb2xlIjoiIn0.gxAH-06d7YWojJu7Foe-_jVYukf6R8j2ec2VcG1N_gE");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        System.out.println(conn.getResponseMessage());

    }

    public static void initialCheck() throws IOException {
        File profiles = new File(Constants.APPDATA);
        if(!profiles.exists()){
            profiles.mkdirs();
            Files.setAttribute(profiles.getParentFile().toPath(), "dos:hidden", true);
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
