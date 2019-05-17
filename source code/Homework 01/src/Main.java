import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String polynomial = scanner.nextLine();
        checkFormat(polynomial);
        polynomial = polynomial.replaceAll("[ \\t]","");
        List<Term> list = getList(polynomial);        
        List<Term> diff = new ArrayList<Term>();
        for (int i = 0; i < list.size(); i++) {
            diff.add(list.get(i).diff());
        }
        sortList(diff);
        printList(diff);
        scanner.close();
        
    }
    
    private static void checkFormat(String str) {
        if (str.length() > 1000 || str.length() == 0) {
            System.out.println("WRONG FORMAT!");
            System.exit(0);
        }
        String regex1 = ".*(?<=[\\^+-][ \\t]{0,1000}[+-])([ \\t]++)(?!x).*";
        String regex2 = ".*(?<=[0-9])[ \\t]{1,1000}(?=[0-9]).*";
        String regex3 = "[ \\t]*";

        if (str.matches(regex1) || str.matches(regex2) || str.matches(regex3)) {
            System.out.println("WRONG FORMAT!");
            System.exit(0);
        } else {
            return;
        }
    }
    
    private static int getOpIndex(String str, int start) {
        int counter = 1;
        int index = start;
        char c;
        while ((str.charAt(index) != '+' && str.charAt(index) != '-')
                || counter != 0) {
            c = str.charAt(index);
            if (c >= '0' && c <= '9') {
                counter = 0;
            } else if (c == 'x') {
                counter = 0;
            } else if (c == '^') {
                counter = 1;
            } else if (c == '+' || c == '-') {
                counter = 0;
            } else {
                //c==* do nothing
            }
            if (index != str.length() - 1) {
                index++;
            } else {
                return index + 1;
            }
        }
        return index;
    }

    private static void sortList(List<Term> diff) {
        diff.sort(new Comparator<Term>() {
            @Override
            public int compare(Term t1, Term t2) {
                if (t1.getCoe().compareTo(t2.getCoe()) == 1) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
    }
    
    private static void printList(List<Term> diff) {
        String output = "";
        for (int i = 0; i < diff.size(); i++) {
            output += diff.get(i).toString();
        }
        if (output.length() == 0) {
            output += "0";
        } else if (output.charAt(0) == '+') {
            output = output.substring(1, output.length());
        }
        System.out.println(output);
    }
    
    private static List<Term> getList(String polynomial) {
        int indexStart;
        int indexEnd;
        char op;
        if (polynomial.length() == 1) {
            //x 1
            indexStart = 0;
            indexEnd = 1;
            op = '+';
        } else if ((polynomial.charAt(1) == '+' || 
                polynomial.charAt(1) == '-') && (polynomial.charAt(0) == '+' || 
                polynomial.charAt(0) == '-')) {
            indexStart = 1;
            indexEnd = getOpIndex(polynomial, indexStart);
            op = polynomial.charAt(0);
        } else {
            indexStart = 0;
            indexEnd = getOpIndex(polynomial, indexStart);
            op = '+';
        }
        Term first = new Term(polynomial.substring(indexStart,indexEnd));
        first = first.updateOp(op);
        List<Term> list = new ArrayList<Term>();
        list.add(first);
        int length = polynomial.length();
        while (indexEnd != length) {
            op = polynomial.charAt(indexEnd);
            indexStart = indexEnd + 1;
            indexEnd = getOpIndex(polynomial, indexStart);
            Term tmp = new Term(polynomial.substring(indexStart,indexEnd));
            tmp = tmp.updateOp(op);
            int i;
            for (i = 0; i < list.size(); i++) {
                if (list.get(i).getExp().equals(tmp.getExp())) {
                    list.get(i).updateTerm(tmp);
                    break;
                }
            }
            if (i == list.size()) {
                list.add(tmp);
            }
        }
        return list;
    }

}
