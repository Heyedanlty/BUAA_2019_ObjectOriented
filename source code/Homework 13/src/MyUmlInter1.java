import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.oocourse.uml1.models.elements.UmlElement;
import com.oocourse.uml1.models.elements.UmlGeneralization;
import com.oocourse.uml1.models.elements.UmlInterface;
import com.oocourse.uml1.models.elements.UmlOperation;
import com.oocourse.uml1.models.elements.UmlParameter;

public class MyUmlInter1 {    
    private UmlInterface umlInterface;

    private HashMap<String, MyUmlOperation1> opeMap;
    
    private ArrayList<UmlGeneralization> genList;
    
    public MyUmlInter1(UmlInterface it) {
        umlInterface = it;
        opeMap = new HashMap<>();
        genList = new ArrayList<>();
    }
    
    public void addElement(UmlElement e) {
        if (e instanceof UmlOperation) {
            MyUmlOperation1 tmpOp = new MyUmlOperation1((UmlOperation)e);
            opeMap.put(e.getId(), tmpOp);
        } else if (e instanceof UmlParameter) {
            opeMap.get(e.getParentId()).addPara((UmlParameter) e);            
        } else if (e instanceof UmlGeneralization) {
            genList.add((UmlGeneralization) e);
        }
    }
    
    public String getName() {
        return umlInterface.getName();
    }
    
    public HashSet<String> getImplementInterfaceList(
            HashMap<String, MyUmlInter1> interMap) {
        HashSet<String> idSet = new HashSet<>();
        for (UmlGeneralization ge : genList) {
            String interId = ge.getTarget();
            idSet.add(interId);
            MyUmlInter1 superInter = interMap.get(interId);
            idSet.addAll(superInter.getImplementInterfaceList(interMap));
        }
        return idSet;
    }
    
}
