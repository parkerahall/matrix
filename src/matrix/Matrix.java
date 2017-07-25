package matrix;

import java.util.Map;
import java.util.Set;

/*
 * Representation of a mathematical matrix parameterized by the type of its entry.sz
 */
public interface Matrix<R> {
    
    /**
     * Returns the element at the specified location
     * @param row nonnegative integer value
     * @param column nonnegative integer value
     * @return element at Matrix[row, column]
     * @throws IllegalArgumentException if row or column out of range
     */
    public R getElement(int row, int column) throws IndexOutOfBoundsException;
    
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
    public R[] getRow(int row) throws IndexOutOfBoundsException;
    
    /**
     * Returns the desired column in array form
     * @param column nonnegative index of desired column
     * @return double array containing column elements in order
     * @throws IllegalArgumentException if column index out of range
     */
    public R[] getColumn(int column) throws IndexOutOfBoundsException;
    
    /**
     * adds the two matrices together
     * @param matr valid Matrix instance
     * @return sum of two matrices
     * @throws IllegalArgumentException if dimensions are different
     */
    public Matrix<R> add(Matrix<R> matr) throws IncompatibleDimensionsException;
    
    /**
     * subtracts the two matrices (this - matr)
     * @param matr valid Matrix instance
     * @return difference of two matrices
     * @throws IllegalArgumentException if dimensions are different
     */
    public Matrix<R> subtract(Matrix<R> matr) throws IncompatibleDimensionsException;
    
    /**
     * multiplies the two matrices together (this x matr)
     * @param matr valid Matrix instance
     * @return product of two matrices
     * @throws IllegalArgumentException if dimensions are incompatible
     */
    public Matrix<R> multiply(Matrix<R> matr) throws IncompatibleDimensionsException;
    
    /**
     * multiplies all elements in matrix by element
     * @param element
     * @return new Matrix instance
     */
    public Matrix<R> multiply(double element);
    
    /**
     * reduces the matrix to row echelon form
     * @return new Matrix instance
     */
    public Matrix<R> rref();
    
    /**
     * calculates the determinant of the matrix (AKA the signed n-dimensional volume)
     * @return the determinant of this
     * @throws IllegalArgumentException if the matrix is not square
     */
    public R determinant() throws IncompatibleDimensionsException;
    
    /**
     * returns the minor of the given location (cofactor)
     * @param row row index of the cofactor
     * @param column column index of the cofactor
     * @return minor of the cofactor
     * @throws IndexOutOfBoundsException if the indices are out of range
     * @throws IncompatibleDimensionsException if the matrix is not square
     */
    public Matrix<R> minor(int row, int column) throws IndexOutOfBoundsException, IncompatibleDimensionsException;
    
    /**
     * @return the rank of the matrix
     */
    public int rank();
    
    /**
     * @return the nullity of the matrix
     */
    public int nullity();
    
    /**
     * for matrix A, return A^(-1) if it exists, such that AA^(-1) = A^(-1)A = I
     * @return the inverse of the matrix
     * @throws IllegalArgumentException if the matrix is not invertible
     */
    public Matrix<R> inverse() throws IncompatibleDimensionsException;
    
    /**
     * computes the nullspace of the matrix, the set of vectors v such that Av = 0
     * @return a set of linearly independent column vectors (n x 1 matrices) that span the nullspace
     */
    public Set<Matrix<R>> nullspace();
    
    /**
     * computes the transpose of the matrix A, A^T
     * @return the tranpsoe of the matrix
     */
    public Matrix<R> transpose();
    
    /**
     * Calculate the eigenvalues of this, ie
     * the values for lambda where Av = lambda * v
     * @return an array of Complex representations of the eigenvalues
     * @throws IncompatibleDimensionsException if this not square
     */
    public Complex[] eigenvalues() throws IncompatibleDimensionsException ;
    
    /**
     * Calculate all eigenvalues and eigenvectors for this
     * @return mapping of eigenvalues to a set of vectors representing a basis for its eigenspace
     * @throws IncompatibleDimensionsException if this not square
     */
    public Map<Complex, Set<Matrix<Complex>>> eigenMap() throws IncompatibleDimensionsException;
    
    /**
     * Compute the eigenvectors for the specified eigenvalue, ie
     * the vectors v such that Av = eigenvalue * v
     * @param eigenvalue must be an eigenvalue of this
     * @return an array of eignevectors of this corresponding to eigenvalue
     * @throws IncompatibleDimensionsException if this not square
     */
    public Set<Matrix<R>> eigenvectors(Complex eigenvalue) throws IncompatibleDimensionsException ;
    
    /**
     * checks whether the row contains only zeros
     * @return false if row only contains zero, true otherwise
     */
    public boolean rowNotZero(int row);
    
    /**
     * stacks the two matrices on top of each other
     * @param bottom matrix to become the bottom rows
     * @return the result of stacking this on top of bottom
     * @throws IncompatibleDimensionsException if the two matrices have different numbers of columns
     */
    public Matrix<R> stack(Matrix<R> bottom) throws IncompatibleDimensionsException;
}

class IncompatibleDimensionsException extends IndexOutOfBoundsException {
    
    private static final long serialVersionUID = 1L;
    
    public IncompatibleDimensionsException() {
        super();
    }
    
    public IncompatibleDimensionsException(String message) {
        super(message);
    }
}
