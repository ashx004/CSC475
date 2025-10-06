/*
 * Name: Ashton Harrell
 * Date: 10/3/2025
 * Description: Small neural network training 
 */


public class Main {
    public static void main(String[] args) {
        
    }

    // activation function 
    public static float sigmoid(float z) {
        return (float) (1 / (1 + Math.pow(Math.E, -z)));
    }
}