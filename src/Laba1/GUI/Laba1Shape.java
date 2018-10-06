package Laba1.GUI;

import Prototypes.AbstractShape;

import java.awt.*;

/**
 * Created by ptaxom on 03.10.2018.
 */
public class Laba1Shape extends AbstractShape {

    public Laba1Shape(int cageSize, Point leftTopCorner, int n) {
        super(cageSize, leftTopCorner, n, -1); //-1 = White, +1 = Red
    }

    @Override
    protected Color getColorOfCell(double x) {
        return x == -1 ? Color.WHITE : Color.RED;
    }

    @Override
    protected void reverseValue(int i, int j){
        M[i][j] *= -1;
    }
}
