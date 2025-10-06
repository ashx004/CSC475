/*
 * Name: Ashton Harrell
 * Date: 10/3/2025
 * Description: Small neural network training 
 */


public class Main {
    // learning rate 
    private final static int LR = 10;
    public static void main(String[] args) {
        // create weights and biases
        Matrix w_1 = new Matrix(3, 4);
        Matrix w_2 = new Matrix(2, 3);
        Matrix b_1 = new Matrix(3, 1);
        Matrix b_2 = new Matrix(2, 1);

        // fill weights and biases
        w_1.setData(0, 0, -0.21f);
        w_1.setData(0, 1, 0.72f);
        w_1.setData(0, 2, 0.25f);
        w_1.setData(0, 3, 1f);

        w_1.setData(1, 0,-0.94f);
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
    }

    // activation function 
    public static float sigmoid(float z) {
        return (float) (1 / (1 + Math.pow(Math.E, -z)));
    }
}