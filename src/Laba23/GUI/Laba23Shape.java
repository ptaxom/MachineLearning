package Laba23.GUI;

import Prototypes.AbstractShape;

import java.awt.*;

/**
 * Created by ptaxom on 04.10.2018.
 */
public class Laba23Shape extends AbstractShape {

    public Laba23Shape(double cageSize, Point leftTopCorner, int n) {
        super(cageSize, leftTopCorner, n, 0);
    }

    @Override
    protected Color getColorOfCell(double x) {
        return x == 0 ? Color.WHITE : Color.RED;
    }

    @Override
    protected void reverseValue(int i, int j) {
        M[i][j] = 1 - M[i][j];
    }
}
