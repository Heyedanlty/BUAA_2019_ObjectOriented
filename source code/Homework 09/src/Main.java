import com.oocourse.specs1.AppRunner;

public class Main {
    public static void main(String[] args) throws Exception {
        AppRunner runner = 
                AppRunner.newInstance(MyPath1.class, MyPathContainer1.class);
        runner.run(args);        
    }
}
