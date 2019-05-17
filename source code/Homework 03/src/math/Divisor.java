package math;

import java.math.BigInteger;
import java.util.ArrayList;

public abstract class Divisor { 
    public abstract Expression diff();
    
    public abstract String toString();
    
    public abstract ArrayList<Divisor> mul(ArrayList<Divisor> list);
    
    public abstract void mul(Divisor another);
    
    public void mul(int value) {}

    public void mul(BigInteger value) {}
    
    public void mul(Number another) {}
    
    public void mul(Power another) {}
    
    public void mul(Sin another) {}
    
    public void mul(Cos another) {}
    
    public abstract Divisor clone();
    
    public abstract boolean isZero();

    public boolean typeOf(Divisor variable) {
        return false;
    }
}
