// a Matrix has rows and columns, and can do addition, multiplication, subtraction 
// hadamard multiplication with other matrices, and can transpose
public class Matrix {
    private final double[][] data;
    // use these to be able to do addition and multiplication operations easier
    // later
    private final int rows; // represents how many subarrays are in the array
    private final int cols; // represents how many elements are in a subarray
    // ctor

    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        data = new double[rows][cols];
    }

    public double[][] getData() {
        return this.data;
    }

    public int getRows() {
        return this.rows;
    }

    public int getCols() {
        return this.cols;
    }

    // given two indices and a value, replace the value at (i, j) with the new value
    public void setData(int i, int j, double value) {
        data[i][j] = value;
    }

    // a method to multiply two matrices and return their product as a new Matrix
    // reference: https://en.wikipedia.org/wiki/Matrix_multiplication_algorithm
    public Matrix multiply(Matrix other) {
        // rows of first must correspond to the depth of the second
        if (this.cols != other.rows) {
            return null;
        } 
        else {
            // new matrix becomes an a x b, where a is the rows of the first matrix and b is
            // the depth of the second matrix
            Matrix mat = new Matrix(this.rows, other.cols);
            // i: iterate across rows
            for (int i = 0; i < this.rows; i++) {
                // j: iterate across cols
                for (int j = 0; j < other.cols; j++) {
                    float sum = 0;
                    // k: index to track correllated values
                    // take dot product of row (i) with corresponding column (j), where k is the
                    // index of the elements at each corresponding row or column
                    for (int k = 0; k < this.cols; k++) {
                        sum += this.data[i][k] * other.data[k][j];
                    }
                    mat.setData(i, j, sum);
                }
            }
            return mat;
        }
    }

    // a method to add two matrices and return their sum as a new Matrix
    public Matrix add(Matrix other) {
        // matrices must both be n x m, where n is rows and m is depth
        // if both dimensions dont respectively match, the operation is not defined
        if (this.rows != other.rows || this.cols != other.cols) {
            return null;
        } 
        else {
            Matrix matrix = new Matrix(this.rows, this.cols);
            // go row by row and add each element at position (i, j) and place it in the new
            // matrix we are creating at (i, j)
            for (int i = 0; i < this.getRows(); i++) {
                for (int j = 0; j < this.getCols(); j++) {
                    matrix.data[i][j] = this.data[i][j] + other.data[i][j];
                }
            }
            return matrix;
        }
    }

    // a method to subtract two matrices and return their difference as a new Matrix
    // exact same logic as add() with obvious difference 
    public Matrix subtract(Matrix other) {
        if (this.rows != other.rows || this.cols != other.cols) {
            return null;
        } 
        else {
            Matrix matrix = new Matrix(this.rows, this.cols);
            for (int i = 0; i < this.getRows(); i++) {
                for (int j = 0; j < this.getCols(); j++) {
                    matrix.data[i][j] = this.data[i][j] - other.data[i][j];
                }
            }
            return matrix;
        }
    }

    // a method that returns a transposed matrix
    public Matrix transpose() {
        // new Matrix that reverses the shape of the original
        Matrix mat = new Matrix(this.cols, this.rows);
        // this literally flips the orientation that we navigate through the matrix
        for (int j = 0; j < this.cols; j++) {
            for (int i = 0; i < this.rows; i++) {
                mat.data[j][i] = this.data[i][j];
            }
        }
        return mat;
    }

    // a method that performs hadamard multiplication between two matrices
    public Matrix hadamard(Matrix other) {
        if (this.rows != other.rows || this.cols != other.cols) {
            return null;
        } 
        else {
            Matrix mat = new Matrix(this.rows, this.cols);
            for (int i = 0; i < this.rows; i++) {
                for (int j = 0; j < this.cols; j++) {
                    mat.data[i][j] = this.data[i][j] * other.data[i][j];
                }
            }
            return mat;
        }
    }

    // a method to print the contents of a Matrix (use only for debugging and testing)
    public void print() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(data[i][j] + " ");
            }
            System.out.println();
        }
    }
}
