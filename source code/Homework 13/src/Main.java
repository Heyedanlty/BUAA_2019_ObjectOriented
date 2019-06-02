import com.oocourse.uml1.interact.AppRunner;

public class Main {

    public static void main(String[] args) throws Exception {
        AppRunner appRunner = AppRunner.newInstance(MyUmlInteraction1.class);
        appRunner.run(args);
    }

}
