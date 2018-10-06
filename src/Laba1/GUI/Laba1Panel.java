package Laba1.GUI;

import Laba1.RecognitionException;
import Prototypes.Matrix;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by ptaxom on 03.10.2018.
 */
public class Laba1Panel extends JPanel {

    private Laba1Frame frame;
    private Laba1Panel panel = this;

    public Laba1Panel(Laba1Frame frame) {
        super(null);
        this.frame = frame;

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.getInputShape().inverse(e.getPoint());

                frame.getOutputShape().setFromVector(frame.getNet().getValue(new Matrix(frame.getInputShape().getM()), frame.getDepth()));

                //Распознование с возможностью ошибки
//                try{
//                    Matrix output = frame.getNet().recognize(new Matrix(frame.getInputShape().getM()));
//                    frame.getOutputShape().setFromVector(output);
//                } catch (RecognitionException e1) {
//                    frame.getOutputShape().clear();
//                    frame.getInputShape().clear();
//                    JOptionPane.showMessageDialog(frame, "Образ нельзя распознать!");
//                }
                panel.repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {          }
        });


    }





    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        frame.getInputShape().draw(g2);
        frame.getOutputShape().draw(g2);
        g2.drawString("Количество итераций: "+(frame.getDepth()+1),485,340);
        g2.drawString("Исходный файл: "+frame.getInputPath(),50,340);
    }
}
