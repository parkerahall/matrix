package matrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ComplexMatrix implements Matrix<Complex> {

    private final static double ERROR = Math.pow(10, -15);
    private final static int RREF_INDEX = 0;
    private final static int INV_INDEX = 1;
    private final static Complex ZERO = new Complex(0, 0);
    private final static Complex ONE = new Complex(1, 0);
    
    private final static List<List<Complex>> matrix = new ArrayList<>();
    private final int numRows;
    private final int numCols;
    
    /**
     * Create a matrix from a two-dimensional list
     * @param grid must be nonempty
     */
    public ComplexMatrix(List<List<Complex>> grid) {
        numRows = grid.size();
        numCols = grid.get(0).size();
        for (int row = 0; row < numRows; row++) {
            List<Complex> newRow = new ArrayList<>();
            for (int column = 0; column < numCols; column++) {
                newRow.add(grid.get(row).get(column));
            }
            matrix.add(newRow);
        }
    }
    
    /**
     * Create a matrix from a two-dimensional array
     * @param grid must be nonempty
     */
    public ComplexMatrix(Complex[][] grid) {
        numRows = grid.length;
        numCols = grid[0].length;
        for (int row = 0; row < numRows; row++) {
            List<Complex> newRow = new ArrayList<>();
            for (int column = 0; column < numCols; column++) {
                newRow.add(grid[row][column]);
            }
            matrix.add(newRow);
        }
    }
    
    @Override
    public Complex getElement(int row, int column) throws IndexOutOfBoundsException {
        return matrix.get(row).get(column);
    }

    @Override
    public int[] size() {
        int[] size = {numRows, numCols};
        return size;
    }

    @Override
    public Complex[] getRow(int row) throws IndexOutOfBoundsException {
        Complex[] outRow = new Complex[numCols];
        List<Complex> realRow = matrix.get(row);
        for (int i = 0; i < numCols; i++) {
            outRow[i] = realRow.get(i);
        }
        return outRow;
    }

    @Override
    public Complex[] getColumn(int column) throws IndexOutOfBoundsException {
        Complex[] outCol = new Complex[numRows];
        for (int i = 0; i < numRows; i++) {
            outCol[i] = matrix.get(i).get(column);
        }
        return outCol;
    }

    @Override
    public Matrix<Complex> add(Matrix<Complex> matr) throws IncompatibleDimensionsException {
        int[] thisSize = this.size();
        int[] thatSize = matr.size();
        if (thisSize[0] != thatSize[0] || thisSize[1] != thatSize[1]) {
            throw new IncompatibleDimensionsException("Matrix dimensions are incompatible");
        }
        
        List<List<Complex>> newGrid = new ArrayList<>();
        for (int row = 0; row < numRows; row++) {
            List<Complex> newRow = new ArrayList<>();
            for (int col = 0; col < numCols; col++) {
                Complex thisElt = this.getElement(row, col);
                Complex thatElt = matr.getElement(row, col);
                newRow.add(thisElt.add(thatElt));
            }
            newGrid.add(newRow);
        }
        
        return new ComplexMatrix(newGrid);
    }

    @Override
    public Matrix<Complex> subtract(Matrix<Complex> matr) throws IncompatibleDimensionsException {
        int[] thisSize = this.size();
        int[] thatSize = matr.size();
        if (thisSize[0] != thatSize[0] || thisSize[1] != thatSize[1]) {
            throw new IncompatibleDimensionsException("Matrix dimensions are incompatible");
        }
        
        List<List<Complex>> newGrid = new ArrayList<>();
        for (int row = 0; row < numRows; row++) {
            List<Complex> newRow = new ArrayList<>();
            for (int col = 0; col < numCols; col++) {
                Complex thisElt = this.getElement(row, col);
                Complex thatElt = matr.getElement(row, col);
                newRow.add(thisElt.sub(thatElt));
            }
            newGrid.add(newRow);
        }
        
        return new ComplexMatrix(newGrid);
    }

    @Override
    public Matrix<Complex> multiply(Matrix<Complex> matr) throws IncompatibleDimensionsException {
        int[] thisSize = this.size();
        int[] thatSize = matr.size();
        if (thisSize[0] != thatSize[1] || thisSize[1] != thatSize[0]) {
            throw new IncompatibleDimensionsException("Matrix dimensions are incompatible");
        }
        
        List<List<Complex>> newGrid = new ArrayList<>();
        for (int row = 0; row < numRows; row++) {
            List<Complex> newRow = new ArrayList<>();
            Complex[] currentRow = this.getRow(row);
            for (int column = 0; column < thatSize[1]; column++) {
                Complex[] currentColumn = matr.getColumn(column);
                Complex dotProduct = new Complex(0,0);
                for (int i = 0; i < numCols; i++) {
                    Complex partial = currentRow[i].mult(currentColumn[i]);
                    dotProduct = dotProduct.add(partial);
                }
                newRow.add(dotProduct);
            }
            newGrid.add(newRow);
        }
        
        return new ComplexMatrix(newGrid);
    }

    @Override
    public Matrix<Complex> multiply(double element) {
        Complex product = new Complex(element, 0);
        
        List<List<Complex>> newGrid = new ArrayList<>();
        for (int row = 0; row < numRows; row++) {
            List<Complex> newRow = new ArrayList<>();
            for (int col = 0; col < numCols; col++) {
                newRow.add(this.getElement(row, col).mult(product));
            }
            newGrid.add(newRow);
        }
        
        return new ComplexMatrix(newGrid);
    }

    @Override
    public Matrix<Complex> rref() {
        return this.rrefAndPseudoInverse().get(RREF_INDEX);
    }

    @Override
    public Complex determinant() throws IncompatibleDimensionsException {
        if (numRows != numCols) {
            throw new IncompatibleDimensionsException("Determinant not defined for non-square matrix");
        }
        
        if (numCols == 1) {
            return this.getElement(0, 0);
        }
        
        Complex[] firstRow = this.getRow(0);
        Complex determinant = new Complex(0, 0);
        for (int i = 0; i < numRows; i++) {
            Complex cofactor = firstRow[i];
            Complex sign = new Complex(Math.pow(-1, i), 0);
            
            Matrix<Complex> minor = this.minor(0, i);
            Complex minorDet = minor.determinant();
            
            determinant = determinant.add(sign.mult(cofactor.mult(minorDet)));
        }
        
        return determinant;
    }

    @Override
    public Matrix<Complex> minor(int row, int column)
            throws IndexOutOfBoundsException, IncompatibleDimensionsException {
        if (numRows != numCols) {
            throw new IncompatibleDimensionsException("Matrix needs to be square");
        }
        
        if (row >= numRows || column >= numCols || row < 0 || column < 0) {
            throw new IndexOutOfBoundsException("Indices out of range");
        }
        
        List<List<Complex>> newGrid = new ArrayList<>();
        for (int rowIndex = 0; rowIndex < numRows; rowIndex++) {
            if (rowIndex != row) {
                List<Complex> newRow = new ArrayList<>();
                for (int colIndex = 0; colIndex < numCols; colIndex++) {
                    if (colIndex != column) {
                        newRow.add(this.getElement(rowIndex, colIndex));
                    }
                }
                newGrid.add(newRow);
            }
        }
        
        return new ComplexMatrix(newGrid);
    }

    @Override
    public int rank() {
        int numNonzeroRows = 0;
        Matrix<Complex> ref = this.rref();
        for (int row = 0; row < numRows; row++) {
            if (ref.rowNotZero(row)) {
                numNonzeroRows += 1;
            } else {
                return numNonzeroRows;
            }
        }
        return numNonzeroRows;
    }

    @Override
    public int nullity() {
        int rank = this.rank();
        return numCols - rank;
    }

    @Override
    public Matrix<Complex> inverse() throws IncompatibleDimensionsException {
        if (numRows != numCols) {
            throw new IncompatibleDimensionsException("Inverse not defined for non-square matrix");
        }
        
        Complex determinant = this.determinant();
        if (determinant.equals(ZERO)) {
            throw new IncompatibleDimensionsException("Inverse not defined for matrices with determinant of zero");
        }
        
        return this.rrefAndPseudoInverse().get(INV_INDEX);
    }

    @Override
    public Set<Matrix<Complex>> nullspace() {
        ComplexMatrix transpose = (ComplexMatrix)this.transpose();
        
        List<Matrix<Complex>> matrices = transpose.rrefAndPseudoInverse();
        Matrix<Complex> rref = matrices.get(RREF_INDEX);
        Matrix<Complex> inverse = matrices.get(INV_INDEX);
        
        int[] dimensions = rref.size();
        
        Set<Matrix<Complex>> nullspace = new HashSet<>();
        for (int row = 0; row < dimensions[1]; row++) {
            if (!rref.rowNotZero(row)) {
                Complex[] nullArr = inverse.getRow(row);
                List<List<Complex>> colList = new ArrayList<>();
                for (int columnIndex = 0; columnIndex < dimensions[1]; columnIndex++) {
                    List<Complex> newRow = new ArrayList<>();
                    newRow.add(nullArr[columnIndex]);
                    colList.add(newRow);
                }
                Matrix<Complex> columnVec = new ComplexMatrix(colList);
                nullspace.add(columnVec);
            }
        }
        
        return nullspace;
    }

    @Override
    public Matrix<Complex> transpose() {
        List<List<Complex>> newGrid = new ArrayList<>();
        for (int col = 0; col < numRows; col++) {
            List<Complex> newRow = new ArrayList<>();
            for (int row = 0; row < numCols; row++) {
                newRow.add(this.getElement(col, row));
            }
            newGrid.add(newRow);
        }
        
        return new ComplexMatrix(newGrid);
    }

    @Override
    public boolean rowNotZero(int row) {
        Complex[] currentRow = this.getColumn(row);
        for (Complex elt: currentRow) {
            if (!elt.equals(ZERO)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    /**
     * @return a String representation of the matrix in the following form:
     * a + bi   c + di
     * e + fi   g + hi
     */
    public String toString() {
        String grid = "";
        for (int row = 0; row < numRows; row++) {
            for (int column = 0; column < numCols; column++) {
                String currentElt = this.getElement(row, column).toString();
                if (column == numCols - 1) {
                    grid += currentElt + "\n";
                } else {
                    grid += currentElt + "\t";
                }
            }
        }
        return grid;
    }
    
    /**
     * two matrices are considered equivalent if their dimensions are the same, and each element
     * is equal up to an error of 10^(-15) each for the real and imaginary parts
     * this and that must have both have Complex elements
     */
    @Override
    public boolean equals(Object that) {
        if (!(that instanceof Matrix)) return false;
        // warnings suppressed as javadoc requires that to be Matrix<Complex>
        @SuppressWarnings("unchecked")
        Matrix<Complex> thatMat = (Matrix<Complex>)that;
        
        int[] thisDim = this.size();
        int[] thatDim = thatMat.size();
        if (thisDim[0] != thatDim[0] || thisDim[1] != thatDim[1]) {
            return false;
        }
        
        for (int i = 0; i < thisDim[0]; i++) {
            for (int j = 0; j < thisDim[1]; j++) {
                Complex thisElt = this.getElement(i, j);
                Complex thatElt = thatMat.getElement(i, j);
                if (Math.abs(thisElt.getReal() - thatElt.getReal()) > ERROR ||
                        Math.abs(thisElt.getImag() - thatElt.getImag()) > ERROR) {
                    return false;
                }
            }
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        Complex sum = ZERO;
        
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                sum = sum.add(this.getElement(i, j));
            }
        }
        return sum.hashCode();
    }
    
    /**
     * checks whether row contains nonzero values
     * @param row array of Complex numbers
     * @return false if row only contains 0, true otherwise
     */
    private static boolean rowIsZero(List<Complex> row) {
        for (Complex elt: row) {
            if (!elt.equals(ZERO)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * while reducing matrix A to rref, perform all necessary row operations on identity matrix I
     * @return an two-element list of Matrix objects
     *          the first element is A in rref, while the second element is the result of performing
     *          the same row operations on I
     */
    private List<Matrix<Complex>> rrefAndPseudoInverse() {
        //perform same operations as ref, while copying row operations to identity matrix of same size
        Complex[][] id = new Complex[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (i == j) {
                    id[i][j] = ONE;
                } else {
                    id[i][j] = ZERO;
                }
            }
        }
        
        List<List<Complex>> newMatrix = new ArrayList<>();
        for (int row = 0; row < numRows; row++) {
            List<Complex> newRow = new ArrayList<>();
            for (int col = 0; col < numCols; col++) {
                newRow.add(this.getElement(row, col));
            }
            newMatrix.add(newRow);
        }
        
        // perform row swaps for each column
        for (int columnCheck = 0; columnCheck < numCols; columnCheck++) {
            
            // find any row with a nonzero value in this column
            Complex valueCheck = ZERO;
            Complex[] lowestRow = new Complex[numCols];
            int index = columnCheck - 1;
            while (valueCheck.equals(ZERO) && index < numRows - 1) {
                index++;
                lowestRow = this.getRow(index);
                valueCheck = lowestRow[columnCheck];
            }
            if (valueCheck.equals(ZERO)) {
                continue;
            }
            
            // retrieve same information from id matrix
            Complex[] idRow = id[index];
            
            // simplify row so that first element is 1
            Complex criticalElt = valueCheck;
            for (int j = 0; j < lowestRow.length; j++) {
                try {
                    lowestRow[j] = lowestRow[j].div(criticalElt);
                    idRow[j] = idRow[j].div(criticalElt);
                } catch (ZeroDenominatorException zde) {
                    System.err.println("Denominator is zero. This should never happen.");
                }
            }
            
            // use simplified row ot reduce rest of matrix
            for (int row = 0; row < numRows; row++) {
                if (row != index) {
                    List<Complex> rowToBeReduced = newMatrix.get(row);
                    Complex entryFactor = rowToBeReduced.get(columnCheck);
                    
                    Complex[] idTBR = new Complex[numCols];
                    for (int zero = 0; zero < numCols; zero++) {
                        idTBR[zero] = ZERO;
                    }
                    if (row < numCols) {
                        idTBR = id[row];
                    }
                    
                    for (int j = 0; j < numCols; j++) {
                        Complex temp = rowToBeReduced.get(j).sub(entryFactor.mult(lowestRow[j]));
                        rowToBeReduced.set(j, temp);
                        idTBR[j] = idTBR[j].sub(entryFactor.mult(idRow[j]));
                    }
                }
            }
            
            // swap rows
            List<Complex> tmp = newMatrix.get(columnCheck);
            newMatrix.set(columnCheck, Arrays.asList(lowestRow));
            newMatrix.set(index, tmp);
            
            Complex[] idTmp = id[columnCheck];
            id[columnCheck] = idRow;
            id[index] = idTmp;
        }
        
        // move all zero rows to the bottom of the matrix
        for (int i = 0; i < numRows; i++) {
            List<Complex> currentRow = newMatrix.get(i);
            if(ComplexMatrix.rowIsZero(currentRow)) {
                for (int j = i; j < numRows; j++) {
                    List<Complex> tmp = newMatrix.get(j + 1);
                    newMatrix.set(j + 1, newMatrix.get(j));
                    newMatrix.set(j, tmp);
                    
                    if (j < numCols) {
                        Complex[] idTmp = id[j + 1];
                        id[j + 1] = id[j];
                        id[j] = idTmp;
                    }
                }
            }
        }
        
        Matrix<Complex> rref = new ComplexMatrix(newMatrix);
        Matrix<Complex> pseudoId = new ComplexMatrix(id);
        List<Matrix<Complex>> output = new ArrayList<>(Arrays.asList(rref, pseudoId));
        return output;
    }
}