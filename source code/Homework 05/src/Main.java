import java.util.ArrayList;

import com.oocourse.TimableOutput;
import com.oocourse.elevator1.PersonRequest;

public class Main {
    private static final int AMOUNT = 1;//amount of elevators
    private static ArrayList<PersonRequest> upList;
    private static ArrayList<PersonRequest> downList;
    private static StopFlag sf;
    private static StopFlag esf;
    
    public static void main(String[] args) {
        /*initialize part*/
        TimableOutput.initStartTimestamp();
        upList = new ArrayList<PersonRequest>();
        downList = new ArrayList<PersonRequest>();
        sf = new StopFlag();
        esf = new StopFlag();
        
        /*start the input thread*/
        InputHandler inputHandler = new InputHandler(upList,downList,sf);
        Thread inputThread = new Thread(inputHandler);
        inputThread.start();
        
        /*start all elevators and scheduler*/
        Scheduler scheduler = new Scheduler(AMOUNT,upList,downList,sf,esf);
        for (int i = 0; i < AMOUNT; i++) {
            Elevator e = new Elevator(i + 1, esf);
            Thread elevatorThread = new Thread(e);
            scheduler.addElevator(i, e);
            elevatorThread.start();
        }
        Thread schedulerThread = new Thread(scheduler);
        schedulerThread.start();
    }
}
