
public class Main {

    public static void main(String[] args) {
        Expression exp = InputHandler.getExp();
        exp = new Expression(exp.diff());
        exp = exp.merge();
        exp = exp.tidy();
        exp.sort(exp.getList());
        OutputHandler.putExp(exp);
    }

}
