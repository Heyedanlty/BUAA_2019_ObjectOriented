import java.util.ArrayList;
import java.util.List;

import com.oocourse.uml2.models.elements.UmlLifeline;
import com.oocourse.uml2.models.elements.UmlMessage;

public class MyUmlLifeline {

    private List<UmlMessage> receiveMessageList;
    
    public MyUmlLifeline(UmlLifeline e) {
        receiveMessageList = new ArrayList<>();
    }
    
    public void addMessage(UmlMessage e) {
        receiveMessageList.add(e);
    }
    
    public int getIncomingCount() {
        return receiveMessageList.size();
    }
}
