package math;

import java.math.BigInteger;
import java.util.ArrayList;

public class Power extends Divisor {
    private BigInteger power;
    
    public Power() {
        power = BigInteger.ZERO;
    }
    
    public Power(BigInteger power) {
        this.power = power;
    }

    @Override
    public Expression diff() {
        if (power.equals(BigInteger.ZERO)) {
            return new Expression();
        } else if (power.equals(BigInteger.ONE)) {
            Number tmpN = new Number(BigInteger.ONE);
            ArrayList<Divisor> tmpL = new ArrayList<Divisor>();
            tmpL.add(tmpN);
            Term tmpT = new Term(tmpL);
            ArrayList<Term> tmpR = new ArrayList<Term>();
            tmpR.add(tmpT);
            return new Expression(tmpR);
        } else {
            Number tmpN = new Number(this.power);
            ArrayList<Divisor> tmpL = new ArrayList<Divisor>();
            tmpL.add(tmpN);
            Power tmpP = new Power(power.subtract(BigInteger.ONE));
            tmpL.add(tmpP);
            Term tmpT = new Term(tmpL);
            ArrayList<Term> tmpR = new ArrayList<Term>();
            tmpR.add(tmpT);
            return new Expression(tmpR);
        }
    }
    
    @Override
    public String toString() {
        if (power.equals(BigInteger.ZERO)) {
            return "";
        } else if (power.equals(BigInteger.ONE)) {
            return "x";
        } else {
            return "x^" + power.toString();
        }
    }

    @Override
    public ArrayList<Divisor> mul(ArrayList<Divisor> list) {
        if (list.isEmpty()) {
            return new ArrayList<Divisor>();
        } else {
            for (Divisor tmp : list) {
                if (tmp instanceof Power) {
                    tmp.mul(this);
                    return list;
                }
            }
            list.add(this);
            return list;
        }
    }
    
    public void mul(Power another) {
        power = power.add(another.power);
    }

    @Override
    public void mul(Divisor another) {}

    @Override
    public void mul(int value) {
        
    }
    
    public Power clone() {
        return new Power(power);
    }
    
    @Override
    public boolean isZero() {
        return false;
    }
    
    public boolean isOne() {
        return power.equals(BigInteger.ZERO);
    }

}
