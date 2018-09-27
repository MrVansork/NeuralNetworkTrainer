package com.mrvansork.nnt.view;

import com.mrvansork.nnt.MainApp;
import com.mrvansork.nnt.model.Utilities;
import com.mrvansork.nnt.model.perceptron.Perceptron;
import com.mrvansork.nnt.model.Settings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class OpenMenuOverviewController implements Initializable {

    @FXML
    private Button main_menu, check;

    @FXML
    private VBox inputs, outputs;

    private Perceptron perceptron;
    private double[] d_inputs;
    private TextField[] fields;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        main_menu.setOnAction(event -> MainApp.changeScene("main menu"));
        check.setDisable(true);
    }

    @FXML
    private void onExplore(){
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Data file");
        File defaultDirectory = new File(Settings.get().LAST_OPEN_PROFILE_PATH);
        chooser.setInitialDirectory(defaultDirectory);
        File file = chooser.showOpenDialog(null);
        if(file != null){
            Settings.get().LAST_OPEN_PROFILE_PATH = file.getParent();
            perceptron = Utilities.loadPerceptron(file.getPath());
            check.setDisable(false);
            d_inputs = new double[perceptron.getNumOfInputs()];
            inputs.getChildren().removeAll(inputs.getChildren());
            inputs.setPrefHeight(48*d_inputs.length);
            fields = new TextField[d_inputs.length];
            for(int i = 0; i < d_inputs.length; i++){
                inputs.getChildren().add(getInputPane(i));
            }
        }
    }

    private Pane getInputPane(int index){
        Pane pane = new Pane();
        pane.setPrefSize(183, 48);
        pane.setStyle("-fx-border-color: #00BBFF");
        fields[index] = new TextField();
        fields[index].setLayoutX(5);
        fields[index].setLayoutY(10);
        fields[index].setPrefWidth(64);
        pane.getChildren().add(fields[index]);
        return pane;
    }

    private Pane getOutputPane(String value){
        Pane pane = new Pane();
        pane.setPrefSize(183, 32);
        pane.setStyle("-fx-border-color: #00BBFF");
        Label label = new Label(value);
        label.setLayoutX(5);
        label.setLayoutY(10);
        label.setPrefWidth(160);
        pane.getChildren().add(label);

        return pane;
    }

    @FXML
    private void onCheck(){
        try{
            for(int i = 0; i < perceptron.getNumOfInputs(); i++){
                d_inputs[i] = Utilities.normalize(Double.parseDouble(fields[i].getText()), perceptron.getMinInput(), perceptron.getMaxInput());
            }

            double[] d_outputs = perceptron.activation(d_inputs);

            outputs.getChildren().removeAll(outputs.getChildren());
            outputs.setPrefHeight(32*d_outputs.length);
            for (int i = 0; i < d_outputs.length; i++){
                outputs.getChildren().add(getOutputPane(Utilities.revertNormalize(d_outputs[i], perceptron.getMinOutput(), perceptron.getMaxOutput())+""));
            }
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
    }

}
