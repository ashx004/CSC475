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
        
        // fill weights and biases
        w_1.setData(0, 0, -0.21);
        w_1.setData(0, 1, 0.72);
        w_1.setData(0, 2, 0.25);
        w_1.setData(0, 3, 1);

        w_1.setData(1, 0, -0.94);
        w_1.setData(1, 1, -0.41);
        w_1.setData(1, 2, -0.47);
        w_1.setData(1, 3, 0.63);

        w_1.setData(2, 0, 0.15);
        w_1.setData(2, 1, 0.55);
        w_1.setData(2, 2, -0.49);
        w_1.setData(2, 3, -0.75);

        w_2.setData(0, 0, 0.76);
        w_2.setData(0, 1, 0.48);
        w_2.setData(0, 2, -0.73);

        w_2.setData(1, 0, 0.34);
        w_2.setData(1, 1, 0.89);
        w_2.setData(1, 2, -0.23);

        b_1.setData(0, 0, 0.1);
        b_1.setData(1, 0, -0.36);
        b_1.setData(2, 0, -0.31);

        b_2.setData(0, 0, 0.16);
        b_2.setData(1, 0, -0.46);

        // fill inputs and outputs
        x_1.setData(0, 0, 0);
        x_1.setData(1, 0, 1);
        x_1.setData(2, 0, 0);
        x_1.setData(3, 0, 1);

        x_2.setData(0, 0, 1);
        x_2.setData(1, 0, 0);
        x_2.setData(2, 0, 1);
        x_2.setData(3, 0, 0);

        x_3.setData(0, 0, 0);
        x_3.setData(1, 0, 0);
        x_3.setData(2, 0, 1);
        x_3.setData(3, 0, 1);

        x_4.setData(0, 0, 1);
        x_4.setData(1, 0, 1);
        x_4.setData(2, 0, 0);
        x_4.setData(3, 0, 0);

        y_1.setData(0, 0, 0);
        y_1.setData(1, 0, 1);

        y_2.setData(0, 0, 1);
        y_2.setData(1, 0, 0);


        // minibatches (associate indices of inputs with the expected output they are intended to have)
        Matrix[] mx1 = { x_1, x_2 };
        Matrix[] mx2 = { x_3, x_4, };
        Matrix[][] trainingData = { mx1, mx2 };
        Matrix[] my = { y_1, y_2 };

        // Matrix[] batch = { x_1, x_2, x_3, x_4};

        // associate all weights, biases and activations with indices in lists
        // this is so we know what layer we are in 
        Matrix[] weights = { w_1, w_2 };
        Matrix[] biases = { b_1, b_2 };

        ArrayList<Matrix> activations = new ArrayList<>();

        // lists to hold bias and weight errors for later accumulation and updating 
        ArrayList<Matrix> gradientB = new ArrayList<>();
        ArrayList<Matrix> gradientW = new ArrayList<>();

        for (int i = 0; i < trainingData.length; i++) {
            // accumulators for later updating at the end of the minibatch, corresponding to first and hidden layers
            Matrix biasError1 = new Matrix(3, 1);
            Matrix biasError2 = new Matrix(2, 1);
            Matrix weightError1 = new Matrix(3, 4);
            Matrix weightError2 = new Matrix(2, 3);

            System.out.println("Minibatch " + (i + 1));
            Matrix[] minibatch = trainingData[i];
            for (int j = 0; j < minibatch.length; j++) {
                System.out.println("Input " + (j + 1));
                activations.clear();
                activations.add(minibatch[j]);
                // feed forward
                for (int l = 0; l < weights.length; l++) {
                    System.out.println(l + 1 + " forward pass");
                    Matrix mat = forwardPass(weights[l], activations.get(l), biases[l]);
                    mat.print();
                    System.out.println();
                    activations.add(mat);
                }
                // backprop
                for (int l = weights.length - 1; l >= 0; l--) {
                    System.out.println();
                    System.out.println("Backwards pass through layer" + l);
                    System.out.println();
                    if (l == weights.length - 1) {
                        Matrix biasmat = biasBackpropFinal(activations.get(l + 1), my[l-1]);
                        System.out.println();
                        System.out.println("bias gradient: ");
                        biasmat.print();
                        System.out.println();
                        System.out.println("weight gradient: ");
                        Matrix weightmat = weightsBackprop(biasmat, activations.get(l));
                        weightmat.print();
                        gradientB.add(biasmat);
                        gradientW.add(weightmat);
                    }
                    else {
                        Matrix biasmat = biasBackpropHidden(weights[l + 1], gradientB.get(l), activations.get(l + 1));
                        System.out.println();
                        System.out.println("bias gradient: ");
                        biasmat.print();
                        Matrix weightmat = weightsBackprop(biasmat, activations.get(l));
                        System.out.println();
                        System.out.println("weight gradient: ");
                        weightmat.print();
                        System.out.println();
                        gradientW.add(weightmat);
                        gradientB.add(biasmat);
                    }
                }
            }
        }
    }

    // activation function 
    public static double sigmoid(double z) {
        return (1 / (1 + Math.pow(Math.E, -z)));
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
    public static Matrix weightsBackprop(Matrix bias, Matrix activations) {
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