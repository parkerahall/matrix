package matrix;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
 * Implementation of Matrix with nonzero dimensions.
 */

public class BigDecimalMatrix implements Matrix<BigDecimal> {
    
    private final static BigDecimal ERROR = new BigDecimal(Math.pow(10, -15));
    private final static int RREF_INDEX = 0;
    private final static int INV_INDEX = 1;
    
    private final BigDecimal[][] matrix;
    private final int numRows;
    private final int numCols;

    public BigDecimalMatrix(double[][] entries) {
        matrix = new BigDecimal[entries.length][entries[0].length];
        for (int row = 0; row < entries.length; row++) {
            double[] currentRow = entries[row];
            for (int column = 0; column < currentRow.length; column++) {
                matrix[row][column] = new BigDecimal(currentRow[column]);
            }
        }
        numRows = entries.length;
        numCols = entries[0].length;
    }
    
    public BigDecimalMatrix(int[][] entries) {
        matrix = new BigDecimal[entries.length][entries[0].length];
        for (int row = 0; row < entries.length; row++) {
            int[] currentRow = entries[row];
            for (int column = 0; column < currentRow.length; column++) {
                matrix[row][column] = new BigDecimal(currentRow[column]);
            }
        }
        numRows = entries.length;
        numCols = entries.length;
    }
    
    public BigDecimalMatrix(BigDecimal[][] entries) {
        matrix = new BigDecimal[entries.length][entries[0].length];
        for (int row = 0; row < entries.length; row++) {
            BigDecimal[] currentRow = entries[row];
            for (int column = 0; column < currentRow.length; column++) {
                matrix[row][column] = currentRow[column];
            }
        }
        numRows = entries.length;
        numCols = entries[0].length;
    }
    
    public BigDecimalMatrix(List<List<Double>> entries) {
        matrix = new BigDecimal[entries.size()][entries.get(0).size()];
        for (int row = 0; row < entries.size(); row++) {
            List<Double> currentRow = entries.get(row);
            for (int column = 0; column < currentRow.size(); column++) {
                matrix[row][column] = new BigDecimal(currentRow.get(column));
            }
        }
        numRows = entries.size();
        numCols = entries.get(0).size();
    }
    
    public static Matrix<BigDecimal> identity(int size) {
        double[][] newMatrix = new double[size][size];
        for (int i = 0; i < size; i++) {
            newMatrix[i][i] = 1;
        }
        return new BigDecimalMatrix(newMatrix);
    }
    
    @Override
    public BigDecimal getElement(int row, int column) throws IndexOutOfBoundsException {
        if (row < 0 || row >= numRows || column < 0 || column >= numCols) {
            throw new IndexOutOfBoundsException("Location out of bounds");
        }
        return matrix[row][column];
    }

    @Override
    public int[] size() {
        int[] dimensions = {numRows, numCols};
        return dimensions;
    }

    @Override
    public BigDecimal[] getRow(int row) throws IndexOutOfBoundsException {
        if (row < 0 || row >= numRows) {
            throw new IndexOutOfBoundsException("Row index out of bounds");
        }
        
        BigDecimal[] currentRow = matrix[row];
        BigDecimal[] copyRow = new BigDecimal[numCols];
        for (int i = 0; i < numCols; i++) {
            copyRow[i] = currentRow[i];
        }
        return copyRow;
    }

    @Override
    public BigDecimal[] getColumn(int column) throws IndexOutOfBoundsException {
        if (column < 0 || column >= numCols) {
            throw new IndexOutOfBoundsException("Column index out of bounds");
        }
        
        BigDecimal[] copyColumn = new BigDecimal[numRows];
        for (int i = 0; i < numRows; i++) {
            copyColumn[i] = matrix[i][column];
        }
        return copyColumn;
    }

    @Override
    public Matrix<BigDecimal> add(Matrix<BigDecimal> matr) throws IncompatibleDimensionsException {
        int[] thisSize = size();
        int[] thatSize = matr.size();
        if (thisSize[0] != thatSize[0] || thisSize[1] != thatSize[1]) {
            throw new IncompatibleDimensionsException("Invalid dimensions for addition");
        }
        
        BigDecimal[][] newMatrix = new BigDecimal[thisSize[0]][thisSize[1]];
        for (int row = 0; row < thisSize[0]; row++) {
            for (int column = 0; column < thisSize[1]; column++) {
                newMatrix[row][column] = getElement(row, column).add(matr.getElement(row, column));
            }
        }
        
        return new BigDecimalMatrix(newMatrix);
    }
    
    @Override
    public Matrix<BigDecimal> subtract(Matrix<BigDecimal> matr) throws IncompatibleDimensionsException {
        int[] thisSize = size();
        int[] thatSize = matr.size();
        if (thisSize[0] != thatSize[0] || thisSize[1] != thatSize[1]) {
            throw new IncompatibleDimensionsException("Invalid dimensions for addition");
        }
        
        BigDecimal[][] newMatrix = new BigDecimal[thisSize[0]][thisSize[1]];
        for (int row = 0; row < thisSize[0]; row++) {
            for (int column = 0; column < thisSize[1]; column++) {
                newMatrix[row][column] = getElement(row, column).subtract(matr.getElement(row, column));
            }
        }
        
        return new BigDecimalMatrix(newMatrix);
    }

    @Override
    public Matrix<BigDecimal> multiply(Matrix<BigDecimal> matr) throws IncompatibleDimensionsException {
        int[] thisSize = size();
        int[] thatSize = matr.size();
        if (thisSize[1] != thatSize[0]) {
            throw new IncompatibleDimensionsException("Invalid dimensions for multiplication");
        }
        
        BigDecimal[][] newMatrix = new BigDecimal[thisSize[0]][thatSize[1]];
        for (int row = 0; row < thisSize[0]; row++) {
            BigDecimal[] currentRow = matrix[row];
            for (int column = 0; column < thatSize[1]; column++) {
                BigDecimal[] currentColumn = matr.getColumn(column);
                BigDecimal dotProduct = BigDecimal.ZERO;
                for (int i = 0; i < thisSize[1]; i++) {
                    BigDecimal partial = currentRow[i].multiply(currentColumn[i]);
                    dotProduct = dotProduct.add(partial);
                }
                newMatrix[row][column] = dotProduct;
            }
        }
        
       return new BigDecimalMatrix(newMatrix);
    }

    @Override
    public Matrix<BigDecimal> multiply(double element) {
        BigDecimal[][] newMatrix = new BigDecimal[matrix.length][matrix[0].length];
        
        for (int row = 0; row < numRows; row++) {
            BigDecimal[] currentRow = matrix[row];
            for (int column = 0; column < numCols; column++) {
                BigDecimal currentElement = currentRow[column];
                newMatrix[row][column] = currentElement.multiply(new BigDecimal(element));
            }
        }
        
        return new BigDecimalMatrix(newMatrix);
    }

    @Override
    public Matrix<BigDecimal> rref() {
        return rrefAndPseudoInverse().get(RREF_INDEX);
    }

    @Override
    public int rank() {
        int numNonzeroRows = 0;
        Matrix<BigDecimal> ref = rref();
        for (int row = 0; row < ref.size()[0]; row++) {
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
        int rank = rank();
        return numCols - rank;
    }
    
    @Override
    /**
     * @return a String representation of the matrix in the following form:
     *      1   0   0
     *      0   1   0
     *      0   0   1
     */
    public String toString() {
        String grid = "";
        for (int row = 0; row < numRows; row++) {
            BigDecimal[] currentRow = matrix[row];
            for (int column = 0; column < numCols; column++) {
                String currentElt = currentRow[column].toString();
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
     * is equal up to an error of 10^(-15)
     * this and that must have both have BigDecimal elements
     */
    @Override
    public boolean equals(Object that) {
        if (!(that instanceof Matrix)) return false;
        // warnings suppressed as javadoc requires that to be Matrix<BigDecimal>
        @SuppressWarnings("unchecked")
        Matrix<BigDecimal> thatMat = (Matrix<BigDecimal>)that;
        
        int[] thisDim = size();
        int[] thatDim = thatMat.size();
        if (thisDim[0] != thatDim[0] || thisDim[1] != thatDim[1]) {
            return false;
        }
        
        for (int i = 0; i < thisDim[0]; i++) {
            for (int j = 0; j < thisDim[1]; j++) {
                BigDecimal thisElt = getElement(i, j);
                BigDecimal thatElt = thatMat.getElement(i, j);
                if (thisElt.subtract(thatElt).abs().compareTo(ERROR) == 1) {
                    return false;
                }
            }
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        double sum = 0;
        
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                sum += getElement(i, j).doubleValue();
            }
        }
        
        return (int)sum % 10000;
    }
    
    @Override
    public BigDecimal determinant() throws IncompatibleDimensionsException {
        if (numRows != numCols) {
            throw new IncompatibleDimensionsException("Determinant not defined for non-square matrix");
        }
        
        if (numCols == 1) {
            return getElement(0,0);
        }
        
        BigDecimal[] firstRow = getRow(0);
        BigDecimal determinant = BigDecimal.ZERO;
        for (int i = 0; i < numRows; i++) {
            BigDecimal cofactor = firstRow[i];
            BigDecimal sign = new BigDecimal(Math.pow(-1, i));
            
            Matrix<BigDecimal> minor = minor(0, i);
            BigDecimal minorDet = minor.determinant();
            
            determinant = determinant.add(sign.multiply(cofactor.multiply(minorDet)));
        }
        
        return determinant;
    }
    
    @Override
    public Matrix<BigDecimal> minor(int row, int column) throws IndexOutOfBoundsException, IncompatibleDimensionsException {
        if (numRows != numCols) {
            throw new IncompatibleDimensionsException("Matrix needs to be square");
        }
        
        if (row >= numRows || column >= numCols || row < 0 || column < 0) {
            throw new IndexOutOfBoundsException("Indices out of range");
        }
        
        BigDecimal[][] minorMatrix = new BigDecimal[numRows - 1][numCols - 1];
        
        int currentRow = 0;
        for (int rowIndex = 0; rowIndex < numRows; rowIndex++) {
            if (rowIndex != row) {
                int currentCol = 0;
                for (int colIndex = 0; colIndex < numCols; colIndex++) {
                    if (colIndex != column) {
                        minorMatrix[currentRow][currentCol] = matrix[rowIndex][colIndex];
                        currentCol++;
                    }
                }
                currentRow++;
            }
        }
        
        return new BigDecimalMatrix(minorMatrix);
    }
    
    @Override
    public Matrix<BigDecimal> inverse() throws IncompatibleDimensionsException {
        if (numRows != numCols) {
            throw new IncompatibleDimensionsException("Inverse not defined for non-square matrix");
        }
        
        BigDecimal determinant = determinant();
        if (determinant.compareTo(BigDecimal.ZERO) == 0) {
            throw new IncompatibleDimensionsException("Inverse not defined for matrices with determinant of zero");
        }
        
        return rrefAndPseudoInverse().get(INV_INDEX);
    }

    @Override
    public Set<Matrix<BigDecimal>> nullspace() {
        //perform same column operations on matrix and identity simultaneously, similarly to inverse
        //any zero columns in augmented matrix correspond to columns in augmented id that are in nullspace of matrix
        
        //construct transpose of matrix
        BigDecimalMatrix transpose = (BigDecimalMatrix)transpose();
        
        //find ref and inverse of transpose
        List<Matrix<BigDecimal>> matrices = transpose.rrefAndPseudoInverse();
        Matrix<BigDecimal> rref = matrices.get(RREF_INDEX);
        Matrix<BigDecimal> inverse = matrices.get(INV_INDEX);
        
        int[] dimensions = rref.size();
        
        //any zero rows in ref correspond to the transpose of nullspace vectors in inverse
        Set<Matrix<BigDecimal>> nullspace = new HashSet<>();
        for (int row = 0; row < dimensions[1]; row++) {
            if (!rref.rowNotZero(row)) {
                BigDecimal[] nullArr = inverse.getRow(row);
                BigDecimal[][] colArr = new BigDecimal[dimensions[1]][1];
                for (int columnIndex = 0; columnIndex < dimensions[1]; columnIndex++) {
                    colArr[columnIndex][0] = nullArr[columnIndex];
                }
                Matrix<BigDecimal> columnVec = new BigDecimalMatrix(colArr);
                nullspace.add(columnVec);
            }
        }
        
        return nullspace;
    }
    
    @Override
    public Matrix<BigDecimal> transpose() {
        BigDecimal[][] transposeArr = new BigDecimal[numCols][numRows];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                transposeArr[j][i] = matrix[i][j];
            }
        }
        return new BigDecimalMatrix(transposeArr);
    }
    
    @Override
    public boolean rowNotZero(int row) {
        BigDecimal[] currentRow = matrix[row];
        for (BigDecimal elt: currentRow) {
            if (elt.compareTo(BigDecimal.ZERO) != 0) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * checks whether row contains nonzero values
     * @param row array of BigDecimals
     * @return false if row only contains 0, true otherwise
     */
    private static boolean rowNotZero(BigDecimal[] row) {
        for (BigDecimal elt: row) {
            if (elt.compareTo(BigDecimal.ZERO) != 0) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * while reducing matrix A to rref, perform all necessary row operations on identity matrix I
     * @return an two-element list of Matrix objects
     *          the first element is A in rref, while the second element is the result of performing
     *          the same row operations on I
     */
    private List<Matrix<BigDecimal>> rrefAndPseudoInverse() {
        //perform same operations as ref, while copying row operations to identity matrix of same size
        BigDecimal[][] id = new BigDecimal[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (i == j) {
                    id[i][j] = BigDecimal.ONE;
                } else {
                    id[i][j] = BigDecimal.ZERO;
                }
            }
        }
        
        BigDecimal[][] newMatrix = new BigDecimal[numRows][numCols];
        for (int row = 0; row < numRows; row++) {
            for (int column = 0; column < numCols; column++) {
                newMatrix[row][column] = getElement(row, column);
            }
        }
        
        //perform row swaps for each column
        for (int columnCheck = 0; columnCheck < numCols; columnCheck++) {
           
            //find any row with a nonzero value in this column
            BigDecimal valueCheck = BigDecimal.ZERO;
            BigDecimal[] lowestRow = new BigDecimal[numCols];
            int index = columnCheck - 1;
            while (valueCheck.compareTo(BigDecimal.ZERO) == 0 && index < numRows - 1) {
                index++;
                lowestRow = newMatrix[index];
                valueCheck = lowestRow[columnCheck];
            }
            if (valueCheck.compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }
            
            //retrieve same information from id matrix
            BigDecimal[] idRow = id[index];
            
            //simplify row so that first element is 1
            BigDecimal criticalElt = valueCheck;
            for (int j = 0; j < lowestRow.length; j++) {
                lowestRow[j] = new BigDecimal(lowestRow[j].doubleValue() / criticalElt.doubleValue());
                idRow[j] = new BigDecimal(idRow[j].doubleValue() / criticalElt.doubleValue());
            }
            
            //use simplified row to reduce rest of matrix
            for (int row = 0; row < numRows; row++) {
                if (row != index) {
                    BigDecimal[] rowToBeReduced = newMatrix[row];
                    BigDecimal entryFactor = rowToBeReduced[columnCheck];
                    
                    BigDecimal[] idTBR = new BigDecimal[numCols];
                    for (int zero = 0; zero < numCols; zero++) {
                        idTBR[zero] = BigDecimal.ZERO;
                    }
                    if (row < numCols) {
                        idTBR = id[row];
                    }
                    
                    for (int j = 0; j < numCols; j++) {
                        rowToBeReduced[j] = rowToBeReduced[j].subtract(entryFactor.multiply(lowestRow[j]));
                        idTBR[j] = idTBR[j].subtract(entryFactor.multiply(idRow[j]));
                    }
                }
            }
            
            //swap rows
            BigDecimal[] tmp = newMatrix[columnCheck];
            newMatrix[columnCheck] = lowestRow;
            newMatrix[index] = tmp;
            
            BigDecimal[] idTmp = id[columnCheck];
            id[columnCheck] = idRow;
            id[index] = idTmp;
        }
        
        //move all zero rows to the bottom of the matrix
        for (int i = 0; i < numRows; i++) {
            BigDecimal[] currentRow = newMatrix[i];
            if (!BigDecimalMatrix.rowNotZero(currentRow)) {
                for (int j = i; j < numRows - 1; j++) {
                    BigDecimal[] tmp = newMatrix[j + 1];
                    newMatrix[j + 1] = newMatrix[j];
                    newMatrix[j] = tmp;
                    
                    if (j < numCols) {
                        BigDecimal[] idTmp = id[j + 1];
                        id[j + 1] = id[j];
                        id[j] = idTmp;
                    }
                }
            }
        }
        
        Matrix<BigDecimal> rref = new BigDecimalMatrix(newMatrix);
        Matrix<BigDecimal> pseudoId = new BigDecimalMatrix(id);
        List<Matrix<BigDecimal>> output = new ArrayList<>(Arrays.asList(rref, pseudoId));
        return output;
    }
}
