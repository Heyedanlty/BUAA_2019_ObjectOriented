import java.util.ArrayList;

import com.oocourse.elevator2.PersonRequest;

public class Scheduler implements Runnable {
    private ArrayList<Elevator> eleList;
    private ArrayList<PersonRequest> upList;
    private ArrayList<PersonRequest> downList;
    private StopFlag sf;
    private StopFlag esf;
    
    Scheduler(int amount, 
            ArrayList<PersonRequest> upList, 
            ArrayList<PersonRequest> downList,
            StopFlag sf, StopFlag esf) {
        eleList = new ArrayList<Elevator>();
        this.upList = upList;
        this.downList = downList;
        this.sf = sf;
        this.esf = esf;
    }
    
    public void addElevator(int i, Elevator e) {
        synchronized (eleList) {
            eleList.add(e);
        }
    }
    
    private Elevator chooseUpElevator(PersonRequest pr) {
        for (Elevator e : eleList) {
            if (e.onWay(pr)) {
                return e;
            }
        }
        for (Elevator e : eleList) {
            if (e.isSleep()) {
                return e;
            }
        }
        for (Elevator e : eleList) {
            if (e.isDown()) {
                return e;
            }
        }
        
        int minDst = 16;
        for (Elevator e : eleList) {
            int tmpDst = e.getDst();
            if (tmpDst < minDst) {
                minDst = tmpDst;
            }
        }
        for (Elevator e : eleList) {
            if (e.getDst() == minDst) {
                return e;
            }
        }
        return eleList.get(0);
    }
    
    private Elevator chooseDownElevator(PersonRequest pr) {
        for (Elevator e : eleList) {
            if (e.onWay(pr)) {
                return e;
            }
        }
        for (Elevator e : eleList) {
            if (e.isSleep()) {
                return e;
            }
        }
        for (Elevator e : eleList) {
            if (e.isUp()) {
                return e;
            }
        }
        
        int maxDst = 0;
        for (Elevator e : eleList) {
            int tmpDst = e.getDst();
            if (tmpDst > maxDst) {
                maxDst = tmpDst;
            }
        }
        for (Elevator e : eleList) {
            if (e.getDst() == maxDst) {
                return e;
            }
        }
        return eleList.get(0);
    }
    
    @Override
    public void run() {
        while (true) {
            while (!upList.isEmpty()) {
                synchronized (upList) {
                    if (!upList.isEmpty()) {
                        Elevator e = chooseUpElevator(upList.get(0));
                        e.addUpReq(upList.get(0));
                        upList.remove(0);
                    }
                }   
            }
            while (!downList.isEmpty()) {
                synchronized (downList) {
                    if (!downList.isEmpty()) {
                        int size = downList.size();
                        Elevator e = chooseDownElevator(downList.get(size - 1));
                        e.addDownReq(downList.get(size - 1));
                        downList.remove(size - 1);
                    }
                }                
            }
            for (Elevator e : eleList) {
                synchronized (e) {
                    e.notify();
                }
            }
            if (upList.isEmpty() && downList.isEmpty() && sf.getFlag()) {
                break;
            }
            synchronized (upList) {
                try {
                    upList.wait();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
            Thread.yield();
        }
        esf.setFlag();
        for (Elevator e : eleList) {
            synchronized (e) {
                e.notify();
            }
        }
    }

}
