package math;

import java.math.BigInteger;
import java.util.ArrayList;

public class Term {
    private ArrayList<Divisor> term;
    
    public Term() {
        term = new ArrayList<Divisor>();
    }
    
    public Term(ArrayList<Divisor> term) {
        this.term = term;
    }
    
    public Expression diff() {
        ArrayList<Term> tmpR = new ArrayList<Term>();
        for (int i = 0; i < term.size(); i++) {
            Expression tmpE = term.get(i).diff();
            if (tmpE.isEmpty()) {
                continue;
            }
            Term tmpT = this.clone();
            tmpT.term.remove(i);
            ArrayList<Divisor> tmpL = tmpT.term;
            tmpL = tmpE.mul(tmpL);
            tmpR.add(new Term(tmpL));
        }
        return new Expression(tmpR);
    }
    
    @Override
    public String toString() {
        //term size should not be 0
        if (term.size() == 0) {
            return "";
        }
        String str = "";
        for (int i = 0; i < term.size(); i++) {
            str += term.get(i).toString() + "*";
        }
        str = str.substring(0, str.length() - 1);
        return str;        
    }
    
    @Override
    public Term clone() {
        ArrayList<Divisor> tmpL = new ArrayList<Divisor>();
        for (Divisor d : term) {
            tmpL.add(d.clone());
        }
        return new Term(tmpL);
    }
    
    public boolean isEmpty() {
        return term.isEmpty();
    }
    
    public void update(char op) {
        if (op == '+') {
            for (Divisor d : term) {
                if (d instanceof Number) {
                    d.mul(BigInteger.ONE);
                    return;
                } else {
                    continue;
                }
            }
            term.add(new Number(BigInteger.ONE));
        } else {
            for (Divisor d : term) {
                if (d instanceof Number) {
                    d.mul(BigInteger.ONE.negate());
                    return;
                } else {
                    continue;
                }
            }
            term.add(new Number(BigInteger.ONE.negate()));
        }
    }

    public void add(Divisor d) {
        if (term.size() == 0) {
            term.add(d);
            return;
        } else {
            for (Divisor dt : term) {
                if (dt instanceof Number && d instanceof Number) {
                    dt.mul((Number)d);
                    return;
                } else if (dt instanceof Power && d instanceof Power) {
                    dt.mul((Power)d);
                    return;
                } else if (dt instanceof Sin && d instanceof Sin) {
                    if (dt.typeOf((Sin)d)) {
                        dt.mul((Sin)d);
                        return;
                    }
                } else if (dt instanceof Cos && d instanceof Cos) {
                    if (dt.typeOf((Cos)d)) {
                        dt.mul((Cos)d);
                        return;
                    }
                }
            }
            term.add(d);
            return;
        }
    }

    public ArrayList<Term> expand() {     
        if (isEmpty()) {
            return new ArrayList<Term>();
        }
        for (Divisor d : term) {
            ArrayList<Term> retList = new ArrayList<Term>();
            if (d instanceof Expression) {
                for (Term t : ((Expression)d).getExp()) {
                    ArrayList<Divisor> tmp = new ArrayList<Divisor>();
                    ArrayList<Divisor> list = term;
                    list.remove(d);
                    tmp.addAll(t.getTerm());
                    tmp.addAll(list);
                    retList.add(new Term(tmp));
                }
                return retList;
            } else {
                retList.add(this);
                return retList;
            } 
        }
        return new ArrayList<Term>();
    }
    
    public boolean isZero() {
        for (Divisor d : term) {
            if (d.isZero()) {
                return true;
            }
        }
        return false;
    }
    
    public ArrayList<Divisor> getTerm() {
        return term;
    }
}
