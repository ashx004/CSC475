/*
 * Name: Ashton Harrell
 * Date: 10/3/2025
 * Description: Small neural network training 
 */

import java.util.ArrayList;

public class Main {

    // learning rate 
    private final static int ETA = 10;

    public static void main(String[] args) {
        // create weights and biases
        Matrix w_1 = new Matrix(3, 4);
        Matrix w_2 = new Matrix(2, 3);
        Matrix b_1 = new Matrix(3, 1);
        Matrix b_2 = new Matrix(2, 1);

        // create inputs and outputs
        Matrix x_1 = new Matrix(4, 1);
        Matrix x_2 = new Matrix(4, 1);
        Matrix x_3 = new Matrix(4, 1);
        Matrix x_4 = new Matrix(4, 1);

        Matrix y_1 = new Matrix(2, 1);
        Matrix y_2 = new Matrix(2, 1);
        Matrix y_3 = new Matrix(2, 1);
        Matrix y_4 = new Matrix(2, 1);

        // fill weights and biases
        w_1.setData(0, 0, -0.21f);
        w_1.setData(0, 1, 0.72f);
        w_1.setData(0, 2, 0.25f);
        w_1.setData(0, 3, 1f);

        w_1.setData(1, 0, -0.94f);
        w_1.setData(1, 1, -0.41f);
        w_1.setData(1, 2, -0.47f);
        w_1.setData(1, 3, 0.63f);

        w_1.setData(2, 0, 0.15f);
        w_1.setData(2, 1, 0.55f);
        w_1.setData(2, 2, -0.49f);
        w_1.setData(2, 3, -0.75f);

        w_2.setData(0, 0, 0.76f);
        w_2.setData(0, 1, 0.48f);
        w_2.setData(0, 2, -0.73f);

        w_2.setData(1, 0, 0.34f);
        w_2.setData(1, 1, 0.89f);
        w_2.setData(1, 2, -0.23f);

        b_1.setData(0, 0, 0.1f);
        b_1.setData(1, 0, -0.36f);
        b_1.setData(2, 0, -0.31f);

        b_2.setData(0, 0, 0.16f);
        b_2.setData(1, 0, -0.46f);

        // fill inputs and outputs
        x_1.setData(0, 0, 0f);
        x_1.setData(1, 0, 1f);
        x_1.setData(2, 0, 0f);
        x_1.setData(3, 0, 1f);

        x_2.setData(0, 0, 1f);
        x_2.setData(1, 0, 0f);
        x_2.setData(2, 0, 1f);
        x_2.setData(3, 0, 0f);

        x_3.setData(0, 0, 0f);
        x_3.setData(1, 0, 0f);
        x_3.setData(2, 0, 1f);
        x_3.setData(3, 0, 1f);

        x_4.setData(0, 0, 1f);
        x_4.setData(0, 0, 1f);
        x_4.setData(0, 0, 0f);
        x_4.setData(0, 0, 0f);

        y_1.setData(0, 0, 0f);
        y_1.setData(1, 0, 1f);

        y_2.setData(0, 0, 1f);
        y_2.setData(1, 0, 0f);

        y_3.setData(0, 0, 0f);
        y_3.setData(1, 0, 1f);

        y_4.setData(0, 0, 1f);
        y_4.setData(1, 0, 0f);

        // minibatches 
        Matrix[] m_1 = {x_1, x_2, y_1, y_2};
        Matrix[] m_2 = {x_3, x_4, y_3, y_4};

        // associate all weights, biases and activations with indices in lists
        // this is so we know what layer we are in 
        ArrayList<Matrix> weights = new ArrayList<>();
        weights.add(w_1);
        weights.add(w_2);

        ArrayList<Matrix> biases = new ArrayList<>();
        biases.add(b_1);
        biases.add(b_2);

        ArrayList<Matrix> activations = new ArrayList<>();
        activations.add(x_1);

        // lists to hold bias and weight errors
        ArrayList<Matrix> gradientB_w1 = new ArrayList<>();
        ArrayList<Matrix> gradientW = new ArrayList<>();

        Matrix mat1 = forwardPass(w_1, x_1, b_1);
        //mat1.print();
        Matrix mat2 = forwardPass(w_2, mat1, b_2);
        // mat2.print();
        Matrix matback1 = biasBackpropFinal(mat2, y_1);
        //matback1.print();
        Matrix matback2 = biasBackpropHidden(w_2, matback1, mat1);
        matback2.print();
    }

    // activation function 
    public static float sigmoid(double z) {
        return (float) (1 / (1 + Math.pow(Math.E, -z)));
    }

    // calculate activation matrix (forward pass through one layer)
    // WORKS
    public static Matrix forwardPass(Matrix weights, Matrix oldActivations, Matrix bias) {
        // (W^L * A^(L-1)) + B^L is what this is performing
        Matrix mat = weights.multiply(oldActivations).add(bias);
        for (int i = 0; i < mat.getRows(); i++) {
            for (int j = 0; j < mat.getCols(); j++) {
                mat.setData(i, j, sigmoid(mat.getData()[i][j]));
            }
        }
        return mat;
    }

    // WORKS
    public static Matrix weightBackprop(Matrix bias, Matrix activations) {
        // B^l * (A^l-1)^T is what this is performing 
        return bias.multiply(activations.transpose());
    }

    // WORKS
    public static Matrix biasBackpropFinal(Matrix acts, Matrix expected) {
        // generate matrix full of only one's
        Matrix one = new Matrix(acts.getRows(), acts.getCols());
        for (int i = 0; i < acts.getRows(); i++) {
            for (int j = 0; j < acts.getCols(); j++) {
                one.setData(i, j, 1);
            }
        }
        return acts.subtract(expected).hadamard(acts).hadamard(one.subtract(acts));
    }

    // WORKS
    public static Matrix biasBackpropHidden(Matrix weights, Matrix biasGrad, Matrix acts) {
        Matrix one = new Matrix(acts.getRows(), acts.getCols());
        for (int i = 0; i < acts.getRows(); i++) {
            for (int j = 0; j < acts.getCols(); j++) {
                one.setData(i, j, 1);
            }
        }
        return weights.transpose().multiply(biasGrad).hadamard(acts).hadamard(one.subtract(acts));
    }
}