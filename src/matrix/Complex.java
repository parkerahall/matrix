package matrix;

/*
 * Immutable representation of complex numbers
 * Constructors require that both arguments are the same type
 */

public class Complex {
    
    private final double real;
    private final double imaginary;
    
    public Complex(int real, int imag) {
        this.real = real;
        this.imaginary = imag;
    }
    
    public Complex(double real, double imag) {
        this.real = real;
        this.imaginary = imag;
    }
    
    /**
     * @return the real part of the complex number
     */
    public double getReal() {
        return real;
    }
    
    /**
     * @return the imaginary part of the complex number
     */
    public double getImag() {
        return imaginary;
    }
    
    /**
     * Complex conjugate of a + bi is defined to be a - bi
     * @return the complex conjugate of this
     */
    public Complex conjugate() {
        double newReal = this.getReal();
        double newImag = this.getImag() * -1;
        return new Complex(newReal, newImag);
    }
    
    /**
     * Magnitude of a + bi is defined to be sqrt(a^2 + b^2)
     * @return magnitude of this
     */
    public double magnitude() {
        double sumOfSquares = Math.pow(this.getReal(), 2) + Math.pow(this.getImag(), 2);
        double doubleRoot = Math.pow(sumOfSquares, 0.5);
        return doubleRoot;
    }
    
    /**
     * Argument of a + bi ranges from -pi/2 to pi/2
     * Arctan calculation implemented using Math.atan2
     * @return argument of this
     * @throws UndefinedException if argument is undefined (0 + 0i)
     */
    public double argument() throws UndefinedException {
        double x = this.getReal();
        double y = this.getImag();
        if (x == 0 && y == 0) {
            throw new UndefinedException("Argument is undefined");
        }
        return Math.atan2(y, x);
    }
    
    /**
     * (a + bi) + (c + di) = (a + c) + (b + d)i
     * @param that valid Complex number
     * @return this + that
     */
    public Complex add(Complex that) {
        double newReal = this.getReal() + that.getReal();
        double newImag = this.getImag() + that.getImag();
        return new Complex(newReal, newImag);
    }
    
    /**
     * (a + bi) - (c + di) = (a - c) + (b - d)i
     * @param that valid Complex number
     * @return this - that
     */
    public Complex sub(Complex that) {
        double newReal = this.getReal() - that.getReal();
        double newImag = this.getImag() - that.getImag();
        return new Complex(newReal, newImag);
    }
    
    /**
     * (a + bi) * (c + di) = (a*c - b*d) + (a*d + b*c)i
     * @param that valid Complex number
     * @return this * that
     */
    public Complex mult(Complex that) {
        double ac = this.getReal() * that.getReal();
        double bd = this.getImag() * that.getImag();
        double ad = this.getReal() * that.getImag();
        double bc = this.getImag() * that.getReal();
        double newReal = ac - bd;
        double newImag = ad + bc;
        return new Complex(newReal, newImag);
    }
    
    /**
     * (a + bi) / (c + di) = (a + bi)(c - di) / (c^2 + d^2)
     * @param that
     * @return this / that
     * @throws ZeroDenominatorException if that == 0
     */
    public Complex div(Complex that) throws ZeroDenominatorException {
        if (that.getReal() == 0 &&
                that.getImag() == 0) {
            throw new ZeroDenominatorException("Denominator is equal to zero");
        }
        
        Complex numerator = this.mult(that.conjugate());
        double denominator = Math.pow(this.magnitude(), 2);
        double newReal = numerator.getReal() / denominator;
        double newImag = numerator.getImag() / denominator;
        return new Complex(newReal, newImag);
    }
    
    @Override
    public boolean equals(Object that) {
        if (!(that instanceof Complex)) return false;
        Complex thatComplex = (Complex)that;
        return (this.getReal() == thatComplex.getReal()) &&
                (this.getImag() == thatComplex.getImag());
    }
    
    @Override
    public String toString() {
        return real + "+" + imaginary + "i";
    }
    
    @Override
    public int hashCode() {
        return (int)(Math.pow(this.getReal(), 2) - Math.pow(this.getImag(), 2));
    }
}

class ZeroDenominatorException extends Exception {
    
    private static final long serialVersionUID = 1L;

    public ZeroDenominatorException() {
        super();
    }
    
    public ZeroDenominatorException(String message) {
        super(message);
    }
}

class UndefinedException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    public UndefinedException() {
        super();
    }
    
    public UndefinedException(String message) {
        super(message);
    }
}
