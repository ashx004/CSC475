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

    public void add(Matrix other) {
      if (this.getRows() != other.getRows() || this.getCols() != other.getCols()) {
        throw new RuntimeException("Matrices must be the same dimension to be added");
      }

      for (int i = 0; i < getRows(); i++) {
        for (int j = 0; j < getCols(); j++) {
          double sum = this.data[i][j] + other.getData()[i][j];
          this.setDataAt(i, j, sum);
        }
      }
    }
}
