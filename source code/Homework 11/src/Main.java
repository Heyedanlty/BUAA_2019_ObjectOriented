import com.oocourse.specs3.AppRunner;

public class Main {
    public static void main(String[] args) throws Exception {
        AppRunner runner = 
                AppRunner.newInstance(MyPath3.class, MyRailwaySystem3.class);
        runner.run(args);
    }
}
