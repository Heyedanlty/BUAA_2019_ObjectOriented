package io;

public class OutputHandler {
    public static void put(String str) {
        String out = str;
        if (out.length() == 0) {
            System.out.println("0");
            return;
        } else if (out.charAt(0) == '(') {
            out = out.substring(1,out.length() - 1);
        }
        out = out.replaceAll("--", "+");// -- -> +
        out = out.replaceAll("\\+-", "-");// +- -> -
        out = out.replaceAll("-\\+", "-");// -+ -> -
        out = out.replaceAll("\\*1(?!\\d)","");// *1 -> null
        out = out.replaceAll("\\+1\\*", "+");// +1* -> +
        out = out.replaceAll("(?<![\\^|\\*])-1\\*","-");// -1* -> -
        out = out.replaceAll("(?<![\\^-|\\*-|\\^|\\*])1\\*", "");// 1* -> null  
        System.out.println(out);
        return;
    }
}
