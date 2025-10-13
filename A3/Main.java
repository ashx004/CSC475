/*
 * Name: Ashton Harrell
 * Date: 10/3/2025
 * Description: Small neural network training to get desired outputs. Uses SGD and backpropagation to correct weights and biases
 */

public class Main {

    // learning rate 
    private final static double ETA = 10;
    private final static double MINIBATCHSIZE = 2;

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
        w_1.setData(0, 0, -0.21);
        w_1.setData(0, 1, 0.72);
        w_1.setData(0, 2, -0.25);
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

        y_3.setData(0, 0, 0);
        y_3.setData(1, 0, 1);

        y_4.setData(0, 0, 1);
        y_4.setData(1, 0, 0);

        // hold these to associate by index for ease of for loop iteration
        Matrix[] weights = {w_1, w_2};
        Matrix[] biases = {b_1, b_2};
        // for future assignment, this is probably gonna need to be allocated by size instead of by instantiating with elements 
        Matrix[] activations = {null, null, null};

        // minibatch 1, 2
        Matrix[] m1 = {x_1, x_2};
        Matrix[] m2 = {x_3, x_4};

        // associating outputs with their inputs by index to ensure forwardpass and gradient calculations are performed properly
        // had bad time correlating indices in for loops before doing this. its ugly but easier and safer 
        Matrix[] my1 = {y_1, y_2,};
        Matrix[] my2 = {y_3, y_4};

        // will be used to iterate across each minibatch per each epoch
        Matrix[][] arr1 = {m1, m2};
        Matrix[][] arr2 = {my1, my2};

        // Main learning loop
        // for 6 epochs...
        for (int epoch = 1; epoch <= 6; epoch++) {
            System.out.println("Epoch " + epoch + ":\n");
            System.out.println("------------------------------------------------------------------------------------------");

            // for each minibatch in arr1...
            for (int k = 0; k < arr1.length; k++) {
                System.out.println("Minibatch" + (k + 1) + ":\n");

                // error accumulators to be used for updating weights and biases after each minibatch iteration
                // need one for each weight and bias because they will be different shapes => cannot do addition
                // place them inside the loop instead of outside so they can just be reset upon a full minibatch iteration
                // so that error does not compound between epochs/minibatches
                Matrix weightAcc1 = new Matrix(w_1.getRows(), w_1.getCols());
                Matrix weightAcc2 = new Matrix(w_2.getRows(), w_2.getCols());
                Matrix[] weightAccArr = {weightAcc1, weightAcc2};
                Matrix biasAcc1 = new Matrix(b_1.getRows(), b_1.getCols());
                Matrix biasAcc2 = new Matrix(b_2.getRows(), b_2.getCols());
                Matrix[] biasAccArr = {biasAcc1, biasAcc2};

                // for each input in minibatch...
                for (int i = 0; i < arr1[k].length; i++) {
                    System.out.println("Input" + (i + 1) + "\n");
                    // forward pass through all layers
                    // load the proper initial input into this algorithm
                    activations[0] = arr1[k][i];
                    for (int l = 0; l < weights.length; l++) {
                        activations[l + 1] = forwardPass(weights[l], activations[l], biases[l]);
                        System.out.println("Activations:\n");
                        activations[l + 1].print();
                    }

                    // for weight gradient, we need the bias gradient from the layer ahead
                    // use this to store the latest one
                    Matrix B = null;
                    for (int l = weights.length - 1; l >= 0; l--) {
                        // indicates we are in final layer
                        if (l == weights.length - 1) {
                            B = backpropBiasFinal(activations[l + 1], arr2[k][i]);
                            biasAccArr[l].sum(B);
                            System.out.println("Final layer Bias Gradient:\n");
                            B.print();
                            weightAccArr[l].sum(backpropWeight(B, activations[l]));
                            System.out.println("Weight Gradient:\n");
                            backpropWeight(B, activations[l]).print();
                        } // we are in final layer
                        else {
                            B = backpropBiasHidden(weights[l + 1], B, activations[l + 1]);
                            biasAccArr[l].sum(B);
                            System.out.println("Bias Gradient:\n");
                            B.print();
                            weightAccArr[l].sum(backpropWeight(B, activations[l]));
                            System.out.println("Weight Gradient:\n");
                            backpropWeight(B, activations[l]).print();
                        }
                    }
                }
                // update all weights and biases after the minibatch has been processed all the way
                weights[0] = update(weights[0], weightAccArr[0]);
                weights[1] = update(weights[1], weightAccArr[1]);
                biases[0] = update(biases[0], biasAccArr[0]);
                biases[1] = update(biases[1], biasAccArr[1]);
            }
        }
    }

    // a method to perform the activation function
    public static double sigmoid(double z) {
        return (1 / (1 + Math.pow(Math.E, -z)));
    }

    // a method to calculate the activation matrix (which is a forward pass through one layer)
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

    // a method to grab the weight error at any layer
    public static Matrix backpropWeight(Matrix bias, Matrix acts) {
        return bias.multiply(acts.transpose());
    }

    // a method to grab the bias error at the final layer
    public static Matrix backpropBiasFinal(Matrix acts, Matrix expected) {
        Matrix one = new Matrix(acts.getRows(), acts.getCols());
        // fill an equivalently shaped matrix with 1's for subtraction
        for (int i = 0; i < one.getRows(); i++) {
            for (int j = 0; j < one.getCols(); j++) {
                one.setData(i, j, 1);
            }
        }
        // one liner of doom
        return acts.subtract(expected).hadamard(acts).hadamard(one.subtract(acts));
    }

    // a method to grab the bias error at the hidden layer
    public static Matrix backpropBiasHidden(Matrix weights, Matrix biasGradient, Matrix acts) {
        Matrix one = new Matrix(acts.getRows(), acts.getCols());
        for (int i = 0; i < one.getRows(); i++) {
            for (int j = 0; j < one.getCols(); j++) {
                one.setData(i, j, 1);
            }
        }
        return weights.transpose().multiply(biasGradient).hadamard(acts).hadamard(one.subtract(acts));
    }

    // a method to update the weights and biases of the network after minibatch processing 
    public static Matrix update(Matrix old, Matrix acc) {
        for (int i = 0; i < acc.getRows(); i++) {
            for (int j = 0; j < acc.getCols(); j++) {
                // scale the accumulator by the learning rate to minibatchsize ratio
                acc.setData(i, j, acc.getData()[i][j] * (ETA / MINIBATCHSIZE));
            }
        }
        // new weights/biases are the difference of the scaled accumulator, which is in theory the sum of errors
        // across the training cases 
        return old.subtract(acc);
    }
}
