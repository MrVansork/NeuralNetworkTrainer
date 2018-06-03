package com.mrvansork.nnt.model.perceptron;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Layer implements Serializable {

    public List<Neuron> neurons;
    public double[] outputs;

    public Layer(int inputCounts, int neuronCounts, Random r){
        neurons = new ArrayList<>();

        for(int i = 0; i < neuronCounts; i++){
            neurons.add(new Neuron(inputCounts, r));
        }
    }

    public double[] activation(double[] inputs){
        outputs = new double[neurons.size()];

        for(int i = 0; i < neurons.size(); i++){
            outputs[i] = neurons.get(i).activacion(inputs);
        }

        return outputs;
    }
}
