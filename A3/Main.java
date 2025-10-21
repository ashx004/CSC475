/*
 * Name: Ashton Harrell
 * Date: 10/13/2025
 * Description: Large Neural Network being trained/tested on the MNIST dataset
 * Neural Network Name (SHOUTOUT DECLAN JOHNSON BOOTH FR ONG): Digit Encoding and Classification Learning Artificial Network (or, D.E.C.L.A.N., for short)
 */

// imports
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.io.IOException;
import java.io.FileWriter;


public class Main {
    // constants for this specific network. if you change these you must take into consideration the methods being called to ensure these constants are being used properly
    // very brittle network ngl lol
    // learning rate 
    private final static double ETA = 2.8;
    // amount of minibatches 
    private final static int MINIBATCHES = 60;
    // use this for gradient calculations later for eta / m 
    private final static int MINIBATCHSIZE = 1000;
    // amount of epochs the data will go through
    private final static int EPOCHS = 35;

    public static void main(String[] args) {
        // flags for our main loop to ensure logical flow is achieved
        boolean hasTrained = false;
        boolean running = true;
        Scanner scanner = new Scanner(System.in);

        // structures to hold networkData with training/testing runs
        List<Matrix[]> networkData = new ArrayList<>();
        Matrix[] weights = {null, null};
        Matrix[] biases = {null, null};

        // main menu loop
        while (running) {
            // main menu text prompts
            System.out.println("1. Train the network");
            System.out.println("2. Load a pre-trained network");
            System.out.println("3. Display network accuracy on training data");
            System.out.println("4. Display network accuracy on testing data");
            System.out.println("5. Run network on testing data showing images and labels");
            System.out.println("6. Display the misclassified testing images");
            System.out.println("7. Save the network state to file");
            System.out.println("0. Exit");
            System.out.println("\n Please enter a number 0-7. 3-7 are only available after using 1. or 2.\n");
            String input = scanner.next();
            System.out.println();

            // process actions based on user input
            switch (input) {
                // user wants to quit
                case "0":
                    System.out.println("\nGoodbye!\n");
                    scanner.close();
                    running = false;
                    break;
                // user wants to train a new network
                case "1":
                    networkData = trainingLoop(EPOCHS, MINIBATCHSIZE, MINIBATCHES, ETA);
                    // toggle this flag so we can do actions 3-7
                    hasTrained = true;
                    break;
                // DONE
                case "2":
                    // user wants to load network data from files in the given directory
                    // works on the assumption that we previously have a file with given weights and biases
                    Matrix[] loadedWeights = loadFromFile(new File("weights.csv"), 2);
                    Matrix[] loadedBiases = loadFromFile(new File("biases.csv"), 2);
                    networkData.add(loadedWeights);
                    networkData.add(loadedBiases);
                    hasTrained = true;
                    break;
                // user wants to test trained/pre-trained network on training data for accuracy 
                case "3":
                    // we can only do any of the cases below if this flag is true (must have data to work with already) 
                    if (hasTrained) {
                        // allocate these arrays to increment indices to show which element has been processed and/or correctly identified
                        // count is for all elements, properCount is for elements that are correctly identified
                        int[] count = new int[10];
                        int[] properCount = new int[10];

                        // grab network data and allocate activations so we can perform forward passes
                        weights = networkData.get(0);
                        biases = networkData.get(1);
                        Matrix[] activations = {null, null, null};

                        // grab training data
                        List<Matrix> inputs = readAndFill(new File("mnist_train.csv"), 60000);
                        Matrix[] labels = getLabelActivations(new File("mnist_train.csv"), 60000);
                        
                        // list to hold training data so we can group inputs with their intended labels (similar logic as in batch())
                        List<Matrix[]> list = new ArrayList<>();
                        for (int i = 0; i < inputs.size(); i++) {
                            Matrix[] arr = {inputs.get(i), labels[i]};
                            list.add(arr);
                        }
                        // iterate across training data 
                        for (int i = 0; i < list.size(); i++) {
                            // list.get(i)[0] is the input stored for the current forwardPass
                            activations[0] = list.get(i)[0];
                            // hold index of the final layer activations to be argmax'd and compared with the labelIndex
                            int outputIndex = 0;
                            // standard forwardpass logic
                            for (int l = 0; l < weights.length; l++) {
                                activations[l + 1] = forwardPass(weights[l], activations[l], biases[l]);
                                outputIndex = argmax(activations[l + 1]);
                            }
                            int labelIndex = argmax(list.get(i)[1]);
                            // increment count regardless and increment properCount at the index iff the network got it correct
                            if (outputIndex == labelIndex) {
                                properCount[outputIndex]++;
                            }
                            count[labelIndex]++;
                        }
                        // sum holders for network accuracy calculations
                        double sum = 0;
                        double properSum = 0;
                        for (int i = 0; i < count.length; i++) {
                            sum += count[i];
                            properSum += properCount[i];
                        }
                        // print digit statistics 
                        for (int i = 0; i < count.length; i++) {
                            System.out.println("Digit " + i + ": " + properCount[i] + "/" + count[i]);
                        }
                        // print network statistics
                        System.out.println("Network accuracy: " + (properSum / sum) * 100 + "%\n");
                    }
                    // 
                    else {
                        System.out.println("No network data to use!");
                    }
                    break;

                // user wants to run network on testing data 
                case "4":
                    // exact same logic as case "3", just uses different inputs and labels
                    if (hasTrained) {
                        int[] count = new int[10];
                        int[] properCount = new int[10];
                        weights = networkData.get(0);
                        biases = networkData.get(1);
                        Matrix[] activations = {null, null, null};
                        List<Matrix> inputs = readAndFill(new File("mnist_test.csv"), 10000);
                        Matrix[] labels = getLabelActivations(new File("mnist_test.csv"), 10000);
                        List<Matrix[]> list = new ArrayList<>();
                        for (int i = 0; i < inputs.size(); i++) {
                            Matrix[] arr = {inputs.get(i), labels[i]};
                            list.add(arr);
                        }
                        for (int i = 0; i < list.size(); i++) {
                            activations[0] = list.get(i)[0];
                            int outputIndex = 0;
                            for (int l = 0; l < weights.length; l++) {
                                activations[l + 1] = forwardPass(weights[l], activations[l], biases[l]);
                                outputIndex = argmax(activations[l + 1]);
                            }
                            int labelIndex = argmax(list.get(i)[1]);
                            if (outputIndex == labelIndex) {
                                properCount[outputIndex]++;
                            }
                            count[labelIndex]++;
                        }
                        double sum = 0;
                        double properSum = 0;
                        for (int i = 0; i < count.length; i++) {
                            sum += count[i];
                            properSum += properCount[i];
                        }
                        for (int i = 0; i < count.length; i++) {
                            System.out.println("Digit " + i + ": " + count[i] + "/" + properCount[i]);
                        }
                        System.out.println("\nNetwork accuracy: " + (properSum / sum) * 100 + "%\n");
                    }
                    else {
                        System.out.println("No network data to use!");
                    }
                    break;

                // user wants to see ASCII displays of all images from the testing data
                case "5":
                    if (hasTrained) {
                        // grab normal network testing necessary data
                        weights = networkData.get(0);
                        biases = networkData.get(1);
                        Matrix[] activations = {null, null, null};
                        List<Matrix> inputs = readAndFill(new File("mnist_test.csv"), 10000);
                        Matrix[] labels = getLabelActivations(new File("mnist_test.csv"), 10000);
                        List<Matrix[]> list = new ArrayList<>();

                        // hold this so we do not iterate over our amount of training data. also used for printing stuff
                        int count = 0;
                        for (int i = 0; i < inputs.size(); i++) {
                            Matrix[] arr = {inputs.get(i), labels[i]};
                            list.add(arr);
                        }
                        while (count < 10000) {
                            // flag so we can break out of these loops and switch statement back to the main menu loop 
                            boolean all_running = true;
                            while (all_running) {
                                for (int i = 0; i < list.size(); i++) {
                                    activations[0] = list.get(i)[0];
                                    int outputIndex = 0;
                                    for (int l = 0; l < weights.length; l++) {
                                        activations[l + 1] = forwardPass(weights[l], activations[l], biases[l]);
                                        outputIndex = argmax(activations[l + 1]);
                                    }
                                    count++;
                                    int labelIndex = argmax(list.get(i)[1]);
                                    Matrix mat = convertMatrix(28, 28, activations[0]);
                                    System.out.print("Testing case #" + count + ":\t" + "Correct Classification = " + labelIndex + " Network output = " + outputIndex);
                                    if (outputIndex == labelIndex) {
                                        System.out.println(" Correct.");
                                    }
                                    else {
                                        System.out.println(" Incorrect.");
                                    }
                                    // iterate across the current matrix elements
                                    for (int j = 0; j < mat.getRows(); j++) {
                                        for (int k = 0; k < mat.getCols(); k++) {
                                            // grab the greyscale value we are currently looking at 
                                            double shade = mat.getData()[j][k];
                                            // run it through these conditionals to see what value will be printed for it, based on how close it is to 1 and how dark it should be 
                                            if (shade == 0) {
                                                System.out.print(" ");
                                            }
                                            else if (shade > 0 && shade <= 0.2) {
                                                System.out.print(".");
                                            }
                                            else if (shade > 0.2 && shade <= 0.4) {
                                                System.out.print("!");
                                            }
                                            else if (shade > 0.4 && shade <= 0.6) {
                                                System.out.print("K");
                                            }
                                            else if (shade > 0.6 && shade <= 0.8) {
                                                System.out.print("%");
                                            }
                                            else {
                                                System.out.print("@");
                                            }
                                        }
                                        System.out.println();
                                    }
                                    System.out.println("Enter 1 to continue. All other values go back to the main menu ");
                                    String all_input = scanner.next();
                                    if (!all_input.equals("1")) {
                                        all_running = false;
                                        // break that cascades us out. probably better way to do this but ez to envision
                                        break;
                                    }
                                }
                                break;
                            }
                            break;
                        }
                        break;
                    }
                    break;

                // user wants to see ASCII displays of misclassified images
                case "6":
                    // identical logic
                    if (hasTrained) {
                        weights = networkData.get(0);
                        biases = networkData.get(1);
                        Matrix[] activations = {null, null, null};
                        List<Matrix> inputs = readAndFill(new File("mnist_test.csv"), 10000);
                        Matrix[] labels = getLabelActivations(new File("mnist_test.csv"), 10000);
                        List<Matrix[]> list = new ArrayList<>();
                        int count = 0;

                        // pair inputs and labels
                        for (int i = 0; i < inputs.size(); i++) {
                            Matrix[] arr = {inputs.get(i), labels[i]};
                            list.add(arr);
                        }

                        // main display loop
                        while (count < 10000) {
                            boolean all_running = true;

                            while (all_running) {
                                for (int i = 0; i < list.size(); i++) {
                                    activations[0] = list.get(i)[0];
                                    int outputIndex = 0;

                                    // forward pass
                                    for (int l = 0; l < weights.length; l++) {
                                        activations[l + 1] = forwardPass(weights[l], activations[l], biases[l]);
                                        outputIndex = argmax(activations[l + 1]);
                                    }

                                    count++;
                                    int labelIndex = argmax(list.get(i)[1]);

                                    // only show misclassified images by checking if outputLabel is not equivalent to labelIndex
                                    if (outputIndex != labelIndex) {
                                        Matrix mat = convertMatrix(28, 28, activations[0]);
                                        System.out.print("Testing case #" + count + ":\t" +
                                            "Correct Classification = " + labelIndex +
                                            " Network output = " + outputIndex + " Incorrect.\n");

                                        for (int j = 0; j < mat.getRows(); j++) {
                                            for (int k = 0; k < mat.getCols(); k++) {
                                                double shade = mat.getData()[j][k];
                                                if (shade == 0) {
                                                    System.out.print(" ");
                                                } else if (shade > 0 && shade <= 0.2) {
                                                    System.out.print(".");
                                                } else if (shade > 0.2 && shade <= 0.4) {
                                                    System.out.print("!");
                                                } else if (shade > 0.4 && shade <= 0.6) {
                                                    System.out.print("K");
                                                } else if (shade > 0.6 && shade <= 0.8) {
                                                    System.out.print("%");
                                                } else {
                                                    System.out.print("@");
                                                }
                                            }
                                            System.out.println();
                                        }

                                        System.out.println("Enter 1 to continue. All other values go back to the main menu ");
                                        String all_input = scanner.next();

                                        if (!all_input.equals("1")) {
                                            all_running = false;
                                            break;
                                        }
                                    }
                                }

                                // break out of inner while loop when finished
                                break;
                            }
                            // break out of outer while loop after completion
                            break;
                        }
                    }
                    break;

                // user wants to save network data to file(s)
                case "7":
                    if (hasTrained) {
                        // TODO: save returned weights and biases to a file
                        writeToFile(new File("weights.csv"), weights);
                        writeToFile(new File("biases.csv"), biases);
                    }
                    break;
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

    // a method to return batched data that is organized by (input Matrix, label Matrix) and is shuffled 
    // this is so we dont lose data when we need to randomize the data after every minibatch 
    public static List<List<Matrix[]>> batch(List<Matrix[]> data, int miniNum, int miniSize) {
        List<List<Matrix[]>> batches = new ArrayList<>();
        // randomize data
        Collections.shuffle(data);
        int index = 0;
        for (int i = 0; i < miniNum; i++) {
            List<Matrix[]> minibatch = new ArrayList<>();
            // batching data into minibatches
            for (int j = 0; j < miniSize; j++) {
                minibatch.add(data.get(index));
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
            System.out.println("Accuracy: " + properSum + "/" + countSum + " = " + (properSum / countSum) * 100 + "%\n");
        }
        List<Matrix[]> list = new ArrayList<>();
        // network data that will be important to use for testing and network accuracy use later 
        list.add(weights);
        list.add(biases);
        return list;
    }

    // a method to "convert" a matrix from its current dimensions to new ones 
    public static Matrix convertMatrix(int m, int n, Matrix other) {
        // ensure that the original matrix can be scaled by checking if the division yields no remainder
        if (other.getRows() % m == 0 && other.getRows() % n == 0) {
            int index = 0;
            Matrix mat = new Matrix(m, n);
            for (int i = 0; i < mat.getRows(); i++) {
                for (int j = 0; j < mat.getCols(); j++) {
                    mat.setData(i, j, other.getData()[index][0]);
                    index++;
                }
            }
            return mat;
        }
        // if the new dimensions dont work, we dont want to give something that will break other operations
        // also good indicator for potential debugging
        return null;
    }

    // a method to save network data to a file 
    // reference: File and FileWriter java docs from Oracle
    public static void writeToFile(File fileName, Matrix[] mats) {
        // create the file if it does not already exist
        if (!fileName.exists()) {
            try {
                fileName.createNewFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        // concatenate everything into a big string for each Matrix in mats and then write it to the file
        try (FileWriter writer = new FileWriter(fileName)) {
            String str = new String();
            for (Matrix mat: mats) {
                str += mat.getRows() + "," + mat.getCols() + ",";
                for (int i = 0; i < mat.getRows(); i++) {
                    for (int j = 0; j < mat.getCols(); j++) {
                        str += mat.getData()[i][j];
                        if (i != mat.getRows() * mat.getCols() - 1) {
                            str += ",";
                        }
                    }
                }
                str += "\n";
            }
            writer.write(str);
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // a method to load weights and biases from a given file
    public static Matrix[] loadFromFile(File fileName, int fileSize) {
        Matrix[] mats = new Matrix[fileSize];
        try (Scanner scanner = new Scanner(fileName)) {
        // iterate through every line of the file 
        for (int i = 0; i < fileSize; i++) {
            // very similar logic to readAndFill()
            if (scanner.hasNextLine()) {
                String[] vals = scanner.nextLine().split(",");
                // weights.csv and biases.csv first two values are the rows and cols of the Matrix we need to make. allows us to not have to pass flags on 
                // whether we want to make a weight or bias matrix (bc they are different sizes)
                Matrix mat = new Matrix(Integer.parseInt(vals[0]), Integer.parseInt(vals[1]));
                // start at 2 to offset the actual values we care about pushing
                int index = 2;
                // iterate through the matrix
                for (int j = 0; j < mat.getRows(); j++) {
                    for (int k = 0; k < mat.getCols(); k++) {
                        // convert the value stored at the index to a double
                        mat.setData(j, k, Double.parseDouble(vals[index]));
                        index++;
                    }
                }
                // append it to the returned matrix array
                mats[i] = mat;
            }
        }
        return mats;
        }
        catch (FileNotFoundException e) {
            return null;
        }
    }
}