package com.mrvansork.nnt.model;

import com.mrvansork.nnt.model.perceptron.Perceptron;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Utilities {

    public static String readFileToString(File file){
        String text = "";
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while((line = br.readLine()) != null)
                text += line+"\n";

            text = text.substring(0, text.length()-2);
            fr.close();
            br.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

        return text;
    }

    public static List<String> getInputOutput(String path){
        List<String> doubles = new ArrayList<>();
        File file = new File(path);

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line;
            while((line = br.readLine()) != null){
                line = line.replace("\n", "");
                line = line.replace("\r", "");
                line = line.replace(",", ".");
                doubles.add(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return doubles;
    }

    public static void writeInFile(String path, String text){
        File file = new File(path);

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));

            bw.write(text);
            bw.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveObject(String path, Object o){
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));

            oos.writeObject(o);
            oos.flush();

            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Perceptron loadPerceptron(String path){
        return (Perceptron) loadObject(path);
    }

    public static Object loadObject(String path){
        Object o = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));

            o = ois.readObject();

            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return o;
    }

    public static double normalize(double value, double min, double max){
        return (value - min) / (max - min);
    }

    public static double revertNormalize(double value, double min, double max){
        return value * (max - min) + min;
    }

    public static double[] getImageData(String path){
        Mat img = Imgcodecs.imread(new File(path).getPath(), Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
        Imgproc.GaussianBlur(img, img, new Size(15, 15), 0);
        Imgproc.Canny(img, img, 11, 11*3, 3, false);

        double[] data = new double[img.rows()*img.cols()];
        int c = 0;
        for(int i = 0; i < img.cols(); i++){
            for(int j = 0; j < img.rows(); j++){
                data[c++] = normalize(img.get(j, i)[0], 0, 255);
            }
        }

        return data;
    }

}
