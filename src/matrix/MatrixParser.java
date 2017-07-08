package matrix;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Parsing class for user input
 * @author ParkerHall
 *
 */
public class MatrixParser {
    
    private final Map<String, Matrix<BigDecimal>> variableMap = new HashMap<>();
    
    /**
     * Parse user input and respond accordingly
     * Accepted operations are:
     *      setting a variable: a = MATRIX
     *      binary operations on two variables: a + b, a - b, a * b
     *      unary operations on a variable: size(a), rref(a), det(a), rank(a), nullity(a)
     *                                      inv(a), nullspace(a), trans(a)
     *      proper format for matrix declaration:
     *          matrix wrapped by brackets {}
     *          rows wrapped by parentheses (), delimited by commas
     *          elements delimited by a single space
     *          ex, {(1 2 3),(4 5 6),(7 8 9)}
     * @param input command from the user
     * @return output string the user will read
     * @throws MatrixFormatException if matrix string in input is not formatted correctly
     * @throws NumberFormatException if entries in the matrix are not formatted correctly
     */
    public String parseString(String input) throws MatrixFormatException, NumberFormatException {
        String output = "";
        if (input.contains("=")) {
            String[] splitInput = input.split("=");
            String variable = splitInput[0].trim();
            String matrixString = splitInput[1].trim();
            Matrix<BigDecimal> matrix = MatrixParser.parseMatrix(matrixString);
            variableMap.put(variable, matrix);
            output = "Variable '" + variable + "' successfully added to memory";
        } else if (input.contains("+")) {
            String[] variables = input.split("\\+");
            String left = variables[0].trim();
            String right = variables[1].trim();
            
            Matrix<BigDecimal> leftMatrix = variableMap.get(left);
            Matrix<BigDecimal> rightMatrix = variableMap.get(right);
            Matrix<BigDecimal> sum = leftMatrix.add(rightMatrix);
            output = sum.linearString();
        } else if (input.contains("-")) {
            String[] variables = input.split("-");
            String left = variables[0].trim();
            String right = variables[1].trim();
            
            Matrix<BigDecimal> leftMatrix = variableMap.get(left);
            Matrix<BigDecimal> rightMatrix = variableMap.get(right);
            Matrix<BigDecimal> diff = leftMatrix.subtract(rightMatrix);
            output = diff.linearString();
        } else if (input.contains("*")) {
            String[] variables = input.split("\\*");
            String left = variables[0].trim();
            String right = variables[1].trim();
            
            Matrix<BigDecimal> leftMatrix = variableMap.get(left);
            Matrix<BigDecimal> rightMatrix = variableMap.get(right);
            Matrix<BigDecimal> product = leftMatrix.multiply(rightMatrix);
            output = product.linearString();
        } else {
            int openParenIndex = input.indexOf("(");
            int closeParenIndex = input.indexOf(")");
            String variable = input.substring(openParenIndex + 1, closeParenIndex).trim();
            Matrix<BigDecimal> matrix = variableMap.get(variable);
            
            String operation = input.substring(0, openParenIndex).trim();
            if (operation.equals("size")) {
                int[] dims = matrix.size();
                output = dims[0] + " rows, " + dims[1] + " columns";
            } else if (operation.equals("rref")) {
                Matrix<BigDecimal> rref = matrix.rref();
                output = rref.linearString();
            } else if (operation.equals("det")) {
                BigDecimal determinant = matrix.determinant();
                output = determinant.toPlainString();
            } else if (operation.equals("rank")) {
                int rank = matrix.rank();
                output = String.valueOf(rank);
            } else if (operation.equals("nullity")) {
                int nullity = matrix.nullity();
                output = String.valueOf(nullity);
            } else if (operation.equals("inv")) {
                Matrix<BigDecimal> inverse = matrix.inverse();
                output = inverse.linearString();
            } else if (operation.equals("nullspace")) {
                Set<Matrix<BigDecimal>> nullspace = matrix.nullspace();
                Set<String> stringNull = new HashSet<>();
                for (Matrix<BigDecimal> nullVec: nullspace) {
                    stringNull.add(nullVec.linearString());
                }
                output = stringNull.toString();
            } else if (operation.equals("trans")) {
                Matrix<BigDecimal> transpose = matrix.transpose();
                output = transpose.linearString();
            } else {
                throw new IllegalArgumentException("Invalid operation request");
            }
        }
        return output;
    }
    
    /**
     * 
     * @param matrixString linear form of matrix, must follow format described in parseString javadoc
     * @return Matrix object represented by matrixString
     * @throws MatrixFormatException if matrix formatted incorrectly
     * @throws NumberFormatException if element in matrix formatted incorrectly
     */
    public static Matrix<BigDecimal> parseMatrix(String matrixString) throws MatrixFormatException, NumberFormatException {
        List<List<Double>> matrixList = new ArrayList<>();
        int firstIndex = 0;
        int lastIndex = matrixString.length() - 1;
        char firstChar = matrixString.charAt(firstIndex);
        char lastChar = matrixString.charAt(lastIndex);
        if (firstChar != '{' || lastChar != '}') {
            throw new MatrixFormatException("Matrix must be enclosed in brackets {}");
        }
        
        String debracketed = matrixString.substring(firstIndex + 1,  lastIndex);
        String[] rowStrings = debracketed.split(",");
        for (String rowString: rowStrings) {
            List<Double> rowList = MatrixParser.parseRow(rowString);
            matrixList.add(rowList);
        }
        
        return new BigDecimalMatrix(matrixList);
    }
    
    /**
     * Create list of doubles representing a row in a matrix based on rowString
     * @param rowString a single row in a matrixString following the format in the parseString javadoc
     * @return list of doubles representing the row represented by rowString
     * @throws MatrixFormatException if rowString formatted incorrectly
     * @throws NumberFormatException if element in rowString formatted incorrectly
     */
    public static List<Double> parseRow(String rowString) throws MatrixFormatException, NumberFormatException{
        int firstIndex = 0;
        int lastIndex = rowString.length() - 1;
        char firstChar = rowString.charAt(firstIndex);
        char lastChar = rowString.charAt(lastIndex);
        if (firstChar != '(' || lastChar != ')') {
            throw new MatrixFormatException("Rows must be enclosed in parentheses ()");
        }
        
        String noParentheses = rowString.substring(firstIndex + 1, lastIndex);
        String[] elements = noParentheses.split(" ");
        
        List<Double> row = new ArrayList<>();
        for (String eltString: elements) {
            double eltDouble = Double.parseDouble(eltString);
            row.add(eltDouble);
        }
        return row;
    }
    
    public static void main(String[] args) {
        MatrixParser mp = new MatrixParser();
        String declaration = "a = {(1 0 0),(0 1 0),(0 0 1)}";
        System.out.println(mp.parseString(declaration));
        String askSize = "rank(a)";
        System.out.println(mp.parseString(askSize));
    }
}


class MatrixFormatException extends NumberFormatException {
    
    private static final long serialVersionUID = 1L;
    
    public MatrixFormatException() {
        super();
    }
    
    public MatrixFormatException(String message) {
        super(message);
    }
}