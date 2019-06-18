import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml2.interact.format.UmlStateChartInteraction;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlFinalState;
import com.oocourse.uml2.models.elements.UmlPseudostate;
import com.oocourse.uml2.models.elements.UmlRegion;
import com.oocourse.uml2.models.elements.UmlState;
import com.oocourse.uml2.models.elements.UmlStateMachine;
import com.oocourse.uml2.models.elements.UmlTransition;

public class MyUmlStateChartInteraction implements UmlStateChartInteraction {
    private HashMap<String, MyUmlStateMachine> smMap;//id2sm
    private HashMap<String, Integer> smCount;//name2smCount
    private HashMap<String, MyUmlStateMachine> smNameMap;//name2sm
    
    private HashMap<String, UmlRegion> regionMap;//id2region

    public MyUmlStateChartInteraction(ArrayList<UmlElement> stateChartList) {
        smMap = new HashMap<>();
        smCount = new HashMap<>();
        smNameMap = new HashMap<>();
        regionMap = new HashMap<>();
        
        List<UmlElement> othersList = new ArrayList<>();
        for (UmlElement e : stateChartList) {            
            if (e instanceof UmlStateMachine) {
                String id = e.getId();
                String name = e.getName();
                MyUmlStateMachine tmp = new MyUmlStateMachine();
                smMap.put(id, tmp);
                smNameMap.put(name, tmp);
                if (smCount.containsKey(name)) {
                    Integer count = smCount.get(name);
                    smCount.replace(name, count, count + 1);
                } else {
                    smCount.put(name, 1);
                }
            } else if (e instanceof UmlRegion) {
                regionMap.put(e.getId(), (UmlRegion) e);
            } else if (e instanceof UmlPseudostate) {
                othersList.add(e);
            } else if (e instanceof UmlFinalState) {
                othersList.add(e);
            } else if (e instanceof UmlState) {
                othersList.add(e);
            } else if (e instanceof UmlTransition) {
                othersList.add(e);
            }            
        }
        for (UmlElement e : othersList) {
            String smId = regionMap.get(e.getParentId()).getParentId();
            smMap.get(smId).addElement(e);
        }
    }

    @Override
    public int getStateCount(String smName)
        throws StateMachineNotFoundException, StateMachineDuplicatedException {
        if (!smCount.containsKey(smName)) {
            throw new StateMachineNotFoundException(smName);
        } else if (smCount.get(smName) > 1) {
            throw new StateMachineDuplicatedException(smName);
        } else {
            return smNameMap.get(smName).getStateCount();
        }
    }

    @Override
    public int getSubsequentStateCount(String smName, String stateName)
        throws StateMachineNotFoundException, StateMachineDuplicatedException,
        StateNotFoundException, StateDuplicatedException {
        if (!smCount.containsKey(smName)) {
            throw new StateMachineNotFoundException(smName);
        } else if (smCount.get(smName) > 1) {
            throw new StateMachineDuplicatedException(smName);
        } else {
            return smNameMap.get(smName).getSubStateCount(smName, stateName);
        }
    }

    @Override
    public int getTransitionCount(String smName)
        throws StateMachineNotFoundException, StateMachineDuplicatedException {
        if (!smCount.containsKey(smName)) {
            throw new StateMachineNotFoundException(smName);
        } else if (smCount.get(smName) > 1) {
            throw new StateMachineDuplicatedException(smName);
        } else {
            return smNameMap.get(smName).getTransitionCount();
        }
    }

}
