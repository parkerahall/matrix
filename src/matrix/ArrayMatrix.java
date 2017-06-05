package matrix;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
 * Implementation of Matrix with nonzero dimensions.
 */

public class ArrayMatrix implements Matrix {
    
    private final double[][] matrix;

    public ArrayMatrix(double[][] entries) {
        matrix = new double[entries.length][entries[0].length];
        for (int row = 0; row < entries.length; row++) {
            double[] currentRow = entries[row];
            for (int column = 0; column < currentRow.length; column++) {
                matrix[row][column] = currentRow[column];
            }
        }
    }
    
    public ArrayMatrix(int[][] entries) {
        matrix = new double[entries.length][entries[0].length];
        for (int row = 0; row < entries.length; row++) {
            int[] currentRow = entries[row];
            for (int column = 0; column < currentRow.length; column++) {
                matrix[row][column] = currentRow[column];
            }
        }
    }
    
    public ArrayMatrix(List<List<Double>> entries) {
        matrix = new double[entries.size()][entries.get(0).size()];
        for (int row = 0; row < entries.size(); row++) {
            List<Double> currentRow = entries.get(row);
            for (int column = 0; column < currentRow.size(); column++) {
                matrix[row][column] = currentRow.get(column);
            }
        }
    }
    
    public static Matrix identity(int size) {
        double[][] newMatrix = new double[size][size];
        for (int i = 0; i < size; i++) {
            newMatrix[i][i] = 1;
        }
        return new ArrayMatrix(newMatrix);
    }
    
    @Override
    public double getElement(int row, int column) throws IllegalArgumentException {
        return matrix[row][column];
    }

    @Override
    public int[] size() {
        int[] dimensions = new int[2];
        dimensions[0] = matrix.length;
        dimensions[1] = matrix[0].length;
        return dimensions;
    }

    @Override
    public double[] getRow(int row) throws IllegalArgumentException {
        if (row < 0 || row >= matrix.length) {
            throw new IllegalArgumentException("Row index out of bounds");
        }
        
        double[] currentRow = matrix[row];
        double[] copyRow = new double[currentRow.length];
        for (int i = 0; i < currentRow.length; i++) {
            copyRow[i] = currentRow[i];
        }
        return copyRow;
    }

    @Override
    public double[] getColumn(int column) throws IllegalArgumentException {
        if (column < 0 || column >= matrix[0].length) {
            throw new IllegalArgumentException("Column index out of bounds");
        }
        
        double[] copyColumn = new double[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            copyColumn[i] = matrix[i][column];
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
        
        double[][] newMatrix = new double[thisSize[0]][thisSize[1]];
        for (int row = 0; row < matrix.length; row++) {
            double[] currentRow = matrix[row];
            for (int column = 0; column < currentRow.length; column++) {
                newMatrix[row][column] = getElement(row, column) + matr.getElement(row, column);
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
        
        double[][] newMatrix = new double[thisSize[0]][thatSize[1]];
        for (int row = 0; row < thisSize[0]; row++) {
            double[] currentRow = matrix[row];
            for (int column = 0; column < thatSize[1]; column++) {
                double[] currentColumn = matr.getColumn(column);
                double dotProduct = 0;
                for (int i = 0; i < currentRow.length; i++) {
                    double partial = currentRow[i] * currentColumn[i];
                    dotProduct += partial;
                }
                newMatrix[row][column] = dotProduct;
            }
        }
        
       return new ArrayMatrix(newMatrix);
    }

    @Override
    public Matrix multiply(double element) {
        double[][] newMatrix = new double[matrix.length][matrix[0].length];
        
        for (int row = 0; row < matrix.length; row++) {
            double[] currentRow = matrix[row];
            for (int column = 0; column < currentRow.length; column++) {
                double currentElement = currentRow[column];
                newMatrix[row][column] = currentElement * element;
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
        for (int row = 0; row < matrix.length; row++) {
            double[] currentRow = matrix[row];
            for (int column = 0; column < currentRow.length; column++) {
                String currentElt = String.valueOf(currentRow[column]);
                if (column == currentRow.length - 1) {
                    grid += currentElt + "\n";
                } else {
                    grid += currentElt + "\t";
                }
            }
        }
        return grid;
    }
    
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
                double thisElt = getElement(i, j);
                double thatElt = thatMat.getElement(i, j);
                if (thisElt != thatElt) {
                    return false;
                }
            }
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        double sum = 0;
        
        int[] dimensions = size();
        for (int i = 0; i < dimensions[0]; i++) {
            for (int j = 0; j < dimensions[1]; j++) {
                sum += getElement(i, j);
            }
        }
        
        return (int)sum % 10000;
    }
    
    @Override
    public double determinant() throws IllegalArgumentException {
        int[] dimensions = size();
        if (dimensions[0] != dimensions[1]) {
            throw new IllegalArgumentException("Determinant not defined for non-square matrix");
        }
        
        double determinant = 0;
        for (int column = 0; column < dimensions[1]; column++) {
            double posPartial = 1;
            double negPartial = 1;
            for (int row = 0; row < dimensions[0]; row++) {
                double posElt = matrix[row][(column + row) % dimensions[1]];
                int negCheck = column - row;
                if (negCheck < 0) {
                    negCheck += dimensions[1];
                }
                double negElt = matrix[row][negCheck % dimensions[1]];
                posPartial *= posElt;
                negPartial *= negElt;
            }
            determinant += posPartial;
            determinant -= negPartial;
        }
        
        return determinant;
    }
    
    @Override
    public Matrix inverse() throws IllegalArgumentException {
        int[] dimensions = size();
        if (dimensions[0] != dimensions[1]) {
            throw new IllegalArgumentException("Inverse not defined for non-square matrix");
        }
        
        double determinant = determinant();
        if (determinant == 0) {
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
                double[] nullArr = inverse.getRow(row);
                double[][] colArr = new double[dimensions[1]][1];
                for (int columnIndex = 0; columnIndex < dimensions[1]; columnIndex++) {
                    colArr[columnIndex][0] = nullArr[columnIndex];
                }
                Matrix columnVec = new ArrayMatrix(colArr);
                nullspace.add(columnVec);
            }
        }
        
        return nullspace;
    }
    
    @Override
    public Matrix transpose() {
        int[] dimensions = size();
        double[][] transposeArr = new double[dimensions[1]][dimensions[0]];
        for (int i = 0; i < dimensions[0]; i++) {
            for (int j = 0; j < dimensions[1]; j++) {
                transposeArr[j][i] = matrix[i][j];
            }
        }
        return new ArrayMatrix(transposeArr);
    }
    
    @Override
    public boolean rowNotZero(int row) {
        double[] currentRow = matrix[row];
        for (double elt: currentRow) {
            if (elt != 0) {
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
    private static boolean rowNotZero(double[] row) {
        for (double elt: row) {
            if (elt != 0) {
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
        int[] dimensions = size();
        
        //perform same operations as ref, while copying row operations to identity matrix of same size
        double[][] id = new double[dimensions[0]][dimensions[1]];
        for (int i = 0; i < dimensions[1]; i++) {
            id[i][i] = 1;
        }
        
        double[][] newMatrix = new double[dimensions[0]][dimensions[1]];
        for (int row = 0; row < dimensions[0]; row++) {
            for (int column = 0; column < dimensions[1]; column++) {
                newMatrix[row][column] = matrix[row][column];
            }
        }
        
        //perform row swaps for each column
        for (int columnCheck = 0; columnCheck < dimensions[1]; columnCheck++) {
           
            //find any row with the nonzero value in this column
            double valueCheck = 0;
            double[] lowestRow = new double[dimensions[1]];
            int index = columnCheck - 1;
            while (valueCheck == 0 && index < dimensions[0] - 1) {
                index++;
                lowestRow = newMatrix[index];
                valueCheck = lowestRow[columnCheck];
            }
            if (valueCheck == 0) {
                continue;
            }
            
            //retrieve same information from id matrix
            double[] idRow = id[index];
            
            //simplify row so that first element is 1
            double criticalElt = valueCheck;
            for (int j = 0; j < lowestRow.length; j++) {
                lowestRow[j] = lowestRow[j] / criticalElt;
                idRow[j] = idRow[j] / criticalElt;
            }
            
            //use simplified row to reduce rest of matrix
            for (int row = 0; row < dimensions[0]; row++) {
                if (row != index) {
                    double[] rowToBeReduced = newMatrix[row];
                    double entryFactor = rowToBeReduced[columnCheck];
                    
                    double[] idTBR = id[row];
                    
                    for (int j = 0; j < rowToBeReduced.length; j++) {
                        rowToBeReduced[j] = rowToBeReduced[j] - entryFactor * lowestRow[j];
                        idTBR[j] = idTBR[j] - entryFactor * idRow[j];
                    }
                }
            }
            
            //swap rows
            double[] tmp = newMatrix[columnCheck];
            newMatrix[columnCheck] = lowestRow;
            newMatrix[index] = tmp;
            
            double[] idTmp = id[columnCheck];
            id[columnCheck] = idRow;
            id[index] = idTmp;
        }
        
        //move all zero rows to the bottom of the matrix
        for (int i = 0; i < dimensions[0]; i++) {
            double[] currentRow = newMatrix[i];
            if (!ArrayMatrix.rowNotZero(currentRow)) {
                for (int j = i; j < dimensions[0] - 1; j++) {
                    double[] tmp = newMatrix[j + 1];
                    newMatrix[j + 1] = newMatrix[j];
                    newMatrix[j] = tmp;
                    
                    double[] idTmp = id[j + 1];
                    id[j + 1] = id[j];
                    id[j] = idTmp;
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
