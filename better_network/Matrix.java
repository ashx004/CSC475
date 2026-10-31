public class Matrix {
    private final int rows;
    private final int cols;
    private final double[][] data;

    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.data = new double[this.rows][this.cols];
    }

    // getters
    public int getRows() {
      return this.rows;
    }

    public int getCols() {
      return this.cols;
    }

    public double[][] getData() {
      return this.data;
    }

    public void setDataAt(int row, int col, double value) {
      this.data[row][col] = value;
    }

    public Matrix add(Matrix other) {
      if (this.getRows() != other.getRows() || this.getCols() != other.getCols()) {
        throw new IllegalArgumentException("Matrices must be the same dimension to be added");
      }

      Matrix sumMatrix = new Matrix(this.rows, this.cols);
      for (int i = 0; i < this.rows; i++) {
        for (int j = 0; j < this.cols; j++) {
          double sum = this.data[i][j] + other.getData()[i][j];
          sumMatrix.setDataAt(i, j, sum);
        }
      }

      return sumMatrix;
    }

    public Matrix subtract(Matrix other) {
      if (this.rows != other.getRows() || this.cols != other.getCols()) {
        throw new IllegalArgumentException("Matrices must be the same dimensions to be subtracted");
      }

      Matrix differenceMatrix = new Matrix(this.rows, this.cols);
      for (int i = 0; i < this.rows; i++) {
        for (int j = 0; j < this.cols; j++) {
          double difference = this.data[i][j] - other.getData()[i][j];
          differenceMatrix.setDataAt(i, j, difference);
        }
      }

      return differenceMatrix;
    }

    public Matrix multiply(Matrix other) {
      if (this.rows != other.getCols()) {
        throw new IllegalArgumentException("Cannot multiply matrices with invalid dimensions");
      }

      Matrix productMatrix = new Matrix(this.getRows(), other.getCols());
      for (int i = 0; i < this.rows; i++) {
        for (int j = 0; j < other.getCols(); j++) {
          double result = 0;
          for (int k = 0; k < this.rows; k++) {
            result += this.data[i][k] * other.getData()[k][j];
          }

          productMatrix.setDataAt(i, j, result);
        }
      }

      return productMatrix;
    }

    public Matrix hadamardProduct(Matrix other) {
      if (this.rows != other.getRows() || this.cols != other.getCols()) {
        throw new IllegalArgumentException("Matrices must be the same dimensions for the Hadamard product");
      }

      Matrix hadamardMatrix = new Matrix(this.rows, this.cols);
      for (int i = 0; i < this.rows; i++) {
        for (int j = 0; j < this.cols; j++) {
          double product = this.data[i][j] * other.getData()[i][j];
          hadamardMatrix.setDataAt(i, j, product);
        }
      }

      return hadamardMatrix;
    }

    public Matrix transpose() {
      Matrix transposedMatrix = new Matrix(this.cols, this.rows);
      for (int i = 0; i < this.cols; i++) {
        for (int j = 0; j < this.rows; j++) {
          transposedMatrix.setDataAt(i, j, this.data[j][i]);
        }
      }

      return transposedMatrix;
    }
}
