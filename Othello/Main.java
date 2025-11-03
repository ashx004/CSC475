public class Main {
    public static void main(String[] args) {
        
    }

    // reference: https://stackoverflow.com/questions/10241217/how-to-clear-console-in-java
    // a method to clear the screen (pls allow this is just something dumb)
    public static void clear() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}