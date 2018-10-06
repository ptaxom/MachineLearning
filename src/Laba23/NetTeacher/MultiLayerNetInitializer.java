package Laba23.NetTeacher;

import Laba23.PerceptronNet;
import Laba23.RBFNet;
import Prototypes.Matrix;
import Prototypes.MultiLayerNet;

import java.io.File;
import java.io.IOException;

/**
 * Created by ptaxom on 04.10.2018.
 */
public class MultiLayerNetInitializer {


    public static void  createNet(MultiLayerNet net, boolean isInitialized, int amountOfGeneration){
//
        try {
            if (!isInitialized)
                net.init();
            else net.load();

            Matrix[] correctSamples = new Matrix[5];
            Matrix[] ans = new Matrix[5];
            for(int i = 0; i < 5; i++){
                correctSamples[i] = new Matrix(new File("res\\inputData\\"+(i+1)+".txt"));

                ans[i] = new Matrix(1,5);
//                ans[i].setElement(0,i,1);
            }

            for(int i = 0; i < 5; i++)
                for(int j =0; j < 5; j++)
                    ans[i].setElement(0,j, getPercentOfDifference(correctSamples[i], correctSamples[j]));

            for(int i = 0; i < amountOfGeneration; i++){
                System.out.println("Generation: "+i);
                for(int j = 0; j < 5; j++){
                    Matrix vectorInput = new Matrix(correctSamples[j]);
                    vectorInput.toVector();
                    double fault = net.getRMS(vectorInput, ans[j]);
                    System.out.print((int)(fault*100)+"  ");
                    net.trainNet(vectorInput, ans[j]);
                }
                System.out.println();
            }
            net.save();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static double getPercentOfDifference(Matrix A, Matrix B){
        double difference = 0;
        for(int i = 0; i < 6; i++)
            for(int j = 0; j < 6; j++)
                difference += Math.abs(A.getMatrix()[i][j] - B.getMatrix()[i][j]);
        return ((36.0-difference)/36.0);

    }


    public static RBFNet getRBFNetConfiguration(){
        Matrix[] correctSamples = new Matrix[5];
        for(int i = 0; i < 5; i++)
            try {
                correctSamples[i] = new Matrix(new File("res\\inputData\\"+(i+1)+".txt"));
                correctSamples[i].toVector();
            } catch (IOException e) {
                e.printStackTrace();
            }
        RBFNet net = new RBFNet(correctSamples, 5, "res\\Laba3", 0.1);
        return net;

    }


}
