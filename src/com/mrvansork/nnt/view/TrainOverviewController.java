package com.mrvansork.nnt.view;

import com.mrvansork.nnt.MainApp;
import com.mrvansork.nnt.model.Constants;
import com.mrvansork.nnt.model.NeuralNetData;
import com.mrvansork.nnt.model.Utilities;
import com.mrvansork.nnt.model.perceptron.Perceptron;
import com.mrvansork.nnt.model.Settings;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import static com.mrvansork.nnt.model.NeuralNetData.*;
import static com.mrvansork.nnt.model.Utilities.getInputOutput;

public class TrainOverviewController implements Initializable{

    @FXML
    private Button back, save, start, export, cancel;

    @FXML
    private Spinner<Integer> num_layers;

    @FXML
    private AnchorPane main;

    @FXML
    private VBox layers;

    @FXML
    private TextArea general_log;

    @FXML
    private TextField alpha, error, iterations;
    private TextField[] neurons;

    private boolean cancel_learn;

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
        export.setDisable(true);

        cancel_learn = false;

        setNeuronsByLayer(1);

        num_layers = new Spinner<>();
        num_layers.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 1));
        num_layers.setLayoutX(347);
        num_layers.setLayoutY(228);
        num_layers.setPrefWidth(64);
        main.getChildren().add(num_layers);

        num_layers.valueProperty().addListener((observable, oldValue, newValue) -> {
            setNeuronsByLayer(num_layers.getValue());
        });

    }

    private void setNeuronsByLayer(int num){
        layers.getChildren().removeAll(layers.getChildren());
        neurons = new TextField[num];
        for(int i = 0; i < num; i++){
            layers.getChildren().add(getPane(i));
        }
    }

    private Pane getPane(int index){
        Pane pane = new Pane();
        pane.setPrefSize(138, 32);
        pane.setStyle("-fx-border-color: #00b4ff");

        neurons[index] = new TextField("3");
        neurons[index].setPrefWidth(64);
        neurons[index].setLayoutY(4);
        neurons[index].setLayoutX(64);
        pane.getChildren().add(neurons[index]);

        return pane;
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
        int neurons[] = new int[num_layers.getValue()+2];
        for(int i = 0; i < num_layers.getValue(); i++)
            neurons[i+1] = Integer.parseInt(this.neurons[i].getText());
        neurons[0] = inputs.get(0).length;
        neurons[neurons.length-1] = outputs.get(0).length;

        perceptron = new Perceptron(neurons);
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
        cancel.setDisable(false);
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
            while(repeat && !cancel_learn){
                double err = 9999999;
                while(err > d_error && good_end && !cancel_learn){
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
                    perceptron = new Perceptron(neurons);
                    good_end = true;
                }else if(!cancel_learn){
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
                        export.setDisable(false);
                        cancel.setDisable(true);

                    });
                    repeat = false;
                }else{
                    javafx.application.Platform.runLater( () -> {
                        start.setDisable(false);
                        start.setText("Iniciar");
                        back.setDisable(false);
                        save.setDisable(false);
                        error.setEditable(false);
                        alpha.setEditable(false);
                        iterations.setEditable(false);
                        export.setDisable(false);
                        cancel.setDisable(true);

                    });
                    repeat = false;
                }
            }
            cancel_learn = false;
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
    private void onCancel(){
        cancel_learn = true;
    }

    @FXML
    private void onExport(){
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save perceptron");
        File defaultDirectory = new File(Settings.get().LAST_SAVE_PATH);
        chooser.setInitialDirectory(defaultDirectory);
        chooser.setInitialFileName("perceptron.prc");
        File file = chooser.showSaveDialog(null);
        if(file != null){
            Utilities.saveObject(file.getPath(), perceptron);
            Settings.get().LAST_SAVE_PATH = file.getParent();
        }
    }

    @FXML
    private void onSave(){
        PERCEPTRON_PATH = Constants.APPDATA+"\\profiles\\"+NAME_NND.replaceAll(" ", "_")+".prc";
        NeuralNetData nnd = new NeuralNetData(NAME_NND, DESCRIPTION_NND, PERCEPTRON_PATH, new Date());
        NNDS.add(nnd);
        serializeNnds();
        Utilities.saveObject(PERCEPTRON_PATH, perceptron);
    }

}
