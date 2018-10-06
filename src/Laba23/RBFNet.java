package Laba23;

import Prototypes.Matrix;
import Prototypes.MultiLayerNet;

import java.io.File;
import java.io.IOException;

/**
 * Created by ptaxom on 04.10.2018.
 */
public class RBFNet implements MultiLayerNet
{

    private Matrix[] inputShapes;
    private Matrix widths;

    private Matrix weights;

    private int countOfHiddenNeurons, countOfOutputNeurons;

    private String path;

    private Matrix lastResult;

    private double alfa;

    public RBFNet(String path) {
        this.path = path;
    }

    public RBFNet(Matrix[] inputShapes,  int countOfOutputNeurons, String path, double alfa) {
        this.inputShapes = inputShapes;
        this.countOfHiddenNeurons = inputShapes.length;
        this.countOfOutputNeurons = countOfOutputNeurons;
        this.path = path;
        this.widths = new Matrix(1, countOfHiddenNeurons);
        this.weights = new Matrix(countOfHiddenNeurons, countOfOutputNeurons);
        this.alfa = alfa;
    }


    @Override
    public void init() throws IOException {
        for(int i = 0; i < inputShapes.length; i++)
            inputShapes[i].saveToFile(new File(path+"\\i"+i+".txt"));
        Matrix cfg = new Matrix(new double[][]{{inputShapes.length, alfa}});

        cfg.saveToFile(new File(path+"\\cfg.txt"));
        this.weights.randomInitialize();
        weights.saveToFile(new File(path+"\\weights.txt"));
        calculateWidthsOfCells();
        widths.saveToFile(new File(path+"\\widths.txt"));
    }

    private void calculateWidthsOfCells(){
        for(int i = 0; i < inputShapes.length; i++){
            double minDistance = 9999999999999999d;
            for(int j = 0; j < inputShapes.length; j++)
                if (i != j && distance(inputShapes[i], inputShapes[j]) < minDistance)
                    minDistance = distance(inputShapes[i], inputShapes[j]);
            widths.setElement(0,i,  minDistance / 2);
        }
    }

    private double distance(Matrix from, Matrix to){
        double distance = 0;
        for(int i = 0; i < from.getM(); i++)
            distance += (to.getMatrix()[0][i] - from.getMatrix()[0][i]) * (to.getMatrix()[0][i] - from.getMatrix()[0][i]);
        return Math.sqrt(distance);
    }



    @Override
    public void save() throws IOException {
        weights.saveToFile(new File(path+"\\weights.txt"));
    }

    private Matrix valuesOfHiddenLayerFromLastIteration = null;

    @Override
    public Matrix getNetResult(Matrix input) {
        Matrix buf = new Matrix(input);
        if (buf.getN() != 1)
            buf.toVector();
        Matrix values = new Matrix(1,countOfHiddenNeurons);
        for(int i = 0; i < countOfHiddenNeurons; i++)
            values.setElement(0,i, Math.exp( - distance(buf, inputShapes[i]) / widths.getMatrix()[0][i] / widths.getMatrix()[0][i]));
        valuesOfHiddenLayerFromLastIteration = new Matrix(values);
        values.multiply(weights);
        return values;
    }

    @Override
    public void trainNet(Matrix input, Matrix output) {
        Matrix netResult = getNetResult(input);
        netResult.multiply(-1);
        netResult.add(output); //Calculating fault of output Layer

        Matrix deltaWeight = new Matrix(valuesOfHiddenLayerFromLastIteration);
        deltaWeight.transponate();
        deltaWeight.multiply(netResult);
        deltaWeight.multiply(alfa);

        weights.add(deltaWeight);

    }

    @Override
    public void load() throws IOException {
        Matrix cfg = new Matrix(new File(path+"\\cfg.txt"));

        this.countOfHiddenNeurons = (int)cfg.getMatrix()[0][0];
        this.alfa = cfg.getMatrix()[0][1];

        inputShapes = new Matrix[countOfHiddenNeurons];
        for(int i = 0; i < countOfHiddenNeurons; i++)
            inputShapes[i] = new Matrix(new File(path+"\\i"+i+".txt"));

        weights = new Matrix(new File(path+"\\weights.txt"));

        countOfOutputNeurons = weights.getM();
        widths = new Matrix(new File(path+"\\widths.txt"));
    }

    @Override
    public double getRMS(Matrix input, Matrix output){ //Среднеквадратичная ошибка
        Matrix result = this.getNetResult(input);
        double ans = 0;
        for(int i = 0; i < output.getM(); i++)
            ans += (result.getMatrix()[0][i] - output.getMatrix()[0][i]) * (result.getMatrix()[0][i] - output.getMatrix()[0][i]);
        return ans/2;
    }
}
