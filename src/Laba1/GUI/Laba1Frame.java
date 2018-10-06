package Laba1.GUI;

import Laba1.HopfildNet;
import Prototypes.Matrix;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * Created by ptaxom on 03.10.2018.
 */
public class Laba1Frame extends JFrame {


    private Laba1Shape inputShape, outputShape;
    private int depth = 0;

    HopfildNet net;

    private Laba1Frame pointer = this;
    private Laba1Panel panel = new Laba1Panel(this);


    private String inputPath = "";


    public Laba1Frame() {
        super("Laba1");

        setSize(780, 460);

        loadHopfildNet();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(panel);


        inputShape = new Laba1Shape(30, new Point(20, 20), 10);
        outputShape = new Laba1Shape(30, new Point(400, 20), 10);

        JButton changeInputButton = new JButton("Сменить образ");
        changeInputButton.setBounds(100, 350, 120, 20);

        JButton decDepth = new JButton("-1 к итерациям");
        decDepth.setBounds(400, 350, 130, 20);

        JButton incDepth = new JButton("+1 к итерациям");
        incDepth.setBounds(571, 350, 130, 20);

        panel.add(changeInputButton);
        panel.add(incDepth);
        panel.add(decDepth);

        changeInputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                depth = 0;
                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new File("res\\Laba1"));
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "TXT files", "txt");
                chooser.setFileFilter(filter);
                chooser.setDialogTitle("Выбор образа");
                int selected = chooser.showDialog(pointer, "Выбрать");
                if (selected == JFileChooser.APPROVE_OPTION) {
                    String path = chooser.getSelectedFile().getAbsolutePath();
                    try {
                        pointer.inputPath = path.substring(path.length() - 8);
                        inputShape.loadFromFile(new File(path));
                        outputShape.setFromVector(net.getValue(new Matrix(inputShape.getM()), depth));

                        pointer.panel.repaint();

                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog(pointer, "Ошибка!");
                    }

                }
            }
        });



        incDepth.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                depth += depth < 9 ? 1 : 0;

                outputShape.setFromVector(net.getValue(new Matrix(inputShape.getM()), depth));

                pointer.panel.repaint();
            }
        });

        decDepth.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                depth -= depth > 0 ? 1 : 0;
                outputShape.setFromVector(net.getValue(new Matrix(inputShape.getM()), depth));
                pointer.panel.repaint();
            }
        });

        JButton clearButton = new JButton("Очистить");
        clearButton.setBounds(100,385,120,20);

        clearButton.addActionListener(e -> {inputShape.clear(); outputShape.clear(); panel.repaint();});

        panel.add(clearButton);

        setVisible(true);


    }

    private void loadHopfildNet() {
        try {
            Matrix[] matrices = new Matrix[3];
            for (int i = 0; i < 3; i++) {
                matrices[i] = new Matrix(new File("res\\Laba1\\" + (i + 1) + ".txt"));
                matrices[i].toVector();
            }
            net = new HopfildNet(matrices);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Laba1Shape getInputShape() {
        return inputShape;
    }

    public Laba1Shape getOutputShape() {
        return outputShape;
    }

    public int getDepth() {
        return depth;
    }

    public HopfildNet getNet() {
        return net;
    }

    public String getInputPath() {
        return inputPath;
    }
}