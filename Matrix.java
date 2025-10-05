// a matrix has rows and columns, and can do addition and multiplication with other matrices
public class Matrix {
    private final float[][] data;
    private final int length;
    private final int depth;

    public Matrix(int rows, int cols) {
        this.length = rows;
        this.depth = cols;
        data = new float[rows][cols];
    }

    public float[][] getData() {
        return this.data;
    }

    public int getLength() {
        return this.length;
    }

    public int getDepth() {
        return this.depth;
    }

    public Matrix multiply(Matrix other) {
        if (data.length != other.depth) {
            return null;
        }
        else {
            Matrix matrix = new Matrix(depth, other.length);
            for (int i = 0; i < data.length; i++) {
                int k, j = 0;

            }
        }
    }

    // a method to add two matrices and return their sum as a new Matrix
    public Matrix add(Matrix other) {
        // matrices must both be n x m, where n is rows and m is cols
        if (this.length != other.length && this.depth != other.depth) {
            return null;
        } 
        else {
            Matrix matrix = new Matrix(this.length, this.depth);
            // go row by row and add each element at position (i, j) and place it in the new matrix we are creating at (i, j)
            for (int i = 0; i < this.data.length; i++) {
                for (int j = 0; j < this.data[i].length; j++) {
                    matrix.data[i][j] = this.data[i][j] + other.data[i][j];
                }
            }
            return matrix;
        }
    }
}
