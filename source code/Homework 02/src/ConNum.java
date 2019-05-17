import java.math.BigInteger;

class ConNum extends Divisor {
    private BigInteger value;
    
    ConNum() {
        value = BigInteger.ZERO;
    }
    
    ConNum(BigInteger value) {
        this.value = value;
    }
    
    ConNum(String str) {
        value = new BigInteger(str);
    }

    @Override
    Term diff() {
        ConNum coetmp = new ConNum();
        PowFun powtmp = new PowFun();
        TriFun sintmp = new TriFun(TriType.sin);
        TriFun costmp = new TriFun(TriType.cos);
        return new Term(coetmp, powtmp, sintmp, costmp);
    }

    @Override
    public String toString() {
        if (aboveZero()) {
            return "+" + value.toString();
        } else {
            return value.toString();
        } 
    }
    
    public boolean isNull() {
        return value.equals(BigInteger.ZERO);
    }
    
    void update(ConNum another) {
        this.value = this.value.multiply(another.value);
    }
    
    void update(BigInteger value) {
        this.value = this.value.multiply(value);
    }
    
    void add(ConNum another) {
        this.value = this.value.add(another.value);
    }
    
    private boolean aboveZero() {
        if (value.compareTo(BigInteger.ZERO) == 1) {
            return true;
        } else {
            return false;
        }
    }
    
    boolean biggerThan(ConNum another) {
        if (this.value.compareTo(another.value) == 1) {
            return true;
        } else {
            return false;
        }
    }
}
