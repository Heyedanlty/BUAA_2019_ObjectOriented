import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlGeneralization;
import com.oocourse.uml2.models.elements.UmlInterface;
import com.oocourse.uml2.models.elements.UmlOperation;
import com.oocourse.uml2.models.elements.UmlParameter;

public class MyUmlInter {
    private UmlInterface umlInterface;

    private HashMap<String, MyUmlOperation> opeMap;
    
    private ArrayList<UmlGeneralization> genList;
    
    private boolean interReady;
    private HashSet<String> inters;
    private boolean uml009;
    
    public MyUmlInter(UmlInterface it) {
        umlInterface = it;
        opeMap = new HashMap<>();
        genList = new ArrayList<>();
        
        interReady = false;
        inters = new HashSet<>();
        uml009 = false;
    }
    
    public void addElement(UmlElement e) {
        if (e instanceof UmlOperation) {
            MyUmlOperation tmpOp = new MyUmlOperation((UmlOperation)e);
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
            HashMap<String, MyUmlInter> interMap) {
        HashSet<String> idSet = new HashSet<>();
        for (UmlGeneralization ge : genList) {
            String interId = ge.getTarget();
            idSet.add(interId);
            MyUmlInter superInter = interMap.get(interId);
            idSet.addAll(superInter.getImplementInterfaceList(interMap));
        }
        return idSet;
    }
    
    public boolean hasSuperInter() {
        return genList.size() != 0;
    }
    
    public Iterator<MyUmlInter> getSuperInters(
            HashMap<String, MyUmlInter> interMap) {
        List<MyUmlInter> list = new ArrayList<>();
        for (UmlGeneralization ge : genList) {
            list.add(interMap.get(ge.getTarget()));
        }        
        return list.iterator();
    }
    
    public UmlInterface getUmlInter() {
        return umlInterface;
    }

    public String getId() {
        return umlInterface.getId();
    }
    
    public void updateInters(HashMap<String, MyUmlInter> interMap) {
        if (interReady) {
            return;
        }
        for (UmlGeneralization ge : genList) {
            String interId = ge.getTarget();
            MyUmlInter superInter = interMap.get(interId);
            superInter.updateInters(interMap);
            if (superInter.getUml009()) {
                interReady = true;
                uml009 = true;
                return;
            } else {
                if (inters.contains(interId)) {
                    interReady = true;
                    uml009 = true;
                    return;
                } else {
                    inters.add(interId);
                }
                HashSet<String> interSet = superInter.getInters();
                for (String key : interSet) {
                    if (inters.contains(key)) {
                        interReady = true;
                        uml009 = true;
                        return;
                    } else {
                        inters.add(key);
                    }
                }
            }
        }
        interReady = true;
        return;
    }
    
    public HashSet<String> getInters() {
        return inters;
    }
    
    public boolean getUml009() {
        return uml009;
    }
}
