package com.mrvansork.nnt.model.perceptron;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Perceptron implements Serializable{

    public List<Layer> layers;
    private int inputs;
    private double max_input, min_input, max_output, min_output;

    public Perceptron(int[] numOfNeuronsPerLayer){
        layers = new ArrayList<>();
        Random r = new Random();
        setNumOfInputs(numOfNeuronsPerLayer[0]);
        for(int i = 0; i < numOfNeuronsPerLayer.length; i++){
            layers.add(new Layer(i == 0 ? numOfNeuronsPerLayer[i] : numOfNeuronsPerLayer[i - 1], numOfNeuronsPerLayer[i], r));
        }
    }

    public double[] activation(double[] inputs){
        double[] outputs = new double[0];

        for(int i = 1; i < layers.size(); i++){
            outputs = layers.get(i).activation(inputs);
            inputs = outputs;
        }
        return outputs;
    }

    public double individualError(double[] realOutput, double[] desiredOutput){
        double err = 0;
        for(int i = 0; i < realOutput.length; i++){
            err += Math.pow((realOutput[i] - desiredOutput[i]), 2);
        }
        return err;
    }

    public double generalError(List<double[]> inputs, List<double[]> desiredOuputs){
        double err = 0;
        for(int i = 0; i < inputs.size(); i++){
            err += individualError(activation(inputs.get(i)), desiredOuputs.get(i));
        }
        return err;
    }

    public boolean learn(List<double[]> input, List<double[]> desiredOutput, double alpha, double max_err, int maxIterations){
        double err = 9999999;

        boolean good_end = true;
        while(err > max_err && good_end){
            if(--maxIterations <= 0){
                good_end = false;
            }
            applyBackPropagation(input, desiredOutput, alpha);
            err = generalError(input, desiredOutput);
        }
        return good_end;
    }

    private List<double[]> sigmas;
    private List<double[][]> deltas;

    private void setDeltas(){
        deltas = new ArrayList<>();
        for(int i = 0; i < layers.size(); i++){
            deltas.add(new double[layers.get(i).neurons.size()][layers.get(i).neurons.get(0).weights.length]);
        }
    }

    private void setSigmas(double[] desiredOutput){
        sigmas = new ArrayList<>();

        for(int i = 0; i < layers.size(); i++)
            sigmas.add(new double[layers.get(i).neurons.size()]);

        for(int i = layers.size() - 1; i >= 0; i--){
            for(int j = 0; j < layers.get(i).neurons.size(); j++){
                if(i == layers.size() - 1){
                    double y = layers.get(i).neurons.get(j).lastActivation;
                    sigmas.get(i)[j] = (Neuron.sigmoid(y) - desiredOutput[j]) * Neuron.sigmoidDerivated(y);
                } else {
                    double sum = 0;
                    for(int k = 0; k < layers.get(i+1).neurons.size(); k++){
                        sum += layers.get(i + 1).neurons.get(k).weights[j] * sigmas.get(i + 1)[k];
                    }
                    sigmas.get(i)[j] = Neuron.sigmoidDerivated(layers.get(i).neurons.get(j).lastActivation) * sum;
                }
            }
        }
    }

    public void addDelta(){
        for(int i = 1; i < layers.size(); i++){
            for(int j = 0; j < layers.get(i).neurons.size(); j++){
                for(int k = 0; k < layers.get(i).neurons.get(j).weights.length; k++){
                    deltas.get(i)[j][k] +=sigmas.get(i)[j] * Neuron.sigmoid(layers.get(i - 1).neurons.get(k).lastActivation);
                }
            }
        }
    }

    public void applyBackPropagation(List<double[]> inputs, List<double[]> desiredOutputs, double alpha){
        setDeltas();
        for(int i = 0; i < inputs.size(); i++){
            activation(inputs.get(i));
            setSigmas(desiredOutputs.get(i));
            updateBiases(alpha);
            addDelta();
        }
        updateWeights(alpha);
    }

    private void updateWeights(double alpha){
        for(int i = 0; i < layers.size(); i++)
            for(int j = 0; j < layers.get(i).neurons.size(); j++)
                for(int k = 0; k < layers.get(i).neurons.get(j).weights.length; k++)
                    layers.get(i).neurons.get(j).weights[k] -= alpha * deltas.get(i)[j][k];
    }

    private void updateBiases(double alpha){
        for(int i = 0; i < layers.size(); i++)
            for(int j = 0; j < layers.get(i).neurons.size(); j++)
                layers.get(i).neurons.get(j).bias -= alpha * sigmas.get(i)[j];
    }

    public int getNumOfInputs() {
        return inputs;
    }

    public void setMaxInput(double max_input) {
        this.max_input = max_input;
    }

    public void setMinInput(double min_input) {
        this.min_input = min_input;
    }

    public void setMaxOutput(double max_output) {
        this.max_output = max_output;
    }

    public void setMinOutput(double min_output) {
        this.min_output = min_output;
    }

    public double getMaxInput() {
        return max_input;
    }

    public double getMinInput() {
        return min_input;
    }

    public double getMaxOutput() {
        return max_output;
    }

    public double getMinOutput() {
        return min_output;
    }

    public void setNumOfInputs(int inputs) {
        this.inputs = inputs;
    }
}
