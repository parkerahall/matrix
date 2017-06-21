package matrix;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
 * Implementation of Matrix with nonzero dimensions.
 */

public class ArrayMatrix implements Matrix {
    
    private final static BigDecimal ERROR = new BigDecimal(Math.pow(10, -15));
    
    private final BigDecimal[][] matrix;
    private final int numRows;
    private final int numCols;

    public ArrayMatrix(double[][] entries) {
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
    
    public ArrayMatrix(int[][] entries) {
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
    
    public ArrayMatrix(BigDecimal[][] entries) {
        matrix = new BigDecimal[entries.length][entries[0].length];
        for (int row = 0; row < entries.length; row++) {
            BigDecimal[] currentRow = entries[row];
            for (int column = 0; column < currentRow.length; column++) {
                matrix[row][column] = currentRow[column].add(BigDecimal.ZERO);
            }
        }
        numRows = entries.length;
        numCols = entries[0].length;
    }
    
    public ArrayMatrix(List<List<Double>> entries) {
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
    
    public static Matrix identity(int size) {
        double[][] newMatrix = new double[size][size];
        for (int i = 0; i < size; i++) {
            newMatrix[i][i] = 1;
        }
        return new ArrayMatrix(newMatrix);
    }
    
    @Override
    public BigDecimal getElement(int row, int column) throws IllegalArgumentException {
        if (row < 0 || row >= numRows || column < 0 || column >= numCols) {
            throw new IllegalArgumentException("Location out of bounds");
        }
        return matrix[row][column].add(BigDecimal.ZERO);
    }

    @Override
    public int[] size() {
        int[] dimensions = {numRows, numCols};
        return dimensions;
    }

    @Override
    public BigDecimal[] getRow(int row) throws IllegalArgumentException {
        if (row < 0 || row >= numRows) {
            throw new IllegalArgumentException("Row index out of bounds");
        }
        
        BigDecimal[] currentRow = matrix[row];
        BigDecimal[] copyRow = new BigDecimal[numCols];
        for (int i = 0; i < numCols; i++) {
            copyRow[i] = currentRow[i].add(BigDecimal.ZERO);
        }
        return copyRow;
    }

    @Override
    public BigDecimal[] getColumn(int column) throws IllegalArgumentException {
        if (column < 0 || column >= numCols) {
            throw new IllegalArgumentException("Column index out of bounds");
        }
        
        BigDecimal[] copyColumn = new BigDecimal[numRows];
        for (int i = 0; i < numRows; i++) {
            copyColumn[i] = matrix[i][column].add(BigDecimal.ZERO);
        }
        return copyColumn;
    }

    @Override
    public Matrix add(Matrix matr) throws IllegalArgumentException {
        int[] thisSize = size();
        int[] thatSize = matr.size();
        if (thisSize[0] != thatSize[0] || thisSize[1] != thatSize[1]) {
            throw new IllegalArgumentException("Invalid dimensions for addition");
        }
        
        BigDecimal[][] newMatrix = new BigDecimal[thisSize[0]][thisSize[1]];
        for (int row = 0; row < thisSize[0]; row++) {
            for (int column = 0; column < thisSize[1]; column++) {
                newMatrix[row][column] = getElement(row, column).add(matr.getElement(row, column));
            }
        }
        
        return new ArrayMatrix(newMatrix);
    }
    
    @Override
    public Matrix subtract(Matrix matr) throws IllegalArgumentException {
        int[] thisSize = size();
        int[] thatSize = matr.size();
        if (thisSize[0] != thatSize[0] || thisSize[1] != thatSize[1]) {
            throw new IllegalArgumentException("Invalid dimensions for addition");
        }
        
        BigDecimal[][] newMatrix = new BigDecimal[thisSize[0]][thisSize[1]];
        for (int row = 0; row < thisSize[0]; row++) {
            for (int column = 0; column < thisSize[1]; column++) {
                newMatrix[row][column] = getElement(row, column).subtract(matr.getElement(row, column));
            }
        }
        
        return new ArrayMatrix(newMatrix);
    }

    @Override
    public Matrix multiply(Matrix matr) throws IllegalArgumentException {
        int[] thisSize = size();
        int[] thatSize = matr.size();
        if (thisSize[1] != thatSize[0]) {
            throw new IllegalArgumentException("Invalid dimensions for multiplication");
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
        
       return new ArrayMatrix(newMatrix);
    }

    @Override
    public Matrix multiply(double element) {
        BigDecimal[][] newMatrix = new BigDecimal[matrix.length][matrix[0].length];
        
        for (int row = 0; row < numRows; row++) {
            BigDecimal[] currentRow = matrix[row];
            for (int column = 0; column < numCols; column++) {
                BigDecimal currentElement = currentRow[column];
                newMatrix[row][column] = currentElement.multiply(new BigDecimal(element));
            }
        }
        
        return new ArrayMatrix(newMatrix);
    }

    @Override
    public Matrix rref() {
        return rrefAndPseudoInverse()[0];
    }

    @Override
    public int rank() {
        int numNonzeroRows = 0;
        Matrix ref = rref();
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
        int numColumns = size()[1];
        int rank = rank();
        return numColumns - rank;
    }
    
    @Override
    /**
     * @return a String representation of the matrix in the following form:
     *      1 0 0
     *      0 1 0
     *      0 0 1
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
     */
    @Override
    public boolean equals(Object that) {
        if (!(that instanceof Matrix)) return false;
        Matrix thatMat = (Matrix)that;
        
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
                    System.err.println(thisElt);
                    System.err.println(thatElt);
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
    public BigDecimal determinant() throws IllegalArgumentException {
        if (numRows != numCols) {
            throw new IllegalArgumentException("Determinant not defined for non-square matrix");
        }
        
        if (numCols == 1) {
            return getElement(0,0);
        }
        
        BigDecimal[] firstRow = getRow(0);
        BigDecimal determinant = BigDecimal.ZERO;
        for (int i = 0; i < numRows; i++) {
            BigDecimal cofactor = firstRow[i];
            BigDecimal sign = new BigDecimal(Math.pow(-1, i));
            
            Matrix minor = minor(0, i);
            BigDecimal minorDet = minor.determinant();
            
            determinant = determinant.add(sign.multiply(cofactor.multiply(minorDet)));
        }
        
        return determinant;
    }
    
    @Override
    public Matrix minor(int row, int column) throws IllegalArgumentException {
        if (numRows != numCols) {
            throw new IllegalArgumentException("Matrix needs to be square");
        }
        
        if (row >= numRows || column >= numCols) {
            throw new IllegalArgumentException("Indices out of range");
        }
        
        BigDecimal[][] minorMatrix = new BigDecimal[numRows - 1][numCols - 1];
        
        int currentRow = 0;
        for (int rowIndex = 0; rowIndex < numRows; rowIndex++) {
            if (rowIndex != row) {
                int currentCol = 0;
                for (int colIndex = 0; colIndex < numCols; colIndex++) {
                    if (colIndex != column) {
                        minorMatrix[currentRow][currentCol] = matrix[rowIndex][colIndex].add(BigDecimal.ZERO);
                        currentCol++;
                    }
                }
                currentRow++;
            }
        }
        
        return new ArrayMatrix(minorMatrix);
    }
    
    @Override
    public Matrix inverse() throws IllegalArgumentException {
        if (numRows != numCols) {
            throw new IllegalArgumentException("Inverse not defined for non-square matrix");
        }
        
        BigDecimal determinant = determinant();
        if (determinant.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("Inverse not defined for matrices with determinant of zero");
        }
        
        return rrefAndPseudoInverse()[1];
    }

    @Override
    public Set<Matrix> nullspace() {
        //perform same column operations on matrix and identity simultaneously, similarly to inverse
        //any zero columns in augmented matrix correspond to columns in augmented id that are in nullspace of matrix
        
        //construct transpose of matrix
        ArrayMatrix transpose = (ArrayMatrix)transpose();
        
        //find ref and inverse of transpose
        Matrix[] matrices = transpose.rrefAndPseudoInverse();
        Matrix rref = matrices[0];
        Matrix inverse = matrices[1];
        
        int[] dimensions = rref.size();
        
        //any zero rows in ref correspond to the transpose of nullspace vectors in inverse
        Set<Matrix> nullspace = new HashSet<>();
        for (int row = 0; row < dimensions[1]; row++) {
            if (!rref.rowNotZero(row)) {
                BigDecimal[] nullArr = inverse.getRow(row);
                BigDecimal[][] colArr = new BigDecimal[dimensions[1]][1];
                for (int columnIndex = 0; columnIndex < dimensions[1]; columnIndex++) {
                    colArr[columnIndex][0] = nullArr[columnIndex].add(BigDecimal.ZERO);
                }
                Matrix columnVec = new ArrayMatrix(colArr);
                nullspace.add(columnVec);
            }
        }
        
        return nullspace;
    }
    
    @Override
    public Matrix transpose() {
        BigDecimal[][] transposeArr = new BigDecimal[numCols][numRows];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                transposeArr[j][i] = matrix[i][j].add(BigDecimal.ZERO);
            }
        }
        return new ArrayMatrix(transposeArr);
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
     * @param row array of doubles
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
     * @return an two-element array of Matrix objects
     *          the first element is A in rref, while the second element is the result of performing
     *          the same row operations on I
     */
    private Matrix[] rrefAndPseudoInverse() {
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
                newMatrix[row][column] = getElement(row, column).add(BigDecimal.ZERO);
            }
        }
        
        //perform row swaps for each column
        for (int columnCheck = 0; columnCheck < numCols; columnCheck++) {
           
            //find any row with the nonzero value in this column
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
            if (!ArrayMatrix.rowNotZero(currentRow)) {
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
        
        Matrix rref = new ArrayMatrix(newMatrix);
        Matrix pseudoId = new ArrayMatrix(id);
        Matrix[] output = {rref, pseudoId};
        return output;
    }
    
    public static void main(String args[]) {
        double[] row1 = {1, 0, 0};
        double[] row2 = {1, 1, 1};
        double[] row3 = {0, 0, 1};
        double[][] matrix = {row1, row2, row3};
        Matrix check = new ArrayMatrix(matrix);
        System.out.println(check.inverse());
    }
}
