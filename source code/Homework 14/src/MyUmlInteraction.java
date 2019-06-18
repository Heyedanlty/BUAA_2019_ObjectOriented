import java.util.HashMap;

import com.oocourse.uml2.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml2.models.elements.UmlInteraction;
import com.oocourse.uml2.models.elements.UmlLifeline;
import com.oocourse.uml2.models.elements.UmlMessage;

public class MyUmlInteraction {
    
    private HashMap<String, MyUmlLifeline> llMap;//id2lifeline
    private HashMap<String, Integer> llCount;//id2llCount
    private HashMap<String, MyUmlLifeline> llNameMap;//name2lifeline
    
    private int messageCount;

    public MyUmlInteraction(UmlInteraction e) {
        llMap = new HashMap<>();
        llCount = new HashMap<>();
        llNameMap = new HashMap<>();
        messageCount = 0;
    }

    public void addLifeline(UmlLifeline e) {
        String id = e.getId();
        String name = e.getName();
        MyUmlLifeline tmp = new MyUmlLifeline(e);
        llMap.put(id, tmp);
        llNameMap.put(name, tmp);
        if (llCount.containsKey(name)) {
            Integer count = llCount.get(name);
            llCount.replace(name, count, count + 1);
        } else {
            llCount.put(name, 1);
        }
    }
    
    public void addMessage(UmlMessage e) {
        String target = e.getTarget();
        if (llMap.containsKey(target)) {
            llMap.get(target).addMessage(e);
        }
        messageCount++;
    }
    
    public int getParticipantCount() {
        return llMap.size();
    }
    
    public int getMessageCount() {
        return messageCount;
    }
    
    public int getIncomingCount(String interName, String llName)
            throws LifelineNotFoundException, LifelineDuplicatedException {
        if (!llCount.containsKey(llName)) {
            throw new LifelineNotFoundException(interName, llName);
        } else if (llCount.get(llName) > 1) {
            throw new LifelineDuplicatedException(interName, llName);
        } else {
            return llNameMap.get(llName).getIncomingCount();
        }
    }
}
