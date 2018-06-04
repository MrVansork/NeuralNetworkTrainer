package com.mrvansork.nnt.view;

import com.mrvansork.nnt.MainApp;
import com.mrvansork.nnt.model.Utilities;
import com.mrvansork.nnt.model.perceptron.Perceptron;
import com.mrvansork.nnt.model.perceptron.Settings;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.mrvansork.nnt.model.Constants.*;
import static com.mrvansork.nnt.model.Utilities.getInputOutput;

public class TrainOverviewController implements Initializable{

    @FXML
    private Button back, save, start;

    @FXML
    private TextArea general_log;

    @FXML
    private TextField alpha, error, iterations;

    @FXML
    private Label RTError, DError;

    @FXML
    private ProgressIndicator indicator;

    private boolean first = true;
    private Perceptron perceptron;
    private List<double[]> inputs = new ArrayList<>();
    private List<double[]> outputs = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        back.setOnAction(event -> MainApp.changeScene("train menu"));
        save.setDisable(true);

    }

    private void loadData(String path){
        List<String> rowData = getInputOutput(path);

        for (String aRowData : rowData) {
            double[] d_inputs = new double[INPUTS];
            double[] d_outputs = new double[OUTPUTS];

            String[] row = aRowData.split(";");

            for (int j = 0; j < row.length; j++) {
                if (j < INPUTS) {
                    d_inputs[j] = Utilities.normalize(Double.parseDouble(row[j]), MIN_INPUT, MAX_INPUT);
                } else {
                    d_outputs[j - INPUTS] = Utilities.normalize(Double.parseDouble(row[j]), MIN_OUTPUT, MAX_OUTPUT);
                }
            }
            inputs.add(d_inputs);
            outputs.add(d_outputs);
        }
    }

    @FXML
    private void onStart(){
        if(first){
            first = false;
        }

        loadData(DATA_TRAIN_PATH);
        perceptron = new Perceptron(new int[]{inputs.get(0).length, 8, 8, outputs.get(0).length});
        perceptron.setMinInput(MIN_INPUT);
        perceptron.setMaxInput(MAX_INPUT);
        perceptron.setMinOutput(MIN_OUTPUT);
        perceptron.setMaxOutput(MAX_OUTPUT);

        start.setText("...");
        start.setDisable(true);
        back.setDisable(true);
        error.setEditable(true);
        alpha.setEditable(true);
        iterations.setEditable(true);
        DError.setText(error.getText());

        double d_alpha = Double.parseDouble(alpha.getText());
        double d_error = Double.parseDouble(error.getText());
        int d_itt = Integer.parseInt(iterations.getText());


        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
            boolean good_end = true, repeat = true;
            DecimalFormat df = new DecimalFormat("0.00000");
            int max_itt = d_itt;
            long error_refresh = System.currentTimeMillis();
            long indicator_refresh = System.currentTimeMillis();
            while(repeat){
                double err = 9999999;
                while(err > d_error && good_end){
                    if(--max_itt <= 0){
                        good_end = false;
                    }

                    perceptron.applyBackPropagation(inputs, outputs, d_alpha);
                    err = perceptron.generalError(inputs, outputs);

                    updateProgress((System.currentTimeMillis() - indicator_refresh)/(1000.0 * 3.0), 1.0);
                    if(System.currentTimeMillis() - indicator_refresh >= 3000){
                        indicator_refresh = System.currentTimeMillis();
                    }

                    if(System.currentTimeMillis() - error_refresh >= 500){
                        updateMessage(df.format(err));
                        error_refresh = System.currentTimeMillis();
                    }
                }

                if(!good_end){
                    perceptron = new Perceptron(new int[]{inputs.get(0).length, 8, 8, outputs.get(0).length});
                    good_end = true;
                }else{
                    updateMessage(df.format(err));
                    javafx.application.Platform.runLater( () -> {
                        general_log.appendText("APRENDIZAJE CONCLUIDO\n");
                        start.setDisable(false);
                        start.setText("Iniciar");
                        back.setDisable(false);
                        save.setDisable(false);
                        error.setEditable(false);
                        alpha.setEditable(false);
                        iterations.setEditable(false);
                    });
                    repeat = false;
                }
            }
            return null;
            }
        };


        RTError.textProperty().bind(task.messageProperty());
        indicator.progressProperty().bind(task.progressProperty());
        task.setOnSucceeded(e -> {
            RTError.textProperty().unbind();
            indicator.progressProperty().unbind();
            indicator.setProgress(1.0);
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }


    @FXML
    private void onSave(){
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save perceptron");
        File defaultDirectory = new File(Settings.get().LAST_SAVE_PATH);
        chooser.setInitialDirectory(defaultDirectory);
        chooser.setInitialFileName("newPerceptron.prc");
        File file = chooser.showSaveDialog(null);
        if(file != null){
            Utilities.saveObject(file.getPath(), perceptron);
            Settings.get().LAST_SAVE_PATH = file.getParent();
        }
    }


}
