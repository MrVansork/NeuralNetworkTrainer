package com.mrvansork.nnt.view;

import com.mrvansork.nnt.MainApp;
import com.mrvansork.nnt.model.Utilities;
import com.mrvansork.nnt.model.Settings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import static com.mrvansork.nnt.model.NeuralNetData.*;

public class TrainMenuOverviewController implements Initializable {

    @FXML
    private Button main_menu;
    @FXML
    private Button next;

    @FXML
    private Label file_name;

    @FXML
    private TextArea preview;

    @FXML
    private TextField profile_name;

    @FXML
    private TextArea profile_description;

    @FXML
    private TextField inputCount;
    @FXML
    private TextField outputCount;
    @FXML
    private TextField inputMin;
    @FXML
    private TextField inputMax;
    @FXML
    private TextField outputMin;
    @FXML
    private TextField outputMax;

    private String file_path;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        main_menu.setOnAction(event -> MainApp.changeScene("main menu"));

        inputCount.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[0-9]*")) {
                inputCount.setText(newValue.replaceAll("[^0-9]*", ""));
            }
        });

        outputCount.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[0-9]*")) {
                outputCount.setText(newValue.replaceAll("[^0-9]*", ""));
            }
        });

        inputMin.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[0-9.]*")) {
                inputMin.setText(newValue.replaceAll("[^0-9.]*", ""));
            }
        });

        inputMax.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[0-9.]*")) {
                inputMax.setText(newValue.replaceAll("[^0-9.]*", ""));
            }
        });

        outputMin.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[0-9.]*")) {
                outputMin.setText(newValue.replaceAll("[^0-9.]*", ""));
            }
        });

        outputMax.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[0-9.]*")) {
                outputMax.setText(newValue.replaceAll("[^0-9.]*", ""));
            }
        });
    }

    @FXML
    private void onNext(){
        INPUTS = Integer.parseInt(inputCount.getText());
        OUTPUTS = Integer.parseInt(outputCount.getText());
        MIN_INPUT = Double.parseDouble(inputMin.getText());
        MAX_INPUT = Double.parseDouble(inputMax.getText());
        MIN_OUTPUT = Double.parseDouble(outputMin.getText());
        MAX_OUTPUT = Double.parseDouble(outputMax.getText());
        NAME_NND = profile_name.getText();
        DESCRIPTION_NND = profile_description.getText();
        DATA_TRAIN_PATH = file_path;


        MainApp.changeScene("train");
    }

    @FXML
    private void onExplore(){
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Data file");
        File defaultDirectory = new File(Settings.get().LAST_OPEN_DATA_PATH);
        chooser.setInitialDirectory(defaultDirectory);
        File file = chooser.showOpenDialog(null);
        if(file != null){
            Settings.get().LAST_OPEN_DATA_PATH = file.getParent();
            file_path = file.getPath();
            file_name.setText(file.getName());
            preview.setText(Utilities.readFileToString(file));
        }
    }

}
