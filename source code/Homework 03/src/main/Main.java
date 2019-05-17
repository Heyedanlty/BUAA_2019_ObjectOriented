package main;

import io.InputHandler;
import io.OutputHandler;
import math.Expression;

public class Main {

    public static void main(String[] args) {
        Expression input = InputHandler.getInput();
        input.expand();
        Expression diff = input.diff();
        diff.expand();
        
        diff.tidy();
        String str = diff.toString();
        OutputHandler.put(str);
    }

}
