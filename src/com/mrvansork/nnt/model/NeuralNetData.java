package com.mrvansork.nnt.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

import java.io.Serializable;
import java.util.Date;

public class NeuralNetData implements Serializable{

    private final StringProperty name;
    private final StringProperty description;
    private final StringProperty file_path;
    private final ObjectProperty<Date> last_mod, creation_date;

    private Pane pane;

    public NeuralNetData(String name, String description, String file_path, Date last_mod) {
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
        this.file_path = new SimpleStringProperty(file_path);
        this.last_mod = new SimpleObjectProperty<>(last_mod);
        this.creation_date = new SimpleObjectProperty<>(new Date());

        createPane();
    }

    private void createPane(){
        pane = new Pane();
        pane.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #147ba1; -fx-border-width: 3;");
        pane.getStylesheets().add(getClass().getResource("../view/styles/pane.css").toExternalForm());
        pane.getStyleClass().add("history");
        /*pane.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton() == MouseButton.PRIMARY){
                    System.out.println("Load: "+getName());
                }
            }
        });*/
        pane.setPrefSize(284, 96);

        Label name_lbl = new Label(getName());
        name_lbl.setStyle("-fx-font-weight: bold");
        name_lbl.setMaxWidth(275);
        name_lbl.setLayoutX(5);
        name_lbl.setLayoutY(5);
        pane.getChildren().add(name_lbl);

        Label date_lbl = new Label(creation_date.get().toString());
        date_lbl.setMaxWidth(275);
        date_lbl.setFont(new Font(11));
        date_lbl.setLayoutX(5);
        date_lbl.setLayoutY(25);
        pane.getChildren().add(date_lbl);

        TextArea desc_txt = new TextArea(getDescription());
        desc_txt.setStyle("-fx-border-color: #147ba1; -fx-border-width: 1;");
        desc_txt.setPrefWidth(274);
        desc_txt.setPrefHeight(46);
        desc_txt.setLayoutX(5);
        desc_txt.setLayoutY(45);
        desc_txt.setEditable(true);
        pane.getChildren().add(desc_txt);

        Button delete_btn = new Button();
        delete_btn.getStylesheets().add(getClass().getResource("../view/styles/buttons.css").toExternalForm());
        delete_btn.getStyleClass().add("close");
        delete_btn.setStyle("-fx-background-image: url('res/img/close.png');\n" +
                "  -fx-background-repeat: no-repeat;\n" +
                "  -fx-background-position: center;");
        delete_btn.setPrefWidth(28);
        delete_btn.setLayoutX(250);
        delete_btn.setLayoutY(5);
        pane.getChildren().add(delete_btn);

        Button go_btn = new Button();
        go_btn.getStylesheets().add(getClass().getResource("../view/styles/buttons.css").toExternalForm());
        go_btn.getStyleClass().add("close");
        go_btn.setStyle("-fx-background-image: url('res/img/go.png');\n" +
                "  -fx-background-repeat: no-repeat;\n" +
                "  -fx-background-position: center;");
        go_btn.setPrefWidth(28);
        go_btn.setLayoutX(218);
        go_btn.setLayoutY(5);
        pane.getChildren().add(go_btn);

    }

    public Pane getPane() {
        return pane;
    }

    public Date getCreationDate() {
        return creation_date.get();
    }

    public ObjectProperty<Date> getCreationDateProperty() {
        return creation_date;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty getNameProperty() {
        return name;
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty getDescriptionProperty() {
        return description;
    }

    public String getFilePath() {
        return file_path.get();
    }

    public StringProperty getFilePathProperty() {
        return file_path;
    }

    public Date getLastMod() {
        return last_mod.get();
    }

    public ObjectProperty<Date> getLastModProperty() {
        return last_mod;
    }
}
