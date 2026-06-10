public class Matrix {
  private final double[][] data;
  private final int rows;
  private final int columns;

  public Matrix(int rows, int columns) {
    if (rows <= 0 || columns <= 0) {
      throw new IllegalArgumentException("Cannot instantiate a matrix with negative dimensions!");
    }

    this.rows = rows;
    this.columns = columns;
    this.data = new double[this.rows][this.columns];
  }

  public int getRows() {
    return this.rows;
  }

  public int getColumns() {
    return this.columns;
  }

  public double getDataAt(int i, int j) {
    return this.data[i][j];
  }

  public void setDataAt(int i, int j, double value) {
    if (i >= this.rows || i < 0 || j >= this.columns || j < 0) {
      throw new IllegalArgumentException("Index out of bounds!");
    }

    this.data[i][j] = value;
  }

  // FACTORIES
  public static Matrix zeroesMatrix(int rows, int columns) {
    if (rows < 0 || columns < 0) {
      throw new IllegalArgumentException("Cannot make a matrix of negative size!");
    }

    return new Matrix(rows, columns); 
  }

  public static Matrix onesMatrix(int rows, int columns) {
    if (rows < 0 || columns < 0) {
      throw new IllegalArgumentException("Cannot make a matrix of negative size!");
    }

    Matrix m = new Matrix(rows, columns);
    for (int i = 0; i < m.getRows(); i++) {
      for (int j = 0; j < m.getColumns(); j++) {
        m.setDataAt(i, j, 1.0);
      }
    }
    return m;
  }

  public static Matrix randomMatrix(int rows, int columns) {
    if (rows < 0 || columns < 0) {
      throw new IllegalArgumentException("Cannot make a matrix of negative size!");
    }

    Matrix m = new Matrix(rows, columns);
    for (int i = 0; i < m.getRows(); i++) {
      for (int j = 0; j < m.getColumns(); j++) {
        m.setDataAt(i, j, (Math.random() * 2) - 1);
      }
    }
    return m;
  }

  // METHODS 
  public Matrix addition(Matrix other) {
    if (this.rows != other.getRows() || this.columns != other.getColumns()) {
      throw new IllegalArgumentException("Matrix addition is only defined for matrices of the same dimensions!");
    }

    Matrix m = new Matrix(this.getRows(), this.getColumns());
    for (int i = 0; i < this.getRows(); i++) {
      for (int j = 0; j < this.getColumns(); j++) {
        m.setDataAt(i, j, this.getDataAt(i, j) + other.getDataAt(i, j));
      }
    }
    return m;
  }

  public Matrix multiplication(Matrix other) {
    if (this.columns != other.getRows()) {
      throw new IllegalArgumentException("Matrix multiplication is only defined for (M x N) * (N * K), both N's are equal");
    }

    Matrix m = new Matrix(this.getRows(), other.getColumns());
    for (int i = 0; i < this.getRows(); i++) {
      for (int j = 0; j < other.getColumns(); j++) {
        double sum = 0.0;
        for (int k = 0; k < this.getColumns(); k++) {
          sum += this.getDataAt(i, k) * other.getDataAt(k, j);
        }
        m.setDataAt(i, j, sum);
      }
    }
    return m;
  }

  public Matrix subtract(Matrix other) {
    if (this.rows != other.getRows() || this.columns != other.getColumns()) {
      throw new IllegalArgumentException("Matrix subtraction is only defined for matrices of the same dimensions!");
    }

    Matrix m = new Matrix(this.rows, this.columns);
    for (int i = 0; i < this.rows; i++) {
      for (int j = 0; j < this.columns; j++) {
        m.setDataAt(i, j, this.getDataAt(i, j) - other.getDataAt(i, j));
      }
    }
    return m;
  }

  public Matrix transpose() {
    Matrix m = new Matrix(this.columns, this.rows);
    for (int j = 0; j < this.columns; j++) {
      for (int i = 0; i < this.rows; i++) {
        m.setDataAt(j, i, this.getDataAt(i, j));
      }
    }
    return m;
  }

  public Matrix hadamardMultiplication(Matrix other) {
    if (this.rows != other.getRows() || this.columns != other.getColumns()) {
      throw new IllegalArgumentException("Hadamard multiplication is only defined for matrices of the same dimensions!");
    }

    Matrix m = new Matrix(this.rows, this.columns);
    for (int i = 0; i < this.rows; i++) {
      for (int j = 0; j < this.columns; j++) {
        m.setDataAt(i, j, this.getDataAt(i, j) * other.getDataAt(i, j));
      }
    }
    return m;
  }

  public void scaleBy(double scalar) {
    for (int i = 0; i < this.rows; i++) {
      for (int j = 0; j < this.columns; j++) {
        this.setDataAt(i, j, scalar * this.getDataAt(i, j));
      }
    }
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < this.rows; i++) {
      for (int j = 0; j < this.columns; j++) {
        sb.append(this.getDataAt(i, j) + " ");
      }
      sb.append("\n");
    }
    sb.append("\n");
    return sb.toString();
  }

  public static void main(String[] args) {
    Matrix m1 = Matrix.onesMatrix(4, 3);

    Matrix m2 = Matrix.onesMatrix(3, 4);
    System.out.println("Ones matrix: \n" + m1);
    System.out.println();
    System.out.println(m2);

    Matrix m3 = m1.multiplication(m2);
    System.out.println(m3);
    System.out.println();
    m3.scaleBy(0.0);
    System.out.println(m3);
  }
} 
