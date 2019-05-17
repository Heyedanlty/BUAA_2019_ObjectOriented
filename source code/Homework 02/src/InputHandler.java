import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputHandler {
    
    private static final String powreg = "x(\\^[+-]?\\d++)?+";
    private static final String trireg = "(sin|cos)\\(x\\)(\\^[+-]?\\d++)?+";
    private static final String numreg = "[+-]?\\d++";
    private static final String numstart = "[+-]?+";
    private static final String divreg = "(" + powreg + "|" + 
            trireg + "|" + numreg + ")"; 
    
    static Expression getExp() { 
        String str = getInput();
        str = checkSpaces(str);
        ArrayList<Term> tmplist = new ArrayList<Term>();
        StringBuilder sbl = new StringBuilder(str);
        Term first = getFirstTerm(sbl);
        tmplist.add(first);
        while (sbl.length() != 0) {
            Term tmp = getOtherTerm(sbl);
            tmplist.add(tmp);
        }
        return new Expression(tmplist);
    }
    
    static String getInput() {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) {
            wrong();
        }
        String input = scanner.nextLine();
        scanner.close();
        return input;
    }
    
    static String checkSpaces(String str) {
        String regex1 = "[ \\t]*";
        String regex2 = ".*(?<=[a-z])[ \\t]+(?=[a-z]).*";
        String regex3 = ".*(?<=[\\^\\*][ \\t]{0,100}[+-])[ \\t]++(?=\\d).*";
        String regex4 = ".*(?<=[+-][ \\t]{0,100}[+-][ \\t]{0,100}[+-])"
                + "[ \\t]++(?=\\d).*";
        String regex5 = ".*(?<=[0-9])[ \\t]+(?=[0-9]).*";
        if (str.matches(regex1) || str.matches(regex2) || 
            str.matches(regex3) || str.matches(regex4) || str.matches(regex5)) {
            wrong();
        }
        return str.replaceAll("[ \\t]","");
    }
      
    static Term getFirstTerm(StringBuilder sbl) {
        
        String firTerm = "[+-]?" + numstart + divreg + "(\\*" + divreg + ")*+";
        Pattern p = Pattern.compile(firTerm);
        Matcher m = p.matcher(sbl.toString());
        if (!m.find()) {
            wrong();
        } else if (m.start() != 0) {
            wrong();
        }
        Term tmp = new Term(m.group());
        sbl.delete(0, m.end());
        return tmp;
    }
    
    static Term getOtherTerm(StringBuilder sbl) {
        String othTerm = "[+-]" + numstart + divreg + "(\\*" + divreg + ")*+";
        Pattern p = Pattern.compile(othTerm);
        Matcher m = p.matcher(sbl.toString());
        if (!m.find()) {
            wrong();
        } else if (m.start() != 0) {
            wrong();
        }
        Term tmp = new Term(m.group());
        sbl.delete(0, m.end());
        return tmp;
    }
    
    static void wrong() {
        System.out.println("WRONG FORMAT!");
        System.exit(0);
    }
    
}
