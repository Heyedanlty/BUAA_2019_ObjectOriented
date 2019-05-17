import java.math.BigInteger;

class Term {
    
    private BigInteger coefficient;
    private BigInteger exponent;
    
    public Term() {
        coefficient = new BigInteger("0");
        exponent = new BigInteger("0");
    }
    
    public Term(BigInteger coe, BigInteger exp) {
        coefficient = coe;
        exponent = exp;
    }
    
    public Term(String str) {
        //we have finish format check before calling this function
        checkFormat(str);
        String term = str.replaceAll("[ \\t]", "");
        if (str.contains("x")) {
            if (!str.contains("*")) {
                if (str.charAt(0) == '-') {
                    coefficient = new BigInteger("-1");
                } else {
                    coefficient = new BigInteger("1");
                }
            } else {
                coefficient = new BigInteger(
                        term.substring(0, term.indexOf('*')));
            }
            if (!str.contains("^")) {
                exponent = new BigInteger("1");
            } else {
                exponent = new BigInteger(
                        term.substring(term.indexOf('^') + 1, term.length()));
            }
        } else {
            coefficient = new BigInteger(term);
            exponent = new BigInteger("0");   
        }
    }
    
    Term updateOp(char op) {
        if (op == '+') {
            //do nothing
        } else {
            coefficient = coefficient.negate();
        }
        return this;
    }
    
    Term updateTerm(Term another) {
        coefficient = coefficient.add(another.getCoe());
        return this;
    }
    
    BigInteger getCoe() {
        return coefficient;
    }
    
    BigInteger getExp() {
        return exponent;
    }
    
    Term diff() {
        Term tmpTerm = new Term(
                coefficient.multiply(this.getExp()),
                exponent.subtract(new BigInteger("1")));
        return tmpTerm;
    }
    
    private void checkFormat(String str) {
        //space <=> [ \\t]* (space and tab)
        String space = "[ \\t]*";
        // num <=> +n -n n
        String num = "[+-]?[0-9]+";
        //num
        String term1 = "(" + space + num + space + ")";
        //+x|-x|x
        String term2 = "(" + space + "[+-]?" + space + "x" + space + ")";
        //num*x
        String term3 = "(" + space + num + space + "\\*" + space + 
                "x" + space + ")";
        //+x|-x|x ^ num
        String term4 = "(" + space + "[+-]?" + space + 
                "x" + space + "\\^" + space + num + ")";
        //num * x ^ num
        String term5 = "(" + space + num + space + "\\*" + space + 
                "x" + space + "\\^" + space + num + ")";
        //term = |terms
        String term = "(" + term1 + "|" + term2 + "|" + 
                term3 + "|" + term4 + "|" + term5 + ")";
        if (str.matches(term)) {
            return;
        } else {
            System.out.println("WRONG FORMAT!");
            System.exit(0);
        }
    }
    
    public String toString() {
        String string = "";
        if (coefficient.toString().equals("0")) {
            //0 x^*
            return string;
        } else if (coefficient.toString().equals("1")) {
            string += "+";
            if (exponent.toString().equals("0")) {
                string += "1";
            } else if (exponent.toString().equals("1")) {
                string += "x";
            } else {
                string += "x^" + exponent.toString();
            }
        } else if (coefficient.toString().equals("-1")) {
            string += "-";
            if (exponent.toString().equals("0")) {
                string += "1";
            } else if (exponent.toString().equals("1")) {
                string += "x";
            } else {
                string += "x^" + exponent.toString();
            }
        } else if (coefficient.compareTo(new BigInteger("0")) == 1) {
            string += "+";
            if (exponent.toString().equals("0")) {
                string += coefficient.toString();
            } else if (exponent.toString().equals("1")) {
                string += coefficient.toString() + "*x";
            } else {
                string += coefficient.toString() + "*x^" + exponent.toString();
            }
        } else {
            //do nothing, because number has a '-'
            if (exponent.toString().equals("0")) {
                string += coefficient.toString();
            } else if (exponent.toString().equals("1")) {
                string += coefficient.toString() + "*x";
            } else {
                string += coefficient.toString() + "*x^" + exponent.toString();
            } 
        }
        return string;
    }
}
