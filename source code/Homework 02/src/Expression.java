import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class Expression implements IsNull {
    private ArrayList<Term> exp;

    Expression(ArrayList<Term> exp) {
        this.exp = exp;
    }
    
    public ArrayList<Term> diff() {
        ArrayList<Term> diff = new ArrayList<Term>();
        if (isNull()) {
            return null;
        }
        for (int i = 0; i < exp.size(); i++) {
            diff.addAll(exp.get(i).diff());
        }
        return diff;
    }
    
    public String toString() {
        if (isNull()) {
            return "0";
        }
        String str = "";
        for (int i = 0; i < exp.size(); i++) {
            str += exp.get(i).toString();
        }
        if (str.startsWith("+")) {
            str = str.substring(1,str.length());
        }
        if (str.length() == 0) {
            return "0";
        }
        return str;
    }
    
    public boolean isNull() {
        return exp.isEmpty();
    }
    
    void sort(List<Term> exp) {
        exp.sort(new Comparator<Term>() {
            @Override
            public int compare(Term t1, Term t2) {
                if (t1.biggerThan(t2)) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
    }
    
    Expression merge() {
        if (exp.size() == 0 || exp.size() == 1) {
            return this;
        } else {
            ArrayList<Term> newone = new ArrayList<Term>();
            for (int i = 0; i < exp.size(); i++) {
                Term t = exp.get(i);
                for (int j = i + 1; j < exp.size(); j++) {
                    if (t.sameAs(exp.get(j))) {
                        t.update(exp.get(j));
                        exp.remove(j);
                        j--;
                    }
                }
                newone.add(t);
            }
            return new Expression(newone);
        }
    }

    Expression tidy() {
        ArrayList<Term> newone = new ArrayList<Term>();
        for (int i = 0; i < exp.size(); i++) {
            if (!exp.get(i).isNull()) {
                newone.add(exp.get(i));
            }
        }
        return new Expression(newone);
    }

    ArrayList<Term> getList() {
        return exp;
    }
}
