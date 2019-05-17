import com.oocourse.specs2.AppRunner;

public class Main {

    public static void main(String[] args) throws Exception {
        AppRunner runner = 
                AppRunner.newInstance(MyPath2.class, MyGraph2.class);
        runner.run(args);
    }

}
