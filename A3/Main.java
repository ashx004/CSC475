/*
 * Name: Ashton Harrell
 * Date: 10/13/2025
 * Description: Large Neural Network being trained/tested on the MNIST dataset
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;


public class Main {
    // constants for this specific network
    // learning rate 
    private final static double ETA = 2.8;
    // amount of minibatches 
    private final static int MINIBATCHES = 60;
    // use this for gradient calculations later for eta / m 
    private final static int MINIBATCHSIZE = 1000;
    // amount of epochs the data will go through
    private final static int EPOCHS = 50;

    public static void main(String[] args) {
        boolean running = true;
        boolean hasTrained = false;
        Scanner scanner = new Scanner(System.in);
        while (running) {
            System.out.println("1. Train the network");
            System.out.println("2. Load a pre-trained network");
            System.out.println("3. Display network accuracy on training data");
            System.out.println("4. Display network accuracy on testing data");
            System.out.println("5. Run network on testing data showing images and labels");
            System.out.println("6. Display the misclassified testing images");
            System.out.println("7. Save the network state to file");
            System.out.println("0. Exit");
            System.out.println("\n Please enter a number 0-7. 3-7 are only available after using 1. or 2.");
            String input = scanner.next();
            switch (input) {
                case "0":
                    running = false;
                case "1":
                    List<Matrix[]> networkData = trainingLoop(EPOCHS, MINIBATCHSIZE, MINIBATCHES, ETA);
                    hasTrained = true;
                case "2":
                    // TODO: implement loading/writing to file
                case "3":
                    // TODO: implement network accuracy on training data
                    // in theory, just print the data stored at index 2 in what is returned by 
                case "4":
                    // TODO: implement network accuracy on testing data
                case "5":
                    // TODO: run network on testing data
                    // TODO: implement ASCII art method (somehow with matrix stuff);
                case "6":
                    // TODO: Display misclassified testing images
                case "7":
                    // TODO: save returned weights and biases to a file
            }
        }
        scanner.close();
        // List<Matrix[]> trained = trainingLoop(EPOCHS, MINIBATCHSIZE, MINIBATCHES, ETA);
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
                        mat.setData(j - 1, 0, Double.parseDouble(vals[j]) / 255);
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

    // reworked so that it takes paired up 
    public static List<List<Matrix[]>> batch(List<Matrix[]> pairs, int miniNum, int miniSize) {
        List<List<Matrix[]>> batches = new ArrayList<>();
        Collections.shuffle(pairs);
        int index = 0;
        for (int i = 0; i < miniNum; i++) {
            List<Matrix[]> minibatch = new ArrayList<>();
            for (int j = 0; j < miniSize; j++) {
                minibatch.add(pairs.get(index));
                index++;
            }
            batches.add(minibatch);
        }
        return batches;
    }

    // works under the assumption that we are just looking at 10x1 matrices
    // a method to return what index the maximum value is at in a given matrix 
    public static int argmax(Matrix mat) {
        int index = 0;
        double max = mat.getData()[0][0];
        for (int i = 0; i < mat.getRows(); i++) {
            if (max < mat.getData()[i][0]) {
                max = mat.getData()[i][0];
                index = i;
            }
        }
        return index;
    }

    // main training loop that trains the network on inputs 
    public static List<Matrix[]> trainingLoop(int epochs, int miniNum, int miniSize, double learningRate) {
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
        List<Matrix> inputs = readAndFill(new File("mnist_train.csv"), miniNum * miniSize);
        Matrix[] acts = getLabelActivations(new File("mnist_train.csv"), miniNum * miniSize);

        // associate inputs and activations so that they can be randomized together
        // this is so we can actually classify images properly
        List<Matrix[]> associatedList = new ArrayList<>();
        for (int i = 0; i < inputs.size(); i++) {
            Matrix[] arr = {inputs.get(i), acts[i]};
            associatedList.add(arr);
        }

        // main training loop 
        // for each epoch
        Matrix[] countArr = {null};
        for (int epoch = 1; epoch <= epochs; epoch++) {
        // use these arr to count how many of each digit, and whether it was properly classified
            int[] count = new int[10];
            int[] properCount = new int[10];
            System.out.println("Epoch " + epoch + ":\n");
            // copy of original list so we do not shuffle the original list (not sure if this is really necessary, will do more testing later)
            List<Matrix[]> trainingData = new ArrayList<>(associatedList);
            // randomize and rebatch training data at the beginning of each epoch for SGD
            List<List<Matrix[]>> batches = batch(trainingData, MINIBATCHES, MINIBATCHSIZE);
            // for each minibatch in batches
            for (List<Matrix[]> minibatch : batches) {
                // error accumulators
                Matrix weightAcc1 = new Matrix(w_1.getRows(), w_1.getCols());
                Matrix weightAcc2 = new Matrix(w_2.getRows(), w_2.getCols());
                Matrix biasAcc1 = new Matrix(b_1.getRows(), b_1.getCols());
                Matrix biasAcc2 = new Matrix(b_2.getRows(), b_2.getCols());
                Matrix[] weightAccArr = {weightAcc1, weightAcc2};
                Matrix[] biasAccArr = {biasAcc1, biasAcc2};

                // for each input in minibatch (with its associated expected activation/label)
                int outputIndex = 0;
                for (Matrix[] mat : minibatch) {
                    activations[0] = mat[0];
                    for (int l = 0; l < weights.length; l++) {
                        activations[l + 1] = forwardPass(weights[l], activations[l], biases[l]);
                        outputIndex = argmax(activations[l + 1]);
                    }
                    int labelIndex = argmax(mat[1]);
                    count[labelIndex]++;
                    if (labelIndex == outputIndex) {
                        properCount[outputIndex]++;
                    }
                    Matrix B = null;
                    for (int l = weights.length - 1; l >= 0; l--) {
                        if (l == weights.length - 1) {
                            B = backpropBiasFinal(activations[l + 1], mat[1]);
                            biasAccArr[l].sum(B);
                            weightAccArr[l].sum(backpropWeight(B, activations[l]));
                        }
                        else {
                            B = backpropBiasHidden(weights[l + 1], B, activations[l + 1]);
                            biasAccArr[l].sum(B);
                            weightAccArr[l].sum(backpropWeight(B, activations[l]));
                        }
                    }
                }
                // update weights and biases each minibatch
                weights[0] = update(weights[0], weightAccArr[0]);
                weights[1] = update(weights[1], weightAccArr[1]);
                biases[0] = update(biases[0], biasAccArr[0]);
                biases[1] = update(biases[1], biasAccArr[1]);

            }
            for (int i = 0; i < count.length; i++) {
                System.out.println("Digit " + i + ": " + properCount[i] + " / " + count[i] + "\n");
            }
            double properSum = 0;
            double countSum = 0;
            for (int i = 0; i < count.length; i++) {
                properSum += (double) properCount[i];
                countSum += (double) count[i];
            }
            if (epoch == epochs) {
                countArr[0] = new Matrix(1, 1);
                countArr[0].setData(0, 0, (properSum / countSum) * 100);
            }
            System.out.println("Accuracy: " + properSum + "/" + countSum + " = " + (properSum / countSum) * 100 + "\n");
        }
        List<Matrix[]> list = new ArrayList<>();
        list.add(weights);
        list.add(biases);
        list.add(countArr);
        return list;
    }

    // TODO: implement testing loop logic 
    public static List<Matrix[]> testingLoop(List<Matrix[]> networkData, File fileName, ) {

    }
}
