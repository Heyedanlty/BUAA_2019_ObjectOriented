package io;

import math.Expression;
import math.Term;
import math.Divisor;
import math.Number;
import math.Power;
import math.Sin;
import math.Cos;

import java.math.BigInteger;
import java.util.Scanner;
/*
 * caution:
 * we should give each term a number(coe)
 * in case of "0*...", we delete it(term)
 */

public class InputHandler {
    
    private static String str;
    
    public static Expression getInput() {
        /*
         *  using recursive descent parsing to decode input string
         */
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) {
            wrongFormat();
        }
        str = scanner.nextLine();
        scanner.close();
        str = checkSpace(str);
        Expression exp = getAllExp();
        return exp;
    }
    
    public static void wrongFormat() {
        System.out.println("WRONG FORMAT!");
        System.exit(0);
    }

    private static String checkSpace(String str) {
        String regex1 = "[ \\t]*";
        String regex2 = ".*(?<=[a-z])[ \\t]+(?=[a-z]).*";
        String regex3 = ".*(?<=[\\^\\*][ \\t]{0,100}[+-])[ \\t]++(?=\\d).*";
        String regex4 = ".*(?<=[+-][ \\t]{0,100}[+-][ \\t]{0,100}[+-])"
                + "[ \\t]++(?=\\d).*";
        String regex5 = ".*(?<=[0-9])[ \\t]+(?=[0-9]).*";
        if (str.matches(regex1) || str.matches(regex2) || 
            str.matches(regex3) || str.matches(regex4) || 
            str.matches(regex5)) {
            wrongFormat();
        }
        return str.replaceAll("[ \\t]","");
    }

    private static void checkLength() {
        if (str.length() == 0) {
            wrongFormat();
        }
    }
    
    private static void cut() {
        checkLength();
        str = str.substring(1);
    }
    
    private static boolean jdg(char c) {
        checkLength();
        if (str.charAt(0) == c) {
            return true;
        } else {
            return false;
        }
    }
    
    private static boolean jdg() {
        checkLength();
        if (str.charAt(0) >= '0' && str.charAt(0) <= '9') {
            return true;
        } else {
            return false;
        }
    }
    
    private static Expression getAllExp() {
        checkLength();
        Expression ret = new Expression();
        Term t = new Term();
        char op = '+';
        if (jdg('+') || jdg('-')) {
            op = str.charAt(0);
            cut();
            t = getTerm();
            t.update(op);
        } else {
            //op = '+';
            t = getTerm();
        }
        ret.add(t);
        while (str.length() > 0) {
            if (jdg('+') || jdg('-')) {
                op = str.charAt(0);
                cut();
                Term tmp = getTerm();
                tmp.update(op);
                ret.add(tmp);
            } else {
                wrongFormat();
            }
        }
        ret.expand();
        return ret;
    }
    
    private static Expression getExpression() {
        checkLength();
        Expression ret = new Expression();
        Term t = new Term();
        char op = '+';
        if (jdg('+') || jdg('-')) {
            op = str.charAt(0);
            cut();
            t = getTerm();
            t.update(op);
        } else {
            //op = '+';
            t = getTerm();
        }
        ret.add(t);
        while (str.length() > 0) {
            if (jdg('+') || jdg('-')) {
                op = str.charAt(0);
                cut();
                Term tmp = getTerm();
                tmp.update(op);
                ret.add(tmp);
            } else {
                ret.expand();
                return ret;
            }
        }
        ret.expand();
        return ret;
    }
    
    private static Expression getExpDivisor() {
        checkLength();
        Expression retExp = new Expression();
        if (jdg('(')) {
            cut();
            retExp = getExpression();
            if (jdg(')')) {
                cut();
                return retExp;
            }
        }
        wrongFormat();
        return retExp;
    }
    
    private static Term getTerm() {
        checkLength();
        Term retTerm = new Term();
        Divisor d;
        char op = '+';
        if (jdg('+') || jdg('-')) {
            op = str.charAt(0);
            cut();
            d = getDivisor();
        } else {
            //op = '+';
            d = getDivisor();
        }
        retTerm.add(d);
        while (str.length() > 0) {
            if (jdg('*')) {
                cut();
                Divisor tmp = getDivisor();
                retTerm.add(tmp);
            } else {
                retTerm.update(op);
                return retTerm;
            }
        }
        retTerm.update(op);
        return retTerm;
    }
    
    private static Divisor getDivisor() {
        checkLength();
        if (jdg('(')) {
            Expression exp = getExpDivisor();
            return exp;
        } else if (jdg('+') || jdg('-') || jdg()) {           
            BigInteger value = getNumber();
            Number num = new Number(value);            
            return num;
        } else if (jdg('s')) {
            Sin sin = getSin();
            if (sin.isOne()) {
                return new Number(BigInteger.ONE);
            } else if (sin.isZero()) {
                return new Number(BigInteger.ZERO);
            } else {
                return sin;
            }  
        } else if (jdg('c')) {
            Cos cos = getCos();
            if (cos.isOne()) {
                return new Number(BigInteger.ONE);                
            } else if (cos.isZero()) {
                return new Number(BigInteger.ZERO);
            } else {
                return cos;
            }
        } else if (jdg('x')) {
            Power power = getPower();
            if (power.isOne()) {
                return new Number(BigInteger.ONE);
            }
            return power;
        } else {
            wrongFormat();
            return null;//this is not necessary
        }
    }
    
    private static BigInteger getNumber() {
        checkLength();
        char op = '+';
        BigInteger ret = new BigInteger("0");
        if (jdg('+') || jdg('-')) {
            op = str.charAt(0);
            cut();
        }
        if (jdg()) {
            ret = ret.multiply(BigInteger.TEN);
            ret = ret.add(new BigInteger(String.valueOf(str.charAt(0))));
            cut();
        } else {
            wrongFormat();
        }
        while (str.length() > 0 && jdg()) {
            ret = ret.multiply(BigInteger.TEN);
            ret = ret.add(new BigInteger(String.valueOf(str.charAt(0)))); 
            cut();
        }
        if (op == '-') {
            ret = ret.negate();
        }
        return ret;
    }
    
    private static Sin getSin() {
        checkLength();
        Divisor d;
        BigInteger power;
        if (jdg('s')) {
            cut();
            if (jdg('i')) {
                cut();
                if (jdg('n')) {
                    cut();
                    if (jdg('(')) {
                        cut();
                        d = getDivisor();
                        if (jdg(')')) {
                            cut();
                            power = getPowerPart();
                            Sin sin = new Sin(d,power);
                            return sin;
                        }
                    }
                }
            }
        }
        wrongFormat();
        return null;//this is not necessary
    }
    
    private static Cos getCos() {
        checkLength();
        Divisor d;
        BigInteger power;
        if (jdg('c')) {
            cut();
            if (jdg('o')) {
                cut();
                if (jdg('s')) {
                    cut();
                    if (jdg('(')) {
                        cut();
                        d = getDivisor();
                        if (jdg(')')) {
                            cut();
                            power = getPowerPart();
                            Cos cos = new Cos(d,power);
                            return cos;
                        }
                    }
                }
            }
        }
        wrongFormat();
        return null;//this is not necessary
    }
    
    private static Power getPower() {
        checkLength();
        if (jdg('x')) {
            cut();
            BigInteger power = getPowerPart();            
            Power pow = new Power(power);
            return pow;
        }
        wrongFormat();
        return null;//this is not necessary
    }
    
    private static BigInteger getPowerPart() {
        if (str.length() == 0) {
            return BigInteger.ONE;
        }
        if (jdg('^')) {
            cut();
            BigInteger pow = getNumber();
            if (pow.compareTo(new BigInteger("10000")) > 0 ||
                    pow.compareTo(new BigInteger("-10000")) < 0) {
                wrongFormat();
            }
            return pow;
        } else {
            return BigInteger.ONE;
        }
    }

}
