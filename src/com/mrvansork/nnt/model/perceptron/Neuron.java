package com.mrvansork.nnt.model.perceptron;

import java.io.Serializable;
import java.util.Random;

public class Neuron implements Serializable {

    public double[] weights;
    public double bias;

    public double lastActivation;

    public Neuron(int inputsCount, Random r){
        bias = 10 * r.nextDouble() - 5;
        weights = new double[inputsCount];

        for(int i = 0; i < inputsCount; i++)
            weights[i] = 10 * r.nextDouble() - 5;
    }

    public double activacion(double[] inputs){
        double activation = bias;

        for(int i = 0; i < inputs.length; i++){
            activation += inputs[i] * weights[i];
        }
        lastActivation = activation;
        return sigmoid(activation);
    }

    public static double sigmoid(double input){
        return 1 / (1 + Math.exp(-input));
    }

    public static double sigmoidDerivated(double input){
        double y = sigmoid(input);
        return y * (1 - y);
    }

}
