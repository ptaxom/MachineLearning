package Laba23.GUI;

import Prototypes.Matrix;
import Prototypes.MultiLayerNet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

/**
 * Created by ptaxom on 04.10.2018.
 */
public class Laba23Panel extends JPanel {

    private Laba23Shape[] shapes = new Laba23Shape[5];

    private Laba23Shape controler;

    private Laba23Panel panelPointer = this;

    private MultiLayerNet net;

    private Matrix lastResult = null;

    private static final String correctSamplesPath = "res\\inputData";

    public Laba23Panel(MultiLayerNet net) {
        super(null);
        this.net = net;
        try {
            net.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(int i = 0; i < 5; i++){
            shapes[i] = new Laba23Shape(15, new Point(40+100*i,400),6);
            try {
                shapes[i].loadFromFile(new File(correctSamplesPath+"\\"+(i+1)+".txt"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }




        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controler.inverse(e.getPoint());
                lastResult = net.getNetResult(Matrix.toVector(new Matrix(controler.getM())));
                panelPointer.repaint();


            }
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        controler = new Laba23Shape(30,new Point(170,50),6);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        for(int i = 0; i < 5; i++) {
            shapes[i].draw(g2);
            if (lastResult != null)
                g2.drawString((int)(lastResult.getMatrix()[0][i]*100)+"%",50+100*i,510);
        }
        controler.draw(g2);
        g2.drawString("Совпадение: ",5, 390);
    }

    public Laba23Shape getControler() {
        return controler;
    }

    public void setLastResult(Matrix lastResult) {
        this.lastResult = lastResult;
    }
}
