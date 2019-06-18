import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.oocourse.uml2.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml2.interact.format.UmlCollaborationInteraction;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlInteraction;
import com.oocourse.uml2.models.elements.UmlLifeline;
import com.oocourse.uml2.models.elements.UmlMessage;

public class MyUmlCollaborationInteraction 
    implements UmlCollaborationInteraction {

    private HashMap<String, MyUmlInteraction> interMap;//id2Interaction
    private HashMap<String, Integer> interCount;//name2interCount
    private HashMap<String, MyUmlInteraction> interNameMap;//name2Interaction
    
    public MyUmlCollaborationInteraction(ArrayList<UmlElement> clList) {
        interMap = new HashMap<>();
        interCount = new HashMap<>();
        interNameMap = new HashMap<>();
        
        List<UmlLifeline> llList = new ArrayList<>();
        List<UmlMessage> msList = new ArrayList<>();
        for (UmlElement e : clList) {            
            if (e instanceof UmlInteraction) {
                String id = e.getId();
                String name = e.getName();
                MyUmlInteraction tmp = new MyUmlInteraction((UmlInteraction) e);
                interMap.put(id, tmp);
                interNameMap.put(name, tmp);                
                if (interCount.containsKey(name)) {
                    Integer count = interCount.get(name);
                    interCount.replace(name, count, count + 1);
                } else {
                    interCount.put(name, 1);
                }
            } else if (e instanceof UmlLifeline) {
                llList.add((UmlLifeline) e);
            } else if (e instanceof UmlMessage) {
                msList.add((UmlMessage) e);
            }
        }
        for (UmlLifeline ll : llList) {
            String parentId = ll.getParentId();
            if (interMap.containsKey(parentId)) {
                interMap.get(parentId).addLifeline(ll);
            }
        }
        for (UmlMessage ms : msList) {
            String parentId = ms.getParentId();
            if (interMap.containsKey(parentId)) {
                interMap.get(parentId).addMessage(ms);
            }
        }
    }

    @Override
    public int getIncomingMessageCount(String interactionName, String llName) 
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        if (!interCount.containsKey(interactionName)) {
            throw new InteractionNotFoundException(interactionName);
        } else if (interCount.get(interactionName) > 1) {
            throw new InteractionDuplicatedException(interactionName);
        } else {
            return interNameMap.get(interactionName).
                    getIncomingCount(interactionName, llName);
        }        
    }

    @Override
    public int getMessageCount(String interactionName)
           throws InteractionNotFoundException, InteractionDuplicatedException {
        if (!interCount.containsKey(interactionName)) {
            throw new InteractionNotFoundException(interactionName);
        } else if (interCount.get(interactionName) > 1) {
            throw new InteractionDuplicatedException(interactionName);
        } else {
            return interNameMap.get(interactionName).getMessageCount();
        }        
    }

    @Override
    public int getParticipantCount(String interactionName)
           throws InteractionNotFoundException, InteractionDuplicatedException {
        if (!interCount.containsKey(interactionName)) {
            throw new InteractionNotFoundException(interactionName);
        } else if (interCount.get(interactionName) > 1) {
            throw new InteractionDuplicatedException(interactionName);
        } else {
            return interNameMap.get(interactionName).getParticipantCount();
        }
    }

}
