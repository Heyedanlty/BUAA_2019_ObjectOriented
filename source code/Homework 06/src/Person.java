import com.oocourse.elevator2.PersonRequest;

public class Person {
    private int id;
    private int dst;
    
    Person(int id, int dst) {
        this.id = id;
        this.dst = dst;
    }
    
    Person(PersonRequest pr) {
        this.id = pr.getPersonId();
        this.dst = pr.getToFloor();
    }
    
    public int getId() {
        return id;
    }
    
    public int getDst() {
        return dst;
    }
}
