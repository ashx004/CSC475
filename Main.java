public class Main {
    public static void main(String[] args) {
        Matrix mat = new Matrix(3, 4);
        Matrix other = new Matrix(3, 4);
        Matrix added = mat.add(other);
        if (added != null) {
            float[][] mat2 = added.getData();
            for (int i = 0; i < mat2.length; i++) {
                System.out.println("This is the ");
                for (int j = 0; j < mat2[i].length; j++) {
                    System.out.println(mat2[i][j]);
                }
            }
        }
        else {
            System.out.println(added);
        }
    }
}