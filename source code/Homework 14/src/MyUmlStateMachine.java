import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlFinalState;
import com.oocourse.uml2.models.elements.UmlPseudostate;
import com.oocourse.uml2.models.elements.UmlState;
import com.oocourse.uml2.models.elements.UmlTransition;

public class MyUmlStateMachine {
    private HashMap<String, UmlPseudostate> startMap;
    private HashMap<String, UmlFinalState> finalMap;
    
    private HashMap<String, UmlState> stateMap;
    private HashMap<String, Integer> stateCount;
    private HashMap<String, UmlState> stateNameMap;
    
    private HashMap<String, Integer> subCount;//name2subCount
    
    private HashMap<String, List<UmlTransition>> transMap;//id2trans
    
    private int countOfState;
    private int countOfTransition;
    
    public MyUmlStateMachine() {
        startMap = new HashMap<>();
        finalMap = new HashMap<>();
        
        stateMap = new HashMap<>();
        stateCount = new HashMap<>();
        stateNameMap = new HashMap<>();
        
        subCount = new HashMap<>();
        
        transMap = new HashMap<>();
        countOfState = -1;
        countOfTransition = 0;
    }

    public void addElement(UmlElement e) {
        List<UmlTransition> trList = new ArrayList<>();
        if (e instanceof UmlPseudostate) {
            startMap.put(e.getId(), (UmlPseudostate) e);
        } else if (e instanceof UmlFinalState) {
            finalMap.put(e.getId(), (UmlFinalState) e);
        } else if (e instanceof UmlState) {
            String id = e.getId();
            String name = e.getName();
            stateMap.put(id, (UmlState) e);
            stateNameMap.put(name, (UmlState) e);
            if (stateCount.containsKey(name)) {
                Integer count = stateCount.get(name);
                stateCount.replace(name, count, count + 1);
            } else {
                stateCount.put(name, 1);
            }
        } else if (e instanceof UmlTransition) {
            trList.add((UmlTransition) e);
        }
        for (UmlTransition tr : trList) {
            countOfTransition++;
            String from = tr.getSource();
            if (transMap.containsKey(from)) {
                transMap.get(from).add(tr);
            } else {
                List<UmlTransition> tmp = new ArrayList<>();
                tmp.add(tr);
                transMap.put(from, tmp);
            }
        }
    }
    
    public int getStateCount() {
        if (countOfState == -1) {
            countOfState = stateMap.size();
            if (startMap.size() != 0) {
                countOfState++;
            }
            if (finalMap.size() != 0) {
                countOfState++;
            }
            return countOfState;
        } else {
            return countOfState;
        }
    }

    public int getSubStateCount(String smName, String stateName)
            throws StateNotFoundException, StateDuplicatedException {
        if (!stateCount.containsKey(stateName)) {
            throw new StateNotFoundException(smName, stateName);
        } else if (stateCount.get(stateName) > 1) {
            throw new StateDuplicatedException(smName, stateName);
        } else {
            if (subCount.containsKey(stateName)) {
                return subCount.get(stateName);
            } else {
                countSubCount(stateName);
                return subCount.get(stateName);
            }
        }
    }
    
    private void countSubCount(String stateName) {
        HashMap<String, Boolean> visited = getVisitedInit();
        ArrayList<String> list = new ArrayList<>();        
        String startId = stateNameMap.get(stateName).getId();
        if (transMap.containsKey(startId)) {
            {
                List<UmlTransition> nextList = transMap.get(startId);
                for (UmlTransition tr : nextList) {
                    String target = tr.getTarget();
                    if (visited.get(target) == false) {
                        list.add(target);
                        visited.replace(target, false, true);
                    } else {
                        continue;
                    }
                }
            }            
            int countOfSub = 0;
            boolean startFlag = false;
            boolean finalFlag = false;
            while (list.size() != 0) {
                String currentId = list.remove(0);
                if (startMap.containsKey(currentId)) {
                    startFlag = true;
                } else if (finalMap.containsKey(currentId)) {
                    finalFlag = true;
                } else {
                    countOfSub++;
                }
                if (transMap.containsKey(currentId)) {
                    List<UmlTransition> nextList = transMap.get(currentId);
                    for (UmlTransition tr : nextList) {
                        String target = tr.getTarget();
                        if (visited.get(target) == false) {
                            list.add(target);
                            visited.replace(target, false, true);
                        } else {
                            continue;
                        }
                    }
                } else {
                    continue;
                }
            }
            if (startFlag) {
                countOfSub++;
            }
            if (finalFlag) {
                countOfSub++;
            }
            subCount.put(stateName, countOfSub);
            return;
        } else {
            subCount.put(stateName, 0);
            return;
        }
    }
    
    private HashMap<String, Boolean> getVisitedInit() {
        HashMap<String, Boolean> visited = new HashMap<>();
        for (String id : stateMap.keySet()) {
            visited.put(id, false);
        }
        for (String id : startMap.keySet()) {
            visited.put(id, false);
        }
        for (String id : finalMap.keySet()) {
            visited.put(id, false);
        }
        return visited;
    }
    
    public int getTransitionCount() {
        return countOfTransition;
    }
}
