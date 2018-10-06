import Laba1.GUI.Laba1Frame;
import Laba23.GUI.Laba23Frame;
import Laba23.NetTeacher.MultiLayerNetInitializer;
import Laba23.PerceptronNet;
import Laba23.RBFNet;

import java.io.IOException;

/**
 * Created by ptaxom on 03.10.2018.
 */
public class Main {


    public static void main(String[] args) {
//        new Laba1Frame();
        Laba23();
    }


    public static void Laba23(){

//        MultiLayerNetInitializer.createNet(MultiLayerNetInitializer.getRBFNetConfiguration(), false, 100); //Реинициализация RBF

//       PerceptronNet net = new PerceptronNet("res\\Laba2",0.7,0.7,0.7,36,10,5); //Реинициализация перцептронной
//       MultiLayerNetInitializer.createNet(net,false,100); //Переобучение\дообучение

       PerceptronNet PCPnet = new PerceptronNet("res\\Laba2");           //Загрузка последней сохраненной перцептронной
       RBFNet RBFNet = MultiLayerNetInitializer.getRBFNetConfiguration();//Загрузка последней RBF



//       new Laba23Frame("RBF",RBFNet);
       new Laba23Frame("Perceptron",PCPnet);
    }





}
