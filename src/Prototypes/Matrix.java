package Prototypes;

import java.io.*;

/**
 * Created by ptaxom on 03.10.2018.
 */
public class Matrix {

    private int n, m;

    private double[][] matrix;

    public Matrix() {
    }

    public Matrix(int n, int m) {
        this.n = n;
        this.m = m;
        matrix = new double[n][m];
    }

    public Matrix(int n, int m, double[][] matrix) {
        this.n = n;
        this.m = m;
        this.matrix = new double[n][m];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                this.matrix[i][j] = matrix[i][j];
    }

    public Matrix(double[][] matrix) {
        this.n = matrix.length;
        this.m = matrix[0].length;
        this.matrix = new double[n][m];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                this.matrix[i][j] = matrix[i][j];
    }

    public Matrix(Matrix matrix) {
        this.n = matrix.n;
        this.m = matrix.m;
        this.matrix = new double[n][m];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                this.matrix[i][j] = matrix.matrix[i][j];
    }

    public void copy(Matrix matrix) {
        this.n = matrix.n;
        this.m = matrix.m;
        this.matrix = new double[n][m];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                this.matrix[i][j] = matrix.matrix[i][j];
    }

    public void randomInitialize() {
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                matrix[i][j] = Math.random() * (Math.random() < 0.5 ? -1 : 1);
    }


    public void function(Function func) {
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                matrix[i][j] = func.function(matrix[i][j]);
    }

    public void add(Matrix matrix) {
        if (n == matrix.n && m == matrix.m)
            for (int i = 0; i < n; i++)
                for (int j = 0; j < m; j++)
                    this.matrix[i][j] += matrix.matrix[i][j];
    }

    public void multiply(double value) {
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                this.matrix[i][j] *= value;
    }

    public void multiply(Matrix matrix) {
        if (this.m == matrix.n) {
            Matrix c = new Matrix(this.n, matrix.m);
            for (int i = 0; i < this.n; i++)
                for (int j = 0; j < matrix.m; j++)
                    for (int k = 0; k < matrix.n; k++)
                        c.matrix[i][j] += this.matrix[i][k] * matrix.matrix[k][j];
            this.copy(c);
        }
    }


    @Override
    public String toString() {
        String string = super.toString() + "\n" + n + " " + m + "\n";
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++)
                string += matrix[i][j] + " ";
            string += "\n";
        }
        return string;
    }


    public void transponate() {
        Matrix buf = new Matrix(m, n);
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                buf.matrix[j][i] = matrix[i][j];
        this.copy(buf);
    }


    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
    }

    public double[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(double[][] matrix) {
        this.matrix = matrix;
    }

    public boolean equals(Matrix M) {
        if (this.n == M.n && this.m == M.m) {
            for (int i = 0; i < n; i++)
                for (int j = 0; j < m; j++)
                    if (matrix[i][j] != M.matrix[i][j])
                        return false;
        } else
            return false;
        return true;
    }


    public void saveToFile(File file) throws IOException {
        if (file != null) {
            if (!file.exists())
                file.createNewFile();
            PrintWriter writer = new PrintWriter(file.getAbsoluteFile());
            writer.println(n + " " + m);
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++)
                    writer.print(matrix[i][j] + " ");
                writer.println();
            }
            writer.close();
        }
    }

    public void saveToFile(File file, boolean asInt) throws IOException {
        if (file != null) {
            if (!file.exists())
                file.createNewFile();
            PrintWriter writer = new PrintWriter(file.getAbsoluteFile());
            writer.println(n + " " + m);
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++)
                    if (asInt)
                        writer.print((int) matrix[i][j] + " ");
                    else writer.print(matrix[i][j] + " ");
                writer.println();
            }
            writer.close();
        }
    }

    public Matrix(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file.getAbsoluteFile()));
        String line = reader.readLine();
        this.n = Integer.parseInt(line.split(" ")[0]);
        this.m = Integer.parseInt(line.split(" ")[1]);
        matrix = new double[n][m];
        for (int i = 0; i < n; i++) {
            line = reader.readLine();
            String[] values = line.split(" ");
            for (int j = 0; j < m; j++)
                matrix[i][j] = Double.parseDouble(values[j]);
        }
    }

    public void toVector() {
        Matrix buf = new Matrix(1, n * m);
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                buf.matrix[0][i * n + j] = matrix[i][j];
        this.copy(buf);
    }

    public static Matrix toVector(Matrix matrix) {
        Matrix buf = new Matrix(1, matrix.n * matrix.m);
        for (int i = 0; i < matrix.n; i++)
            for (int j = 0; j < matrix.m; j++)
                buf.matrix[0][i * matrix.n + j] = matrix.matrix[i][j];
        return buf;
    }

    public void setElement(int i, int j, double value) {
        matrix[i][j] = value;
    }

}

