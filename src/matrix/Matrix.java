package matrix;

/*
 * Representation of a mathematical matrix with real entries.
 */
public interface Matrix {
    
    /**
     * Returns the element at the specified location
     * @param row nonnegative integer value
     * @param column nonnegative integer value
     * @return element at Matrix[row, column]
     * @throws IllegalArgumentException if row or column out of range
     */
    public double getElement(int row, int column) throws IllegalArgumentException;
    
    /**
     * @return a two-element array: {number rows, number columns}
     */
    public int[] size();
    
    /**
     * Returns the desired row in array form
     * @param row nonnegative index of desired row
     * @return double array containing row elements in order
     * @throws IllegalArgumentException if row index out of range
     */
    public double[] getRow(int row) throws IllegalArgumentException;
    
    /**
     * Returns the desired column in array form
     * @param column nonnegative index of desired column
     * @return double array containing column elements in order
     * @throws IllegalArgumentException if column index out of range
     */
    public double[] getColumn(int column) throws IllegalArgumentException;
    
    /**
     * adds the two matrices together
     * @param matr valid Matrix instance
     * @return sum of two matrices
     * @throws IllegalArgumentException if dimensions are different
     */
    public Matrix add(Matrix matr) throws IllegalArgumentException;
    
    /**
     * multiplies the two matrices together (this x matr)
     * @param matr valid Matrix instance
     * @return product of two matrices
     * @throws IllegalArgumentException if dimensions are incompatible
     */
    public Matrix multiply(Matrix matr) throws IllegalArgumentException;
    
    /**
     * multiplies all elements in matrix by element
     * @param element
     * @return new Matrix instance
     */
    public Matrix multiply(double element);
    
    /**
     * reduces the matrix to row echelon form
     * @return new Matrix instance
     */
    public Matrix ref();
    
    /**
     * calculates the determinant of the matrix (AKA the signed n-dimensional volume)
     * @return the determinant of this
     * @throws IllegalArgumentException if the matrix is not square
     */
    public double determinant() throws IllegalArgumentException;
    
    /**
     * @return the rank of the matrix
     */
    public int rank();
    
    /**
     * @return the nullity of the matrix
     */
    public int nullity();
}
