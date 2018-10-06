package Laba1;

import Prototypes.Function;
import Prototypes.Matrix;

/**
 * Created by ptaxom on 03.10.2018.
 */
public class HopfildNet {


    private Matrix weights;
    private Matrix[] inputShapes;


    private Function function = Math::signum;


    public HopfildNet(Matrix[] inputShapes) {
        this.inputShapes = inputShapes;
        buildWeights(inputShapes);
    }

    public void buildWeights(Matrix[] inputShapes){
        weights = new Matrix(inputShapes[0].getM(), inputShapes[0].getM());
        for(int i = 0; i < inputShapes.length; i++){
            Matrix buf = new Matrix(inputShapes[i]), bufT = new Matrix(inputShapes[i]);
            bufT.transponate();
            bufT.multiply(buf);
            weights.add(bufT);
        }
        for(int i = 0; i < weights.getM(); i++)
            weights.setElement(i,i,0);
    }

    public Matrix getValue(Matrix input){
        Matrix buf = new Matrix(input);
        if (buf.getM() != weights.getN())
            buf.toVector();
        buf.multiply(weights);
        buf.function(function);
        return buf;
    }

    public Matrix getValue(Matrix input, int depth){
        Matrix buf = new Matrix(input);
        for(; depth >= 0; depth--){
            buf = new Matrix(getValue(buf));
        }
        return buf;
    }

    public Matrix recognize(Matrix inputShape) throws RecognitionException {
        Matrix[] recognitionEra = new Matrix[10];
        recognitionEra[0] = getValue(inputShape);
        int i;
        boolean canRecognize = (isSavedShape(recognitionEra[0]) != -1);
        for(i = 1; i < recognitionEra.length; i++){
            recognitionEra[i] = getValue(recognitionEra[i-1]);
            canRecognize |= (isSavedShape(recognitionEra[i]) != -1);
            if (canRecognize)
                break;
        }
        if (!canRecognize)
            throw new RecognitionException();
        return recognitionEra[i];
    }


    public int isSavedShape(Matrix m){
        Matrix inverseM = new Matrix(m);
        inverseM.multiply(-1);
        for(int i = 0; i < inputShapes.length; i++)
            if (inputShapes[i].equals(m) || inputShapes[i].equals(inverseM))
                return i;
        return -1;
    }

}
