public class Main {
    public static void main(String[] args) {
        Matrix mat = new Matrix(2, 4);
        int counter = 0;
        for (int i = 0; i < mat.getLength(); i++) {
            for (int j = 0; j < mat.getDepth(); j++) {
                counter += 2;
                mat.setData(i, j, counter);
            }
        }
        Matrix other = new Matrix(3, 2);
        for (int i = 0; i < other.getLength(); i++) {
            for (int j = 0; j < other.getDepth(); j++) {
                counter += 2;
                other.setData(i, j, counter);
            }
        }
        Matrix added = mat.add(other);
        if (added != null) {
            for (int i = 0; i < mat.getLength(); i++) {
                for (int j = 0; j < mat.getDepth(); j++) {
                    System.out.println(mat.getData()[i][j] + " " + other.getData()[i][j] + " " + added.getData()[i][j]);
                }
                System.out.println();
            }
        }
        else {
            System.out.println("didnt work!");
        }
    }
}