package Prototypes;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by ptaxom on 03.10.2018.
 */
public abstract class AbstractShape {

    private int n;
    private double cageSize;

    private Point leftTopCorner;
    protected double[][] M;
    private double defaultValue;


    public AbstractShape(double cageSize, Point leftTopCorner, int n, double defaultValue) {
        this.cageSize = cageSize;
        this.leftTopCorner = leftTopCorner;
        this.n = n;
        this.defaultValue = defaultValue;
        M = new double[n][n];
        for(int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                M[i][j] = defaultValue;
    }

    public void loadFromFile(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file.getAbsoluteFile()));
        reader.readLine();
        for(int i = 0; i < n; i++){
            String line = reader.readLine();
            for(int j = 0; j < n; j++)
                M[i][j] = Double.parseDouble(line.split(" ")[j]);
        }
    }



    public void draw(Graphics2D g2){
        for(int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Rectangle2D rect = new Rectangle2D.Double(leftTopCorner.x + i * cageSize, leftTopCorner.y + j * cageSize, cageSize, cageSize);
                g2.setPaint(getColorOfCell(M[j][i]));
                g2.fill(rect);
                g2.setPaint(Color.BLACK);
                g2.draw(rect);
            }
        }
    }

    protected abstract Color getColorOfCell(double x);

    public void inverse(Point p){
        int dx = p.x - leftTopCorner.x;
        int dy = p.y - leftTopCorner.y;
        if (0 <= dx && dx <= cageSize*n &&
                0 <= dy && dy <= cageSize*n){
            int j = (int)((double)dx / cageSize);
            int i = (int)((double)dy / cageSize);
            if (i < n && j < n)
                reverseValue(i,j);
        }
    }

    protected abstract void reverseValue(int i, int j);

    public void setM(double[][] m) {
        M = m;
    }


    public void setFromVector(Matrix vector){
        for(int i = 0; i < n; i++)
            for(int j = 0; j < n; j++)
                M[i][j] = vector.getMatrix()[0][i*n+j];
    }

    public void setN(int n) {
        this.n = n;
    }

    public double[][] getM() {
        return M;
    }

    public void clear(){
        for(int i = 0; i < n; i++)
            for(int j = 0; j < n; j++)
                M[i][j] = defaultValue;
    }


}
