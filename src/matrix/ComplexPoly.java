package matrix;

public class ComplexPoly implements Polynomial<Complex> {
    
    private final static Complex ONE = new Complex(1, 0);
    private final static Complex ZERO = new Complex(0, 0);
    private final static Complex ROOT_START = new Complex(0.4, 0.9);
    
    private final Complex[] coefficients;
    private final int degree;
    
    public ComplexPoly(Complex constant) {
        degree = 0;
        coefficients = new Complex[1];
        coefficients[degree] = constant;
    }
    
    public ComplexPoly(Complex[] coeffs) {
        if (coeffs.length == 1) {
            degree = 0;
            coefficients = new Complex[coeffs.length];
            coefficients[degree] = coeffs[0];
        } else {
            int offset = coeffs.length - 1;
            while (coeffs[offset].equals(ZERO)) {
                offset--;
            }
            
            this.degree = offset;
            this.coefficients = new Complex[offset + 1];
            for (int i = 0; i < offset + 1; i++) {
                this.coefficients[i] = coeffs[i];
            }
        }
    }

    @Override
    public int degree() {
        return degree;
    }

    /**
     * @throws ArrayIndexOutOfBoundsException is degree < 0
     */
    @Override
    public Complex coeffAtDeg(int degree) throws ArrayIndexOutOfBoundsException {
        if (degree > this.degree()) {
            return ZERO;
        }
        return this.coefficients[degree];
    }

    @Override
    public Complex[] coefficients() {
        Complex[] copy = new Complex[this.degree() + 1];
        int index = 0;
        for (Complex co: this.coefficients) {
            copy[index] = co;
            index++;
        }
        return copy;
    }
    
    @Override
    public Complex plugIn(Complex value) {
        Complex runningTotal = ZERO;
        for (int i = 0; i < this.degree() + 1; i++) {
            Complex co = this.coeffAtDeg(i);
            Complex partialProduct = co.mult(value.pow(i));
            runningTotal = runningTotal.add(partialProduct);
        }
        
        return runningTotal;
    }

    @Override
    public Polynomial<Complex> add(Polynomial<Complex> that) {
        Polynomial<Complex> smaller;
        Polynomial<Complex> bigger;
        int maxDegree;
        if (this.degree() >= that.degree()) {
            smaller = that;
            bigger = this;
            maxDegree = this.degree();
        } else {
            smaller = this;
            bigger = that;
            maxDegree = that.degree();
        }
        
        Complex[] copy = new Complex[maxDegree + 1];
        
        Complex[] smallCoeffs = smaller.coefficients();
        Complex[] bigCoeffs = bigger.coefficients();
        for (int i = 0; i < copy.length; i++) {
            if (i <= smaller.degree()) {
                copy[i] = smallCoeffs[i].add(bigCoeffs[i]);
            } else {
                copy[i] = bigCoeffs[i];
            }
        }
        return new ComplexPoly(copy);
    }

    @Override
    public Polynomial<Complex> sub(Polynomial<Complex> that) {
        Polynomial<Complex> smaller;
        Polynomial<Complex> bigger;
        int maxDegree;
        if (this.degree() >= that.degree()) {
            smaller = that;
            bigger = this;
            maxDegree = this.degree();
        } else {
            smaller = this;
            bigger = that;
            maxDegree = that.degree();
        }
        
        Complex[] copy = new Complex[maxDegree + 1];
        
        Complex[] smallCoeffs = smaller.coefficients();
        Complex[] bigCoeffs = bigger.coefficients();
        for (int i = 0; i < copy.length; i++) {
            if (i <= smaller.degree()) {
                copy[i] = smallCoeffs[i].sub(bigCoeffs[i]);
            } else {
                copy[i] = bigCoeffs[i];
            }
        }
        return new ComplexPoly(copy);
    }

    @Override
    public Polynomial<Complex> mult(double factor) {
        Complex[] product = new Complex[this.degree() + 1];
        
        for (int i = 0; i < this.degree() + 1; i++) {
            product[i] = this.coeffAtDeg(i).mult(factor);
        }
        return new ComplexPoly(product);
    }
    
    @Override
    public Polynomial<Complex> mult(Complex factor) {
        Complex[] product = new Complex[this.degree() + 1];
        
        for (int i = 0; i < this.degree() + 1; i++) {
            product[i] = this.coeffAtDeg(i).mult(factor);
        }
        return new ComplexPoly(product);
    }

    @Override
    public Polynomial<Complex> mult(Polynomial<Complex> that) {
        Complex[] product = new Complex[this.degree() + that.degree() + 1];
        for (int i = 0; i < product.length; i++) {
            product[i] = ZERO;
        }
        
        for (int j = 0; j < this.degree() + 1; j++) {
            Complex factor1 = this.coeffAtDeg(j);
            for (int k = 0; k < that.degree() + 1; k++) {
                int newDegree = j + k;
                Complex factor2 = that.coeffAtDeg(k);
                Complex partialProduct = factor1.mult(factor2);
                product[newDegree] = product[newDegree].add(partialProduct);
            }
        }
        return new ComplexPoly(product);
    }
    
    @Override
    public Polynomial<Complex> div(double dividend) throws ArithmeticException {
        double factor = 1 / dividend;
        return this.mult(factor);
    }
    
    @Override
    public Polynomial<Complex> div(Complex dividend) throws ArithmeticException {
        Complex factor = dividend.pow(-1);
        return this.mult(factor);
    }

    @Override
    public Complex[] zeroes(int numPlaces) {
        double errorBound = Math.pow(10, numPlaces * -1);
        ComplexPoly altered = (ComplexPoly)this.div(this.coeffAtDeg(this.degree()));
        
        Complex[] current = new Complex[this.degree()];
        Complex[] next = new Complex[this.degree()];
        for (int i = 0; i < current.length; i++) {
            current[i] = ROOT_START.pow(i);
            next[i] = current[i];
        }
        
        boolean notCloseEnough = true;
        while (notCloseEnough) {
            for (int root = 0; root < current.length; root++) {
                Complex currentValue = current[root];
                Complex delta = altered.plugIn(currentValue);
                for (int otherRoot = 0; otherRoot < current.length; otherRoot++) {
                    if (otherRoot != root) {
                        Complex otherValue = current[otherRoot];
                        Complex partial = currentValue.sub(otherValue);
                        delta = delta.div(partial);
                    }
                }
                next[root] = next[root].sub(delta);
            }
            
            notCloseEnough = false;
            for (int rootCheck = 0; rootCheck < current.length; rootCheck++) {
                Complex oldValue = current[rootCheck];
                Complex newValue = next[rootCheck];
                Complex valueCheck = newValue.sub(oldValue);
                if (valueCheck.magnitude() > errorBound) {
                    notCloseEnough = true;
                    break;
                }
            }
            for (int i = 0; i < current.length; i++) {
                current[i] = next[i];
            }
        }
        
        for (int i = 0; i < next.length; i++) {
            next[i] = next[i].round(numPlaces);
        }
        return next;
    }
    
    @Override
    public String toString() {
        String output = "";
        for (int deg = this.degree(); deg >= 0; deg--) {
            String x_term = "x^" + String.valueOf(deg);
            String coeff = "(" + this.coeffAtDeg(deg).toString() + ")";
            output += coeff + x_term;
            if (deg > 0) {
                output += " + ";
            }
        }
        return output;
    }
    
    public static void main(String[] args) {
        Complex[] arr = new Complex[3];
        arr[0] = ONE;
        arr[1] = ZERO;
        arr[2] = ONE;
        ComplexPoly poly = new ComplexPoly(arr);
        Complex[] solutions = poly.zeroes(3);
        for (Complex sol: solutions) {
            System.out.println(sol);
        }
    }
}
