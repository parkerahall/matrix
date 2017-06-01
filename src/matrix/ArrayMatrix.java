package matrix;

import java.util.List;
import java.util.Map;

/*
 * Implementation of Matrix with nonzero dimensions.
 */

public class ArrayMatrix implements Matrix {
    
    double[][] matrix;

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
    public Matrix ref() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<Double, List<Matrix>> eigen() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int rank() {
        int numZeroRows = 0;
        ArrayMatrix ref = (ArrayMatrix)ref();
        for (int row = ref.size()[0] - 1; row >= 0; row--) {
            if (ref.rowIsZero(row)) {
                numZeroRows += 1;
            } else {
                return numZeroRows;
            }
        }
        return numZeroRows;
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
                    if (row == matrix.length - 1) {
                        grid += currentElt;
                    } else {
                        grid += currentElt + "\n";
                    }
                } else {
                    grid += currentElt + "\t";
                }
            }
        }
        return grid;
    }
    
    /**
     * checks if row contains only zeroes
     * @param row integer in range [0, number rows)
     * @return true if all elements in row are 0, false otherwise
     */
    private boolean rowIsZero(int row) {
        double[] currentRow = matrix[row];
        for (double elt: currentRow) {
            if (elt != 0) {
                return false;
            }
        }
        return true;
    }
    
    public static void main(String args[]) {
        Matrix id = ArrayMatrix.identity(3);
        System.out.println(id.multiply(0));
    }
}
