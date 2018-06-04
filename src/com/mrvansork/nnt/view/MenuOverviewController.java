package com.mrvansork.nnt.view;

import com.mrvansork.nnt.MainApp;
import com.mrvansork.nnt.model.NeuralNetData;

import static com.mrvansork.nnt.model.Constants.*;
import static com.mrvansork.nnt.model.Utilities.loadObject;

import com.mrvansork.nnt.model.perceptron.Settings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;


public class MenuOverviewController implements Initializable {

    private ObservableList<NeuralNetData> nnds = FXCollections.observableArrayList();

    @FXML
    private VBox history;

    @FXML
    private Pane import_pane;

    @FXML
    private Label import_path;

    @FXML
    private CheckBox create_profile;

    @FXML
    private TextField name_import;

    @FXML
    private TextArea desc_import;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        File profiles = new File(APPDATA+"\\profiles");
        for(String s:profiles.list()){
            nnds.add((NeuralNetData) loadObject(profiles.getPath()+s));
        }

        history.getChildren().removeAll(history.getChildren());
        history.setPrefHeight(nnds.size()*96);
        for(NeuralNetData nnd:nnds){
            history.getChildren().add(nnd.getPane());
        }
    }



    @FXML
    private void onTrain(){
        MainApp.changeScene("train menu");
    }

    @FXML
    private void onOpen(){
        MainApp.changeScene("open menu");
    }

    @FXML
    private void onImport(){
        import_pane.setDisable(false);
        import_pane.setVisible(true);
    }

    @FXML
    private void onDoImport(){

    }

    @FXML
    private void onCancelImport(){
        import_path.setText("");
        desc_import.setText("");
        name_import.setText("");
        create_profile.setSelected(true);

        import_pane.setDisable(true);
        import_pane.setVisible(false);
    }

    @FXML
    private void onExplore(){
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Perceptron file");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Perceptron files (*.prc)", "*.prc"));
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All files", "*"));
        File defaultDirectory = new File(Settings.get().LAST_IMPORT_PATH);
        chooser.setInitialDirectory(defaultDirectory);
        File file = chooser.showOpenDialog(null);
        if(file != null){
            import_path.setText(file.getName());
            DATA_TRAIN_PATH = file.getPath();
            Settings.get().LAST_IMPORT_PATH = file.getParent();
        }
    }

}
