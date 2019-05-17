import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import com.oocourse.elevator3.PersonRequest;

public class Scheduler implements Runnable {
    private ArrayList<Elevator> eleList;
    private ArrayList<PersonRequest> upList;
    private ArrayList<PersonRequest> downList;
    private ArrayList<PersonRequest> bufferList;
    private StopFlag sf;
    private StopFlag esf;
    private Random rand;
    
    Scheduler(int amount, 
            ArrayList<PersonRequest> upList, 
            ArrayList<PersonRequest> downList,
            StopFlag sf, StopFlag esf) {
        eleList = new ArrayList<Elevator>();
        this.upList = upList;
        this.downList = downList;
        this.bufferList = new ArrayList<PersonRequest>();
        this.sf = sf;
        this.esf = esf;
        //random seed for rand
        Date date = new Date();
        long timeMill = date.getTime();
        rand = new Random(timeMill);
    }
    
    public void addElevator(Elevator e) {
        synchronized (eleList) {
            eleList.add(e);
        }
    }
    
    private void insert(PersonRequest pr, ArrayList<PersonRequest> list) {
        synchronized (list) {
            list.add(pr);
        }
    }
    
    private Elevator splitPr(PersonRequest pr) {
        int from = pr.getFromFloor();
        int to = pr.getToFloor();
        int pid = pr.getPersonId();
        PersonRequest pr1;
        PersonRequest pr2;
        if (isA(from)) {
            if (from < 0) {
                //from == -3 change at 1
                pr1 = new PersonRequest(from,1,pid);
                pr2 = new PersonRequest(1,to,pid);
            } else {
                //from > 15 change at 15
                pr1 = new PersonRequest(from,15,pid);
                pr2 = new PersonRequest(15,to,pid);
            }
            eleList.get(0).addReq(pr1);
            insert(pr2,bufferList);
            return eleList.get(0);
        } else if (isB(from) && isA(to)) {
            if (to < 0) {
                //to == -3 change at 1
                pr1 = new PersonRequest(from,1,pid);
                pr2 = new PersonRequest(1,to,pid);
            } else {
                // to > 15 change at 15
                pr1 = new PersonRequest(from,15,pid);
                pr2 = new PersonRequest(15,to,pid);
            }
            eleList.get(1).addReq(pr1);
            insert(pr2,bufferList);
            return eleList.get(1);
        } else if (isB(from) && isC(to)) {
            if (from < 3) {
                //from < 3 change at 1
                pr1 = new PersonRequest(from,1,pid);
                pr2 = new PersonRequest(1,to,pid);                
            } else {
                //from > 3 change at 5
                pr1 = new PersonRequest(from,5,pid);
                pr2 = new PersonRequest(5,to,pid);
            }
            eleList.get(1).addReq(pr1);
            insert(pr2,bufferList);
            return eleList.get(1);
        } else if (isC(from) && isA(to)) {
            //3 -> -1-2-3 16-20
            if (to < 0) {
                //change at 1
                pr1 = new PersonRequest(from,1,pid);
                pr2 = new PersonRequest(1,to,pid);
            } else {
                //change at 15
                pr1 = new PersonRequest(from,15,pid);
                pr2 = new PersonRequest(15,to,pid);                
            }
            eleList.get(2).addReq(pr1);
            insert(pr2,bufferList);
            return eleList.get(2);
        } else if (isC(from) && isB(to)) {
            //3 -> 246...14 change at 1
            pr1 = new PersonRequest(from,1,pid);
            pr2 = new PersonRequest(1,to,pid);
            eleList.get(2).addReq(pr1);
            insert(pr2,bufferList);
            return eleList.get(2);
        }
        return eleList.get(2);
    }
    
    private Elevator handlePr(PersonRequest pr) {
        int num;
        Elevator e;
        if (isA(pr) && isB(pr) && isC(pr)) {
            num = rand.nextInt(128);
            if (num < 45) {
                e = eleList.get(0);
            } else if (num < 93) {
                e = eleList.get(1);
            } else {
                e = eleList.get(2);
            }
            e.addReq(pr);
        } else if (isA(pr) && isB(pr) && !isC(pr)) {
            num = rand.nextInt(93);
            if (num < 45) {
                e = eleList.get(0);
            } else {
                e = eleList.get(1);
            }
            e.addReq(pr);
        } else if (isA(pr) && !isB(pr) && isC(pr)) {
            num = rand.nextInt(80);
            if (num < 45) {
                e = eleList.get(0);
            } else {
                e = eleList.get(2);
            }
            e.addReq(pr);
        } else if (isA(pr) && !isB(pr) && !isC(pr)) {
            e = eleList.get(0);//A
            e.addReq(pr);
        } else if (!isA(pr) && isB(pr) && isC(pr)) {
            num = rand.nextInt(83);
            if (num < 48) {
                e = eleList.get(1);
            } else {
                e = eleList.get(2);
            }
            e.addReq(pr);
        } else if (!isA(pr) && isB(pr) && !isC(pr)) {
            e = eleList.get(1);//B
            e.addReq(pr);
        } else if (!isA(pr) && !isB(pr) && isC(pr)) {
            e = eleList.get(2);//C
            e.addReq(pr);
        } else {
            return splitPr(pr);
        }
        return e;
    }
    
    @Override
    public void run() {
        while (true) {
            while (!upList.isEmpty()) {
                synchronized (upList) {
                    if (!upList.isEmpty()) {
                        handlePr(upList.get(0));
                        upList.remove(0);
                    }
                }   
            }
            while (!downList.isEmpty()) {
                synchronized (downList) {
                    if (!downList.isEmpty()) {
                        int size = downList.size();
                        handlePr(downList.get(size - 1));
                        downList.remove(size - 1);
                    }
                }                
            }
            for (Elevator e : eleList) {
                synchronized (e) {
                    e.notify();
                }
            }
            if (upList.isEmpty() && downList.isEmpty() &&
                    bufferList.isEmpty() && sf.getFlag()) {
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
    
    private boolean isA(int floor) {
        if (floor >= -3 && floor <= 1 ||
                floor >= 15 && floor <= 20) {
            return true;
        } else {
            return false;
        }
    }
    
    private boolean isA(PersonRequest pr) {
        int from = pr.getFromFloor();
        int to = pr.getToFloor();
        if (isA(from) && isA(to)) {
            return true;
        } else {
            return false;
        }
    }
    
    private boolean isB(int floor) {
        if (floor >= -2 && floor <= 2 ||
                floor >= 4 && floor <= 15) {
            return true;
        } else {
            return false;
        }
    }
    
    private boolean isB(PersonRequest pr) {
        int from = pr.getFromFloor();
        int to = pr.getToFloor();
        if (isB(from) && isB(to)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isC(int floor) {
        if (floor >= 1 && floor <= 15 && floor % 2 == 1) {
            return true;
        } else {
            return false;
        }
    }
    
    private boolean isC(PersonRequest pr) {
        int from = pr.getFromFloor();
        int to = pr.getToFloor();
        if (isC(from) && isC(to)) {
            return true;
        } else {
            return false;
        }
    }

    public void awakeP(int pid) {
        Elevator e = eleList.get(0);
        synchronized (bufferList) {
            for (int i = 0; i < bufferList.size(); i++) {
                PersonRequest pr = bufferList.get(i);
                if (pr.getPersonId() == pid) {
                    e = handlePr(pr);                    
                    bufferList.remove(pr);
                }
            }
        }
        synchronized (e) {
            e.notify();
        }
        synchronized (upList) {
            upList.notify();
        }
    }
}
