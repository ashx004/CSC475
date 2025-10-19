/*
 * Name: Ashton Harrell
 * Date: 10/13/2025
 * Description: Large Neural Network being trained/tested on the MNIST dataset
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;


public class Main {
    // learning rate 
    private final static double ETA = 3;
    // amount of minibatches 
    private final static int MINIBATCHES = 10;
    // use this for gradient calculations later for eta / m 
    private final static int MINIBATCHSIZE = 6000;
    // amount of epochs the data will go through
    private final static int EPOCHS = 30;

    public static void main(String[] args) {
        

        // Main learning loop
        // for 6 epochs...
        // for (int epoch = 1; epoch <= EPOCHS; epoch++) {
        //     System.out.println("Epoch " + epoch + ":\n");
        //     System.out.println("------------------------------------------------------------------------------------------");

        //     // for each minibatch in arr1...
        //     for (int k = 0; k < arr1.length; k++) {
        //         System.out.println("Minibatch" + (k + 1) + ":\n");

        //         // error accumulators to be used for updating weights and biases after each minibatch iteration
        //         // need one for each weight and bias because they will be different shapes => cannot do addition
        //         // place them inside the loop instead of outside so they can just be reset upon a full minibatch iteration
        //         // so that error does not compound between epochs/minibatches
        //         Matrix weightAcc1 = new Matrix(w_1.getRows(), w_1.getCols());
        //         Matrix weightAcc2 = new Matrix(w_2.getRows(), w_2.getCols());
        //         Matrix[] weightAccArr = {weightAcc1, weightAcc2};
        //         Matrix biasAcc1 = new Matrix(b_1.getRows(), b_1.getCols());
        //         Matrix biasAcc2 = new Matrix(b_2.getRows(), b_2.getCols());
        //         Matrix[] biasAccArr = {biasAcc1, biasAcc2};

        //         // for each input in minibatch...
        //         for (int i = 0; i < arr1[k].length; i++) {
        //             System.out.println("Input" + (i + 1) + "\n");
        //             // forward pass through all layers
        //             // load the proper initial input into this algorithm
        //             activations[0] = arr1[k][i];
        //             for (int l = 0; l < weights.length; l++) {
        //                 activations[l + 1] = forwardPass(weights[l], activations[l], biases[l]);
        //                 System.out.println("Activations:\n");
        //                 activations[l + 1].print();
        //             }

        //             // for weight gradient, we need the bias gradient from the layer ahead
        //             // use this to store the latest one
        //             Matrix B = null;
        //             for (int l = weights.length - 1; l >= 0; l--) {
        //                 // indicates we are in final layer
        //                 if (l == weights.length - 1) {
        //                     B = backpropBiasFinal(activations[l + 1], arr2[k][i]);
        //                     biasAccArr[l].sum(B);
        //                     System.out.println("Final layer Bias Gradient:\n");
        //                     B.print();
        //                     weightAccArr[l].sum(backpropWeight(B, activations[l]));
        //                     System.out.println("Weight Gradient:\n");
        //                     backpropWeight(B, activations[l]).print();
        //                 } // we are in final layer
        //                 else {
        //                     B = backpropBiasHidden(weights[l + 1], B, activations[l + 1]);
        //                     biasAccArr[l].sum(B);
        //                     System.out.println("Bias Gradient:\n");
        //                     B.print();
        //                     weightAccArr[l].sum(backpropWeight(B, activations[l]));
        //                     System.out.println("Weight Gradient:\n");
        //                     backpropWeight(B, activations[l]).print();
        //                 }
        //             }
        //         }
        //         // update all weights and biases after the minibatch has been processed all the way
        //         weights[0] = update(weights[0], weightAccArr[0]);
        //         weights[1] = update(weights[1], weightAccArr[1]);
        //         biases[0] = update(biases[0], biasAccArr[0]);
        //         biases[1] = update(biases[1], biasAccArr[1]);
        //    }
        //}

        // generate random weights and biases
        Matrix w_1 = genRandomMat(15, 784);
        Matrix b_1 = genRandomMat(15, 1);
        Matrix w_2 = genRandomMat(10, 15);
        Matrix b_2 = genRandomMat(10, 1);

        // batch weights and biases for associated indexing
        Matrix[] weights = {w_1, w_2};
        Matrix[] biases = {b_1, b_2};

        // hold activations for each layer as we make forward passes to hold for backprop
        Matrix[] activations = {null, null, null};

        // get inputs and one hot activation vectors from input file
        List<Matrix> inputs = readAndFill(new File("mnist_train.csv"), 60000);
        Matrix[] acts = getLabelActivations(new File("mnist_train.csv"), 60000);

        System.out.println("Inputs size: " + inputs.size());
        System.out.println("Acts size: " + acts.length);
        // main training loop 
        // for each epoch
        for (int epoch = 1; epoch <= EPOCHS; epoch++) {
            System.out.println("Epoch " + epoch + ":\n");
            // copy of original list so we do not shuffle the original list (not sure if this is really necessary, will do more testing later)
            List<Matrix> inputCopy = inputs;
            // randomize and rebatch training data at the beginning of each epoch for SGD
            Matrix[][] batches = batch(inputCopy, MINIBATCHES, MINIBATCHSIZE);
            // for each minibatch
            for (Matrix[] minibatch : batches) {
                // error accumulators
                Matrix weightAcc1 = new Matrix(w_1.getRows(), w_1.getCols());
                Matrix weightAcc2 = new Matrix(w_2.getRows(), w_2.getCols());
                Matrix biasAcc1 = new Matrix(b_1.getRows(), b_1.getCols());
                Matrix biasAcc2 = new Matrix(b_2.getRows(), b_2.getCols());

                // for each input in minibatch
                for (Matrix mat : minibatch) {

                }
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
                acc.setData(i, j, acc.getData()[i][j] * (ETA / MINIBATCHES));
            }
        }
        // new weights/biases are the difference of the scaled accumulator, which is in theory the sum of errors
        // across the training cases 
        return old.subtract(acc);
    }

    // reference: https://www.baeldung.com/java-read-input-character for Scanner logic
    // another reference: https://docs.oracle.com/javase/8/docs/api/java/util/Scanner.html to see if we can pass a File object 
    // a method to read in from a given file and return an array of all inputs
    public static List<Matrix> readAndFill(File fileName, int fileSize) {
        // array to hold all inputs 
        List<Matrix> inputs = new ArrayList<>();
        // try to open the file 
        try (Scanner scanner = new Scanner(fileName)) {
            // iterate through every line of the file 
            for (int i = 0; i < fileSize; i++) {
                Matrix mat = new Matrix(784, 1);
                if (scanner.hasNextLine()) {
                    String[] vals = scanner.nextLine().split(",");
                    for (int j = 1; j < vals.length; j++) {
                        mat.setData(j - 1, 0, Double.parseDouble(vals[j - 1]));
                    }
                    inputs.add(mat);
                }
            }
            return inputs;
        }
        catch (FileNotFoundException e) {
            return null;
        }
    }

    // a method to convert labels of MNIST data into one hot "vectors"
    public static Matrix[] getLabelActivations(File fileName, int fileSize) {
        Matrix[] acts = new Matrix[fileSize];
        try (Scanner scanner = new Scanner(fileName)) {
            for (int i = 0; i < acts.length; i++) {
                Matrix mat = new Matrix(10, 1);
                if (scanner.hasNext()) {
                    String label = scanner.nextLine().split(",")[0];
                    mat.setData(Integer.parseInt(label), 0, 1.0);
                }
                acts[i] = mat;
            }
            return acts;
        }
        catch (FileNotFoundException e) {
            return null;
        }
    }

    // reference: https://www.geeksforgeeks.org/java/generating-random-numbers-in-java/
    // reference: https://docs.vultr.com/java/standard-library/java/lang/Math/random (for range scaling)
    // a method to generate random weights and biases for training
    public static Matrix genRandomMat(int m, int n) {
        Matrix mat = new Matrix(m, n);
        for (int i = 0; i < mat.getRows(); i++) {
            for (int j = 0; j < mat.getCols(); j++) {
                // range scaling so we can actually generate negative doubles < 1 (check reference)
                // java has no built in method that does this 
                mat.setData(i, j, Math.random() * 2 - 1);
            }
        }
        return mat;
    }

    public static Matrix[][] batch(List<Matrix> inputs, int miniNum, int miniSize) {
        Matrix[][] minibatches = new Matrix[miniNum][miniSize];
        Collections.shuffle(inputs);
        int index = 0;
        for (int i = 0; i < minibatches.length; i++) {
            for (int j = 0; j < minibatches[i].length; j++) {
                minibatches[i][j] = inputs.get(index);
                index++;
            }
        }
        return minibatches;
    }

    // works under the assumptiont hat we are just looking at 10x1 matrices
    public static int argmax(Matrix mat) {
        int max = mat.getData()[0][0];
        for (int i = 1; i < mat.getRows(); i++) {
            if (max < mat.getData()[i][0]) {
                max = mat.getData()[i][0];
            }
        }
        return max;
    }
}
