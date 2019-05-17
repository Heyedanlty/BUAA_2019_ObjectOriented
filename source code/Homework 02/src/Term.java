import java.math.BigInteger;
import java.util.ArrayList;

class Term implements IsNull {
    private ConNum numF;
    private PowFun powF;
    private TriFun sinF;
    private TriFun cosF;

    Term(ConNum numF, PowFun powF, TriFun sinF, TriFun cosF) {
        this.numF = numF;
        this.powF = powF;
        this.sinF = sinF;
        this.cosF = cosF;
    }
    
    Term(String str) {
        ConNum numF = new ConNum(BigInteger.ONE);
        PowFun powF = new PowFun();
        TriFun sinF = new TriFun(TriType.sin);
        TriFun cosF = new TriFun(TriType.cos);
        String tmp = str;
        tmp = tmp.replaceAll("-\\+-","+");
        tmp = tmp.replaceAll("--","+");
        tmp = tmp.replaceAll("\\+","");
        if (tmp.startsWith("-")) {
            tmp = tmp.substring(1,tmp.length());
            numF.update(BigInteger.ONE.negate());
        }
        String[] strgroup = tmp.split("\\*");
        for (String s : strgroup) {
            if (s.matches("-?\\d+")) {
                numF.update(new ConNum(s));
            } else if (s.matches("x(\\^-?\\d+)?")) {
                powF.update(new PowFun(s));
            } else if (s.matches("sin\\(x\\)(\\^-?\\d+)?")) {
                sinF.update(new TriFun(s));
            } else if (s.matches("cos\\(x\\)(\\^-?\\d+)?")) {
                cosF.update(new TriFun(s));
            } else {
                System.out.println("WRONG FORMAT!");
                System.out.println(s + "     unexpected wrong matching term");
                System.exit(0);
            }
        }
        this.numF = numF;
        this.powF = powF;
        this.sinF = sinF;
        this.cosF = cosF;
    }
    
    public ArrayList<Term> diff() {
        if (numF.isNull()) {
            return new ArrayList<Term>();
        } else if (powF.isNull() && sinF.isNull() && cosF.isNull()) {
            return new ArrayList<Term>();
        } else {
            ArrayList<Term> tmp = new ArrayList<Term>();
            if (!powF.diff().isNull()) {
                Term term1 = powF.diff();
                term1.update(numF);
                term1.update(sinF);
                term1.update(cosF);
                tmp.add(term1);
            }
            if (!sinF.diff().isNull()) {
                Term term2 = sinF.diff();
                term2.update(numF);
                term2.update(powF);
                term2.update(cosF);
                tmp.add(term2);
            }
            if (!cosF.diff().isNull()) {
                Term term3 = cosF.diff();
                term3.update(numF);
                term3.update(powF);                
                term3.update(sinF);
                tmp.add(term3);
            }
            return tmp;
        }
        
    }
    
    void update(ConNum numF) {
        this.numF.update(numF);
    }
    
    void update(PowFun powF) {
        this.powF.update(powF);
    }
    
    void update(TriFun triF) {
        if (triF.getType() == TriType.sin) {
            this.sinF.update(triF);
        } else {
            this.cosF.update(triF);
        }
    }

    void update(Term another) {
        if (!this.sameAs(another)) {
            System.out.println("WRONG FORMAT!");
            System.out.println("unexpected tidy!");
            System.exit(0);
        }
        numF.add(another.numF);
    }

    public String toString() {
        String str = "";
        //number part
        if (numF.isNull()) {
            return str;
        } else {
            str += numF.toString() + "*";
        }
        //power function part
        if (powF.isNull()) {
            //do nothing
        } else {
            str += powF.toString() + "*";
        }
        //sin part
        if (sinF.isNull()) {
            //do nothing
        } else {
            str += sinF.toString() + "*";
        }
        //cos part
        if (cosF.isNull()) {
            //do nothing
        } else {
            str += cosF.toString();
        }
        //deal with str
        if (str.endsWith("*")) {
            str = str.substring(0,str.length() - 1);
        }
        if (str.startsWith("+1*")) {
            str = "+" + str.substring(3,str.length());
        }
        if (str.startsWith("-1*")) {
            str = "-" + str.substring(3,str.length());
        }
        return str;
    }
    
    public boolean isNull() {
        return numF.isNull();
    }
    
    public boolean sameAs(Term another) {
        return powF.sameAs(another.powF) &&
               sinF.sameAs(another.sinF) && 
               cosF.sameAs(another.cosF);
    }
    
    public boolean biggerThan(Term another) {
        return this.numF.biggerThan(another.numF);
    }
}
