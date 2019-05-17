import java.util.ArrayList;

import com.oocourse.TimableOutput;
import com.oocourse.elevator3.PersonRequest;

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
        
        { //start A
            Elevator e = new Elevator(scheduler,esf,"A",400,6);
            Thread elevatorThread = new Thread(e);
            scheduler.addElevator(e);
            elevatorThread.start();
        }
        { //start B
            Elevator e = new Elevator(scheduler,esf,"B",500,8);
            Thread elevatorThread = new Thread(e);
            scheduler.addElevator(e);
            elevatorThread.start();
        }
        { //start C
            Elevator e = new Elevator(scheduler,esf,"C",600,7);
            Thread elevatorThread = new Thread(e);
            scheduler.addElevator(e);
            elevatorThread.start();
        }
        
        Thread schedulerThread = new Thread(scheduler);
        schedulerThread.start();
    }
}
