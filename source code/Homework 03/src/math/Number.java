package math;

import java.math.BigInteger;
import java.util.ArrayList;

public class Number extends Divisor {
    private BigInteger value;
    
    public Number() {
        value = BigInteger.ZERO;
    }
    
    public Number(BigInteger value) {
        this.value = value;
    }

    @Override
    public Expression diff() {
        return new Expression();
    }
    
    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public ArrayList<Divisor> mul(ArrayList<Divisor> list) {
        if (list.isEmpty()) {
            return new ArrayList<Divisor>();
        } else if (this.value.equals(BigInteger.ZERO)) {
            return new ArrayList<Divisor>();
        } else {
            for (Divisor tmp : list) {
                if (tmp instanceof Number) {
                    tmp.mul(this);
                    return list;
                }
            }
            list.add(this);
            return list;
        }
    }
    
    @Override
    public void mul(Number another) {
        value = value.multiply(another.value);
    }

    public void mul(BigInteger value) {
        this.value = this.value.multiply(value);
    }
    
    @Override
    public void mul(Divisor another) {}

    @Override
    public void mul(int value) {
        
    }
    
    public Number clone() {
        return new Number(value);
    }

    public boolean isZero() {
        return value.equals(BigInteger.ZERO);
    }
}
