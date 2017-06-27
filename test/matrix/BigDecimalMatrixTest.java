package matrix;

import java.math.BigDecimal;

/**
 * Testing file for Matrix data type
 * @author ParkerHall
 *
 */
public class BigDecimalMatrixTest {
    
    private final static BigDecimal ERROR = new BigDecimal(Math.pow(10, -15));
    
    private final static double[][] TWO_BY_TWO_ARR = {{1, 2},{3, 4}};
    private final static Matrix<BigDecimal> TWO_BY_TWO = new BigDecimalMatrix(TWO_BY_TWO_ARR);
    
    private final static double[][] NEG_2_BY_2_ARR = {{-2,-4},{-6,-8}};
    private final static Matrix<BigDecimal> NEG_2_BY_2 = new BigDecimalMatrix(NEG_2_BY_2_ARR);
    
    private final static double[][] DIFFERENCE_ARR = {{3, 6},{9, 12}};
    private final static Matrix<BigDecimal> DIFFERENCE = new BigDecimalMatrix(DIFFERENCE_ARR);
    
    private final static double[][] ADD_1_ARR = {{2, 7},{-9, 10}};
    private final static Matrix<BigDecimal> ADD_1 = new BigDecimalMatrix(ADD_1_ARR);
    
    private final static double[][] SUM_ARR = {{3, 9}, {-6, 14}};
    private final static Matrix<BigDecimal> SUM = new BigDecimalMatrix(SUM_ARR);
    
    private final static double[][] THREE_BY_THREE_ARR = {{1, 0, 4},{-1, -4, 0},{0, 21, -2}};
    private final static Matrix<BigDecimal> THREE_BY_THREE = new BigDecimalMatrix(THREE_BY_THREE_ARR);
    
    private final static double[][] TWO_BY_THREE_ARR = {{1, 2, 3},{4, 5, 6}};
    private final static Matrix<BigDecimal> TWO_BY_THREE = new BigDecimalMatrix(TWO_BY_THREE_ARR);
    
    private final static double[][] PRODUCT_ARR = {{9, 12, 15},{19, 26, 33}};
    private final static Matrix<BigDecimal> PRODUCT = new BigDecimalMatrix(PRODUCT_ARR);
    
    private final static double[][] PRODUCT_2_ARR = {{3, 0, 12},{-3, -12, 0},{0, 63, -6}};
    private final static Matrix<BigDecimal> PRODUCT_2 = new BigDecimalMatrix(PRODUCT_2_ARR);
    
    private final static Matrix<BigDecimal> ZERO_2 = BigDecimalMatrix.identity(2).subtract(BigDecimalMatrix.identity(2));
    
    private static String checkEquals(String string, Object expected, Object actual) {
        String output;
        if (expected.equals(actual) || expected == actual) {
            output = "Passed";
        } else {
            output = "Failed: " + string;
        }
        return output;
    }
    
    private static String checkEqualsBD(String string, BigDecimal expected, BigDecimal actual) {
        String output;
        if (expected.subtract(actual).abs().compareTo(ERROR) < 1) {
            output = "Passed";
        } else {
            output = "Failed: " + string;
        }
        return output;
    }
    
    private static String checkTrue(String string, boolean result) {
        String output;
        if (result) {
            output = "Passed";
        } else {
            output = "Failed: " + string;
        }
        return output;
    }
    
    /*
     * Testing strategy for getElement:
     *      row: in bounds, out of bounds
     *      column: in bounds, out of bounds
     */
    public static void testGetElementInIn() {
        BigDecimal elt = TWO_BY_TWO.getElement(0, 1);
        String output = "Test 1\t";
        output += checkEqualsBD("expected correct element", new BigDecimal(2.0), elt);
        System.out.println(output);
    }
    
    public static void testGetElementOutIn() {
        boolean check = false;
        try {
            BigDecimal elt = THREE_BY_THREE.getElement(3, 1);
        } catch (IllegalArgumentException iae) {
            check = true;
        }
        String output = "Test 2\t";
        output += checkTrue("expected out of bounds error", check);
        System.out.println(output);
    }
    
    public static void testGetElementInOut() {
        boolean check = false;
        try {
            BigDecimal elt = THREE_BY_THREE.getElement(2, -1);
        } catch (IllegalArgumentException iae) {
            check = true;
        }
        String output = "Test 3\t";
        output += checkTrue("expected out of bounds error", check);
        System.out.println(output);
    }
    
    public static void testGetElementOutOut() {
        boolean check = false;
        try {
            BigDecimal elt = THREE_BY_THREE.getElement(-1, 4);
        } catch (IllegalArgumentException iae) {
            check = true;
        }
        String output = "Test 4\t";
        output += checkTrue("expected out of bounds error", check);
        System.out.println(output);
    }
    
    /*
     * Testing strategy for size:
     *      row != column, row == column
     */
    public static void testSizeSame() {
        int[] dimensions = TWO_BY_TWO.size();
        String output = "Test 5\t";
        output += checkEquals("expected two rows", 2, dimensions[0]) + "\t";
        output += checkEquals("expected two columns", 2, dimensions[1]);
        System.out.println(output);
    }
    
    public static void testSizeDiff() {
        int[] dimensions = TWO_BY_THREE.size();
        String output = "Test 6\t";
        output += checkEquals("expected two rows", 2, dimensions[0]) + "\t";
        output += checkEquals("expected three columns", 3, dimensions[1]);
        System.out.println(output);
    }
    
    /*
     * Testing strategy for getRow:
     *      check that mutating row does not change matrix
     *      row index: in range, out of range
     */
    public static void testGetRowInRange() {
        String output = "Test 7\t";
        String beforeRow = THREE_BY_THREE.toString();
        BigDecimal[] firstRow = THREE_BY_THREE.getRow(0);
        for (int i = 0; i < firstRow.length; i++) {
            if (firstRow[i].compareTo(new BigDecimal(THREE_BY_THREE_ARR[0][i])) != 0) {
                output += "Failed: incorrect row\t";
                break;
            }
        }
        for (int i = 0; i < firstRow.length; i++) {
            firstRow[i] = BigDecimal.ZERO;
        }
        String afterRow = THREE_BY_THREE.toString();
        output += checkEquals("expected no change to matrix", beforeRow, afterRow);
        System.out.println(output);
    }
    
    public static void testGetRowOutRange() {
        String output = "Test 8\t";
        boolean check = false;
        try {
            BigDecimal[] lastRow = TWO_BY_THREE.getRow(2);
        } catch (IllegalArgumentException iae) {
            check = true;
        }
        output += checkTrue("expected out of bounds", check);
        System.out.println(output);
    }
    
    /*
     * Testing strategy for getColumn:
     *      check that mutating column does not change matrix
     *      column index: in range, out of range
     */
    public static void testGetColumnInRange() {
        String output = "Test 9\t";
        String beforeRow = TWO_BY_TWO.toString();
        BigDecimal[] secondRow = TWO_BY_TWO.getColumn(1);
        for (int i = 0; i < secondRow.length; i++) {
            if (secondRow[i].compareTo(new BigDecimal(TWO_BY_TWO_ARR[i][1])) != 0) {
                output += "Failed: incorrect column\t";
                break;
            }
        }
        for (int i = 0; i < secondRow.length; i++) {
            secondRow[i] = BigDecimal.ZERO;
        }
        String afterRow = TWO_BY_TWO.toString();
        output += checkEquals("expected no change to matrix", beforeRow, afterRow);
        System.out.println(output);
    }
    
    public static void testGetColumnOutRange() {
        String output = "Test 10\t";
        boolean check = false;
        try {
            BigDecimal[] firstColumn = TWO_BY_THREE.getRow(-1);
        } catch (IllegalArgumentException iae) {
            check = true;
        }
        output += checkTrue("expected out of bounds", check);
        System.out.println(output);
    }
    
    /*
     * Testing strategy for add:
     *      valid and invalid dimensions
     */
    public static void testAddValid() {
        String output = "Test 11\t";
        Matrix<BigDecimal> sumCheck = TWO_BY_TWO.add(ADD_1);
        output += checkEquals("expected correct sum", SUM, sumCheck);
        System.out.println(output);
    }
    
    public static void testAddInvalid() {
        String output = "Test 12\t";
        boolean check = false;
        try {
            Matrix<BigDecimal> sumCheck = THREE_BY_THREE.add(TWO_BY_THREE);
        } catch (IllegalArgumentException iae) {
            check = true;
        }
        output += checkTrue("expected invalid dimensions", check);
        System.out.println(output);
    }
    
    /*
     * Testing strategy for subtract:
     *      valid and invalid dimensions
     */
    public static void testSubtractValid() {
        String output = "Test 21\t";
        Matrix<BigDecimal> differenceCheck = TWO_BY_TWO.subtract(NEG_2_BY_2);
        output += checkEquals("expected correct difference", DIFFERENCE, differenceCheck);
        System.out.println(output);
    }
    
    public static void testSubtractInvalid() {
        String output = "Test 22\t";
        boolean check = false;
        try {
            Matrix<BigDecimal> differenceCheck = TWO_BY_TWO.subtract(THREE_BY_THREE);
        } catch (IllegalArgumentException iae) {
            check = true;
        }
        output += checkTrue("expected invalid dimensions", check);
        System.out.println(output);
    }
    
    /*
     * Testing strategy: multiply(Matrix):
     *      valid and invalid dimensions
     *      check that multiplying by A*I = I*A = A
     */
    public static void testMultiplyValid() {
        String output = "Test 13\t";
        Matrix<BigDecimal> productCheck = TWO_BY_TWO.multiply(TWO_BY_THREE);
        output += checkEquals("expected correct product", PRODUCT, productCheck);
        System.out.println(output);
    }
    
    public static void testMultiplyIdentity1() {
        String output = "Test 14\t";
        Matrix<BigDecimal> productCheck = TWO_BY_TWO.multiply(BigDecimalMatrix.identity(2));
        output += checkEquals("expected correct product", TWO_BY_TWO, productCheck);
        System.out.println(output);
    }
    
    public static void testMultiplyIdentity2() {
        String output = "Test 15\t";
        Matrix<BigDecimal> productCheck = BigDecimalMatrix.identity(3).multiply(THREE_BY_THREE);
        output += checkEquals("expected correct product", THREE_BY_THREE, productCheck);
        System.out.println(output);
    }
    
    public static void testMultiplyInvalid() {
        String output = "Test 16\t";
        boolean check = false;
        try {
            Matrix<BigDecimal> productCheck = THREE_BY_THREE.multiply(TWO_BY_THREE);
        } catch (IllegalArgumentException iae) {
            check = true;
        }
        output += checkTrue("expected invalid dimension", check);
        System.out.println(output);
    }
    
    /*
     * Testing strategy: multiply(double):
     *      factor value: <-1, 0, 1, >1
     */
    public static void testMultiplyDoubleNeg() {
        String output = "Test 17\t";
        Matrix<BigDecimal> productCheck = TWO_BY_TWO.multiply(-2);
        output += checkEquals("expected correct product", NEG_2_BY_2, productCheck);
        System.out.println(output);
    }
    
    public static void testMultiplyDoubleZero() {
        String output = "Test 18\t";
        Matrix<BigDecimal> productCheck = TWO_BY_TWO.multiply(0);
        output += checkEquals("expected correct product", ZERO_2, productCheck);
        System.out.println(output);
    }
    
    public static void testMultiplyDoubleOne() {
        String output = "Test 19\t";
        Matrix<BigDecimal> productCheck = TWO_BY_THREE.multiply(1);
        output += checkEquals("expected correct product", TWO_BY_THREE, productCheck);
        System.out.println(output);
    }
    
    public static void testMultiplyDoubleBigger() {
        String output = "Test 20\t";
        Matrix<BigDecimal> productCheck = THREE_BY_THREE.multiply(3);
        output += checkEquals("expected correct product", PRODUCT_2, productCheck);
        System.out.println(output);
    }
    
    //MORE IMPORTANT TESTS
    
    //rref tests
    public static void testRrefFirst() {
        String output = "Test 23\t";
        double[][] matrixArr = {{1,2,3},{4,5,6},{7,8,9}};
        Matrix<BigDecimal> matrix = new BigDecimalMatrix(matrixArr);
        Matrix<BigDecimal> rref = matrix.rref();
        
        double[][] solutionArr = {{1,0,-1},{0,1,2},{0,0,0}};
        Matrix<BigDecimal> solution = new BigDecimalMatrix(solutionArr);
        output += checkEquals("expected correct rref", solution, rref);
        System.out.println(output);
    }
    
    public static void testRrefSecond() {
        String output = "Test 24\t";
        double[][] matrixArr = {{-12,-7./11,3,-8,-6,-8}};
        Matrix<BigDecimal> matrix = new BigDecimalMatrix(matrixArr);
        Matrix<BigDecimal> rref = matrix.rref();
        
        double[][] solutionArr = {{1,7./132,-.25,2./3,.5,2./3}};
        Matrix<BigDecimal> solution = new BigDecimalMatrix(solutionArr);
        output += checkEquals("expected correct rref", solution, rref);
        System.out.println(output);
    }
    
    public static void testRrefThird() {
        String output = "Test 25\t";
        double[][] matrixArr = {{15},{14},{-9},{-3}};
        Matrix<BigDecimal> matrix = new BigDecimalMatrix(matrixArr);
        Matrix<BigDecimal> rref = matrix.rref();
        
        double[][] solutionArr = {{1},{0},{0},{0}};
        Matrix<BigDecimal> solution = new BigDecimalMatrix(solutionArr);
        output += checkEquals("expected correct rref", solution, rref);
        System.out.println(output);
    }
    
    public static void testRrefFourth() {
        String output = "Test 26\t";
        double[][] matrixArr = {{2,-1,15,8,-9},{6,-15,7,-10,10},{4,-10,-6,3,5},{13,-13,-5,0,-9}};
        Matrix<BigDecimal> matrix = new BigDecimalMatrix(matrixArr);
        Matrix<BigDecimal> rref = matrix.rref();
        
        double[][] solutionArr = {{1,0,0,0,-29144./13801},
                                    {0,1,0,0,-37067./27602},
                                    {0,0,1,0,-5491./27602},
                                    {0,0,0,1,-5409./13801}};
        Matrix<BigDecimal> solution = new BigDecimalMatrix(solutionArr);
        output += checkEquals("expected correct rref", solution, rref);
        System.out.println(output);
    }
    
    //determinant tests
    public static void testDeterminantFirst() {
        String output = "Test 27\t";
        double[][] matrixArr = {{3}};
        Matrix<BigDecimal> matrix = new BigDecimalMatrix(matrixArr);
        BigDecimal determinant = matrix.determinant();
        BigDecimal solution = new BigDecimal(3);
        output += checkEquals("expected correct determinant", solution, determinant);
        System.out.println(output);
    }
    
    public static void testDeterminantSecond() {
        String output = "Test 28\t";
        double[][] matrixArr = {{1,4},{7,8}};
        Matrix<BigDecimal> matrix = new BigDecimalMatrix(matrixArr);
        BigDecimal determinant = matrix.determinant();
        BigDecimal solution = new BigDecimal(-20);
        output += checkEquals("expected correct determinant", solution, determinant);
        System.out.println(output);
    } 
    
    public static void testDeterminantThird() {
        String output = "Test 29\t";
        double[][] matrixArr = {{15,14,3},{-14,-5,4},{-9,-1,13}};
        Matrix<BigDecimal> matrix = new BigDecimalMatrix(matrixArr);
        BigDecimal determinant = matrix.determinant();
        BigDecimal solution = new BigDecimal(1036);
        output += checkEquals("expected correct determinant", solution, determinant);
        System.out.println(output);
    }
    
    public static void testDeterminantFourth() {
        String output = "Test 30\t";
        double[][] matrixArr = {{7,3,-1,2,3,-2},{4,10,-1,-3,4,5},{12,1,4,7,9,2},
                                {-1,3,-4,-10,5,7},{12,9,5,3,8,1},{-9,12,4,5,1,2}};
        Matrix<BigDecimal> matrix = new BigDecimalMatrix(matrixArr);
        BigDecimal determinant = matrix.determinant();
        BigDecimal solution = new BigDecimal(-262985);
        output += checkEquals("expected correct determinant", solution, determinant);
        System.out.println(output);
    } 
    
    public static void main(String args[]) {
        testGetElementInIn();
        testGetElementInOut();
        testGetElementOutIn();
        testGetElementOutOut();
        testSizeSame();
        testSizeDiff();
        testGetRowInRange();
        testGetRowOutRange();
        testGetColumnInRange();
        testGetColumnOutRange();
        testAddValid();
        testAddInvalid();
        testMultiplyValid();
        testMultiplyIdentity1();
        testMultiplyIdentity2();
        testMultiplyInvalid();
        testMultiplyDoubleNeg();
        testMultiplyDoubleZero();
        testMultiplyDoubleOne();
        testMultiplyDoubleBigger();
        testSubtractValid();
        testSubtractInvalid();
        testRrefFirst();
        testRrefSecond();
        testRrefThird();
        testRrefFourth();
        testDeterminantFirst();
        testDeterminantSecond();
        testDeterminantThird();
        testDeterminantFourth();
    }
}
