package com.mrvansork.nnt.view;

import com.mrvansork.nnt.MainApp;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class TestImageOverviewController implements Initializable{

    @FXML
    private ImageView imageView;

    private Mat img;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        img = Imgcodecs.imread(new File("data/analizar/gatos/gato_8.jpg").getPath(), Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
        Imgproc.GaussianBlur(img, img, new Size(15, 15), 0);
        //Imgproc.adaptiveThreshold(img, img, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY_INV, 11, 2);
        Imgproc.Canny(img, img, 11, 11*3, 3, false);


        imageView.setImage(mat2Image(img, ".jpg"));
    }

    @FXML
    private void onBack(){
        MainApp.changeScene("main menu");
    }

    public static Image mat2Image(Mat frame, String ext) {
        MatOfByte buffer = new MatOfByte();
        Imgcodecs.imencode(ext, frame, buffer);
        return new Image(new ByteArrayInputStream(buffer.toArray()));
    }}
