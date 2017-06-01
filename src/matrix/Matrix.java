package matrix;

import java.util.List;
import java.util.Map;

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
    public Double[] size();
    
    /**
     * Returns the desired row in array form
     * @param row nonnegative index of desired row
     * @return double array containing row elements in order
     * @throws IllegalArgumentException if row index out of range
     */
    public Double[] getRow(int row) throws IllegalArgumentException;
    
    /**
     * Returns the desired column in array form
     * @param column nonnegative index of desired column
     * @return double array containing column elements in order
     * @throws IllegalArgumentException if column index out of range
     */
    public Double[] getColumn(int column) throws IllegalArgumentException;
    
    /**
     * adds the two matrices together
     * @param matr valid Matrix instance
     * @return sum of two matrices
     * @throws IllegalArgumentException if dimensions are different
     */
    public Matrix add(Matrix matr) throws IllegalArgumentException;
    
    /**
     * multiplies the two matrices together
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
     * calculates the real eigenvalues and eigenvectors of the matrix, if any exist
     * @return a mapping of eigenvalues to their corresponding eigenvectors (represented as single-column matrices)
     */
    public Map<Double, List<Matrix>> eigen();
    
    /**
     * @return the rank of the matrix
     */
    public int rank();
    
    /**
     * @return the nullity of the matrix
     */
    public int nullity();
}
