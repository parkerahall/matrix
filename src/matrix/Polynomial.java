package matrix;

/**
 * Abstract data type for a polynomial
 * @author ParkerHall
 *
 * @param <R> type of coefficients of polynomial
 */

public interface Polynomial<R> {
    
    /**
     * @return the degree of the polynomial
     */
    public int degree();
    
    /**
     * @param degree nonnegative integer
     * @return coefficient of x ^ degree
     */
    public R coeffAtDeg(int degree);
    
    /**
     * @return array of coefficients in descending order of degree
     */
    public R[] coefficients();
    
    /**
     * evaluates the polynomial for a given value of x
     * @param value input value to the polynomial function
     * @return polynomial(x) when x = value
     */
    public R plugIn(R value);
    
    /**
     * @param that Polynomial to be added to this
     * @return this + that
     */
    public Polynomial<R> add(Polynomial<R> that);
    
    /**
     * @param that Polynomial to be subtracted from that
     * @return this - that
     */
    public Polynomial<R> sub(Polynomial<R> that);
    
    /**
     * @param factor factor to be multiplied with this
     * @return this * that
     */
    public Polynomial<R> mult(double factor);
    
    /**
     * @param factor factor to be multiplied with this
     * @return this * that
     */
    public Polynomial<R> mult(R factor);
    
    /**
     * @param that Polynomial to be multiplied with this
     * @return this * that
     */
    public Polynomial<R> mult(Polynomial<R> that);
    
    /**
     * @param dividend number to divide this by
     * @return this / dividend
     * @throws ArithmeticException if dividend == 0
     */
    public Polynomial<R> div(double dividend) throws ArithmeticException;
    
    /**
     * @param dividend number to divide this by
     * @return this / dividend
     * @throws ArithmeticException if dividend == 0
     */
    public Polynomial<R> div(R dividend) throws ArithmeticException ;
    
    /**
     * Calculate the zeroes of the polynomial, ie the values for x
     * where a_n * x^n + a_n-1 * x^(n-1) + ... + a_1 * x + a_0 = 0
     * @param numPlaces desired number of decimal places of accuracy
     * @return an array of approximations of the roots
     */
    public Complex[] zeroes(int numPlaces);
    
    /**
     * @return human-readable representation of this
     */
    public String toString();
}
