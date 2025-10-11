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

        // minibatches (associate indices of inputs with the expected output they are intended to have)
        Matrix[] mx1 = { x_1, x_2 };
        Matrix[] mx2 = { x_3, x_4, };
        Matrix[] my = { y_1, x_2 };

        // associate all weights, biases and activations with indices in lists
        // this is so we know what layer we are in 
        Matrix[] weights = { w_1, w_2 };
        Matrix[] biases = { b_1, b_2 };

        ArrayList<Matrix> activations = new ArrayList<>();
        activations.add(x_1);

        // lists to hold bias and weight errors for later accumulation and updating 
        ArrayList<Matrix> gradientB = new ArrayList<>();
        ArrayList<Matrix> gradientW = new ArrayList<>();

        // WORKS!!!!!!!!
        // feeding forward
        for (int i = 0; i < weights.length; i++) {
            Matrix mat = forwardPass(weights[i], activations.get(i), biases[i]);
            mat.print();
            System.out.println();
            activations.add(mat);
        }
        // backpropagation
        for (int l = weights.length - 1; l >= 0; l--) {
            if (l == weights.length - 1) {
                Matrix biasmat = biasBackpropFinal(activations.get(l + 1), my[l-1]);
                biasmat.print();
                System.out.println();
                Matrix weightmat = weightBackprop(biasmat, activations.get(l));
                weightmat.print();
                System.out.println();
                gradientB.add(biasmat);
                gradientW.add(weightmat);
            }
            else {
                Matrix matbias = biasBackpropHidden(weights[l + 1], gradientB.get(l), activations.get(l + 1));
                Matrix weightmat = weightBackprop(matbias, activations.get(l));
                matbias.print();
                System.out.println();
                weightmat.print();
                System.out.println();
                gradientW.add(matbias);
                gradientB.add(weightmat);
            }
        }
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