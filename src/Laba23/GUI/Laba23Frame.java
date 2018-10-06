package Laba23.GUI;

import Prototypes.MultiLayerNet;

import javax.swing.*;

/**
 * Created by ptaxom on 04.10.2018.
 */
public class Laba23Frame extends JFrame {

    public Laba23Frame(String title, MultiLayerNet net) {
        super(title);

        setSize(600,600);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Laba23Panel panel = new Laba23Panel(net);
        add(panel);

        JButton clearButton = new JButton("Очистить");

        clearButton.setBounds(200,280,120,30);
        clearButton.addActionListener(e -> {panel.getControler().clear(); panel.setLastResult(null); panel.repaint(); });
        panel.add(clearButton);

        setVisible(true);

    }


}
