import java.util.ArrayList;

import com.oocourse.TimableOutput;
import com.oocourse.elevator1.PersonRequest;

public class Elevator implements Runnable {
    enum State {
        SLEEP,UP,DOWN
    }
    
    enum Direc {
        UP,DOWN,STILL
    }

    private final long doorTime = 250;
    private final long floorTime = 500;
    
    private StopFlag sf;
    
    private int id;//id of the Elevator
    private State state;//state of the Elevator
    private Direc direc;
    private int curFloor;//current floor of the Elevator
    private int dstFloor;//destiny of the Elevator
    
    private ArrayList<Person> personList;
    //up and down list buffer
    private ArrayList<PersonRequest> upList;
    private ArrayList<PersonRequest> downList;
    
    Elevator(int id, StopFlag sf) {
        this.id = id;
        this.state = State.SLEEP;
        this.direc = Direc.STILL;
        this.curFloor = 1;
        this.dstFloor = 1;       
        this.upList = new ArrayList<PersonRequest>();
        this.downList = new ArrayList<PersonRequest>();
        this.personList = new ArrayList<Person>();
        this.sf = sf;
    }
    
    public void addUpReq(PersonRequest pr) {
        insert(pr,upList);
    }
    
    public void addDownReq(PersonRequest pr) {
        insert(pr,downList);
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
    
    private void insert(Person p) {
        synchronized (personList) {  
            personList.add(p);
        }
    }
    
    private void updateStateAndDirec() {
        if (state == State.UP) {
            updateUp();
        } else {
            updateDown();
        }
    }

    private void updateUp() {
        if (direc == Direc.UP) {
            //man in elevator or not, shouldn't change state
            for (int i = 0; i < upList.size(); i++) {
                PersonRequest pr = upList.get(i);
                if (pr.getFromFloor() >= curFloor &&
                        pr.getToFloor() > dstFloor) {
                    dstFloor = pr.getToFloor();
                }
            }
            if (dstFloor == curFloor) {
                state = State.DOWN;
                direc = Direc.STILL;
            }
        } else if (direc == Direc.DOWN) {
            //the man is not in elevator, can change state to down
            if (!downList.isEmpty() && 
                    downList.get(0).getFromFloor() <= curFloor) {
                state = State.DOWN;
                dstFloor = downList.get(0).getToFloor();
                for (int i = 1; i < downList.size(); i++) {
                    PersonRequest pr = downList.get(i);
                    if (pr.getFromFloor() > curFloor) {
                        break;                           
                    }
                    if (pr.getToFloor() < dstFloor) {
                        dstFloor = pr.getToFloor();
                    }                       
                }
            } else {
                dstFloor = getLowFromUp();
                if (dstFloor == curFloor) {
                    direc = Direc.UP;
                    for (int i = 0; i < upList.size(); i++) {
                        PersonRequest pr = upList.get(i);
                        if (pr.getFromFloor() >= curFloor &&
                                pr.getToFloor() > dstFloor) {
                            dstFloor = pr.getToFloor();
                        }
                    }
                }
            }
        } else {
            int lowFloor = getLowFromUp();
            dstFloor = lowFloor;
            if (lowFloor < curFloor) {
                direc = Direc.DOWN;
            } else {
                direc = Direc.UP;
            }
        }
    }
    
    private void updateDown() {
        if (direc == Direc.DOWN) {
            //man in elevator or not, shouldn't change state
            for (int i = 0; i < downList.size(); i++) {
                PersonRequest pr = downList.get(i);
                if (pr.getFromFloor() <= curFloor &&
                        pr.getToFloor() < dstFloor) {
                    dstFloor = pr.getToFloor();
                }
            }
            if (dstFloor == curFloor) {
                state = State.UP;
                direc = Direc.STILL;
            }
        } else if (direc == Direc.UP) {
            //the man is not in elevator, can change state to up
            if (!upList.isEmpty() && 
                    upList.get(upList.size() - 1).getFromFloor() >= curFloor) {
                state = State.UP;
                dstFloor = upList.get(upList.size() - 1).getToFloor();
                for (int i = upList.size() - 2; i >= 0; i--) {
                    PersonRequest pr = upList.get(i);
                    if (pr.getFromFloor() < curFloor) {
                        break;                           
                    }
                    if (pr.getToFloor() > dstFloor) {
                        dstFloor = pr.getToFloor();
                    }                       
                }
            } else {
                dstFloor = getHighFromDown();
                if (dstFloor <= curFloor) {
                    direc = Direc.DOWN;
                    for (int i = 0; i < downList.size(); i++) {
                        PersonRequest pr = downList.get(i);
                        if (pr.getFromFloor() <= curFloor &&
                                pr.getToFloor() < dstFloor) {
                            dstFloor = pr.getToFloor();
                        }
                    }
                } else {
                    direc = Direc.UP;
                }
            }
        } else {
            int highFloor = getHighFromDown();
            dstFloor = highFloor;
            if (highFloor > curFloor) {
                direc = Direc.UP;                
            } else {
                direc = Direc.DOWN;
            }
        }
    }
    
    private int getLowFromUp() {
        int value = 15;
        for (int i = 0; i < upList.size(); i++) {
            if (upList.get(i).getFromFloor() < value) {
                value = upList.get(i).getFromFloor();
            }
        }
        return value;
    }
    
    private int getHighToUp() {
        int value = 1;
        for (int i = 0; i < upList.size(); i++) {
            if (upList.get(i).getToFloor() > value) {
                value = upList.get(i).getToFloor();
            }
        }
        return value;
    }
    
    private int getHighFromDown() {
        int value = 1;
        for (int i = 0; i < downList.size(); i++) {
            if (downList.get(i).getFromFloor() > value) {
                value = downList.get(i).getFromFloor();
            }
        }
        return value;
    }
    
    private int getLowToDown() {
        int value = 15;
        for (int i = 0; i < downList.size(); i++) {
            if (downList.get(i).getToFloor() < value) {
                value = downList.get(i).getToFloor();
            }
        }
        return value;
    }
    
    private void tryOpenDoor() throws Exception {
        if (state == State.UP) {
            if (direc == Direc.UP) {
                for (Person p : personList) {
                    if (p.getDst() == curFloor) {
                        openDoor(upList);
                        return;
                    }
                }
                for (PersonRequest pr : upList) {
                    if (pr.getFromFloor() == curFloor) {
                        openDoor(upList);
                        return;
                    }
                }
            } else {
                for (Person p : personList) {
                    if (p.getDst() == curFloor) {
                        openDoor();
                        return;
                    }
                }
                return;
            }            
        } else {
            if (direc == Direc.DOWN) {
                for (Person p : personList) {
                    if (p.getDst() == curFloor) {
                        openDoor(downList);
                        return;
                    }
                }
                for (PersonRequest pr : downList) {
                    if (pr.getFromFloor() == curFloor) {
                        openDoor(downList);
                        return;
                    }
                }
            } else {
                for (Person p : personList) {
                    if (p.getDst() == curFloor) {
                        openDoor();
                        return;
                    }
                }
            }
            
        }
    }
    
    private void openDoor() throws Exception {
        TimableOutput.println(String.format("OPEN-%d",curFloor));
        Thread.sleep(doorTime);
        
        synchronized (personList) {
            for (int i = 0; i < personList.size(); i++) {
                int dst = personList.get(i).getDst();
                if (dst == curFloor) {
                    int pid = personList.get(i).getId();
                    TimableOutput.println(
                            String.format("OUT-%d-%d",pid,curFloor));
                    personList.remove(i);
                    i--;
                }
            }
        }
                
        Thread.sleep(doorTime);
        TimableOutput.println(String.format("CLOSE-%d",curFloor));
        trySleep();
    }
    
    private void openDoor(ArrayList<PersonRequest> list) throws Exception {
        TimableOutput.println(String.format("OPEN-%d",curFloor));
        Thread.sleep(doorTime);
        
        synchronized (personList) {
            for (int i = 0; i < personList.size(); i++) {
                int dst = personList.get(i).getDst();
                if (dst == curFloor) {
                    int pid = personList.get(i).getId();
                    TimableOutput.println(
                            String.format("OUT-%d-%d",pid,curFloor));
                    personList.remove(i);
                    i--;
                }
            }
        }
        
        synchronized (list) {
            for (int i = 0; i < list.size(); i++) {
                PersonRequest pr = list.get(i);
                if (pr.getFromFloor() == curFloor) {
                    int pid = pr.getPersonId();
                    TimableOutput.println(
                            String.format("IN-%d-%d",pid,curFloor));
                    insert(new Person(pr));
                    list.remove(i);
                    i--;                    
                }
            }
        }
        
        Thread.sleep(doorTime);
        TimableOutput.println(String.format("CLOSE-%d",curFloor));
        trySleep();       
    }
    
    private boolean trySleep() {
        if (personList.isEmpty() && upList.isEmpty() && downList.isEmpty()) {
            state = State.SLEEP;
            return true;
        }
        return false;
    }
    
    @Override
    public void run() {
        outer : while (true) {
            while (state == State.SLEEP) {
                if (!upList.isEmpty()) {
                    state = State.UP;
                    dstFloor = getLowFromUp();
                    if (dstFloor < curFloor) {
                        direc = Direc.DOWN;
                    } else {
                        direc = Direc.UP;
                        dstFloor = getHighToUp();
                    }
                    break;
                }
                if (!downList.isEmpty()) {
                    state = State.DOWN;
                    dstFloor = getHighFromDown();
                    if (dstFloor > curFloor) {
                        direc = Direc.UP;
                    } else {
                        direc = Direc.DOWN;
                        dstFloor = getLowToDown();
                    }
                    break;
                }
                if (sf.getFlag() && 
                        upList.isEmpty() && 
                        downList.isEmpty() &&
                        personList.isEmpty()) {
                    break outer;                    
                }
                Thread.yield();
            }
            while (state != State.SLEEP) {
                updateStateAndDirec();
                try {
                    tryOpenDoor();
                    if (trySleep()) {
                        break;
                    }
                    if (direc == Direc.UP) {
                        Thread.sleep(floorTime);
                        curFloor++;
                    } else if (direc == Direc.DOWN) {
                        Thread.sleep(floorTime);
                        curFloor--;
                    } else {
                        if (upList.isEmpty() && downList.isEmpty()) {
                            state = State.SLEEP;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public boolean onWay(int i) {
        if (state != State.SLEEP) {
            if (state == State.UP && i > curFloor) {
                return true;
            } else if (state == State.DOWN && i < curFloor) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isUp() {
        return state == State.UP;
    }
    
    public boolean isDown() {
        return state == State.DOWN;
    }
    
    public boolean isSleep() {
        return state == State.SLEEP;
    }
    
    public int getDst() {
        return dstFloor;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
}
