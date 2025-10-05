// a matrix has rows and columns, and can do addition and multiplication with other matrices
public class Matrix {
    private final float[][] data;
    // use these to be able to do addition and multiplication operations 
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

    // given two indices and a value, replace the value at (i, j) with the new value given 
    public void setData(int i, int j, float value) {
        data[i][j] = value;
    }

    // TODO: get working 
    public Matrix multiply(Matrix other) {
        // length of first must correspond to the depth of the second 
        if (this.getLength() != other.getDepth()) {
            return null;
        }
        else {
            // given A is an z x a, and b is a:
            // new matrix becomes an a x b, where a is the depth of the first matrix and b is the length of the second matrix
            Matrix mat = new Matrix(this.getDepth(), other.getLength());
            // iterate across rows 
            for (int i = 0; i < this.getLength(); i++) {
                // iterate across cols
                for (int j = 0; j < mat.getDepth(); j++) {
                    int sum = 0;
                    for (int k = 0; k < this.getDepth(); k++) {
                        sum += this.getData()[i][j] * other.getData()[k][j];
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
