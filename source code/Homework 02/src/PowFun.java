import java.math.BigInteger;

class PowFun extends Divisor {
    private BigInteger power;//power of this PowFun

    PowFun() {
        this.power = BigInteger.ZERO;
    }
    
    PowFun(BigInteger power) {
        this.power = power;
    }
    
    PowFun(String str) {
        if (str.matches("x(\\^-?\\d+)?")) {
            if (str.contains("^")) {
                String[] tmpgroup = str.split("\\^");
                power = new BigInteger(tmpgroup[1]);
            } else {
                power = BigInteger.ONE;
            }
        } else {
            System.out.println("WRONG FORMAT!");
            System.out.println("when build powfun");
            System.exit(0);
        }
    }
    
    @Override
    Term diff() {
        ConNum zerotmp = new ConNum();
        ConNum onetmp = new ConNum(BigInteger.ONE);
        ConNum coetmp = new ConNum(power);
        PowFun powtmp = new PowFun();
        TriFun sintmp = new TriFun(TriType.sin);
        TriFun costmp = new TriFun(TriType.cos);
        if (power.equals(BigInteger.ZERO)) {       
            return new Term(zerotmp, powtmp, sintmp, costmp);
        } else if (power.equals(BigInteger.ONE)) {
            return new Term(onetmp, powtmp, sintmp, costmp);
        } else {
            PowFun pownew = new PowFun(power.subtract(BigInteger.ONE));
            return new Term(coetmp, pownew, sintmp, costmp);
        }
    }

    @Override
    public String toString() {
        if (power.equals(BigInteger.ZERO)) {
            return "";
        } else if (power.equals(BigInteger.ONE)) {
            return new String("x"); 
        } else {
            return new String("x^" + power.toString()); 
        }
    }
    
    void update(PowFun another) {
        power = power.add(another.power);
    }
    
    public boolean isNull() {
        return power.equals(BigInteger.ZERO);
    }
    
    public boolean sameAs(PowFun another) {
        return this.power.equals(another.power);
    }
}
