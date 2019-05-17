package math;

import java.math.BigInteger;
import java.util.ArrayList;

public class Sin extends Divisor {
    private BigInteger power;
    private Divisor variable;
    
    public Sin() {
        power = BigInteger.ZERO;
        variable = new Expression();
    }
    
    public Sin(Divisor variable, BigInteger power) {
        this.power = power;
        this.variable = variable;
    }
    
    @Override
    public Expression diff() {
        if (power.equals(BigInteger.ZERO)) {
            return new Expression();
        } else if (power.equals(BigInteger.ONE)) {
            Number tmpN = new Number(BigInteger.ONE);
            Cos tmpC = new Cos(variable, BigInteger.ONE);
            ArrayList<Divisor> tmpL = new ArrayList<Divisor>();
            tmpL.add(tmpN);
            tmpL.add(tmpC);
            tmpL.add(variable.diff());
            Term tmpT = new Term(tmpL);
            ArrayList<Term> tmpR = new ArrayList<Term>();
            tmpR.add(tmpT);
            return new Expression(tmpR);
        } else {
            Number tmpN = new Number(power);
            Sin tmpS = new Sin(variable, power.subtract(BigInteger.ONE));
            Cos tmpC = new Cos(variable, BigInteger.ONE);
            ArrayList<Divisor> tmpL = new ArrayList<Divisor>();
            tmpL.add(tmpN);
            tmpL.add(tmpS);
            tmpL.add(tmpC);
            tmpL.add(variable.diff());
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
            return "sin(" + variable.toString() + ")";
        } else {
            return "sin(" + variable.toString() + ")^" + power.toString();
        }
    }

    @Override
    public ArrayList<Divisor> mul(ArrayList<Divisor> list) {
        if (list.isEmpty()) {
            return new ArrayList<Divisor>();
        } else {
            for (Divisor tmp : list) {
                if (tmp instanceof Sin) {
                    Sin tmpS = (Sin)tmp;
                    if (tmpS.typeOf(this)) {
                        tmp.mul(this);
                        return list;
                    } else {
                        continue;
                    }
                }
            }
            list.add(this);
            return list;
        }
    }

    public void mul(Sin another) {
        power = power.add(another.power);
    }
    
    @Override
    public void mul(Divisor another) {}
    
    @Override
    public void mul(int value) {
        
    }
    
    public boolean typeOf(Sin another) {
        return this.variable.typeOf(another.variable);   
    }

    public Sin clone() {
        return new Sin(variable.clone(), power);
    }

    @Override
    public boolean isZero() {
        return variable.isZero();
    }
   
    public boolean isOne() {
        return power.equals(BigInteger.ZERO);
    }
}
