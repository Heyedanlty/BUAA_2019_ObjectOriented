package math;

import java.util.ArrayList;

public class Expression extends Divisor {
    private ArrayList<Term> exp;
    
    public Expression() {
        exp = new ArrayList<Term>();
        tidy();
    }
    
    public Expression(ArrayList<Term> exp) {
        this.exp = exp;
        tidy();
    }

    @Override
    public Expression diff() {
        Expression tmpR = new Expression();
        for (Term t : exp) {
            Expression tmpE = t.diff();
            tmpR.add(tmpE);
        }
        return tmpR;
    }
    
    @Override
    public String toString() {
        if (exp.isEmpty()) {
            return "0";
        } else if (exp.size() == 1) {
            String str = exp.get(0).toString();
            if (str.startsWith("+")) {
                return "(" + str.substring(1,str.length()) + ")";
            } else {
                return "(" + str + ")";
            }
        } else {
            String str = "(";
            for (int i = 0; i < exp.size(); i++) {
                str += exp.get(i).toString() + "+";
            }
            str = str.substring(0,str.length() - 1);
            str += ")";        
            return str;
        }
    }

    @Override
    public ArrayList<Divisor> mul(ArrayList<Divisor> list) {
        list.add(this);
        return list;
    }

    @Override
    public void mul(Divisor another) {}
    
    @Override
    public void mul(int value) {}
    
    public boolean typeOf(Expression another) {
        // if always return false, no side effects 
        return false;
    }

    public void add(Expression another) {
        this.exp.addAll(another.exp);
    }
    
    public void add(Term t) {
        exp.addAll(t.expand());
    }

    @Override
    public Expression clone() {
        ArrayList<Term> tmpL = new ArrayList<Term>();
        for (Term t : exp) {
            tmpL.add(t.clone());
        }
        return new Expression(tmpL);
    }
    
    public void expand() {
        ArrayList<Term> list = new ArrayList<Term>();
        for (Term t : exp) {
            ArrayList<Term> tmp = t.expand();
            list.addAll(tmp);
        }
        this.exp = list;
    }
    
    public boolean isEmpty() {
        return exp.isEmpty();
    }
    
    public void tidy() {
        for (int i = 0; i < exp.size(); i++) {
            if (exp.get(i).isZero()) {
                exp.remove(i);
                i--;
            }
        }
    }

    @Override
    public boolean isZero() {

        return false;
    }
    
    public ArrayList<Term> getExp() {
        return exp;
    }
}
