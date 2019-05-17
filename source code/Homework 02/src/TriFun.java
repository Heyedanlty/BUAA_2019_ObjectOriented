import java.math.BigInteger;

class TriFun extends Divisor {
    private TriType type;//sin or cos
    private BigInteger power;//power of this TriFun
    
    TriFun(TriType type) {
        this.type = type;
        this.power = BigInteger.ZERO;
    }
    
    TriFun(TriType type, BigInteger power) {
        this.type = type;
        this.power = power;
    }
    
    TriFun(String str) {
        if (str.matches("sin\\(x\\)(\\^-?\\d+)?")) {
            if (str.contains("^")) {
                String[] tmpgroup = str.split("\\^");
                this.type = TriType.sin;
                this.power = new BigInteger(tmpgroup[1]);
            } else {
                this.type = TriType.sin;
                this.power = BigInteger.ONE;
            }
        } else if (str.matches("cos\\(x\\)(\\^-?\\d+)?")) {
            if (str.contains("^")) {
                String[] tmpgroup = str.split("\\^");
                this.type = TriType.cos;
                this.power = new BigInteger(tmpgroup[1]);
            } else {
                this.type = TriType.cos;
                this.power = BigInteger.ONE;
            }
        } else {
            System.out.println("WRONG FORMAT!");
            System.out.println("when build trifun");
            System.exit(0);
        }
    }

    @Override
    Term diff() {
        ConNum zerotmp = new ConNum();
        ConNum onetmp = new ConNum(BigInteger.ONE);
        ConNum monetmp = new ConNum(BigInteger.ONE.negate());
        PowFun powtmp = new PowFun();
        TriFun sintmp = new TriFun(TriType.sin);
        TriFun costmp = new TriFun(TriType.cos);
        if (power.equals(BigInteger.ZERO)) {
            return new Term(zerotmp, powtmp, sintmp, costmp);
        } else if (power.equals(BigInteger.ONE)) {
            TriFun tmpS;
            TriFun tmpC;
            switch (type) {
                case sin:
                    tmpC = new TriFun(TriType.cos, BigInteger.ONE);
                    return new Term(onetmp, powtmp, sintmp, tmpC);
                case cos:
                    tmpS = new TriFun(TriType.sin, BigInteger.ONE);
                    return new Term(monetmp, powtmp, tmpS, costmp);
                default:       
            }
        } else {
            ConNum coetmp = new ConNum(power);
            ConNum mcoetmp = new ConNum(power.negate());
            TriFun tmpS;
            TriFun tmpC;
            switch (type) {
                case sin:
                    tmpS = new TriFun(
                            TriType.sin, power.subtract(BigInteger.ONE));
                    tmpC = new TriFun(
                            TriType.cos, BigInteger.ONE);
                    return new Term(coetmp, powtmp, tmpS, tmpC);
                case cos:
                    tmpS = new TriFun(
                            TriType.sin, BigInteger.ONE);
                    tmpC = new TriFun(
                            TriType.cos, power.subtract(BigInteger.ONE));
                    return new Term(mcoetmp, powtmp, tmpS, tmpC);
                default:       
            }
        }
        return new Term(zerotmp, powtmp, sintmp, costmp);
    }

    @Override
    public String toString() {
        if (power.equals(BigInteger.ZERO)) {
            return "";
        }
        String tmp = "";
        if (type == TriType.sin) {
            if (power.equals(BigInteger.ONE)) {
                tmp += "sin(x)";
            } else {
                tmp += "sin(x)^" + power.toString();
            }
        } else {
            if (power.equals(BigInteger.ONE)) {
                tmp += "cos(x)";
            } else {
                tmp += "cos(x)^" + power.toString();
            }
        }
        return tmp;
    }
   
    void update(TriFun another) {
        if (another.type != type) {
            System.out.println("update TriFun error: type error!");
            System.exit(0);
        }
        power = power.add(another.power);
    }
    
    TriType getType() {
        return type;
    }
    
    public boolean isNull() {
        return power.equals(BigInteger.ZERO);
    }
    
    public boolean sameAs(TriFun another) {
        return this.type == another.type && this.power.equals(another.power);
    }
}
