package Laba23;

import Prototypes.Function;
import Prototypes.Matrix;
import Prototypes.MultiLayerNet;

import java.io.File;
import java.io.IOException;

/**
 * Created by ptaxom on 03.10.2018.
 */
public class PerceptronNet implements MultiLayerNet {

    private String sourceFilesPath;

    private Matrix[] layerWeights = new Matrix[2]; //
    private Matrix[] deltaLayerWeight = new Matrix[2]; //
    private Matrix[] layerThreshold = new Matrix[2]; //

    private Matrix lastResult;

    private Function function = x -> 1 / (1 + Math.exp(-x));
    private Function derivative = x -> x*(1-x);

    private double alfa, epsilon, beta;

    private int n, m, k; //sizes of layers(input, hidden, output)


    public PerceptronNet(String sourceFilesPath, double alfa, double beta, double epsilon, int n, int m, int k) {
        this.sourceFilesPath = sourceFilesPath;
        this.alfa = alfa;
        this.epsilon = epsilon;
        this.n = n;
        this.m = m;
        this.k = k;
        this.beta = beta;
    }

    public PerceptronNet(String sourceFilesPath) {
        this.sourceFilesPath = sourceFilesPath;
    }




    @Override
    public void init() throws IOException {
        File netCfg = new File(sourceFilesPath + "\\cfg.txt");

        Matrix cfg = new Matrix(new double[][]{{alfa, beta, epsilon}});
        cfg.saveToFile(netCfg);

        for (int i = 1; i < 3; i++) {

            Matrix layerWeight = new Matrix(i == 1 ? n : m, i == 1 ? m : k);
            layerWeight.randomInitialize();
            this.layerWeights[i-1] = layerWeight;

            layerWeight.saveToFile(new File(sourceFilesPath + "\\" + i + ".txt"));

            Matrix deltaLayerWeight = new Matrix(i == 1 ? n : m, i == 1 ? m : k);
            this.deltaLayerWeight[i-1] = deltaLayerWeight;

            deltaLayerWeight.saveToFile(new File(sourceFilesPath + "\\d" + i + ".txt"));

            Matrix layerThresholds = new Matrix(1, i == 1 ? m : k);
            layerThresholds.randomInitialize();
            this.layerThreshold[i-1] = layerThresholds;

            layerThresholds.saveToFile(new File(sourceFilesPath + "\\t" + i + ".txt"));
        }
    }

    @Override
    public void save() throws IOException {
        for (int i = 1; i < 3; i++) {
            layerWeights[i - 1].saveToFile(new File(sourceFilesPath + "\\" + i + ".txt"));
            layerThreshold[i - 1].saveToFile(new File(sourceFilesPath + "\\t" + i + ".txt"));
            deltaLayerWeight[i - 1].saveToFile(new File(sourceFilesPath + "\\d" + i + ".txt"));
        }
    }

    @Override
    public void load() throws IOException {
        Matrix cfg = new Matrix(new File(sourceFilesPath + "\\cfg.txt"));
        alfa = cfg.getMatrix()[0][0];
        beta = cfg.getMatrix()[0][1];
        epsilon = cfg.getMatrix()[0][2];
        for (int i = 1; i < 3; i++) {
            layerWeights[i - 1] = new Matrix(new File(sourceFilesPath + "\\" + i + ".txt"));
            layerThreshold[i - 1] = new Matrix(new File(sourceFilesPath + "\\t" + i + ".txt"));
            deltaLayerWeight[i - 1] = new Matrix(new File(sourceFilesPath + "\\d" + i + ".txt"));
        }
    }

    @Override
    public Matrix getNetResult(Matrix input) {
        Matrix buf = new Matrix();
        buf.copy(input);
        for (int i = 0; i < 2; i++) {
            buf.multiply(layerWeights[i]);
            buf.add(layerThreshold[i]);
            buf.function(function);
        }
        lastResult = buf;
        return buf;
    }

    private Matrix[] getLayerValues(Matrix input){ //Получить значения на выходах нейронов соответствующих слоёв
        Matrix[] layerValue = new Matrix[3];
        layerValue[0] = new Matrix(input);
        Matrix buf = new Matrix();
        buf.copy(input);

        for (int i = 0; i < 2; i++) {
            buf.multiply(layerWeights[i]);
            buf.add(layerThreshold[i]);
            buf.function(function);
            layerValue[i+1] = new Matrix(buf);
        }
        return layerValue;
    }

    private Matrix calculateOutputLayerFault(Matrix realValues, Matrix idealValues){ //Вычислить ошибку выходного слоя нейросети
        Matrix fault = new Matrix(1,k);
        for(int i = 0; i < k; i++)
            fault.setElement(0,i,(idealValues.getMatrix()[0][i] - realValues.getMatrix()[0][i]) * //yR[i] - y[i] (2.6)
                                    derivative.function(realValues.getMatrix()[0][i]) //f(S[k])' = y*(1-y) (2.12)
            );
        return fault;
    }

    /**
     *@param layerWeights  - веса синапсов
     *@param oldDWeight    - старые(текущие) значения dW
     *@param fault         - ошибка следующего слоя нейросети
     *@param layerValue    - значения на выходе нейронов слоя, из которого выходят синапсы с весами layerWeight
     *@param magicConst    - константа задающая скорость обучения
     */

    private void changeLayersWeights(Matrix layerWeights, Matrix oldDWeight, Matrix fault, Matrix layerValue, double magicConst){
        Matrix newDelta = new Matrix(layerValue);  //g[i]
        newDelta.transponate();                    //Транспонируем для перехода к матричным операциям
        newDelta.multiply(fault); //С помощью матричного умножения получаем матрицу новых изменений весов синапсов
        newDelta.multiply(magicConst); //Все вместе обобщение (2.9), (2.3)

        oldDWeight.multiply(epsilon); //(2.19)

        layerWeights.add(oldDWeight); //Добавление eps * w(t-1) (2.19)
        layerWeights.add(newDelta);     //Добавление новой матрицы изменения весов

        oldDWeight.copy(newDelta);   //Сохранение новых dW в старую
    }


    private void changeThreshold(Matrix threshold, Matrix fault, double magicConst){
        Matrix bufferDelta = new Matrix(fault);
        bufferDelta.multiply(magicConst);
        threshold.add(bufferDelta); //(2.13), (2.15)

    }

    private Matrix calculateHiddenLayerFault(Matrix nextLayerFault, Matrix weights){
        Matrix W = new Matrix(weights);
        Matrix O = new Matrix(nextLayerFault);
        O.transponate();
        W.multiply(O);
        W.transponate();
        return W;
    }


    @Override
    public void trainNet(Matrix input, Matrix output) {

        Matrix[] lastLayersValues = getLayerValues(input);

        Matrix outputLayerFault = calculateOutputLayerFault(lastLayersValues[2], output); //Матрица ошибок нейронов выходного слоя

        changeLayersWeights(layerWeights[1], deltaLayerWeight[1],
                outputLayerFault, lastLayersValues[1], alfa); //Изменение весов синапсов между скрытым и выходным слоев

        changeThreshold(layerThreshold[1], outputLayerFault, alfa); //Изменение порогов активации выходного слоя

        Matrix hiddenLayerFault = calculateHiddenLayerFault(outputLayerFault, layerWeights[1]); //Матрица ошибок нейронов скрытого слоя

        changeLayersWeights(layerWeights[0], deltaLayerWeight[0],
                hiddenLayerFault, lastLayersValues[0], beta);  //Изменение весов синапсов между скрытым и выходным слоев

        changeThreshold(layerThreshold[0], hiddenLayerFault, beta);  //Изменение порогов активации скрытого слоя

    }

    @Override
    public double getRMS(Matrix input, Matrix output){ //Среднеквадратичная ошибка
        Matrix result = this.getNetResult(input);
        double ans = 0;
        for(int i = 0; i < k; i++)
            ans += (result.getMatrix()[0][i] - output.getMatrix()[0][i]) * (result.getMatrix()[0][i] - output.getMatrix()[0][i]);
        return ans/2;
    }

}
