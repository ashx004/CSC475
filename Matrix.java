// a matrix has rows and columns, and can do addition and multiplication with other matrices
public class Matrix {
    private final float[][] data;
    // use these to be able to do addition and multiplication operations 
    private final int length; // represents how many subarrays are in the array 
    private final int depth; // represents how many elements are in a subarray

    // ctor
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

    // given two indices and a value, replace the value at (i, j) with the new value given 
    public void setData(int i, int j, float value) {
        data[i][j] = value;
    }

    // a method to multiply two matrices and return their product as a new Matrix
    public Matrix multiply(Matrix other) {
        // length of first must correspond to the depth of the second 
        if (this.getDepth() != other.getLength()) {
            return null;
        } 
        else {
            // new matrix becomes an a x b, where a is the length of the first matrix and b is the depth of the second matrix
            Matrix mat = new Matrix(this.getLength(), other.getDepth());
            // i: iterate across rows 
            for (int i = 0; i < this.getLength(); i++) {
                // j: iterate across cols
                for (int j = 0; j < other.getDepth(); j++) {
                    float sum = 0;
                    // k: index to track correllated values 
                    // take dot product of row (i) with corresponding column (j), where k is the index of the elements at each corresponding row or column
                    for (int k = 0; k < this.getDepth(); k++) {
                        sum += this.getData()[i][k] * other.getData()[k][j];
                    }
                    mat.setData(i, j, sum);
                }
            }
            return mat;
        }
    }

    // a method to add two matrices and return their sum as a new Matrix
    public Matrix add(Matrix other) {
        // matrices must both be n x m, where n is length and m is depth
        // if both dimensions dont respectively match, the operation is not defined 
        if (this.length != other.length || this.depth != other.depth) {
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
