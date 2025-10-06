
public class Main {
    public static void main(String[] args) {
        
    }

    // activation function 
    public static float sigmoid(float z) {
        return (float) (1 / (1 + Math.pow(Math.E, -z)));
    }
}