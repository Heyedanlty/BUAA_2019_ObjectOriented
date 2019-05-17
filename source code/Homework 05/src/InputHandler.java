import java.io.IOException;
import java.util.ArrayList;

import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;

public class InputHandler implements Runnable {
    private ArrayList<PersonRequest> upList;
    private ArrayList<PersonRequest> downList;
    private StopFlag sf;
    
    public InputHandler(ArrayList<PersonRequest> upList, 
            ArrayList<PersonRequest> downList,
            StopFlag sf) {
        this.upList = upList;
        this.downList = downList;
        this.sf = sf;
    }

    private void insert(PersonRequest pr, ArrayList<PersonRequest> list) {
        synchronized (list) {
            if (list.isEmpty()) {
                list.add(pr);
                return;
            } else {
                int i;
                for (i = 0; i < list.size(); i++) {
                    if (list.get(i).getFromFloor() < pr.getFromFloor()) {
                        continue;
                    }
                    break;
                }
                list.add(i,pr);
                return;
            }
        }
    }
    
    @Override
    public void run() {
        ElevatorInput eleInput = new ElevatorInput(System.in);
        while (true) {
            PersonRequest pr = eleInput.nextPersonRequest();
            if (pr == null) {
                break;
            } else {
                if (pr.getFromFloor() < pr.getToFloor()) {
                    //up
                    insert(pr,upList);
                } else {
                    //down
                    insert(pr,downList);
                }
            }
            Thread.yield();
        }        
        try {
            eleInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sf.setFlag();
    }
}
