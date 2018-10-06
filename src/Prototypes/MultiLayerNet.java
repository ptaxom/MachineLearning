package Prototypes;

import java.io.IOException;

/**
 * Created by ptaxom on 03.10.2018.
 */
public interface MultiLayerNet {

    void init() throws IOException;

    void save() throws IOException;

    Matrix getNetResult(Matrix input);

    void trainNet(Matrix input, Matrix output);

    void load() throws IOException;

    double getRMS(Matrix input, Matrix output);

}
